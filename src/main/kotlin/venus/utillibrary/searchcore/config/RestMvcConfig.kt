package venus.utillibrary.searchcore.config

import venus.utillibrary.searchcore.converter.LocalDateCustomConverter
import venus.utillibrary.searchcore.converter.LocalDateTimeCustomConverter
import venus.utillibrary.searchcore.search.ApiQuerydslBindingsFactory
import venus.utillibrary.searchcore.search.ApiQuerydslMethodArgumentResolver
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.PageRequest
import org.springframework.data.querydsl.QuerydslUtils
import org.springframework.data.querydsl.SimpleEntityPathResolver
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration
import org.springframework.data.rest.webmvc.config.RootResourceInformationHandlerMethodArgumentResolver
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.format.support.DefaultFormattingConversionService
import venus.utillibrary.searchcore.search.operator.SearchOperator

@Configuration
@EnableSpringDataWebSupport
@ConditionalOnClass(SearchOperator::class)
class RestMvcConfig(context: ApplicationContext,
                    @Qualifier("mvcConversionService") conversionService: ObjectFactory<ConversionService>)
    : RepositoryRestMvcConfiguration(context, conversionService) {

    @Bean
    override fun repoRequestArgumentResolver(): RootResourceInformationHandlerMethodArgumentResolver {

        if (QuerydslUtils.QUERY_DSL_PRESENT) {
            return querydslMethodArgumentResolver()
        }

        return RootResourceInformationHandlerMethodArgumentResolver(repositories(),
                repositoryInvokerFactory(defaultConversionService()), resourceMetadataHandlerMethodArgumentResolver())
    }

    @Bean
    fun querydslMethodArgumentResolver(): ApiQuerydslMethodArgumentResolver {
        val factory = querydslBindingsFactory()
        val predicateBuilder = QuerydslPredicateBuilder(defaultConversionService(),
                factory.entityPathResolver)
        return ApiQuerydslMethodArgumentResolver(repositories(), predicateBuilder, factory,
                repositoryInvokerFactory(defaultConversionService()), resourceMetadataHandlerMethodArgumentResolver())
    }

    @Bean
    @Qualifier
    override fun defaultConversionService(): DefaultFormattingConversionService {
        val conversionService =  super.defaultConversionService()
        conversionService.addConverter(LocalDateCustomConverter())
        conversionService.addConverter(LocalDateTimeCustomConverter())

        return conversionService
    }

    @Bean
    fun querydslBindingsFactory(): ApiQuerydslBindingsFactory =
            ApiQuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE)

    @Bean
    override fun pageableResolver(): HateoasPageableHandlerMethodArgumentResolver {

        val resolver = super.pageableResolver()
        resolver.setPageParameterName(repositoryRestConfiguration().pageParamName)
        resolver.setSizeParameterName(repositoryRestConfiguration().limitParamName)
        resolver.setFallbackPageable(PageRequest.of(0, repositoryRestConfiguration().maxPageSize))
        resolver.setMaxPageSize(repositoryRestConfiguration().maxPageSize)

        return resolver
    }

}