package venus.utillibrary.searchcore.search.operator

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.util.MultiValueMap
import venus.utillibrary.searchcore.converter.LocalDateCustomConverter
import venus.utillibrary.searchcore.converter.LocalDateTimeCustomConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

enum class SearchOperator(val operator: String,
                          val predicate: (booleanBuilder: BooleanBuilder,
                                          pathBuilder: PathBuilder<*>,
                                          propertyPath: String,
                                          values: MutableList<String>,
                                          fieldType: Class<*>) -> Unit) {

    GT("gt", { booleanBuilder, pathBuilder, propertyPath, value, fieldType ->
        when (fieldType) {
            in longs -> booleanBuilder.and(pathBuilder.getNumber(propertyPath, longClass).gt(value[0].toLong()))
            in doubles -> booleanBuilder.and(pathBuilder.getNumber(propertyPath, doubleClass).gt(value[0].toDouble()))
        }
    }),
    LT("lt", { booleanBuilder, pathBuilder, propertyPath, value, fieldType ->
        when (fieldType) {
            in longs -> booleanBuilder.and(pathBuilder.getNumber(propertyPath, longClass).lt(value[0].toLong()))
            in doubles -> booleanBuilder.and(pathBuilder.getNumber(propertyPath, doubleClass).lt(value[0].toDouble()))
        }
    }),
    GOE("goe", { booleanBuilder, pathBuilder, propertyPath, value, fieldType ->
        when (fieldType) {
            in longs -> booleanBuilder.and(pathBuilder.getNumber(propertyPath, longClass).goe(value[0].toLong()))
            in doubles -> booleanBuilder.and(pathBuilder.getNumber(propertyPath, doubleClass).goe(value[0].toDouble()))
        }
    }),
    LOE("loe", { booleanBuilder, pathBuilder, propertyPath, value, fieldType ->
        when (fieldType) {
            in longs -> booleanBuilder.and(pathBuilder.getNumber(propertyPath, longClass).loe(value[0].toLong()))
            in doubles -> booleanBuilder.and(pathBuilder.getNumber(propertyPath, doubleClass).loe(value[0].toDouble()))
        }
    }),
    CONTAINS("contains", { booleanBuilder, pathBuilder, propertyPath, value, _ ->
        value.forEach { booleanBuilder.and(pathBuilder.getString(propertyPath).containsIgnoreCase(it)) }
    }),
    STARTS_WITH("startsWith", { booleanBuilder, pathBuilder, propertyPath, value, _ ->
        booleanBuilder.and(pathBuilder.getString(propertyPath).startsWith(value[0]))
    }),
    IS_NULL("isNull", { booleanBuilder, pathBuilder, propertyPath, _, _ ->
        booleanBuilder.and(pathBuilder.get(propertyPath).isNull)
    }),
    IS_NOT_NULL("isNotNull", { booleanBuilder, pathBuilder, propertyPath, _, _ ->
        booleanBuilder.and(pathBuilder.get(propertyPath).isNotNull)
    }),
    DATE_LESS_OR_EQUAL("dloe", { booleanBuilder, pathBuilder, propertyPath, value, fieldType ->
        when (fieldType) {
            localDateClass ->
                booleanBuilder.and(pathBuilder.getDate(propertyPath, localDateClass).loe(localDateParser.convert(value[0])))
            localDateTimeClass ->
                booleanBuilder.and(pathBuilder.getDate(propertyPath, localDateTimeClass).loe(localDateTimeParser.convert(value[0])))
        }
    }),
    DATE_GREATER_OR_EQUAL("dgoe", { booleanBuilder, pathBuilder, propertyPath, value, fieldType ->
        when (fieldType) {
            localDateClass ->
                booleanBuilder.and(pathBuilder.getDate<LocalDate>(propertyPath, localDateClass).goe(localDateParser.convert(value[0])))
            localDateTimeClass ->
                booleanBuilder.and(pathBuilder.getDate<LocalDateTime>(propertyPath, localDateTimeClass).goe(localDateTimeParser.convert(value[0])))
        }
    }),
    DATE_EQUAL("deq", { booleanBuilder, pathBuilder, propertyPath, value, fieldType ->
        when (fieldType) {
            localDateClass ->
                booleanBuilder.and(pathBuilder.getDate(propertyPath, localDateClass).eq(localDateParser.convert(value[0])))
            localDateTimeClass ->
                booleanBuilder.and(pathBuilder.getDate(propertyPath, localDateTimeClass).eq(localDateTimeParser.convert(value[0])))
        }
    }),
    IN("in", { booleanBuilder, pathBuilder, propertyPath, values, fieldType ->
//        var array = values
//        if (values.size == 1) {
//            array = values[0]
//        }

        when {
            fieldType.isEnum -> {
                val conversionService = DefaultConversionService()
                booleanBuilder.and(pathBuilder.get(propertyPath).`in`(
                        *values.map { conversionService.convert(it, fieldType) }.toTypedArray()))
            }
            fieldType in ints -> booleanBuilder.and(pathBuilder.get(propertyPath).`in`(*values.map { it.toInt() }.toTypedArray()))
            fieldType in longs -> booleanBuilder.and(pathBuilder.get(propertyPath).`in`(*values.map { it.toLong() }.toTypedArray()))
            fieldType in floats -> booleanBuilder.and(pathBuilder.get(propertyPath).`in`(*values.map { it.toFloat() }.toTypedArray()))
            fieldType in doubles -> booleanBuilder.and(pathBuilder.get(propertyPath).`in`(*values.map { it.toDouble() }.toTypedArray()))
            else -> booleanBuilder.and(pathBuilder.get(propertyPath).`in`(*values.toTypedArray()))
        }
    });

    companion object {

        val longClass = java.lang.Long::class.java
        private val integerClass = java.lang.Integer::class.java
        val doubleClass = java.lang.Double::class.java
        private val floatClass = java.lang.Float::class.java
        val localDateClass = LocalDate::class.java
        val localDateTimeClass = LocalDateTime::class.java

        val longs = listOf(longClass, Long::class.java, integerClass, Int::class.java)
        val ints = listOf(integerClass, Int::class.java)
        val doubles = listOf(doubleClass, Double::class.java, floatClass, Float::class.java)
        val floats = listOf(floatClass, Float::class.java)

        val localDateParser = LocalDateCustomConverter()
        val localDateTimeParser = LocalDateTimeCustomConverter()

        fun handle(bindings: QuerydslBindings, pathBuilder: PathBuilder<*>, propertyPath: String, path: String,
                   map: MultiValueMap<String, String>, fieldType: Class<*>) {

            val fieldSearchCriteria = BooleanBuilder()
            val alias = map.keys.lastOrNull() ?: path

            // Loop by the criteria for the particular field
            map.filter { (_, value) -> value.isNotEmpty() }
                    .forEach { (key, value) ->

                        values().firstOrNull { it.operator == getEntityOperator(key) }
                                ?.let {
                                    it.predicate.invoke(fieldSearchCriteria, pathBuilder, propertyPath, value, fieldType)
                                    clearNullable(value)
                                }
                                ?: bindings.bind(pathBuilder.get(propertyPath)).withDefaultBinding()

//                        bindings.bind(pathBuilder.get(propertyPath)).withDefaultBinding()
                    }
            // Apply criteria for path
            fieldSearchCriteria.value?.let {
                bindings.bind(pathBuilder.get(propertyPath)).`as`(alias).all { _, _ -> Optional.of(it) }
            }
        }
    }
}

fun clearNullable(list: MutableList<String>) {
    if (list.filterNotNull().isEmpty()) list.clear()
}