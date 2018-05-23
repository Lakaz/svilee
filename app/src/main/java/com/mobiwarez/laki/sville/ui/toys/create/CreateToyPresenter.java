package com.mobiwarez.laki.sville.ui.toys.create;

import android.location.Address;
import android.location.Location;
import android.net.Uri;

import com.mobiwarez.device.utils.Locator;
import com.mobiwarez.domain.domain.intereactions.toy.AddNewToyUseCase;
import com.mobiwarez.domain.domain.models.Toy;
import com.mobiwarez.laki.sville.base.BasePresenter;
import com.mobiwarez.laki.sville.util.ImageLoader;
import com.mobiwarez.laki.sville.util.PhotoTaker;
import com.mobiwarez.laki.sville.util.PhotoTaker;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

/**
 * Created by Laki on 25/10/2017.
 */

public class CreateToyPresenter implements CreateToyContract.Presenter {


    private CreateToyContract.View view;


    private Location currentLocation;

    private PhotoTaker photoTaker;


    public CreateToyPresenter(CreateToyContract.View view) {
        this.view = view;

    }

    @Override
    public void loadPhotoFromLibraray() {
        //viewActionQueue.subscribeTo(photoTaker.loadFromGallery().doOnNext(uri -> toViewAction((Uri) uri)),throwable-> cant(), t -> cant());
    }

    private void cant(){

    }

/*
    private Consumer<CreateToyContract.View> toViewAction(final Uri uri) {
        return view -> view.showPhoto(uri);
    }

    private Consumer<CreateToyContract.View> toLocation(Location location) {
        return view -> currentLocation = location;
    }

    private Consumer<CreateToyContract.View> toAddressesAction(List<Address> addresses) {
        return view -> view.showPhoto("");
    }
*/


    @Override
    public void takePictureFromCamera() {

    }

    @Override
    public void addNewToy() {

        Toy toy = new Toy("jjj", "uuuu", "kkk", "yy", 0,"");
        //viewActionQueue.subscribeTo(addNewToyUseCase.execute(toy).toSingleDefault(true).map(toViewAction(new Uri(""))),Throwable::printStackTrace);
    }

    @Override
    public void acquireLocation() {
        //viewActionQueue.subscribeTo(locator.getLocation().doOnNext(location -> toLocation(location)).map(this::toLocation),ee -> disableLocation(), e -> showError());
    }

    @Override
    public void acquireAdresses() {
        //viewActionQueue.subscribeTo(locator.getAddresses(currentLocation).doOnSuccess(addresses -> toAddressesAction(addresses)).map(this::toAddressesAction),throwable -> showError());

    }

    private void showError(){

    }

    private void disableLocation(){

    }


}
