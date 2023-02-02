package com.dirtboll.magica.states;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class EndState extends State<EndState> {
    public State<?> process() {
        return null;
    }
    public void addProcess(Function<EndState, @Nullable IState<?>> chain) {};
}
