package com.yueban.testdemo

import java.util.regex.Pattern

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-02-11
 */
class EmailValidator {
    companion object {
        fun isValidEmail(email: String): Boolean {
            return Pattern.compile("^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})*\$").matcher(email).matches()
        }
    }
}