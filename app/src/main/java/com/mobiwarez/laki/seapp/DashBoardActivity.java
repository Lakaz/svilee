package com.mobiwarez.laki.seapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.mobiwarez.laki.seapp.prefs.GetUserData;
import com.mobiwarez.laki.seapp.ui.auth.SignInActivity;
import com.mobiwarez.laki.seapp.ui.contacts.ContactsActivity;
import com.mobiwarez.laki.seapp.ui.profile.ProfileActivity;
import com.mobiwarez.laki.seapp.ui.starter.DashboardFragment;
import com.mobiwarez.laki.seapp.ui.toys.create.NewToActivity;
import com.mobiwarez.laki.seapp.ui.toys.itemcategories.ItemCategoryFragment;
import com.mobiwarez.laki.seapp.ui.toys.itemcategories.SearchSetUpActivity;
import com.mobiwarez.laki.seapp.ui.toys.receivedToys.ReceivedToysActivity;
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.SharedToysActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DashboardFragment.Navigator {

/*
    @Inject
    MainPresenter presenter;

    @Inject
    FragmentManager fragmentManager;

    @Inject
    ActivityUtils activityUtils;
*/


    private TextView userNameTextView, userEmailTextView;

    private View navHeader;

    private CircleImageView userAvatar;
    private RxPermissions rxPermissions;
    private final int WRITE_PERMISSION = 200;

    private FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("sVill");
        setSupportActionBar(toolbar);


        rxPermissions = new RxPermissions(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        navHeader = navigationView.getHeaderView(0);
        userAvatar = navHeader.findViewById(R.id.userAvatar);
        userEmailTextView = navHeader.findViewById(R.id.userEmail);
        userNameTextView = navHeader.findViewById(R.id.userName);


        manager = getSupportFragmentManager();

        if (manager.findFragmentByTag(DashboardFragment.TAG) == null) {
            DashboardFragment dashboardFragment = DashboardFragment.newInstance();
            manager.beginTransaction().add(R.id.dash_container, dashboardFragment, DashboardFragment.TAG).commit();
        }

        loadNavHeader();

/*
        if (savedInstanceState == null) {
            activityUtils.addFragmentWithTagToActivity(fragmentManager, DashboardFragment.newInstance(), R.id.dash_container, DashboardFragment.TAG);
        }
*/


        ////if (!checkPermissionFinal()) {
        requestAllPermissions();
        //}


/*
        else {
            rxPermissions
                    .request(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.INTERNET)
                    .subscribe(granted -> {
                        if (granted) {
                            // All requested permissions are granted
                        } else {
                            // At least one permission is denied
                        }
                    });
        }
*/


    }

    private void requestAllPermissions(){

        if (Build.VERSION.SDK_INT >= 23 ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            }, WRITE_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch (requestCode){
            case WRITE_PERMISSION:
                if (grantResults.length > 0){
                    boolean internet = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean location = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(internet && camera && write && location){

                    }

                    else{

                    }
                }

                break;
        }
    }




    private boolean checkPermissionFinal(){
        Log.d("dash", "checking permissios");

        if (Build.VERSION.SDK_INT >= 23 ) {
            return internetPermission() && locationPermission() && checkWritePermission() && checkIfAlloweToUseCamera();
        }

        return true;
    }






    private boolean locationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private boolean internetPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;

    }

    private boolean checkWritePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

    }


    private boolean checkIfAlloweToUseCamera() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }



    private void requestPermissions(){

    }


    private void showSignout() {

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent loginIntent = new Intent(DashBoardActivity.this, SignInActivity.class);
                        startActivity(loginIntent);
                        finish();
                    } else {
                        showMessage("Failed to sign out. Try again later");
                    }
                });
    }

    private void showMessage(String s) {

    }



/*
    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

/*
    @Override
    protected ScopedPresenter getPresenter() {
        return presenter;
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setUpUserInfo(){

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contacts) {
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_given_items) {
            Intent intent = new Intent(this, SharedToysActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_received_items) {
            Intent intent = new Intent(this, ReceivedToysActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_exit) {
            showSignout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadNavHeader() {


        String userName = GetUserData.getUserDisplayName(this);
        userEmailTextView.setText(GetUserData.getUserEmail(this));

        if (userName.equals("no_name")){
            userNameTextView.setText("");
        }

        else {
            userNameTextView.setText(userName);
        }

        String photoUrl = GetUserData.getUserPhotoUrl(this);

        if (photoUrl.equals("no_photo")) {
            Glide.with(this).load(R.drawable.ic_account_circle_black_36dp)
                    .thumbnail(0.5f)
                    .into(userAvatar);

        }

        else {
            Glide.with(this).load(photoUrl)
                    .thumbnail(0.5f)
                    .into(userAvatar);
        }
    }


    @Override
    public void startGive() {
        Intent newToyIntent = new Intent(this, NewToActivity.class);
        startActivity(newToyIntent);
    }

    @Override
    public void startReceive() {
        Intent setUp = new Intent(this, SearchSetUpActivity.class);
        startActivity(setUp);
    }
}
