package com.mobiwarez.laki.seapp.base;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.mobiwarez.data.util.connectivity.ConnectivityReceiver;
import com.mobiwarez.laki.seapp.configuration.ViewActionQueue;
import com.mobiwarez.laki.seapp.configuration.ViewActionQueueProvider;
import com.mobiwarez.laki.seapp.dagger.application.module.ThreadingModule;
import com.mobiwarez.laki.seapp.ui.router.Router;

import java.lang.ref.WeakReference;


import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
import oxim.digital.reedly.configuration.ViewActionQueue;
import oxim.digital.reedly.configuration.ViewActionQueueProvider;
import oxim.digital.reedly.dagger.application.module.ThreadingModule;
import oxim.digital.reedly.data.util.connectivity.ConnectivityReceiver;
import oxim.digital.reedly.ui.router.Router;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
*/

public abstract class BasePresenter<View extends BaseView> implements ScopedPresenter {

    @Inject
    protected ViewActionQueueProvider viewActionQueueProvider;

    @Inject
    protected ConnectivityReceiver connectivityReceiver;

    @Inject
    protected Router router;

    @Inject
    @Named(ThreadingModule.MAIN_SCHEDULER)
    Scheduler mainThreadScheduler;

    @Inject
    @Named(ThreadingModule.BACKGROUND_SCHEDULER)
    Scheduler backgroundThread;

    private WeakReference<View> viewReference = new WeakReference<>(null);
    private Disposable viewActionsSubscription;

    protected String viewId;
    protected ViewActionQueue<View> viewActionQueue;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public BasePresenter(final View view) {
        viewReference = new WeakReference<>(view);
    }

    @Override
    @CallSuper
    public void start() {
        viewId = getIfViewNotNull(BaseView::getViewId, "");
        viewActionQueue = viewActionQueueProvider.queueFor(viewId);
        subscribeToConnectivityChange();
    }

    private void subscribeToConnectivityChange() {
        addSubscription(connectivityReceiver.getConnectivityStatus()
                                            .observeOn(mainThreadScheduler)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(this::onConnectivityChange, this::logError));
    }

    protected void onConnectivityChange(final boolean isConnected) {
        // Template method
    }

    @Override
    @CallSuper
    public void activate() {
        subscribeToViewActionQueue();
        viewActionQueue.resume();
    }

    protected void subscribeToViewActionQueue() {
        viewActionsSubscription = viewActionQueue.viewActionsObservable()
                                                 .observeOn(mainThreadScheduler)
                                                 .subscribe(this::onViewAction, this::onViewActionQueueError);
    }

    private void onViewActionQueueError(final Throwable throwable) {
        logError(throwable);
        subscribeToViewActionQueue();
    }

    protected void onViewAction(final Consumer<View> viewAction) {
        doIfViewNotNull(viewAction::accept);
    }

    @Override
    @CallSuper
    public void deactivate() {
        viewActionQueue.pause();
        viewActionsSubscription.dispose();
        subscriptions.clear();
    }

    @Override
    @CallSuper
    public void destroy() {
        viewActionQueue.destroy();
        viewActionQueueProvider.dispose(viewId);
    }

    @Override
    public void back() {
        router.goBack();
    }

    protected void addSubscription(final Disposable subscription) {
        if (subscriptions == null || subscriptions.isDisposed()) {
            subscriptions = new CompositeDisposable();
        }
        subscriptions.add(subscription);
    }

    protected final void doIfConnectedToInternet(final Action ifConnected, final Action ifNotConnected) {
        addSubscription(connectivityReceiver.isConnected()
                                            .subscribeOn(backgroundThread)
                                            .observeOn(mainThreadScheduler)
                                            .subscribe(isConnected -> onConnectedToInternet(isConnected, ifConnected, ifNotConnected), this::logError)
        );
    }

    private void onConnectedToInternet(final boolean isConnected, final Action ifConnected, final Action ifNotConnected) {
        try {
            ((isConnected) ? ifConnected : ifNotConnected).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void logError(final Throwable throwable) {
        if (!TextUtils.isEmpty(throwable.getMessage())) {
            // Error reporting, Crashlytics in example
            Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
        }
    }

    protected void doIfViewNotNull(final Consumer<View> whenViewNotNull) {
        final View view = getNullableView();
        if (view != null) {
            try {
                whenViewNotNull.accept(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected <R> R getIfViewNotNull(final Function<View, R> whenViewNotNull, final R defaultValue) {
        final View view = getNullableView();
        if (view != null) {
            try {
                return whenViewNotNull.apply(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    @Nullable
    protected View getNullableView() {
        return viewReference.get();
    }
}
