package com.mas.ethan.mas_myshadow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.thebluealliance.spectrum.SpectrumDialog;
import com.wonderkiln.camerakit.Size;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.mas.ethan.mas_myshadow.models.Swatch;

public class MyUploadActivity extends AppCompatActivity {

    public static MyUploadActivity currActivity;
    public static byte[] image;
    public static Size nativeCaptureSize = null;
    public static long timeToCallback = 2;

    ImageView imageView;
    byte[] jpeg;

    DatabaseReference databaseSwatches;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_my_upload);

        imageView = findViewById(R.id.image);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));

        MyUploadActivity.currActivity = this;

        Intent intent = getIntent();
        databaseSwatches = FirebaseDatabase.getInstance().getReference("swatches").child(intent.getStringExtra("PRODUCT_ID"));


    }

    public void saveImage(View v) {
        if (jpeg != null) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

            saveImageToExternalStorageWithPermission(this, "masmyshadowtest", bitmap);
        }
    }

    public final int WRITE_REQUEST_CODE = 2;
    public Context thisContext;
    public String thisFilename;
    public Bitmap thisBitmap;

    //https://stackoverflow.com/questions/33178295/how-to-properly-save-to-the-pictures-folder
    public void saveImageToExternalStorageWithPermission(Context context, String filename, Bitmap image) {
        thisContext = context;
        thisFilename = filename;
        thisBitmap = image;
        String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, WRITE_REQUEST_CODE);


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch (requestCode){
            case WRITE_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    try {
                        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                        //https://stackoverflow.com/questions/5280176/make-directory-in-android/17175959
                        File picDirectory = new File(directory, "masmyshadow");
                        picDirectory.mkdirs();


                        File file = new File(picDirectory, thisFilename + ".jpeg");

                        FileOutputStream outputStream = new FileOutputStream(file);
                        thisBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                        outputStream.flush();
                        outputStream.getFD().sync();
                        outputStream.close();

                        MediaScannerConnection.scanFile(thisContext, new String[] {file.getAbsolutePath()}, null, null);

                        //Toast toast = Toast.makeText(getApplicationContext(), "Success: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Toast.LENGTH_SHORT);
                        //toast.show();
                    } catch (IOException e) {
                        //Toast toast = Toast.makeText(getApplicationContext(), "Failed: " + e.toString(), Toast.LENGTH_SHORT);
                        //toast.show();
                    }

                } else {
                    Toast.makeText(this, "Unable to get permission", Toast.LENGTH_LONG).show();
                }
        }
    }

    public void openCamera(View v) {

        Intent i = new Intent(MyUploadActivity.this, MyCameraActivity.class);
        startActivity(i);

    }

    @Override
    public void onResume(){
        super.onResume();
        checkImage();

    }

    public int SELECT_PICTURE = 222;

    public void openGallery(View v) {

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

                //Get ImageURi and load with help of picasso
                //Uri selectedImageURI = data.getData();

                try {
                    Uri imageUri = data.getData();
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    //byte[] jpeg = stream.toByteArray();
                    MyUploadActivity.image = stream.toByteArray();
                    bmp.recycle();

                } catch (IOException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }
                //checkImage();

            }

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Bad Result", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void checkImage() {

        if (image != null) {
            if (!MyUpload2Activity.currentlyOpen) {
                MyUpload2Activity.currentlyOpen = true;
                Intent i = new Intent(MyUploadActivity.this, MyUpload2Activity.class);
                startActivity(i);
            }
        }
        if (MyUpload2Activity.uploaded) {
            MyUpload2Activity.uploaded = false;
            finish();
        }

        /*jpeg = image;
        if (jpeg != null) {
            imageView.setVisibility(View.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

            if (bitmap == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "No Image Found", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                imageView.setImageBitmap(bitmap);
            }


        }*/
    }

}

