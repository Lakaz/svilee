package com.mobiwarez.laki.seapp.ui.router;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mobiwarez.laki.seapp.R;
import com.mobiwarez.laki.seapp.dagger.activity.ActivityScope;
import com.mobiwarez.laki.seapp.ui.starter.DashboardFragment;
import com.mobiwarez.laki.seapp.ui.toys.create.NewToActivity;
import com.mobiwarez.laki.seapp.ui.toys.itemcategories.ItemCategoryFragment;
import com.mobiwarez.laki.seapp.ui.toys.list.ItemListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

import io.reactivex.functions.Consumer;

/**
 * Created by Laki on 27/10/2017.
 */

@ActivityScope
public class RouterImp implements Router {

    private static final int LAST_FRAGMENT = 0;
    private final Activity activity;
    private final FragmentManager fragmentManager;

    public RouterImp(final Activity activity, final FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public void closeScreen() {
        activity.finish();
    }

    @Override
    public void goBack() {
        if (fragmentManager.getBackStackEntryCount() == LAST_FRAGMENT) {
            closeScreen();
        } else {
            fragmentManager.popBackStack();
        }

    }

    @Override
    public void showChatScreen() {

    }

    @Override
    public void showToysScreen(@NotNull String category, @NotNull String ageGroup) {
/*
        advanceToFragment(ItemListFragment.TAG, ItemCategoryFragment.TAG,
                () -> ItemListFragment.newInstance(),
                articlesFragment -> articlesFragment.fetchItems(category, ageGroup));


        advanceToFragment(ItemListFragment.TAG, ItemCategoryFragment.TAG,
                ItemListFragment::newInstance,
                ItemListFragment::fetchItems(category, ageGroup)


        advanceToFragment(ItemListFragment.TAG, ItemCategoryFragment.TAG,
                () -> ItemListFragment.newInstance(),
                articlesFragment -> articlesFragment.fetchItems(category, ageGroup));
*/


/*
        final Fragment fragment = ItemListFragment.Companion.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.dash_container, fragment, ItemListFragment.Companion.getTAG())
                .commit();
*/


    }

    @Override
    public void showSearchSetUpScreen() {
        final Fragment fragment = ItemCategoryFragment.newInstance();
        fragmentManager.beginTransaction()
                .add(R.id.dash_container, fragment, ItemCategoryFragment.TAG)
                .commit();
    }

    @Override
    public void showAddNewToyScreen() {
        Intent newToyIntent = new Intent(activity, NewToActivity.class);
        activity.startActivity(newToyIntent);
    }

    @Override
    public void hideAddNewToyScreen() {

    }

    @Override
    public void showZoneScreen() {

    }

    @Override
    public void showGivenToysScreen() {

    }

    @Override
    public void showReceivedToysScreen() {

    }

    @Override
    public void showReceiveSetUpScreen() {
        advanceToFragment(ItemCategoryFragment.TAG, DashboardFragment.TAG,
                ItemCategoryFragment::newInstance,
                ItemCategoryFragment::getLocation);
    }

    private <T extends Fragment> void advanceToFragment(final String destinationFragmentTag, final String sourceFragmentTag, final Callable<T> destinationFragmentFactory,
                                                        final Consumer<T> destinationFragmentExistsAction) {
        T destinationFragment = (T) fragmentManager.findFragmentByTag(destinationFragmentTag);
        final Fragment sourceFragment = fragmentManager.findFragmentByTag(sourceFragmentTag);

        if (destinationFragment == null) {
            try {
                destinationFragment = destinationFragmentFactory.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_left_enter, R.anim.fragment_right_exit, R.anim.fragment_right_enter, R.anim.fragment_left_exit)
                    .addToBackStack(null)
                    .hide(sourceFragment)
                    .add(R.id.dash_container, destinationFragment, ItemListFragment.Companion.getTAG())
                    .commit();
        } else {
            try {
                //destinationFragmentExistsAction.accept(((ItemListFragment)destinationFragment).fetchItems());
                destinationFragmentFactory.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .hide(sourceFragment)
                    .show(destinationFragment)
                    .commit();
        }
    }

}
