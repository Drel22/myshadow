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

import com.rtugeek.android.colorseekbar.ColorSeekBar;

import com.mas.ethan.mas_myshadow.models.Swatch;

public class MyUpload2Activity extends AppCompatActivity {

    public static boolean uploaded = false;

    public ColorSeekBar mColorPicker;

    public static boolean currentlyOpen = false;
    public static MyUpload2Activity currActivity;
   // public static byte[] image;
    public static Size nativeCaptureSize = null;
    public static long timeToCallback = 2;

    ImageView imageView;
    byte[] jpeg;

    DatabaseReference databaseSwatches;

    @Override
    public void onDestroy() {

        super.onDestroy();
        currentlyOpen = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_my_upload2);

        uploaded = false;

        mColorPicker = findViewById(R.id.colorSlider);
        colorPickerConfig(mColorPicker);

        imageView = findViewById(R.id.image);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));

        MyUpload2Activity.currActivity = this;

        Intent intent = getIntent();
        //databaseSwatches = FirebaseDatabase.getInstance().getReference("swatches").child(intent.getStringExtra("PRODUCT_ID"));
        databaseSwatches = FirebaseDatabase.getInstance().getReference("swatches").child(MyProductActivity.pEntry.id);




    }


    public void colorPickerConfig(ColorSeekBar mColorSeekBar){
        mColorSeekBar.setMaxPosition(50);
        mColorSeekBar.setColorSeeds(R.array.fentyColors); // material_colors is defalut included in res/color,just use it.
        mColorSeekBar.setColorBarPosition(10); //0 - maxValue
        //mColorSeekBar.setAlphaBarPosition(10); //0 - 255
        mColorSeekBar.setShowAlphaBar(false);
        mColorSeekBar.setBarHeight(20); //40dpi
        mColorSeekBar.setThumbHeight(100); //60dpi
        mColorSeekBar.setBarMargin(10); //set the margin between colorBar and alphaBar 10dpi
    }


    public final int WRITE_REQUEST_CODE = 2;
    public Context thisContext;
    public String thisFilename;
    public Bitmap thisBitmap;

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

    @Override
    public void onResume(){
        super.onResume();
        checkImage();
        MyUploadActivity.image = null;

    }

    public void checkImage() {

        jpeg = MyUploadActivity.image;
        if (jpeg != null) {
            imageView.setVisibility(View.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

            if (bitmap == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "No Image Found", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                imageView.setImageBitmap(bitmap);
            }


        }
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();
    UploadTask uploadTask;
    StorageReference storageRef;
    StorageReference swatchesRef;
    StorageReference swatchImagesRef;

    public static boolean doneUploading;
    public static String imgurl = "none";
    public static String swatchName = "defaultName";
    public static String color = "#00FF00";

    public void uploadSwatch(View v) {




        if (TextUtils.isEmpty(swatchName)) {
            Toast.makeText(MyUpload2Activity.currActivity, "Please enter swatch name", Toast.LENGTH_LONG).show();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(MyUpload2Activity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
            Random random = new Random();
            //https://stackoverflow.com/questions/825678/what-is-the-best-way-to-generate-a-unique-and-short-file-name-in-java
            String randStuff = String.format("%s%s", sdf.format( new Date() ),
                    random.nextInt(1000));
            String filename = randStuff + ".jpg";

            // String swatchName = editTextTrackName.getText().toString().trim();
            storageRef = storage.getReference();
            swatchesRef = storageRef.child(filename);
            swatchImagesRef = storageRef.child("images/" + filename);

            // Get the data from an ImageView as bytes
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            MyUpload2Activity.doneUploading = false;

            uploadTask = swatchesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    MyUpload2Activity.currActivity.failMessage();
                    doneUploading = true;
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    MyUpload2Activity.currActivity.successMessage();
                    swatchesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            MyUpload2Activity.currActivity.successMessage2();
                            Uri downloadUrl = uri;
                            String usableUrl = downloadUrl.toString();
                            imgurl = usableUrl;


                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            FirebaseUser userF = mAuth.getCurrentUser();

                            color = String.format("FF%06X", 0xFFFFFF & mColorPicker.getColor());

                            String id = databaseSwatches.push().getKey();
                            Swatch swatch = new Swatch(id, swatchName, imgurl, color, userF.getUid());
                            databaseSwatches.child(id).setValue(swatch);
                            //Toast.makeText(MyUpload2Activity.currActivity, "Swatch saved: " + imgurl, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            imageView = findViewById(R.id.image);
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));
                            uploaded = true;
                            MyUpload2Activity.currActivity.closeActivity();
                            //Do what you want with the url
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            MyUpload2Activity.currActivity.failMessage2();
                            progressDialog.dismiss();
                        }
                    });
                    doneUploading = true;
                }
            });

            //while (!doneUploading);

            doneUploading = false;

        }


    }

    public void failMessage() {
        Toast.makeText(this, "Failed to upload", Toast.LENGTH_LONG).show();
    }
    public void successMessage() {
        Toast.makeText(this, "Successfully uploaded", Toast.LENGTH_LONG).show();



        /*Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return swatchImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    MyUpload2Activity.currActivity.successMessage2();
                    Uri downloadUri = task.getResult();
                    imgurl = downloadUri.toString();
                } else {
                    MyUpload2Activity.currActivity.failMessage2();
                    // Handle failures
                    // ...
                }
            }
        });*/
    }
    public void failMessage2() {
        //Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_LONG).show();
    }
    public void successMessage2() {
        //Toast.makeText(this, "Successfully got download URL", Toast.LENGTH_LONG).show();
    }

    public void closeActivity() {
        finish();
    }

}

