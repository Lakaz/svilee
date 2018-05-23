package com.mobiwarez.laki.sville.ui.toys.sharedtoys;

import com.mobiwarez.data.givenToys.db.model.GivenToyModel;
import com.mobiwarez.laki.sville.base.BasePresenter;
import com.mobiwarez.laki.sville.ui.models.ToyViewModel;
import com.mobiwarez.laki.sville.ui.toys.create.CreateToyContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Laki on 26/10/2017.
 */

public class SharedToysPresenter implements SharedToysContract.Presenter {

    SharedToysContract.View view;

    public SharedToysPresenter(SharedToysContract.View view) {
        this.view = view;
    }

    @Override
    public void loadItems() {

        GivenToyModel givenToyModel = new GivenToyModel();

        givenToyModel.isToySynced = 0;
        givenToyModel.givenLocationName = "Kuils river";
        givenToyModel.longitude = 19.077f;
        givenToyModel.latitude = 12.0945f;
        givenToyModel.userAvatar = "no_image";
        givenToyModel.toyAgeGroup = "babe";
        givenToyModel.toyDescription = "Teddy bear";
        givenToyModel.toyId = "yuuu";
        givenToyModel.toyGiverName = "Laki";
        givenToyModel.giverUUID = "jkijjj";
        givenToyModel.isToyGiven = 0;
        givenToyModel.timePosted = new Date().toString();
        givenToyModel.timeReceived = "nott";
        givenToyModel.toyImagePath = "no_image";
        givenToyModel.toyUrl = "no_image";


/*
        ToyViewModel toyViewModel = new ToyViewModel(
                "mmm",
                "ggg",
                "ted bear",
                "toys",
                "kids",
                "no_image",
                "available",
                "12 minutes ago",
                "Laki",
                "Wallacedene",
                "no_avatar",
                9.00f,
                12.78f,
                "home",
                "any time",
                "uu"
        );

        List<GivenToyModel> toyViewModels = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            toyViewModels.add(givenToyModel);
        }

        view.showItems(toyViewModels);
*/

    }

    @Override
    public void deleteItem() {

    }

    @Override
    public void createNewItem() {

    }

    @Override
    public void updateItem() {

    }

    @Override
    public void editCategory() {

    }

    @Override
    public void editAgeGroup() {

    }

    @Override
    public void editImage() {

    }
}
