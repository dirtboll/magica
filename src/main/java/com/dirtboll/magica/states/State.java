package com.dirtboll.magica.states;

import com.dirtboll.magica.Magica;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class State<T extends IState<T>> implements IState<T> {
    private final List<Function<T, @Nullable IState<?>>> processChain = new ArrayList<>();

    public IState<?> process() {
        IState<?> res;
        for (Function<T, IState<?>> proc : this.getProcessChain()) {
            res = proc.apply((T) this);
            if (res != null) return res;
        }
        res = new EndState();
        if (res != this) {
            this.onChangeState(this, res);
            res.onChangeState(this, res);
        }
        return res;
    }
    public void addProcess(Function<T, @Nullable IState<?>> chain) {
        this.processChain.add(chain);
    };

    public List<Function<T, @Nullable IState<?>>> getProcessChain() {
        return this.processChain;
    };

    public Function<T, @Nullable IState<?>> fallbackProcessFactory(IState<?> nextState, Predicate<T> predicate) {
        return (T state) -> {
            if (predicate == null || !predicate.test(state))
                return null;
            return nextState;
        };
    }

    public void onChangeState(IState<?> from, IState<?> to) {};
}
