package venus.utillibrary.searchcore.search

import com.querydsl.core.BooleanBuilder
import org.springframework.core.MethodParameter
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.QuerydslRepositoryInvokerAdapter
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder
import org.springframework.data.repository.support.Repositories
import org.springframework.data.repository.support.RepositoryInvoker
import org.springframework.data.repository.support.RepositoryInvokerFactory
import org.springframework.data.rest.webmvc.config.ResourceMetadataHandlerMethodArgumentResolver
import org.springframework.data.rest.webmvc.config.RootResourceInformationHandlerMethodArgumentResolver
import org.springframework.data.util.ClassTypeInformation
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class ApiQuerydslMethodArgumentResolver(private val repositories: Repositories,
                                        private val predicateBuilder: QuerydslPredicateBuilder,
                                        private val factory: ApiQuerydslBindingsFactory,
                                        invokerFactory: RepositoryInvokerFactory,
                                        resourceMetadataResolver: ResourceMetadataHandlerMethodArgumentResolver)
    : RootResourceInformationHandlerMethodArgumentResolver(repositories, invokerFactory, resourceMetadataResolver) {

    override fun postProcess(parameter: MethodParameter, invoker: RepositoryInvoker,
                             domainType: Class<*>, parameters: Map<String, Array<String>>): RepositoryInvoker {

        val repository = repositories.getRepositoryFor(domainType).get()

        if (repository !is QuerydslPredicateExecutor<out Any> ||
                !parameter.hasParameterAnnotation(QuerydslPredicate::class.java)) {
            return invoker
        }

        val type = ClassTypeInformation.from(domainType)
        val bindings = factory.createBindingsFor(type)
        val map = toMultiValueMap(parameters)

        //handle custom search
        factory.customize(map, type, bindings)
        val predicate = predicateBuilder.getPredicate(type, map, bindings) ?: BooleanBuilder()

        @Suppress("UNCHECKED_CAST")
        return QuerydslRepositoryInvokerAdapter(invoker, repository as QuerydslPredicateExecutor<Any>, predicate)
    }


    private fun toMultiValueMap(source: Map<String, Array<String>>): MultiValueMap<String, String> {

        val result = LinkedMultiValueMap<String, String>()

        for ((key, value) in source) {
            result[key] = arrayListOf(*value)
        }

        return result
    }
}