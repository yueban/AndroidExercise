package com.yueban.testdemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-03-13
 */
@RunWith(AndroidJUnit4::class)
class CalculatorTest {
    private lateinit var mCalculator: Calculator

    @Before
    fun setUp() {
        mCalculator = Calculator()
    }

    @Test
    fun addTwoNumbers() {
        val resultAdd = mCalculator.add(1.0, 1.0)
        assertThat(resultAdd, `is`(equalTo(2.0)))
    }

    @Test
    fun subTwoNumbers() {
        val resultSub = mCalculator.sub(1.0, 1.0)
        assertThat(resultSub, `is`(equalTo(0.0)))
    }

    @Test
    fun subWorksWithNegativeResult() {
        val resultSub: Double = mCalculator.sub(1.0, 17.0)
        assertThat(resultSub, `is`(equalTo(-16.0)))
    }

    @Test
    fun divTwoNumbers() {
        val resultDiv: Double = mCalculator.div(32.0, 2.0)
        assertThat(resultDiv, `is`(equalTo(16.0)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun divDivideByZeroThrows() {
        mCalculator.div(32.0, 0.0)
    }

    @Test
    fun mulTwoNumbers() {
        val resultMul: Double = mCalculator.mul(32.0, 2.0)
        assertThat(resultMul, `is`(equalTo(64.0)))
    }
}