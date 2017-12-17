package com.mobiwarez.laki.seapp.dagger.fragment;

import android.os.Bundle;

//import oxim.digital.reedly.dagger.ComponentFactory;
import com.mobiwarez.laki.seapp.dagger.ComponentFactory;
import com.mobiwarez.laki.seapp.dagger.activity.DaggerActivity;

//import oxim.digital.reedly.dagger.activity.DaggerActivity;

public abstract class DaggerFragment extends android.support.v4.app.Fragment {

    private FragmentComponent fragmentComponent;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(getFragmentComponent());
    }

    protected abstract void inject(FragmentComponent fragmentComponent);

    public FragmentComponent getFragmentComponent() {
        if (fragmentComponent == null) {
            fragmentComponent = ComponentFactory.createFragmentComponent(this, getDaggerActivity().getActivityComponent());
        }

        return fragmentComponent;
    }

    private DaggerActivity getDaggerActivity() {
        return ((DaggerActivity) getActivity());
    }
}
