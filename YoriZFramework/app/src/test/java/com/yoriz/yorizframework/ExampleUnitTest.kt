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

            fun foo() {
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

    @Test
    fun insertSort() {
        val a = intArrayOf(5, 2, 7, 1, 23, 15, 9, 34, 6, 8, 13)
        for (i in 0 until a.size) {
            for (j in 0 until a.size - i) {
                if (j + 1 > a.size - 1) continue
                if (a[j] > a[j + 1]) {
                    val temp = a[j]
                    a[j] = a[j + 1]
                    a[j + 1] = temp
                }
            }
        }
        println(a.toString())
    }
}
