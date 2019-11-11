package venus.utillibrary.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.module.kotlin.readValue
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be equal to`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RunWith(MockitoJUnitRunner::class)
class LocalDateTimeModuleTest {

    lateinit var objectMapper: ObjectMapper
    lateinit var defaultMapper: ObjectMapper

    @Before
    fun setUp() {
        objectMapper = ObjectMapper().apply {
            registerModule(LocalDateModule())
        }
        defaultMapper = ObjectMapper()
    }

    @Test
    fun `should convert default local date to string`() {

        //given
        val date = LocalDate.of(2019, 3, 20)

        //when
        val result = defaultMapper.writeValueAsString(date)

        //then
        result `should not be equal to` """"2019-03-20""""
    }

    @Test(expected = InvalidDefinitionException::class)
    fun `should fail to convert string to local date`() {

        //given
        val date = """"2019-03-20""""

        //when
        defaultMapper.readValue<LocalDate>(date)
    }

    @Test
    fun `should default convert local date time to string`() {

        //given
        val date = LocalDateTime.of(
                LocalDate.of(2019, 3, 20),
                LocalTime.of(22, 46, 10))

        //when
        val result = defaultMapper.writeValueAsString(date)

        //then
        result `should not be equal to` """"2019-03-20 22:46:10""""
    }

    @Test(expected = InvalidDefinitionException::class)
    fun `should fail to convert string to local date time`() {

        //given
        val dateTime = """"2019-03-20 22:46:10""""

        //when
        defaultMapper.readValue<LocalDateTime>(dateTime)
    }

    @Test
    fun `should convert local date to string`() {

        //given
        val date = LocalDate.of(2019, 3, 20)

        //when
        val result = objectMapper.writeValueAsString(date)

        //then
        result `should be equal to` """"2019-03-20""""
    }

    @Test
    fun `should convert local date time to string`() {

        //given
        val date = LocalDateTime.of(
                LocalDate.of(2019, 3, 20),
                LocalTime.of(22, 46, 10))

        //when
        val result = objectMapper.writeValueAsString(date)

        //then
        result `should be equal to` """"2019-03-20 22:46:10""""
    }

    @Test
    fun `should convert string to local date`() {

        //given
        val date = """"2019-03-20""""

        //when
        val result = objectMapper.readValue<LocalDate>(date)

        //then
        result `should equal` LocalDate.of(2019, 3, 20)
    }

    @Test
    fun `should convert string to local date time`() {

        //given
        val dateTime = """"2019-03-20 22:46:10""""

        //when
        val result = objectMapper.readValue<LocalDateTime>(dateTime)

        //then
        result `should equal` LocalDateTime.of(LocalDate.of(2019, 3, 20), LocalTime.of(22, 46, 10))
    }
}