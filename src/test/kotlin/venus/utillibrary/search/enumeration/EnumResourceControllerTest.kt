package venus.utillibrary.search.enumeration

import venus.utillibrary.security.exceptions.JwtAuthenticationException
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import venus.utillibrary.enumeration.EnumResourceController
import venus.utillibrary.enumeration.EnumResourceService
import venus.utillibrary.enumeration.EnumValue
import venus.utillibrary.enumeration.exception.ResourceNotFoundException
import venus.utillibrary.handler.RestResponseEntityExceptionHandler


@RunWith(MockitoJUnitRunner::class)
class EnumResourceControllerTest {

    @InjectMocks
    lateinit var testSubject: EnumResourceController

    @Mock
    lateinit var enumResourceService: EnumResourceService

    private lateinit var mvc: MockMvc

    @Before
    fun setup() {

        mvc = MockMvcBuilders
                .standaloneSetup(testSubject)
                .setControllerAdvice(RestResponseEntityExceptionHandler())
                .build()
    }

    @Test
    fun `should call service and return response list`() {

        //given
        whenever(enumResourceService.getEnumResource("testEnum"))
                .thenReturn(TestEnum.values().map { EnumValue(it.ordinal, it.name, it.name) }.toList())

        //when
        val result = this.mvc.perform(get("/enums/testEnum").accept(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content.length()").value(7))
    }

    @Test(expected = Exception::class)
    fun `should throw not authorized exception`() {

        //given
        whenever(enumResourceService.getEnumResource("testEnum2"))
                .thenThrow(JwtAuthenticationException("Not authorized"))

        //when
        this.mvc.perform(get("/enums/testEnum2").accept(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `should throw not found exception`() {

        //given
        whenever(enumResourceService.getEnumResource("testEnum3"))
                .thenThrow(ResourceNotFoundException("Requested resource not found"))

        //when
        val result = this.mvc.perform(get("/enums/testEnum3").accept(MediaType.APPLICATION_JSON))

        //then
        result.andExpect(status().isNotFound)
    }
}