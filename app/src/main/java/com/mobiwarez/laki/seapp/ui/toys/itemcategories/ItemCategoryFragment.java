package com.mobiwarez.laki.seapp.ui.toys.itemcategories;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobiwarez.laki.seapp.R;
import com.mobiwarez.laki.seapp.base.BaseFragment;
import com.mobiwarez.laki.seapp.base.ScopedPresenter;
import com.mobiwarez.laki.seapp.dagger.fragment.FragmentComponent;
import com.mobiwarez.laki.seapp.ui.toys.create.ItemCategoryDialogFragment;
import com.mobiwarez.laki.seapp.ui.toys.list.ItemListActivity;
import com.szugyi.circlemenu.view.CircleLayout;

import javax.inject.Inject;

public class ItemCategoryFragment extends Fragment implements ItemCategoryContract.View, CircleLayout.OnItemSelectedListener,
        CircleLayout.OnItemClickListener, CircleLayout.OnRotationFinishedListener, CircleLayout.OnCenterClickListener {

    public static final String TAG = ItemCategoryFragment.class.getSimpleName();


    private View view;
    private String selectedCategory;
    private CircleLayout circleLayout;
    private TextView ageGroupText;
    private ImageView centerImage;
    private TextView selectedCategoryTextView;

    ItemCategoryPresenter presenter;

    public ItemCategoryFragment() {
        // Required empty public constructor
    }

    public static ItemCategoryFragment newInstance() {
        return  new ItemCategoryFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        presenter = new ItemCategoryPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_category, container, false);
        circleLayout = view.findViewById(R.id.circle_layout);
        ageGroupText = view.findViewById(R.id.age_group);
        selectedCategoryTextView = view.findViewById(R.id.item_category);
        centerImage = view.findViewById(R.id.start_receive_image);
        ageGroupText.setOnClickListener(v -> showAgeGroups());
        centerImage.setOnClickListener(v -> showItems());
        setUpCircleMenu();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Drawable leftDrawable = AppCompatResources
                    .getDrawable(getContext(), R.drawable.ic_arrow_drop_down_circle_white_24dp);
            ageGroupText.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);
        }
        else
        {
            //Safely create our VectorDrawable on pre-L android versions.
            Drawable leftDrawable = VectorDrawableCompat
                    .create(getContext().getResources(), R.drawable.ic_arrow_drop_down_circle_white_24dp, null);
            ageGroupText.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);
        }



        return view;
    }

    private void showItems() {
        String ageGroup = ageGroupText.getText().equals("Select Age Group") ? "all" : ageGroupText.getText().toString();

        //String ageGroup = TextUtils.isEmpty(ageGroupText.getText()) ? "all" : ageGroupText.getText().toString();
        String category = TextUtils.isEmpty(selectedCategory) ? "all" : selectedCategory;
        //presenter.moveToItemList(ageGroup, category);
        //String ageGroup = TextUtils.isEmpty(ageGroupText.getText()) ? "all" : ageGroupText.getText().toString();
        //String category = TextUtils.isEmpty(selectedCategory) ? "all" : selectedCategory;
        //presenter.moveToItemList(ageGroup, category);

        ageGroup = ageGroup.equals("All") ? "all" : ageGroup;

        Intent intent = new Intent(getActivity(), ItemListActivity.class);
        intent.putExtra("age", ageGroup);
        intent.putExtra("category", category);
        startActivity(intent);

    }

    private void setUpCircleMenu() {
        circleLayout.setOnItemSelectedListener(this);
        circleLayout.setOnItemClickListener(this);
        circleLayout.setOnRotationFinishedListener(this);
        circleLayout.setOnCenterClickListener(this);
        circleLayout.setFirstChildPosition(CircleLayout.FirstChildPosition.NORTH);
    }

    public void getLocation(){
        presenter.setLocation();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showAgeGroups() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Item Category")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setItems(R.array.age_group_array, (dialog, which) -> {
                    String ageGroup = ((AlertDialog)dialog).getListView().getAdapter().getItem(which).toString();

                    ageGroupText.setText(ageGroup);
                });

        builder.show();


        //ItemCategoryDialogFragment dialogFragment = ItemCategoryDialogFragment.newInstance();
        //dialogFragment.show(getActivity().getSupportFragmentManager(), dialogFragment);
    }

    @Override
    public void onCenterClick() {
/*
        String ageGroup = TextUtils.isEmpty(ageGroupText.getText()) ? "all" : ageGroupText.getText().toString();
        String category = TextUtils.isEmpty(selectedCategory) ? "all" : selectedCategory;
        //presenter.moveToItemList(ageGroup, category);

        Intent intent = new Intent(getActivity(), ItemListActivity.class);
        startActivity(intent);
*/
    }

    @Override
    public void onItemClick(View view) {

        switch (view.getId()) {

            case R.id.any_icon:
                selectedCategory = "all";
                selectedCategoryTextView.setText("All");
                break;


            case R.id.arts_icon:
                selectedCategory = "Arts and Craft";
                selectedCategoryTextView.setText("Arts & Craft");
                break;

            case R.id.books_icon:
                selectedCategory = "Books";
                selectedCategoryTextView.setText("Books");
                break;

            case R.id.boy_clothes_icon:
                selectedCategory = "Boys Clothes";
                selectedCategoryTextView.setText("Boy's Clothes");
                break;

            case R.id.girl_clothes_icon:
                selectedCategory = "Girls Clothes";
                selectedCategoryTextView.setText("Girl's Clothes");
                break;

            case R.id.decor_icon:
                selectedCategory = "Kids Decor";
                selectedCategoryTextView.setText("Kid's Decor");
                break;

            case R.id.meal_icon:
                selectedCategory = "Meal Times";
                selectedCategoryTextView.setText("Meal Times");
                break;

            case R.id.toys_icon:
                selectedCategory = "Toys and Games";
                selectedCategoryTextView.setText("Toys & Games");
                break;

        }

    }

    @Override
    public void onItemSelected(View view) {

    }

    @Override
    public void onRotationFinished(View view) {
        switch (view.getId()) {

            case R.id.any_icon:
                selectedCategory = "all";
                selectedCategoryTextView.setText("All");
                break;


            case R.id.arts_icon:
                selectedCategory = "Arts and Craft";
                selectedCategoryTextView.setText("Arts & Craft");
                break;

            case R.id.books_icon:
                selectedCategory = "Books";
                selectedCategoryTextView.setText("Books");
                break;

            case R.id.boy_clothes_icon:
                selectedCategory = "Boys Clothes";
                selectedCategoryTextView.setText("Boy's Clothes");
                break;

            case R.id.girl_clothes_icon:
                selectedCategory = "Girls Clothes";
                selectedCategoryTextView.setText("Girl's Clothes");
                break;

            case R.id.decor_icon:
                selectedCategory = "Kids Decor";
                selectedCategoryTextView.setText("Kid's Decor");
                break;

            case R.id.meal_icon:
                selectedCategory = "Meal Times";
                selectedCategoryTextView.setText("Meal Times");
                break;

            case R.id.toys_icon:
                selectedCategory = "Toys and Games";
                selectedCategoryTextView.setText("Toys & Games");
                break;

        }

    }


}
