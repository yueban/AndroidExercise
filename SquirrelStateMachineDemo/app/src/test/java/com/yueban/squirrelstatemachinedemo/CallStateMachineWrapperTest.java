package com.yueban.squirrelstatemachinedemo;

import org.junit.Test;

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-06
 */
public class CallStateMachineWrapperTest {
    @Test
    public void test() {
        CallStateMachineWrapper.CallStateMachine fsm = CallStateMachineWrapper.getInstance();
        fsm.fire(CallStateMachineWrapper.FSMEvent.doLogin);
        fsm.fire(CallStateMachineWrapper.FSMEvent.makeCall);
        fsm.fire(CallStateMachineWrapper.FSMEvent.onLoginSuccess);
        fsm.fire(CallStateMachineWrapper.FSMEvent.onLogout);
    }
}
