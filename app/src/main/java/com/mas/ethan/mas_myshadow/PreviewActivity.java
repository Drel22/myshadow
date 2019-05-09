package com.mas.ethan.mas_myshadow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PreviewActivity extends AppCompatActivity {

    ImageView imageView;
    VideoView videoView;
    TextView actualResolution;
    TextView approxUncompressedSize;
    TextView captureLatency;
    byte[] jpeg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        // TODO
        //https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en



        videoView = findViewById(R.id.video);
        imageView = findViewById(R.id.image);
        actualResolution = findViewById(R.id.actualResolution);
        approxUncompressedSize = findViewById(R.id.approxUncompressedSize);
        captureLatency = findViewById(R.id.captureLatency);

        setupToolbar();

        jpeg = ResultHolder.getImage();
        File video = ResultHolder.getVideo();

        if (jpeg != null) {
            imageView.setVisibility(View.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

            if (bitmap == null) {
                finish();
                return;
            }

            imageView.setImageBitmap(bitmap);

            actualResolution.setText(bitmap.getWidth() + " x " + bitmap.getHeight());
            approxUncompressedSize.setText(getApproximateFileMegabytes(bitmap) + "MB");
            captureLatency.setText(ResultHolder.getTimeToCallback() + " milliseconds");
        }

        else if (video != null) {
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(video.getAbsolutePath()));
            MediaController mediaController = new MediaController(this);
            mediaController.setVisibility(View.GONE);
            videoView.setMediaController(mediaController);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.start();

                    float multiplier = (float) videoView.getWidth() / (float) mp.getVideoWidth();
                    videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (mp.getVideoHeight() * multiplier)));
                }
            });
            //videoView.start();
        }

        else {
            finish();
            return;
        }
    }

    public void saveImage(View v) {
        if (jpeg != null) {
            //https://stackoverflow.com/questions/7978905/android-java-saving-a-byte-array-to-a-file-jpeg

            //new SavePhotoTask().execute(jpeg);
            //createDirectoryAndSaveFile(jpeg, "myshadow_test.jpeg");

            Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

            /*try {
                saveImageToExternalStorage(this, "masmyshadowtest", bitmap);
            } catch (IOException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Failed: " + e.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }*/

            saveImageToExternalStorageWithPermission(this, "masmyshadowtest", bitmap);

            //https://stackoverflow.com/questions/24966061/android-write-png-bytearray-to-file
            // This saves to private application storage
            /*
            File file = new File(getFilesDir()+"/masmyshadowtest.jpeg");
            try {
                FileOutputStream fos = new FileOutputStream(file);

//write your byteArray here
                fos.write(jpeg);
                fos.flush();
                fos.close();
                Toast toast = Toast.makeText(getApplicationContext(), "Success: " + getFilesDir(), Toast.LENGTH_SHORT);
                toast.show();
            } catch (Exception e) {

                Toast toast = Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT);
                toast.show();
            }*/
        }
    }

    //https://stackoverflow.com/questions/33178295/how-to-properly-save-to-the-pictures-folder
    public void saveImageToExternalStorage(Context context, String filename, Bitmap image) throws IOException {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, filename + ".jpeg");

        FileOutputStream outputStream = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        outputStream.flush();
        outputStream.getFD().sync();
        outputStream.close();

        MediaScannerConnection.scanFile(context, new String[] {file.getAbsolutePath()}, null, null);

        Toast toast = Toast.makeText(getApplicationContext(), "Success: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Toast.LENGTH_SHORT);
        toast.show();
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

                        Toast toast = Toast.makeText(getApplicationContext(), "Success: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Toast.LENGTH_SHORT);
                        toast.show();
                    } catch (IOException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Failed: " + e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    Toast.makeText(this, "Unable to get permission", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void setupToolbar() {
//        if (getSupportActionBar() != null) {
//            View toolbarView = getLayoutInflater().inflate(R.layout.action_bar, null, false);
//            TextView titleView = toolbarView.findViewById(R.id.toolbar_title);
//            titleView.setText(Html.fromHtml("<b>Camera</b>Kit"));
//
//            getSupportActionBar().setCustomView(toolbarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            getSupportActionBar().setDisplayShowCustomEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }
    }

    private static float getApproximateFileMegabytes(Bitmap bitmap) {
        return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024 / 1024;
    }


    /*class SavePhotoTask extends AsyncTask<byte[], String, String> {
        @Override
        protected String doInBackground(byte[]... jpeg) {
            File photo=
                    new File(Environment.getExternalStorageDirectory(),
                            "photo.jpg");

            if (photo.exists()) {
                photo.delete();
            }

            try {
                FileOutputStream fos=new FileOutputStream(photo.getPath());

                fos.write(jpeg[0]);
                fos.close();
            }
            catch (java.io.IOException e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
            }

            return(null);
        }
    }*/

    private void createDirectoryAndSaveFile(byte[] array, String fileName) {


        Toast toast = Toast.makeText(getApplicationContext(), "Attempting to save", Toast.LENGTH_SHORT);
        toast.show();

        Bitmap imageToSave = BitmapFactory.decodeByteArray(array, 0, array.length);

        //File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        File direct = new File( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        if (!direct.exists()) {
            toast = Toast.makeText(getApplicationContext(), "Directory does not exist?", Toast.LENGTH_SHORT);
            toast.show();
        }

        /*if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }*/

        File file = new File(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM) + "/Camera"), fileName);
        toast = Toast.makeText(getApplicationContext(), "File created", Toast.LENGTH_SHORT);
        toast.show();
        if (file.exists()) {
            file.delete();
            toast = Toast.makeText(getApplicationContext(), "File already existed", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast = Toast.makeText(getApplicationContext(), "File not already created", Toast.LENGTH_SHORT);
            toast.show();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            toast = Toast.makeText(getApplicationContext(), "File opened", Toast.LENGTH_SHORT);
            toast.show();
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            toast = Toast.makeText(getApplicationContext(), "Finished writing", Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            toast = Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT);
            toast.show();
            //toast = Toast.makeText(getApplicationContext(), "Failed to save", Toast.LENGTH_SHORT);
            //toast.show();
        }
    }



}

