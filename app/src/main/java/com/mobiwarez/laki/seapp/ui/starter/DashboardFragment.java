package com.mobiwarez.laki.seapp.ui.starter;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobiwarez.laki.seapp.R;
import com.mobiwarez.laki.seapp.base.BaseFragment;
import com.mobiwarez.laki.seapp.base.ScopedPresenter;
import com.mobiwarez.laki.seapp.dagger.fragment.FragmentComponent;
import com.mobiwarez.laki.seapp.ui.toys.create.ItemCategoryDialogFragment;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements DashContract.View {


    public static final String TAG = DashboardFragment.class.getSimpleName();

    private Button receieveButton;
    private Button giveButton;

    private Navigator mListener;

    //@Inject
    //DashContract.Presenter presenter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        receieveButton = view.findViewById(R.id.receive_button);
        giveButton = view.findViewById(R.id.give_button);

        receieveButton.setOnClickListener(v -> mListener.startReceive());

        giveButton.setOnClickListener(v -> mListener.startGive());
        return view;
    }


    public interface Navigator {
        void startGive();

        void startReceive();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DashboardFragment.Navigator) {
            mListener = (DashboardFragment.Navigator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



/*
    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public ScopedPresenter getPresenter() {
        return presenter;
    }
*/
}
