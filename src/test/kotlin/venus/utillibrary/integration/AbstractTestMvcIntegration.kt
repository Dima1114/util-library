package venus.utillibrary.integration

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import javax.persistence.EntityManager

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
abstract class AbstractTestMvcIntegration {

    @Autowired
    private lateinit var context: WebApplicationContext
    @Autowired
    protected lateinit var em: EntityManager

    protected lateinit var mvc: MockMvc

    @Before
    fun setUpMvc(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
    }
}