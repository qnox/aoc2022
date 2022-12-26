package day25

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day25KtTest {

    @Test
    fun `1 to SANFU is 1`() {
        assertEquals("1", toSnafu(1))
    }

    @Test
    fun `2 to SANFU is 2`() {
        assertEquals("1", toSnafu(1))
    }

    @Test
    fun `3 to SANFU is 1=`() {
        assertEquals("1=", toSnafu(3))
    }

    @Test
    fun `10 to SANFU is 20`() {
        assertEquals("20", toSnafu(10))
    }

    @Test
    fun `2022 to SANFU is 1=11-2`() {
        assertEquals("1=11-2", toSnafu(2022))
    }

}