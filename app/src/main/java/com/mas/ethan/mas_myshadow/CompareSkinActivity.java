package com.mas.ethan.mas_myshadow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rtugeek.android.colorseekbar.ColorSeekBar;

public class CompareSkinActivity extends AppCompatActivity {


    public static boolean uploaded = false;

    public ColorSeekBar mColorPicker;

    public static boolean currentlyOpen = false;

    public static boolean compared = false;
    public static String colorStr;
    public static int colorInt;

    public ImageView imageView;
    public ImageView colorSquare;

    @Override
    public void onDestroy() {

        super.onDestroy();
        currentlyOpen = false;
    }


    @Override
    public void onResume(){
        super.onResume();
        checkImage();
        MyUploadActivity.image = null;

    }

    public void checkImage() {

        if (MyUploadActivity.image != null) {
            imageView.setVisibility(View.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeByteArray(MyUploadActivity.image, 0, MyUploadActivity.image.length);

            if (bitmap == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "No Image Found", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_skin);
        compared = false;
        colorStr = "";
        colorInt = 0;

        imageView = findViewById(R.id.image);
        colorSquare = findViewById(R.id.skinColor);

        mColorPicker = findViewById(R.id.colorSlider);
        colorPickerConfig(mColorPicker);
        colorStr = String.format("FF%06X", 0xFFFFFF & mColorPicker.getColorIndexPosition(0));
        colorInt = mColorPicker.getColorIndexPosition(0);
        mColorPicker.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                colorSquare.setColorFilter(color);
                colorStr = String.format("FF%06X", 0xFFFFFF & color);
                //colorSeekBar.getAlphaValue();
            }
        });
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

    public void chooseColor(View v) {
        compared = true;
        finish();
    }
}
