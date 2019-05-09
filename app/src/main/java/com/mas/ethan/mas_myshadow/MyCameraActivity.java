package com.mas.ethan.mas_myshadow;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;
import com.wonderkiln.camerakit.Size;
import com.wonderkiln.camerakit.core.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyCameraActivity extends AppCompatActivity {

    private static final String TAG = MyCameraActivity.class.getSimpleName();

    DrawerLayout drawerLayout;
    ListView leftDrawer;
    ActionBarDrawerToggle drawerToggle;
    ViewGroup parent;
    CameraView camera;

    public static MyCameraActivity thisActivity;

    private ArrayAdapter drawerAdapter;

    private int cameraMethod = CameraKit.Constants.METHOD_STANDARD;
    private boolean cropOutput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_my_camera);

        thisActivity = this;

        drawerLayout = findViewById(R.id.activityMain);
        leftDrawer = findViewById(R.id.leftDrawer);
        parent = findViewById(R.id.contentFrame);
        camera = findViewById(R.id.camera);

        setupDrawerAndToolbar();

        camera.setMethod(cameraMethod);
        camera.setCropOutput(cropOutput);
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        camera.stop();
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menu_main_about: {
                Drawable icon = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                new AlertDialog.Builder(this)
                        .setIcon(icon)
                        .setTitle(getString(R.string.about_dialog_title))
                        .setMessage(getString(
                                R.string.about_dialog_message,
                                BuildConfig.VERSION_NAME,
                                BuildConfig.VERSION_CODE,
                                BuildConfig.VERSION_NAME
                        ))
                        .setPositiveButton("DONE", null)
                        .show();
                return true;
            }

            case R.id.menu_main_github: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_github)));
                startActivity(intent);
                return true;
            }

            case R.id.menu_main_website: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_website)));
                startActivity(intent);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupDrawerAndToolbar() {
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };

        drawerLayout.setDrawerListener(drawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            View toolbarView = getLayoutInflater().inflate(R.layout.action_bar, null, false);
            TextView titleView = toolbarView.findViewById(R.id.toolbar_title);
            titleView.setText(Html.fromHtml("<b>Camera</b>Kit"));

            getSupportActionBar().setCustomView(toolbarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        final List<CameraSetting> options = new ArrayList<>();
        options.add(captureMethodSetting);
        options.add(cropSetting);

        drawerAdapter = new ArrayAdapter<CameraSetting>(this, R.layout.drawer_list_item, R.id.text1, options) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(R.id.text1);
                text1.setTextColor(Color.WHITE);

                final TextView text2 = view.findViewById(R.id.text2);
                text2.setTextColor(Color.WHITE);

                text1.setText(options.get(position).getTitle());
                text2.setText(options.get(position).getValue());

                return view;
            }
        };

        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                options.get(i).toggle();
                drawerAdapter.notifyDataSetChanged();
            }
        });
        leftDrawer.setAdapter(drawerAdapter);
    }

    private static abstract class CameraSetting {

        abstract String getTitle();
        abstract String getValue();
        abstract void toggle();

    }

    private CameraSetting captureMethodSetting = new CameraSetting() {
        @Override
        String getTitle() {
            return "ckMethod";
        }

        @Override
        String getValue() {
            switch (cameraMethod) {
                case CameraKit.Constants.METHOD_STANDARD: {
                    return "standard";
                }

                case CameraKit.Constants.METHOD_STILL: {
                    return "still";
                }

                default: return null;
            }
        }

        @Override
        void toggle() {
            if (cameraMethod == CameraKit.Constants.METHOD_STANDARD) {
                cameraMethod = CameraKit.Constants.METHOD_STILL;
            } else {
                cameraMethod = CameraKit.Constants.METHOD_STANDARD;
            }

            camera.setMethod(cameraMethod);
        }
    };

    private CameraSetting cropSetting = new CameraSetting() {
        @Override
        String getTitle() {
            return "ckCropOutput";
        }

        @Override
        String getValue() {
            if (cropOutput) {
                return "true";
            } else {
                return "false";
            }
        }

        @Override
        void toggle() {
            cropOutput = !cropOutput;
            camera.setCropOutput(cropOutput);
        }
    };

    public int SELECT_PICTURE = 1;

    public void goToGallery() {

        // Go to upload
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {


                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //byte[] jpeg = stream.toByteArray();
                MyUploadActivity.image = stream.toByteArray();
                bmp.recycle();

//                MyUploadActivity.image = jpeg;
//                MyUploadActivity.nativeCaptureSize = null;
//                MyUploadActivity.timeToCallback = 2;
                /*Intent intent = new Intent(MyCameraActivity.this, MyUploadActivity.class);
                startActivity(intent);*/

            }
        }
    }

}