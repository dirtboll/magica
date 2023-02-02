package com.dirtboll.magica.states;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class State<T extends IState<T>> implements IState<T> {
    private final List<Function<T, @Nullable IState<?>>> processChain = new ArrayList<>();

    public IState<?> process() {
        for (Function<T, IState<?>> proc : this.getProcessChain()) {
            var res = proc.apply((T) this);
            if (res != null) return res;
        }
        return new EndState();
    }
    public void addProcess(Function<T, @Nullable IState<?>> chain) {
        this.processChain.add(chain);
    };

    public List<Function<T, @Nullable IState<?>>> getProcessChain() {
        return this.processChain;
    };
}
