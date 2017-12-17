package com.mobiwarez.laki.seapp.dagger.activity.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.mobiwarez.laki.seapp.DashBoardActivity;
import com.mobiwarez.laki.seapp.dagger.activity.ActivityScope;
import com.mobiwarez.laki.seapp.dagger.activity.DaggerActivity;
import com.mobiwarez.laki.seapp.dagger.activity.ForActivity;
import com.mobiwarez.laki.seapp.ui.MainPresenter;
import com.mobiwarez.laki.seapp.ui.router.Router;
import com.mobiwarez.laki.seapp.ui.router.RouterImp;
import com.mobiwarez.laki.seapp.ui.router.RouterImpl;
import com.mobiwarez.laki.seapp.ui.toys.create.NewToActivity;
import com.mobiwarez.laki.seapp.ui.toys.create.NewToyActivityPresenter;
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.SharedToysActivity;
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.SharedToysActivityPresenter;

import dagger.Module;
import dagger.Provides;
/*
import oxim.digital.reedly.MainActivity;
import oxim.digital.reedly.dagger.activity.ActivityScope;
import oxim.digital.reedly.dagger.activity.DaggerActivity;
import oxim.digital.reedly.dagger.activity.ForActivity;
import oxim.digital.reedly.domain.util.ActionRouter;
import oxim.digital.reedly.domain.util.ActionRouterImpl;
import oxim.digital.reedly.ui.MainPresenter;
import oxim.digital.reedly.ui.router.Router;
import oxim.digital.reedly.ui.router.RouterImpl;
*/

@Module
public class ActivityModule {

    private final DaggerActivity daggerActivity;

    public ActivityModule(final DaggerActivity daggerActivity) {
        this.daggerActivity = daggerActivity;
    }

    @Provides
    @ActivityScope
    @ForActivity
    Context provideActivityContext() {
        return daggerActivity;
    }

    @Provides
    @ActivityScope
    Activity provideActivity() {
        return daggerActivity;
    }

    @Provides
    @ActivityScope
    FragmentManager provideFragmentManager() {
        return daggerActivity.getSupportFragmentManager();
    }

    @Provides
    @ActivityScope
    Router provideRouter(final FragmentManager fragmentManager) {
        return new RouterImp(daggerActivity, fragmentManager);
    }

/*
    @Provides
    @ActivityScope
    ActionRouter provideActionRouter() {
        return new ActionRouterImpl();
    }
*/

/*
    @Provides
    @ActivityScope
    MainPresenter provideMainPresenter() {
        final MainPresenter mainPresenter = new MainPresenter((DashBoardActivity) daggerActivity);
        daggerActivity.getActivityComponent().inject(mainPresenter);
        return mainPresenter;
    }
*/


/*
    @Provides
    @ActivityScope
    NewToyActivityPresenter provideNewToyActivityPresenter() {
        final NewToyActivityPresenter newToyActivityPresenter = new NewToyActivityPresenter((NewToActivity) daggerActivity);
        daggerActivity.getActivityComponent().inject(newToyActivityPresenter);
        return newToyActivityPresenter;
    }
*/

/*
    @Provides
    @ActivityScope
    SharedToysActivityPresenter provideSharedToyActivityPresenter() {
        final SharedToysActivityPresenter sharedToysActivityPresenter = new SharedToysActivityPresenter((SharedToysActivity) daggerActivity);
        daggerActivity.getActivityComponent().inject(sharedToysActivityPresenter);
        return sharedToysActivityPresenter;
    }
*/



    public interface Exposes {

        Activity activity();

        @ForActivity
        Context context();

        FragmentManager fragmentManager();

        Router router();

        //ActionRouter actionRouter();
    }
}
