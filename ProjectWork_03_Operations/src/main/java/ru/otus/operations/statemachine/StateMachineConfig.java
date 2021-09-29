package ru.otus.operations.statemachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;


@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OperationState, OperationEvent> {

    @Override
    public void configure(final StateMachineStateConfigurer<OperationState, OperationEvent> states) throws Exception {
        states.withStates()
                .initial(OperationState.LOADED)
                .states(EnumSet.allOf(OperationState.class));
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<OperationState, OperationEvent> transitions) throws Exception {
        transitions
                .withExternal().source(OperationState.LOADED).target(OperationState.LOADED).event(OperationEvent.LOAD)
                .and()
                .withExternal().source(OperationState.LOADED).target(OperationState.CANCELED).event(OperationEvent.CANCEL)
                .and()
                .withExternal().source(OperationState.LOADED).target(OperationState.EXEC).event(OperationEvent.EXECUTE)
                .and()
                .withExternal().source(OperationState.CANCELED).target(OperationState.CANCELED).event(OperationEvent.CANCEL)
                .and()
                .withExternal().source(OperationState.EXEC).target(OperationState.EXEC).event(OperationEvent.EXECUTE)
        ;
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OperationState, OperationEvent> config) throws Exception {
        StateMachineListenerAdapter<OperationState, OperationEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<OperationState, OperationEvent> from, State<OperationState, OperationEvent> to) {
               // System.out.println(String.format("operation stateChanged(from: %s, to %s", from.getIds(), to.getIds()));
            }
        };

        config.withConfiguration()
                .listener(adapter);
    }

}
