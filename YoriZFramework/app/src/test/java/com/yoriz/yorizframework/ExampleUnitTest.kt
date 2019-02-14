package com.yoriz.yorizframework

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    class C {

        companion object {

            fun foo(){
                println("扩展方法")
            }

        }

        fun foo() {
            println("成员方法")
        }

        @Test
        fun main() {
            C.foo()
            C().foo()
        }
    }
}
