package com.yueban.testdemo

import com.google.common.truth.Truth.assertThat
import org.junit.Test

//import com.google.common.truth.Truth.assthat
/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-02-11
 */
class EmailValidatorTest {

    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertThat(EmailValidator.isValidEmail("123@qq.com")).isTrue()
    }
}