package com.dirtboll.magica.states;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public interface IState<T extends IState<T>> {
    IState<?> process();
    void addProcess(Function<T, @Nullable IState<?>> chain);
    List<Function<T, @Nullable IState<?>>> getProcessChain();
}
