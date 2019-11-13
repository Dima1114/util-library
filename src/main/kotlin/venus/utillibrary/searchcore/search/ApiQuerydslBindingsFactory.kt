package venus.utillibrary.searchcore.search

import venus.utillibrary.searchcore.search.operator.customizeQuery
import com.querydsl.core.types.EntityPath
import org.springframework.data.querydsl.EntityPathResolver
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.util.TypeInformation
import org.springframework.util.ConcurrentReferenceHashMap
import org.springframework.util.MultiValueMap

class ApiQuerydslBindingsFactory(entityPathResolver : EntityPathResolver,
                                 private val entityPaths : MutableMap<TypeInformation<*>, EntityPath<*>> = ConcurrentReferenceHashMap())
    : QuerydslBindingsFactory(entityPathResolver) {

    companion object {
        const val INVALID_DOMAIN_TYPE = "Unable to find Querydsl root type for detected domain type %s! User @%s's root attribute to define the domain type manually!"
    }

    fun customize(params: MultiValueMap<String, String>,
                  type: TypeInformation<*>,
                  bindings: QuerydslBindings) {
        customizeQuery(params, type, bindings, verifyEntityPathPresent(type))
    }

    private fun verifyEntityPathPresent(candidate: TypeInformation<*>): EntityPath<*> {

        return entityPaths.computeIfAbsent(candidate) {
            try {
                entityPathResolver.createPath(it.type)
            } catch (o_O: IllegalArgumentException) {
                throw IllegalStateException(
                        String.format(INVALID_DOMAIN_TYPE, it.type, QuerydslPredicate::class.java.simpleName), o_O)
            }
        }
    }
}
