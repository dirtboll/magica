package com.dirtboll.magica.states;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CoolDownState extends State<CoolDownState> {
    private long coolDownTime;
    private long lastTimeStamp;
    private long timeLeft;

    public long getCoolDownTimeMilis() {
        return coolDownTime;
    }

    public void setCoolDownTimeMilis(long coolDownTime) {
        this.coolDownTime = coolDownTime;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void restart() {
        this.setTimeLeft(this.getCoolDownTimeMilis());
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public static Function<CoolDownState, @Nullable IState<?>> waitProcessFactory(IState<?> nextState) {
        return (CoolDownState state) -> {
            var currentTimeStamp = System.currentTimeMillis();
            var diff = currentTimeStamp - state.getLastTimeStamp();
            var timeLeft = state.getTimeLeft() - diff;

            if (timeLeft < 0)
                return nextState;

            state.setTimeLeft(timeLeft);
            state.setLastTimeStamp(currentTimeStamp);
            return state;
        };
    }
}
