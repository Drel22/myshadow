package com.mas.ethan.mas_myshadow;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mas.ethan.mas_myshadow.models.Bookmark;
import com.mas.ethan.mas_myshadow.models.Skin;
import com.mas.ethan.mas_myshadow.models.Swatch;
import com.thebluealliance.spectrum.SpectrumDialog;

import com.rtugeek.android.colorseekbar.ColorSeekBar;

public class EditSkinToneActivity extends AppCompatActivity {

    public static String color = "#00FF00";
    public static EditSkinToneActivity currActivity;

    public int skintype = -1;
    public int season = -1;
    public String seasonStr = "";
    public String skintypeStr = "";
    public String name = "";

    public EditText editTextName;
    public ImageView season1;
    public ImageView season2;
    public ImageView season3;
    public ImageView season4;
    public ImageView skintype1;
    public ImageView skintype2;
    public ImageView skintype3;
    public ImageView skintype4;

    public ColorSeekBar mColorPicker;

    public String skinId;

    DatabaseReference databaseSkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_skin_tone);
        currActivity = this;

        Intent intent = getIntent();
        skinId = intent.getStringExtra("SKIN_ID");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser userF = mAuth.getCurrentUser();
        databaseSkin = FirebaseDatabase.getInstance().getReference("skins").child(userF.getUid());

        editTextName = (EditText) findViewById(R.id.editText);
        season1 = (ImageView) findViewById(R.id.seasonback0);
        season2 = (ImageView) findViewById(R.id.seasonback1);
        season3 = (ImageView) findViewById(R.id.seasonback2);
        season4 = (ImageView) findViewById(R.id.seasonback3);

        skintype1 = (ImageView) findViewById(R.id.skintypeback0);
        skintype2 = (ImageView) findViewById(R.id.skintypeback1);
        skintype3 = (ImageView) findViewById(R.id.skintypeback2);
        skintype4 = (ImageView) findViewById(R.id.skintypeback3);

        mColorPicker = findViewById(R.id.colorSlider);
        colorPickerConfig(mColorPicker);


        // databaseSkin.child(skinId).removeValue();


        Query query = databaseSkin.orderByChild("id").equalTo(skinId).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Skin skin = postSnapshot.getValue(Skin.class);
                    seasonStr = skin.getSeason();
                    color = skin.getColor();
                    skintypeStr = skin.getType();
                    name = skin.getName();

                    if (seasonStr.equals("WINTER")) {
                        season = 1;
                        season1.setBackgroundResource(R.drawable.circle_shadow_shaded);
                    } else if (seasonStr.equals("SUMMER")) {
                        season = 3;
                        season3.setBackgroundResource(R.drawable.circle_shadow_shaded);
                    } else if(seasonStr.equals("SPRING")) {
                        season = 2;
                        season2.setBackgroundResource(R.drawable.circle_shadow_shaded);
                    } else if(seasonStr.equals("AUTUMN")) {
                        season = 4;
                        season4.setBackgroundResource(R.drawable.circle_shadow_shaded);
                    }

                    if (skintypeStr.equals("OILY")) {
                        skintype = 2;
                        skintype2.setBackgroundResource(R.drawable.circle_shadow_shaded);
                    } else if (skintypeStr.equals("NORMAL")) {
                        skintype = 1;
                        skintype1.setBackgroundResource(R.drawable.circle_shadow_shaded);
                    } else if (skintypeStr.equals("DRY")) {
                        skintype = 3;
                        skintype3.setBackgroundResource(R.drawable.circle_shadow_shaded);
                    } else if (skintypeStr.equals("COMBINATION")) {
                        skintype = 4;
                        skintype4.setBackgroundResource(R.drawable.circle_shadow_shaded);
                    }

                    editTextName.setText(name);
                    mColorPicker.setColor(Color.parseColor("#" + color));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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


    public void resetSeasons() {
        if (season == 1) {
            season1.setBackgroundResource(R.drawable.circle_shadow);
        } else if (season == 2) {
            season2.setBackgroundResource(R.drawable.circle_shadow);
        } else if (season == 3) {
            season3.setBackgroundResource(R.drawable.circle_shadow);
        } else if (season == 4) {
            season4.setBackgroundResource(R.drawable.circle_shadow);
        }
    }
    public void resetSkintypes() {
        if (skintype == 1) {
            skintype1.setBackgroundResource(R.drawable.circle_shadow);
        } else if (skintype == 2) {
            skintype2.setBackgroundResource(R.drawable.circle_shadow);
        } else if (skintype == 3) {
            skintype3.setBackgroundResource(R.drawable.circle_shadow);
        } else if (skintype == 4) {
            skintype4.setBackgroundResource(R.drawable.circle_shadow);
        }
    }

    public void chooseSeason1(View v) {
        resetSeasons();
        season1.setBackgroundResource(R.drawable.circle_shadow_shaded);
        season = 1;
        seasonStr = "WINTER";
    }
    public void chooseSeason2(View v) {
        resetSeasons();
        season2.setBackgroundResource(R.drawable.circle_shadow_shaded);
        season = 2;
        seasonStr = "SPRING";
    }
    public void chooseSeason3(View v) {
        resetSeasons();
        season3.setBackgroundResource(R.drawable.circle_shadow_shaded);
        season = 3;
        seasonStr = "SUMMER";
    }
    public void chooseSeason4(View v) {
        resetSeasons();
        season4.setBackgroundResource(R.drawable.circle_shadow_shaded);
        season = 4;
        seasonStr = "AUTUMN";
    }

    public void chooseSkintype1(View v) {
        resetSkintypes();
        skintype1.setBackgroundResource(R.drawable.circle_shadow_shaded);
        skintype = 1;
        skintypeStr = "NORMAL";
    }
    public void chooseSkintype2(View v) {
        resetSkintypes();
        skintype2.setBackgroundResource(R.drawable.circle_shadow_shaded);
        skintype = 2;
        skintypeStr = "OILY";
    }
    public void chooseSkintype3(View v) {
        resetSkintypes();
        skintype3.setBackgroundResource(R.drawable.circle_shadow_shaded);
        skintype = 3;
        skintypeStr = "DRY";
    }
    public void chooseSkintype4(View v) {
        resetSkintypes();
        skintype4.setBackgroundResource(R.drawable.circle_shadow_shaded);
        skintype = 4;
        skintypeStr = "COMBINATION";
    }


    public void editSkin(View v) {
        String name = editTextName.getText().toString();

        if (!name.equals("")) {
            if (skintype > -1) {
                if (season > -1) {
                    //String id = databaseSkin.push().getKey();
                    String id = skinId;
                    color = String.format("FF%06X", 0xFFFFFF & mColorPicker.getColor());
                    Skin skin = new Skin(id, name, seasonStr, skintypeStr, color);
                    databaseSkin.child(id).setValue(skin);
                    Toast.makeText(this, "Swatch updated", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Please choose a season", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please select a skin type", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please provide a name", Toast.LENGTH_SHORT).show();
        }
    }


    public void openCameraSkinTone(View v) {


        Intent i = new Intent(EditSkinToneActivity.this, MyCameraActivity.class);
        startActivity(i);
    }

    @Override
    public void onResume(){
        super.onResume();
        checkImage();

    }

    public void checkImage() {

        if (MyUploadActivity.image != null) {
            if (!CompareSkinActivity.currentlyOpen) {
                CompareSkinActivity.currentlyOpen = true;
                Intent i = new Intent(EditSkinToneActivity.this, CompareSkinActivity.class);
                startActivity(i);
            }
        }
        if (CompareSkinActivity.compared) {
            CompareSkinActivity.compared = false;
            mColorPicker.setColor(Color.parseColor("#" + CompareSkinActivity.colorStr));
        }
    }

}
