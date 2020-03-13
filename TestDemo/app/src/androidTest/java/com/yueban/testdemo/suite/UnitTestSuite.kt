package com.yueban.testdemo.suite

import com.yueban.testdemo.CalculatorAddParameterizedTest
import com.yueban.testdemo.CalculatorTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-03-13
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(CalculatorTest::class, CalculatorAddParameterizedTest::class)
class UnitTestSuite