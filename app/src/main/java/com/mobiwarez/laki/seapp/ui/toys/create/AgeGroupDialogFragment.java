package com.mobiwarez.laki.seapp.ui.toys.create;


import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiwarez.laki.seapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgeGroupDialogFragment extends DialogFragment {

    private NewToyViewModel newToyViewModel;

    public static final String TAG = AgeGroupDialogFragment.class.getSimpleName();


    public AgeGroupDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newToyViewModel = ViewModelProviders.of(getActivity()).get(NewToyViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Item Category")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setItems(R.array.category_array, (dialog, which) -> {
                    String category = ((AlertDialog)dialog).getListView().getAdapter().getItem(which).toString();
                    newToyViewModel.setCategory(category);
                });
        return builder.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    public static AgeGroupDialogFragment newInstance() {
        AgeGroupDialogFragment fragment = new AgeGroupDialogFragment();
        return fragment;
    }


}
