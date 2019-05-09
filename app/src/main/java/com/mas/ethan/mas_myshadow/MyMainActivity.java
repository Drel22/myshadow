package com.mas.ethan.mas_myshadow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MyMainActivity extends AppCompatActivity
        implements MySearchFragment.OnFragmentInteractionListener, MyCameraFragment.OnFragmentInteractionListener, MyMyEyeFragment.OnFragmentInteractionListener, MySearchListFragment.OnFragmentInteractionListener, MySearchNotFoundFragment.OnFragmentInteractionListener, MySearchDefaultFragment.OnFragmentInteractionListener, MySettingsFragment.OnFragmentInteractionListener, MyBookmarksFragment.OnFragmentInteractionListener, MyBookmarksProductsFragment.OnFragmentInteractionListener, MyBookmarksSkinTonesFragment.OnFragmentInteractionListener, MySearchReadyFragment.OnFragmentInteractionListener, MyProfileFragment.OnFragmentInteractionListener, MyProfileHistoryFragment.OnFragmentInteractionListener, MyProfileUploadsFragment.OnFragmentInteractionListener, MyProfileLikesFragment.OnFragmentInteractionListener, MyProfileTabsFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;

    public FragmentShowing frag = FragmentShowing.SEARCH;

    // Fragments?
    MySearchFragment searchFragment;
    MyMyEyeFragment myeyeFragment;
    MyCameraFragment cameraFragment;
    MySettingsFragment settingsFragment;
    MyBookmarksFragment bookmarksFragment;
    MyProfileFragment profileFragment;

    public static MyMainActivity thisActivity;

    enum FragmentShowing {
        SEARCH, PRODUCT, MYEYE, CAMERA, SETTINGS, BOOKMARKS, PROFILE;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    changeToSearchFragment();
                    return true;
                case R.id.navigation_bookmarks:
                    changeToBookmarksFragment();
                    return true;
                case R.id.navigation_profile:
                    changeToProfileFragment();
                    return true;
            }
            return false;
        }
    };

    public static BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_my_main);

        thisActivity = this;

        //mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchFragment = new MySearchFragment();
        // TODO: add parameters that instantiate it dynamically
        myeyeFragment = new MyMyEyeFragment();
        cameraFragment = new MyCameraFragment();
        bookmarksFragment = new MyBookmarksFragment();
        settingsFragment = new MySettingsFragment();
        profileFragment = new MyProfileFragment();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, searchFragment).commit();

            navigation.setSelectedItemId(R.id.navigation_search);
        }
    }

    public void changeToSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, searchFragment);
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.commit();
        frag = FragmentShowing.SEARCH;
    }
    public void changeToBookmarksFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, bookmarksFragment);
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.commit();
        frag = FragmentShowing.BOOKMARKS;
    }
    public void changeToProfileFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, profileFragment);
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.commit();
        frag = FragmentShowing.PROFILE;
    }
    public void changeToSettingsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, settingsFragment);
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.commit();
        frag = FragmentShowing.SETTINGS;
    }
    public void changeToMyEyeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, myeyeFragment);
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.commit();
        frag = FragmentShowing.MYEYE;
    }
    public void changeToCameraFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, cameraFragment);
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.commit();
        frag = FragmentShowing.CAMERA;
    }

    public void openProductPage() {
        Intent i = new Intent(MyMainActivity.this, MyProductActivity.class);

        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        if (frag != FragmentShowing.SEARCH) {
            changeToSearchFragment();
            navigation.setSelectedItemId(R.id.navigation_search);
        } else {
            if (searchFragment.textEmpty()) {
                finish();
            }
        }
    }

    public void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void newSkinTone() {
        toastMessage("New skin tone");
        Intent i = new Intent(MyMainActivity.this, NewSkinToneActivity.class);
//        i.putExtra("PRODUCT_ID", pEntry.id);
//        i.putExtra("PRODUCT_NAME", pEntry.product);
//        i.putExtra("PRODUCT_BRAND", pEntry.brand);
        startActivity(i);
    }
    public void editSkinTone(String id) {
        toastMessage("Edit skin tone");
        Intent i = new Intent(MyMainActivity.this, EditSkinToneActivity.class);
        i.putExtra("SKIN_ID", id);
//        i.putExtra("PRODUCT_NAME", pEntry.product);
//        i.putExtra("PRODUCT_BRAND", pEntry.brand);
        startActivity(i);
    }

    public void editProfile() {
        MyProfileFragment.thisFragment.showProfilePopUp();
    }
}
