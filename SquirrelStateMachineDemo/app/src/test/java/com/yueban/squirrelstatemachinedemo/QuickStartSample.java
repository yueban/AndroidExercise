package com.yueban.squirrelstatemachinedemo;

import org.junit.Test;
import org.squirrelframework.foundation.fsm.Condition;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.StateMachineConfiguration;
import org.squirrelframework.foundation.fsm.TransitionPriority;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

public class QuickStartSample {
    @Test
    public void test() {
        // 3. Build State Transitions
        StateMachineBuilder<StateMachineSample, String, FSMEvent, MyContext> builder =
                StateMachineBuilderFactory.create(StateMachineSample.class, String.class, FSMEvent.class,
                        MyContext.class);
        builder.externalTransition().from("A").to("B").on(FSMEvent.ToB).when(new Condition<MyContext>() {
            @Override
            public boolean isSatisfied(MyContext context) {
                return context.callNumber.equals("123");
            }

            @Override
            public String name() {
                return "MyContext";
            }
        }).callMethod("fromAToB");
        builder.externalTransition().from("A").to("C").on(FSMEvent.ToB).when(new Condition<MyContext>() {
            @Override
            public boolean isSatisfied(MyContext context) {
                return context.callNumber.equals("456");
            }

            @Override
            public String name() {
                return "MyContext";
            }
        }).callMethod("fromAToC");
        builder.externalTransition().from("B").to("C").on(FSMEvent.ToC).callMethod("fromBToC");
        //builder.internalTransition(TransitionPriority.HIGH).within("A").on(FSMEvent.WithinA).perform(new
        // AnonymousAction<StateMachineSample, String, FSMEvent, MyContext>() {
        //    @Override
        //    public void execute(String from, String to, FSMEvent event, MyContext context,
        //                        StateMachineSample stateMachine) {
        //        System.out.println("from: " + from + " to: " + to + " action: " + event);
        //    }
        //});
        builder.internalTransition(TransitionPriority.HIGH).within("A").on(FSMEvent.WithinA).callMethod("withinA");

        builder.onEntry("B").callMethod("ontoB");

        // 4. Use State Machine
        final StateMachineSample fsm = builder.newStateMachine("A", StateMachineConfiguration.create().enableDebugMode(true));
        fsm.fire(FSMEvent.WithinA);

        System.out.println("Current state is " + fsm.getCurrentState());
    }

    private enum FSMEvent {
        ToA, ToB, ToC, ToD, WithinA
    }

    private static class MyContext {
        String callNumber;

        public MyContext(String callNumber) {
            this.callNumber = callNumber;
        }

        @Override
        public String toString() {
            return "MyContext{" + "callNumber='" + callNumber + '\'' + '}';
        }
    }

    // 2. Define State Machine Class
    static class StateMachineSample extends AbstractStateMachine<StateMachineSample, String, FSMEvent, MyContext> {
        protected void transitFromAToBOnToB(String from, String to, FSMEvent event, MyContext context) {
            System.out.println("transitFromAToBOnToB from '" + from + "' to '" + to + "' on event '" + event + "' with context "
                    + "'" + context + "'.");
        }

        protected void fromAToB(String from, String to, FSMEvent event, MyContext context) {
            try {
                Thread.sleep(3000);
                System.out.println("do: AtoB");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Transition from '" + from + "' to '" + to + "' on event '" + event + "' with context "
                    + "'" + context + "'.");
        }

        protected void fromAToC(String from, String to, FSMEvent event, MyContext context) {
            System.out.println("Transition from '" + from + "' to '" + to + "' on event '" + event + "' with context "
                    + "'" + context + "'.");
        }

        protected void fromBToC(String from, String to, FSMEvent event, MyContext context) {
            System.out.println("do: BtoC");
            System.out.println("Transition from '" + from + "' to '" + to + "' on event '" + event + "' with context "
                    + "'" + context + "'.");
        }

        protected void ontoB(String from, String to, FSMEvent event, MyContext context) {
            System.out.println("Entry State \'" + to + "\'.");
        }

        protected void withinA(String from, String to, FSMEvent event, MyContext context) {
            System.out.println("Transition from '" + from + "' to '" + to + "' on event '" + event + "' with context "
                    + "'" + context + "'.");
        }
    }
}
