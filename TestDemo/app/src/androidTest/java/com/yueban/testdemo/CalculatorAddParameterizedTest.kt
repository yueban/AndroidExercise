package com.yueban.testdemo

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-03-13
 */

/**
 * JUnit4 tests for the calculator's add logic.
 *
 *
 *  This test uses a Junit4s Parameterized tests features which uses annotations to pass
 * parameters into a unit test. The way this works is that you have to use the [Parameterized]
 * runner to run your tests.
 *
 */
@RunWith(Parameterized::class)
class CalculatorAddParameterizedTest(
    val operandOne: Double, val operandTwo: Double, val expectedResult: Double
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Iterable<Array<Any>> {
            return listOf(
                arrayOf<Any>(0, 0, 0),
                arrayOf<Any>(0, -1, -1),
                arrayOf<Any>(2, 2, 4),
                arrayOf<Any>(8, 8, 16),
                arrayOf<Any>(16, 16, 32),
                arrayOf<Any>(32, 0, 32),
                arrayOf<Any>(64, 64, 128)
            )
        }
    }

    private lateinit var mCalculator: Calculator

    @Before
    fun setUp() {
        mCalculator = Calculator()
    }

    @Test
    fun testAdd_TwoNumbers() {
        val resultAdd = mCalculator.add(operandOne, operandTwo)
        assertThat(resultAdd, `is`(equalTo(expectedResult)))
    }
}