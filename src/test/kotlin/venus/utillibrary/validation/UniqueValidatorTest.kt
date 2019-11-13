package venus.utillibrary.validation

import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW
import org.springframework.transaction.support.DefaultTransactionDefinition
import venus.utillibrary.entity.TestEntity
import venus.utillibrary.integration.AbstractTestMvcIntegration
import venus.utillibrary.repository.TestEntityRepository
import java.time.LocalDate
import javax.validation.ConstraintViolationException

@FixMethodOrder(MethodSorters.JVM)
class UniqueValidatorTest : AbstractTestMvcIntegration() {

    @Autowired
    lateinit var testEntityRepository: TestEntityRepository
    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    @Before
    fun setUp() {
        val status = transactionManager.getTransaction(DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW))
        testEntityRepository.save(TestEntity(name = "test1", date = LocalDate.now(), float = 100F))
        transactionManager.commit(status)
    }

    @After
    fun cleanUp() {
        val status = transactionManager.getTransaction(DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW))
        testEntityRepository.deleteAll()
        transactionManager.commit(status)
    }

    @Test
    fun `should fail to save entity because of field reject`() {

        //when
        val result = performPost("/testEntities", """{"name": "test1"}""")

        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[?(@.field == 'name')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[?(@.defaultMessage)]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[?(@.entity == 'TestEntity')]").exists())

    }

    @Test
    fun `should fail to save entity because of fields bunch reject`() {

        //when
        val result = performPost("/testEntities", """{"name": "test2", "date":"${LocalDate.now()}", "float":100}""")

        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[?(@.field == 'float')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[?(@.field == 'date')]").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[?(@.defaultMessage)]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[?(@.entity == 'TestEntity')]").exists())

    }

    private fun performPost(query: String, body: String): ResultActions {
        return mvc.perform(MockMvcRequestBuilders.post(query)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
    }

    private fun getConstraints(exception: ConstraintViolationException): List<Map<String, Any>> {
        return exception.constraintViolations
                .map {
                    mapOf("entity" to it.rootBean::class.java.simpleName,
                            "field" to it.propertyPath.toString(),
                            "defaultMessage" to it.messageTemplate)
                }
    }
}