package com.mobiwarez.laki.sville.dagger.fragment;

/*
import oxim.digital.reedly.ui.article.content.ArticleContentFragment;
import oxim.digital.reedly.ui.article.content.ArticleContentPresenter;
import oxim.digital.reedly.ui.feed.create.NewFeedSubscriptionFragment;
import oxim.digital.reedly.ui.feed.create.NewFeedSubscriptionPresenter;
import oxim.digital.reedly.ui.article.list.ArticlesFragment;
import oxim.digital.reedly.ui.article.list.ArticlesPresenter;
import oxim.digital.reedly.ui.feed.subscription.UserSubscriptionsFragment;
import oxim.digital.reedly.ui.feed.subscription.UserSubscriptionsPresenter;
*/

import com.mobiwarez.laki.sville.ui.starter.DashboardFragment;
import com.mobiwarez.laki.sville.ui.starter.DashboardPresenter;
import com.mobiwarez.laki.sville.ui.toys.create.CreateToyFragment;
import com.mobiwarez.laki.sville.ui.toys.create.CreateToyPresenter;
import com.mobiwarez.laki.sville.ui.toys.itemcategories.ItemCategoryFragment;
import com.mobiwarez.laki.sville.ui.toys.itemcategories.ItemCategoryPresenter;
import com.mobiwarez.laki.sville.ui.toys.list.ItemListFragment;
import com.mobiwarez.laki.sville.ui.toys.list.ItemListPresenter;
import com.mobiwarez.laki.sville.ui.toys.sharedtoys.SharedToysFragment;
import com.mobiwarez.laki.sville.ui.toys.sharedtoys.SharedToysPresenter;

public interface FragmentComponentInjects {


    void inject(DashboardFragment dashboardFragment);
    void inject(DashboardPresenter dashboardPresenter);

    void inject(CreateToyFragment createToyFragment);
    void inject(CreateToyPresenter createToyPresenter);

    void inject(SharedToysFragment sharedToysFragment);
    void inject(SharedToysPresenter sharedToysPresenter);

    //void inject(ItemCategoryFragment itemCategoryFragment);
    //void inject(ItemCategoryPresenter itemCategoryPresenter);

    //void inject(ItemListFragment itemListFragment);
    //void inject(ItemListPresenter itemListPresenter);


/*
    void inject(UserSubscriptionsFragment userSubscriptionsFragment);
    void inject(UserSubscriptionsPresenter userSubscriptionsPresenter);
*/

    //void inject(ToysFragment toysFragment);
    //void inject(ToysPresenter toysPresenter);

    //void inject(NewToyFragment newToyFragment);
    //void inject(NewToyPresenter newToyPresenter);

/*
    void inject(ArticleContentFragment articleContentFragment);
    void inject(ArticleContentPresenter articleContentPresenter);

    void inject(NewFeedSubscriptionFragment newFeedSubscriptionFragment);
    void inject(NewFeedSubscriptionPresenter newFeedSubscriptionPresenter);
*/
}
