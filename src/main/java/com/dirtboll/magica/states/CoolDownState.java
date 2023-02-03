package com.dirtboll.magica.states;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Data
public class CoolDownState extends State<CoolDownState> {
    @NonNull private long coolDownMs;

    @Setter(AccessLevel.PRIVATE) private long startTimeStamp = -1;

    @Override
    public void onChangeState(IState<?> from, IState<?> to) {
        super.onChangeState(from, to);
        if (to == this)
            this.startTimeStamp = System.currentTimeMillis();
        else
            this.startTimeStamp = -1;
    }

    public static Function<CoolDownState, @Nullable IState<?>> coolDownProcessFactory() {
        return (CoolDownState state) -> {
            if (state.startTimeStamp < 0)
                state.startTimeStamp = System.currentTimeMillis();

            if (System.currentTimeMillis() - state.startTimeStamp > state.getCoolDownMs())
                return null;

            return state;
        };
    }

    public static Function<CoolDownState, @Nullable IState<?>> fallbackProcessFactory(IState<?> nextState) {
        return (state -> nextState);
    }
}
