package venus.utillibrary.searchcore.search.operator

import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.mapping.PropertyPath
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.data.util.TypeInformation
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

import java.util.HashMap

val RESERVED_PARAMS = arrayListOf("projection", "size", "page", "sort", "all_in_one_page")
const val DEFAULT_OPERATOR = "default"

fun customizeQuery(params: MultiValueMap<String, String>, type: TypeInformation<*>, bindings: QuerydslBindings, root: EntityPath<*>) {

    // Group params by field names
    val structuredParams = structureParameters(params)

    // Iterate over all the fields that take part in the filter
    structuredParams.forEach { propertyPath, criteria ->

        var pp: PropertyPath = getProperty(propertyPath, type) ?: return@forEach
        var propertyName: String? = null

        while (pp.hasNext()) {
            propertyName = propertyName?.let{"$it." + pp.segment} ?: pp.segment
            pp = pp.next()!!
        }

        val rootBuilder = PathBuilder(root.type, root.metadata)
        val builder = propertyName?.let{ PathBuilder(pp.owningType.type, rootBuilder.get(it).toString()) } ?: rootBuilder

        SearchOperator.handle(bindings, builder, pp.segment, propertyPath, criteria, pp.type)
    }
}

private fun structureParameters(params: MultiValueMap<String, String>)
        : MutableMap<String, MultiValueMap<String, String>> {

    val structuredParams = HashMap<String, MultiValueMap<String, String>>()

    params.entries.forEach { (key, value) ->
        val operator = getEntityOperator(key)
        val propertyPath = if (DEFAULT_OPERATOR == operator) key else key.replace(":$operator", "")

        // Get a group of operators for a particular field
        val group: MultiValueMap<String, String> = structuredParams.getOrPut(propertyPath) {LinkedMultiValueMap()}

        // Put an operator into it's group
        group[key] = value
    }

    return structuredParams
}

private fun getProperty(propertyPath: String, type: TypeInformation<*>): PropertyPath? {
    if (RESERVED_PARAMS.contains(propertyPath)) {
        return null
    }
    //Check valid path
    return try { PropertyPath.from(propertyPath, type) } catch (e: PropertyReferenceException) { null }

}

fun getEntityOperator(path: String): String? {
    val colon = ":"
    return if (!path.contains(colon)) DEFAULT_OPERATOR else path.split(colon.toRegex()).component2()
}
