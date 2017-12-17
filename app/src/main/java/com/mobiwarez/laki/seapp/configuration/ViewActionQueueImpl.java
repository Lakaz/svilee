package com.mobiwarez.laki.seapp.configuration;

import android.location.Location;

import java.util.Iterator;
import java.util.LinkedList;


import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;


public final class ViewActionQueueImpl<View> implements ViewActionQueue<View> {

    private final LinkedList<Consumer<View>> viewActions = new LinkedList<>();
    private final Object queueLock = new Object();

    private final PublishSubject<Consumer<View>> viewActionSubject = PublishSubject.create();
    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final Scheduler observeScheduler;

    private boolean isPaused = true;

    public ViewActionQueueImpl(final Scheduler observeScheduler) {
        this.observeScheduler = observeScheduler;
    }


    @Override
    public void subscribeTo(Observable<Consumer<View>> observable, Consumer<View> onCompleteAction, Consumer<Throwable> errorAction) {
        subscriptions.add(observable.observeOn(observeScheduler).subscribe(this::onResult, errorAction::accept, () -> onResult(onCompleteAction)));
    }



/*
    public void subscribeToLoc(Observable<Location> observable, Consumer<View> onCompleteAction, Consumer<Throwable> errorAction) {
        subscriptions.add(observable.observeOn(observeScheduler).subscribe(this::onResult, errorAction::accept, () -> onResult(onCompleteAction)));
    }
*/



    @Override
    public void subscribeTo(final Single<Consumer<View>> single, final Consumer<Throwable> errorAction) {
        subscriptions.add(single.observeOn(observeScheduler).subscribe(this::onResult, errorAction::accept));
    }

    @Override
    public void subscribeTo(final Completable completable, final Consumer<View> onCompleteAction, final Consumer<Throwable> errorAction) {
        subscriptions.add(completable.observeOn(observeScheduler).subscribe(() -> onResult(onCompleteAction), errorAction::accept));
    }


    private void onResultLocation(){
    }

    private void onResult(final io.reactivex.functions.Consumer<View> resultAction) {
        if (isPaused) {
            synchronized (queueLock) {
                viewActions.add(resultAction);
            }
        } else {
            viewActionSubject.onNext(resultAction);
        }
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
        consumeQueue();
    }

    @Override
    public void destroy() {
        subscriptions.clear();
        viewActionSubject.onComplete();
    }

    private void consumeQueue() {
        synchronized (queueLock) {
            final Iterator<Consumer<View>> actionIterator = viewActions.iterator();
            while (actionIterator.hasNext()) {
                final Consumer<View> pendingViewAction = actionIterator.next();
                viewActionSubject.onNext(pendingViewAction);
                actionIterator.remove();
            }
        }
    }

    @Override
    public Observable<Consumer<View>> viewActionsObservable() {
        return viewActionSubject;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ViewActionQueueImpl<?> that = (ViewActionQueueImpl<?>) o;

        if (isPaused != that.isPaused) {
            return false;
        }
        if (viewActions != null ? !viewActions.equals(that.viewActions) : that.viewActions != null) {
            return false;
        }
        if (queueLock != null ? !queueLock.equals(that.queueLock) : that.queueLock != null) {
            return false;
        }
        if (viewActionSubject != null ? !viewActionSubject.equals(that.viewActionSubject) : that.viewActionSubject != null) {
            return false;
        }
        if (subscriptions != null ? !subscriptions.equals(that.subscriptions) : that.subscriptions != null) {
            return false;
        }
        return observeScheduler != null ? observeScheduler.equals(that.observeScheduler) : that.observeScheduler == null;
    }

    @Override
    public int hashCode() {
        int result = viewActions != null ? viewActions.hashCode() : 0;
        result = 31 * result + (queueLock != null ? queueLock.hashCode() : 0);
        result = 31 * result + (viewActionSubject != null ? viewActionSubject.hashCode() : 0);
        result = 31 * result + (subscriptions != null ? subscriptions.hashCode() : 0);
        result = 31 * result + (observeScheduler != null ? observeScheduler.hashCode() : 0);
        result = 31 * result + (isPaused ? 1 : 0);
        return result;
    }
}
