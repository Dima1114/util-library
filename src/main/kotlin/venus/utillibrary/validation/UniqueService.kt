package venus.searchcore.validation

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import java.util.*
import javax.persistence.EntityManager

class UniqueService<T>(private val em: EntityManager, private val genericType: Class<T>) {

    fun isValueExists(id: Long?, field: String, fieldValue: Any?): Boolean {
        if (fieldValue == null) return false
        val path = PathBuilder(genericType, "entity")
        val idExpression = id?.let { path.get("id").ne(id) } ?: path.get("id").isNotNull
        return JPAQuery<T>(em).from(path).where(path.get(field).eq(fieldValue).and(idExpression)).fetchCount() > 0
    }

    fun isValuesBunchExists(id: Long?, fields: Map<String, Any?>): Boolean {
        if (fields.isEmpty()) return false
        val path = PathBuilder(genericType, "entity")
        val expressionList = ArrayList<Predicate>()
        expressionList.add(id?.let { path.get("id").ne(id) } ?: path.get("id").isNotNull)
        fields.forEach { (key, value) ->
            value?.let { expressionList.add(path.get(key).eq(value)) } ?: expressionList.add(path.get(key).isNull) }

        return JPAQuery<T>(em).from(path).where(ExpressionUtils.allOf(expressionList)).fetchCount() > 0
    }
}
