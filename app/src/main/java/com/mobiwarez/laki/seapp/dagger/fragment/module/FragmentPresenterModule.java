package com.mobiwarez.laki.seapp.dagger.fragment.module;

import com.mobiwarez.laki.seapp.dagger.fragment.DaggerFragment;
import com.mobiwarez.laki.seapp.dagger.fragment.FragmentComponent;
import com.mobiwarez.laki.seapp.dagger.fragment.FragmentScope;
import com.mobiwarez.laki.seapp.ui.starter.DashContract;
import com.mobiwarez.laki.seapp.ui.starter.DashboardPresenter;
import com.mobiwarez.laki.seapp.ui.toys.create.CreateToyContract;
import com.mobiwarez.laki.seapp.ui.toys.create.CreateToyPresenter;
import com.mobiwarez.laki.seapp.ui.toys.itemcategories.ItemCategoryContract;
import com.mobiwarez.laki.seapp.ui.toys.itemcategories.ItemCategoryPresenter;
import com.mobiwarez.laki.seapp.ui.toys.list.ItemListPresenter;
import com.mobiwarez.laki.seapp.ui.toys.list.ItemsContract;
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.SharedToysContract;
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.SharedToysPresenter;

import dagger.Module;
import dagger.Provides;
/*
import oxim.digital.reedly.dagger.fragment.DaggerFragment;
import oxim.digital.reedly.dagger.fragment.FragmentComponent;
import oxim.digital.reedly.dagger.fragment.FragmentScope;
import oxim.digital.reedly.ui.article.content.ArticleContentContract;
import oxim.digital.reedly.ui.article.content.ArticleContentPresenter;
import oxim.digital.reedly.ui.feed.create.NewFeedSubscriptionContract;
import oxim.digital.reedly.ui.feed.create.NewFeedSubscriptionPresenter;
import oxim.digital.reedly.ui.article.list.ArticlesContract;
import oxim.digital.reedly.ui.article.list.ArticlesPresenter;
import oxim.digital.reedly.ui.feed.subscription.UserSubscriptionsContract;
import oxim.digital.reedly.ui.feed.subscription.UserSubscriptionsPresenter;
*/

@Module
public final class FragmentPresenterModule {

    private final DaggerFragment daggerFragment;

    public FragmentPresenterModule(final DaggerFragment daggerFragment) {
        this.daggerFragment = daggerFragment;
    }

    private FragmentComponent getFragmentComponent() {
        return daggerFragment.getFragmentComponent();
    }

    @Provides
    @FragmentScope
    public SharedToysContract.Presenter provideSharedToyPresenter() {
        final SharedToysPresenter sharedToysPresenter = new SharedToysPresenter((SharedToysContract.View) daggerFragment);
        getFragmentComponent().inject(sharedToysPresenter);
        return sharedToysPresenter;
    }

    @Provides
    @FragmentScope
    public DashContract.Presenter provideDashContractPresenter() {
        final DashboardPresenter dashboardPresenter = new DashboardPresenter((DashContract.View) daggerFragment);
        getFragmentComponent().inject(dashboardPresenter);
        return dashboardPresenter;
    }



    @Provides
    @FragmentScope
    public CreateToyContract.Presenter provideCreateToysContractPresenter() {
        final CreateToyPresenter createToyPresenter = new CreateToyPresenter((CreateToyContract.View) daggerFragment);
        getFragmentComponent().inject(createToyPresenter);
        return createToyPresenter;
    }


/*
    @Provides
    @FragmentScope
    public ItemCategoryPresenter provideItemCategoryContractPresenter() {
        final ItemCategoryPresenter itemCategoryPresenter = new ItemCategoryPresenter((ItemCategoryContract.View) daggerFragment);
        getFragmentComponent().inject(itemCategoryPresenter);
        return itemCategoryPresenter;
    }
*/


/*
    @Provides
    @FragmentScope
    public ItemsContract.Presenter provideItemsContractPresenter() {
        final ItemListPresenter itemListPresenter = new ItemListPresenter((ItemsContract.View) daggerFragment);
        getFragmentComponent().inject(itemListPresenter);
        return itemListPresenter;
    }
*/


/*
    @Provides
    @FragmentScope
    public UserSubscriptionsContract.Presenter provideUserSubscriptionsPresenter() {
        final UserSubscriptionsPresenter userSubscriptionsPresenter = new UserSubscriptionsPresenter((UserSubscriptionsContract.View) daggerFragment);
        getFragmentComponent().inject(userSubscriptionsPresenter);
        return userSubscriptionsPresenter;
    }

    @Provides
    @FragmentScope
    public ArticlesContract.Presenter provideArticlesPresenter() {
        final ArticlesPresenter articlesPresenter = new ArticlesPresenter((ArticlesContract.View) daggerFragment);
        getFragmentComponent().inject(articlesPresenter);
        return articlesPresenter;
    }

    @Provides
    @FragmentScope
    public ArticleContentContract.Presenter provideArticleContentPresenter() {
        final ArticleContentPresenter articleContentPresenter = new ArticleContentPresenter((ArticleContentContract.View) daggerFragment);
        getFragmentComponent().inject(articleContentPresenter);
        return articleContentPresenter;
    }

    @Provides
    @FragmentScope
    public NewFeedSubscriptionContract.Presenter provideNewFeedSubscriptionPresenter() {
        final NewFeedSubscriptionPresenter newFeedSubscriptionPresenter = new NewFeedSubscriptionPresenter((NewFeedSubscriptionContract.View) daggerFragment);
        getFragmentComponent().inject(newFeedSubscriptionPresenter);
        return newFeedSubscriptionPresenter;
    }
*/
}
