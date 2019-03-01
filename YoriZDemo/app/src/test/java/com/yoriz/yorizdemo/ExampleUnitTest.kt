package com.yoriz.yorizdemo

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val data = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
    @Test
    fun testSearch() {
        print(search(0, data.size, 11))
    }


    private fun search(start: Int, end: Int, key: Int): Int {
        val mid = (end - start) / 2 + start
        if (data[mid] == key) return mid
        if (start >= end) return -1
        if (data[mid] > key) return search(start, mid, key)
        if (data[mid] < key) return search(mid, end, key)
        return -1
    }
}
