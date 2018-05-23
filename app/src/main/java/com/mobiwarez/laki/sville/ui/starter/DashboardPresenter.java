package com.mobiwarez.laki.sville.ui.starter;

import com.mobiwarez.laki.sville.base.BasePresenter;
import com.mobiwarez.laki.sville.ui.router.Router;
import com.mobiwarez.laki.sville.ui.router.Router;

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
