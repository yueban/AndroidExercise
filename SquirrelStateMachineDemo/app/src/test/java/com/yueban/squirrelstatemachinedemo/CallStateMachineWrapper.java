package com.yueban.squirrelstatemachinedemo;

import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.StateMachineConfiguration;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2020-01-06
 */
public class CallStateMachineWrapper {
    public static CallStateMachine getInstance() {
        return Holder.INSTANCE;
    }

    private enum FSMMethods {
        doLogin, onLogin, makeCall, onOutgoing, onIncoming, onConnected, doTerm, onTermed, onLogout
    }

    enum FSMEvent {
        /**
         * 登录
         */
        doLogin,
        /**
         * 登录成功
         */
        onLoginSuccess,
        /**
         * 登录失败
         */
        onLoginFailed,
        /**
         * 拨号
         */
        makeCall,
        /**
         * 去电
         */
        onOutgoing,
        /**
         * 来电
         */
        onIncoming,
        /**
         * 接听成功
         */
        onAnswerSuccess,
        /**
         * 接听失败
         */
        onAnswerFailed,
        /**
         * 已接通
         */
        onConnected,
        /**
         * 已建立加密通话 (秘钥交换成功)
         */
        onEncrypted,
        /**
         * 挂断
         */
        term,
        /**
         * 已挂断
         */
        onTermed,
        /**
         * 已登出
         */
        onLogout,
    }

    enum FSMState {
        /**
         * 未初始化
         */
        UnInit,
        /**
         * 初始化中
         */
        Initing,
        /**
         * 通话空闲状态
         */
        Idle,
        /**
         * 正在进行拨号
         */
        MakingCall,
        /**
         * 呼叫中
         */
        Outgoing,
        /**
         * 来电中
         */
        Incoming,
        /**
         * 正在进行接听
         */
        Answering,
        /**
         * 已接通
         */
        Connected,
        /**
         * 已建立加密通话 (秘钥交换成功)
         */
        Encrypted,
        /**
         * 挂断中
         */
        Terming,
    }

    private static final class Holder {
        private static final CallStateMachine INSTANCE = newInstance();

        private static CallStateMachine newInstance() {
            StateMachineBuilder<CallStateMachine, FSMState, FSMEvent, CallContext> builder =
                StateMachineBuilderFactory.create(CallStateMachine.class, FSMState.class, FSMEvent.class, CallContext.class);

            // sUnInit --> sIniting: login
            // sIniting --> sIdle: login: success
            // sIniting --> sUnInit: login: fail
            builder.externalTransition()
                .from(FSMState.UnInit)
                .to(FSMState.Initing)
                .on(FSMEvent.doLogin)
                .callMethod(FSMMethods.doLogin.name());
            builder.externalTransition()
                .from(FSMState.Initing)
                .to(FSMState.Idle)
                .on(FSMEvent.onLoginSuccess)
                .callMethod(FSMMethods.onLogin.name());
            builder.externalTransition().from(FSMState.Initing).to(FSMState.UnInit).on(FSMEvent.onLoginFailed);

            // sIdle --> sPrepareCall: make call: success
            // sPrepareCall --> sOutgoing: new Call: direction out
            // sIdle --> sIncoming: new Call: direction in
            // sIncoming --> sAnswering: answer: success
            // sIncoming --> sIdle: answer: fail
            builder.externalTransition()
                .from(FSMState.Idle)
                .to(FSMState.MakingCall)
                .on(FSMEvent.makeCall)
                .callMethod(FSMMethods.makeCall.name());
            builder.externalTransition()
                .from(FSMState.MakingCall)
                .to(FSMState.Outgoing)
                .on(FSMEvent.onOutgoing)
                .callMethod(FSMMethods.onOutgoing.name());
            builder.externalTransition()
                .from(FSMState.Idle)
                .to(FSMState.Incoming)
                .on(FSMEvent.onIncoming)
                .callMethod(FSMMethods.onIncoming.name());
            builder.externalTransition().from(FSMState.Incoming).to(FSMState.Answering).on(FSMEvent.onAnswerSuccess);
            builder.externalTransition().from(FSMState.Incoming).to(FSMState.Idle).on(FSMEvent.onAnswerFailed);

            // sOutgoing --> sConnected: status update: connecting
            // sAnswering --> sConnected: status update: connecting
            // sConnected --> sEncrypted: encrypted
            builder.externalTransitions()
                .fromAmong(FSMState.Outgoing, FSMState.Answering)
                .to(FSMState.Connected)
                .on(FSMEvent.onConnected)
                .callMethod(FSMMethods.onConnected.name());
            builder.externalTransition().from(FSMState.Connected).to(FSMState.Encrypted).on(FSMEvent.onEncrypted);

            // sOutgoing --> sTerming: term
            // sIncoming --> sTerming: term
            // sAnswering --> sTerming: term
            // sConnected --> sTerming: term
            // sEncrypted --> sTerming: term
            builder.externalTransitions()
                .fromAmong(FSMState.Outgoing, FSMState.Incoming, FSMState.Answering, FSMState.Connected, FSMState.Encrypted)
                .to(FSMState.Terming)
                .on(FSMEvent.term)
                .callMethod(FSMMethods.doTerm.name());

            // sPrepareCall --> sIdle: onTermed
            // sOutgoing --> sIdle: onTermed
            // sIncoming --> sIdle: onTermed
            // sAnswering --> sIdle: onTermed
            // sConnected --> sIdle: onTermed
            // sEncrypted --> sIdle: onTermed
            // sTerming --> sIdle: onTermed
            builder.externalTransitions()
                .fromAmong(FSMState.MakingCall, FSMState.Outgoing, FSMState.Incoming, FSMState.Answering, FSMState.Connected,
                    FSMState.Encrypted, FSMState.Terming)
                .to(FSMState.Idle)
                .on(FSMEvent.onTermed)
                .callMethod(FSMMethods.onTermed.name());

            // sIniting --> sUnInit: logout
            // sIdle --> sUnInit: logout
            // sPrepareCall --> sUnInit: logout
            // sOutgoing --> sUnInit: logout
            // sIncoming --> sUnInit: logout
            // sAnswering --> sUnInit: logout
            // sConnected --> sUnInit: logout
            // sEncrypted --> sUnInit: logout
            builder.externalTransitions()
                .fromAmong(FSMState.Initing, FSMState.Idle, FSMState.MakingCall, FSMState.Outgoing, FSMState.Incoming,
                    FSMState.Answering, FSMState.Connected, FSMState.Encrypted, FSMState.Terming)
                .to(FSMState.UnInit)
                .on(FSMEvent.onLogout)
                .callMethod(FSMMethods.onLogout.name());

            return builder.newStateMachine(FSMState.UnInit, StateMachineConfiguration.create().enableDebugMode(true));
        }
    }

    static class CallStateMachine extends AbstractStateMachine<CallStateMachine, FSMState, FSMEvent, CallContext> {
        public void doLogin(FSMState from, FSMState to, FSMEvent event, CallContext context) {
            System.out.println("doLogin called");
        }

        public void onLogin(FSMState from, FSMState to, FSMEvent event, CallContext context) {
            System.out.println("onLogin called");
        }
    }

    static class CallContext {}
}
