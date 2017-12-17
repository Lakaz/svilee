package com.mobiwarez.domain.domain.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Action;


public interface ActionRouter {

    void setTiming(long windowDuration, TimeUnit timeUnit);

    void throttle(Action action);

    void throttle(long windowDuration, TimeUnit timeUnit, Action action);

    void blockActions();

    void unblockActions();
}
