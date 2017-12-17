package com.mobiwarez.laki.seapp.ui.starter;

import com.mobiwarez.laki.seapp.base.BasePresenter;
import com.mobiwarez.laki.seapp.ui.router.Router;

import javax.inject.Inject;

/**
 * Created by Laki on 25/10/2017.
 */

public class DashboardPresenter implements DashContract.Presenter {

    @Inject
    Router router;

    public DashboardPresenter(DashContract.View view) {

    }

    @Override
    public void startGive() {
        router.showAddNewToyScreen();
    }

    @Override
    public void startReceive() {
        router.showSearchSetUpScreen();
    }
}
