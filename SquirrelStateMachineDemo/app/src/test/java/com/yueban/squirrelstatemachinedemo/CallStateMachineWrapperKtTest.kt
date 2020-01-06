package com.yueban.squirrelstatemachinedemo

import org.junit.Test

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-06
 */
class CallStateMachineWrapperKtTest {
    @Test
    fun test() {
        val fsm = CallStateMachineWrapperKt.INSTANCE
        fsm.fire(CallStateMachineWrapperKt.FSMEvent.doLogin)
        fsm.fire(CallStateMachineWrapperKt.FSMEvent.makeCall)
        fsm.fire(CallStateMachineWrapperKt.FSMEvent.onLoginSuccess)
        fsm.fire(CallStateMachineWrapperKt.FSMEvent.onLogout)
    }
}