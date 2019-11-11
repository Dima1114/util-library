package venus.utillibrary.function

import org.amshove.kluent.`should be equal to`
import org.junit.Test
import java.lang.Exception

class FunctionUtils {

    @Test(expected = Exception::class)
    fun `throwIf must throw exception`() {

        //when
        throwIf(true) { Exception() }
    }

    @Test
    fun `throwIf must return Unit`() {

        //when
        throwIf(false) { Exception() }
    }

    @Test(expected = Exception::class)
    fun `takeOrThrow must throw exception`() {

        //when
        1.takeOrThrow({ it > 2 }) { Exception() }
    }

    @Test
    fun `takeOrThrow must return value`() {

        //when
        val result = 1.takeOrThrow({ it < 2 }) { Exception() }

        //then
        result `should be equal to` 1
    }
}