package com.mas.ethan.mas_myshadow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.mas.ethan.mas_myshadow.models.Like;
import com.mas.ethan.mas_myshadow.models.Product;
import com.mas.ethan.mas_myshadow.models.Skin;
import com.mas.ethan.mas_myshadow.models.Swatch;
import com.mas.ethan.mas_myshadow.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProductActivity extends AppCompatActivity {

    public String productName;

    public static Context context;

    static String[] light = new String[] {"fffce8dd",
            "fffadbad",
            "fffce6d9",
            "fffde3cc",
            "fff2d3b2",
            "fffce1c6",
            "ffe2c092",
            "ffefd1b7",
            "fff4cca8",
            "ffe8bfa3",
            "fff9d5b8",
            "ffe7c49e",
            "ffeac4a2"};
    static String[] med = new String[] {"ffe9bd9b",
            "ffe2ad85",
            "ffeeba93",
            "ffdfae8a",
            "ffdfac75",
            "ffdaa67e",
            "ffe5ad7e",
            "ffd9aa74",
            "ffdca77d",
            "ffd39b6c",
            "ffe6ac84",
            "ffdcb17e"};
    static String[] tan = new String[] {"ffcb9364",
            "ffd28f58",
            "ffd49164",
            "ffd29056",
            "ffad7753",
            "ffb67848",
            "ffbe7b46",
            "ffcb8458",
            "ffc98659",
            "ffac7c4b",
            "ffa86b3f",
            "ffa65f23"};

    static String[] deep = new String[] {"ff9d5d2d",
            "ffac6a3a",
            "ff8c512a",
            "ff9a5a33",
            "ff885b32",
            "ff824f30",
            "ff7d452c",
            "ff69371c",
            "ff683a20",
            "ff593218",
            "ff583218",
            "ff4f2817",
            "ff3a2d27"};

    public static boolean isInGroup(int group, String skinColor) {
        boolean retVal = false;
        if (group == 0) {
            for (int i = 0; i < light.length; i++) {
                if (light[i].equalsIgnoreCase(skinColor)) {
                    retVal = true;
                }
            }
        } else if (group == 1) {
            for (int i = 0; i < med.length; i++) {
                if (med[i].equalsIgnoreCase(skinColor)) {
                    retVal = true;
                }
            }
        } else if (group == 2) {
            for (int i = 0; i < tan.length; i++) {
                if (tan[i].equalsIgnoreCase(skinColor)) {
                    retVal = true;
                }
            }
        } else if (group == 3) {
            for (int i = 0; i < deep.length; i++) {
                if (deep[i].equalsIgnoreCase(skinColor)) {
                    retVal = true;
                }
            }
        }
        return retVal;
    }

    public static int whichGroup(String skinColor) {
        int group = -1;
        for (int i = 0; i < light.length; i++) {
            if (light[i].equalsIgnoreCase(skinColor)) {
                group = 0;
            }
        }
        if (group < 0) {
            for (int i = 0; i < med.length; i++) {
                if (med[i].equalsIgnoreCase(skinColor)) {
                    group = 1;
                }
            }
        }
        if (group < 0) {
            for (int i = 0; i < tan.length; i++) {
                if (tan[i].equalsIgnoreCase(skinColor)) {
                    group = 2;
                }
            }
        }
        if (group < 0) {
            for (int i = 0; i < deep.length; i++) {
                if (deep[i].equalsIgnoreCase(skinColor)) {
                    group = 3;
                }
            }
        }
        return group;
    }


    String[] img_urls = new String[] { "https://firebasestorage.googleapis.com/v0/b/myshadow-ef37d.appspot.com/o/swatch1.jpg?alt=media&token=63258c5a-b5f7-448e-be22-7201b5d978d3",
            "https://firebasestorage.googleapis.com/v0/b/myshadow-ef37d.appspot.com/o/swatch2.jpg?alt=media&token=86e8a27a-5011-4c8a-960a-098a3ba5afc7",
            "https://firebasestorage.googleapis.com/v0/b/myshadow-ef37d.appspot.com/o/swatch3.jpg?alt=media&token=05126253-2496-4bf6-bcfc-f26eb1a5843a",
            "https://firebasestorage.googleapis.com/v0/b/myshadow-ef37d.appspot.com/o/swatch4.jpg?alt=media&token=ec6e0011-f1c0-4bc4-8642-eccd6cf24610",
            "https://firebasestorage.googleapis.com/v0/b/myshadow-ef37d.appspot.com/o/swatch5.jpg?alt=media&token=a668ad32-954e-4ac2-bb5b-6eeac53ba9ca"};

    public ArrayAdapter<SwatchEntry> adapter;
    public ArrayList<SwatchEntry> swatchList = new ArrayList<>();
    // TODO: do this more elegantly
    public boolean added = false;

    public static MySearchListFragment.ProductEntry pEntry;
    public static ImageView expandedImage;
    public static boolean showingExpandedImage = false;
    public static View expandedBlack;
    public static TextView swatchTitle;
    public static TextView noSwatchesFound;

    DatabaseReference databaseSwatches;

    public static class SwatchEntry {
        public String uploaderName;
        public String swatchName;
        public String imageURL;
        public String color;
        public String user_id;
        public String id;

        public SwatchEntry(String s0, String s1, String s2, String s3, String s4, String s5) {
            uploaderName = s0;
            swatchName = s1;
            imageURL = s2;
            color = s3;
            user_id = s4;
            id = s5;
        }
    }


    public ArrayList<String> recentProducts = new ArrayList<>();
    public static DatabaseReference userInfo;
    public static FirebaseUser userF;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ArrayList<Like> likeList= new ArrayList<>();

    DatabaseReference databaseLike;
    private ArrayList<Bookmark> bookmarkList = new ArrayList<>();

    DatabaseReference databaseBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_my_product);

        context = this;


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userF = mAuth.getCurrentUser();




        userInfo = FirebaseDatabase.getInstance().getReference("users");
        //userInfo.addValueEventListener(new ValueEventListener() {
        Query query = userInfo;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recentProducts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user= postSnapshot.getValue(User.class);
                    if (user.getUser_id().equals(userF.getUid())) {
                        recentProducts.add(user.getProduct_id_1());
                        recentProducts.add(user.getProduct_id_2());
                        recentProducts.add(user.getProduct_id_3());
                        recentProducts.add(user.getProduct_id_4());
                        recentProducts.add(user.getProduct_id_5());
                    }
                }
                int check = -1;
                for (int i = 0; i < recentProducts.size(); i++) {
                    if (recentProducts.get(i).equals(pEntry.id)) {
                        check = i;
                    }
                }
                if (check == -1) {
                    recentProducts.add(0, pEntry.id);
                } else {
                    recentProducts.remove(check);
                    recentProducts.add(0, pEntry.id);
                }

                // https://stackoverflow.com/questions/41296416/updating-the-data-on-firebase-android
                userInfo.child(userF.getUid()).child("product_id_1").setValue(recentProducts.get(0));
                userInfo.child(userF.getUid()).child("product_id_2").setValue(recentProducts.get(1));
                userInfo.child(userF.getUid()).child("product_id_3").setValue(recentProducts.get(2));
                userInfo.child(userF.getUid()).child("product_id_4").setValue(recentProducts.get(3));
                userInfo.child(userF.getUid()).child("product_id_5").setValue(recentProducts.get(4));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        databaseSwatches = FirebaseDatabase.getInstance().getReference("swatches").child(pEntry.id);
        expandedImage = (ImageView) findViewById(R.id.expandedImage);
        expandedImage.setVisibility(View.GONE);
        showingExpandedImage = false;
        expandedBlack = (View) findViewById(R.id.expandedBlack);
        expandedBlack.setVisibility(View.GONE);

        swatchTitle = (TextView) findViewById(R.id.swatchTitle);
        swatchTitle.setVisibility(View.VISIBLE);
        //swatchTitle.setText("Swatches");
        noSwatchesFound = (TextView) findViewById(R.id.noSwatchesFound);
        noSwatchesFound.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Move", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MyProductActivity.this, MyUploadActivity.class);

                i.putExtra("PRODUCT_ID", pEntry.id);
                i.putExtra("PRODUCT_NAME", pEntry.product);
                i.putExtra("PRODUCT_BRAND", pEntry.brand);
                startActivity(i);
            }
        });

        final TextView text1 = (TextView) findViewById(R.id.productName);
        final TextView text2 = (TextView) findViewById(R.id.brandName);
        final ImageView img = (ImageView) findViewById(R.id.productImage);
        final TextView text3 = (TextView) findViewById(R.id.price);
        final ImageView r0 = (ImageView) findViewById(R.id.review0);
        final ImageView r1 = (ImageView) findViewById(R.id.review1);
        final ImageView r2 = (ImageView) findViewById(R.id.review2);
        final ImageView r3 = (ImageView) findViewById(R.id.review3);
        final ImageView r4 = (ImageView) findViewById(R.id.review4);

        if (pEntry != null) {
            text1.setText(pEntry.product);
            //                text1.setTextSize(bodySize + fontChange);
            //                text1.setLayoutParams(new LinearLayout.LayoutParams(350, 55 + fontChange * 4));
            text2.setText(pEntry.brand);
            text3.setText("$" + pEntry.price + " USD");

            r0.setImageResource(R.drawable.ic_review_circle_clean);
            r1.setImageResource(R.drawable.ic_review_circle_clean);
            r2.setImageResource(R.drawable.ic_review_circle_clean);
            r3.setImageResource(R.drawable.ic_review_circle_clean);
            r4.setImageResource(R.drawable.ic_review_circle_clean);

            if (pEntry.rating > 0) {
                r0.setImageResource(R.drawable.ic_review_circle_filled);
            }
            if (pEntry.rating > 1) {
                r1.setImageResource(R.drawable.ic_review_circle_filled);
            }
            if (pEntry.rating > 2) {
                r2.setImageResource(R.drawable.ic_review_circle_filled);
            }
            if (pEntry.rating > 3) {
                r3.setImageResource(R.drawable.ic_review_circle_filled);
            }
            if (pEntry.rating > 4) {
                r4.setImageResource(R.drawable.ic_review_circle_filled);
            }

            //Picasso.with(curr).load("https://firebasestorage.googleapis.com/v0/b/myshadow-ef37d.appspot.com/o/burgundytimesnine.jpg?alt=media&token=50e1afee-e4ea-476a-b997-81696d2f5cbd").into(img);
            Picasso.with(this).load(pEntry.imageURL).into(img);
        }




    }

    Bookmark thisBookmark = null;
    DatabaseReference databaseUsers;
    DatabaseReference databaseSkins;
    FirebaseAuth mAuth;
    public static Skin currSkin = null;
    public static int group;

    @Override
    protected void onStart() {
        super.onStart();


        final GridView gridview = findViewById(R.id.gridview);
        gridview.setNumColumns(2);
        /*if (!added) {
            String[] values1 = new String[] { "user0", "user1", "user2", "user3",
                    "user4"};
            String[] values2 = new String[] { "image0", "image1", "image2", "image3",
                    "image4"};

//        ArrayList<HistoryEntry> tempVals = new ArrayList<HistoryEntry>();
            for (int i = 0; i < values1.length; ++i) {
                swatchList.add(new SwatchEntry(values1[i], values2[i]));
            }
            added = true;
        }*/



        mAuth = FirebaseAuth.getInstance();
        userF = mAuth.getCurrentUser();
        databaseBookmark = FirebaseDatabase.getInstance().getReference("bookmarks").child(userF.getUid());
        Query query = databaseBookmark;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Bookmark bookmark = postSnapshot.getValue(Bookmark.class);
                    bookmarkList.add(bookmark);
                    if (bookmark.getProduct_id().equals(pEntry.id)) {
                        thisBookmark = bookmark;
                    }
                }

                final ImageView bm = (ImageView) findViewById(R.id.bookmark);
                bm.setTag("");
                bm.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                if (thisBookmark != null) {
                    bm.setTag(thisBookmark.getId());
                    bm.setImageResource(R.drawable.ic_bookmark_black_24dp);
                }


                FrameLayout part3 = (FrameLayout) findViewById(R.id.bookmarkPart);
                part3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView bm = (ImageView) v.findViewById(R.id.bookmark);

                        if (!((String)bm.getTag()).equals("")) {
                            databaseBookmark.child((String)bm.getTag()).removeValue();

                            //TODO: check for response
                            Toast.makeText(MyMainActivity.thisActivity, "Removed from bookmarks!", Toast.LENGTH_SHORT).show();
                            bm.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                            bm.setTag("");
                        } else {
                            String id = databaseBookmark.push().getKey();
                            String prodId = pEntry.id;
                            Bookmark bookmark = new Bookmark(id, prodId);
                            databaseBookmark.child(id).setValue(bookmark);

                            //TODO: check for response
                            Toast.makeText(MyMainActivity.thisActivity, "Bookmarked!", Toast.LENGTH_SHORT).show();
                            bm.setImageResource(R.drawable.ic_bookmark_black_24dp);
                            bm.setTag(bookmark.getId());
                        }


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Added for sorting
        mAuth = FirebaseAuth.getInstance();
        userF = mAuth.getCurrentUser();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        Query query3 = databaseUsers.orderByChild("user_id").equalTo(userF.getUid()).limitToFirst(1);
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    String skinId = user.getSelected_skin();
                    //MyMainActivity.thisActivity.toastMessage("Got data2: " + skinId);

                    mAuth = FirebaseAuth.getInstance();
                    userF = mAuth.getCurrentUser();
                    databaseSkins = FirebaseDatabase.getInstance().getReference("skins").child(userF.getUid());
                    Query query = databaseSkins.orderByChild("id").equalTo(skinId).limitToFirst(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Skin skin = postSnapshot.getValue(Skin.class);
                                currSkin = skin;
                                group = whichGroup(skin.getColor());

                            }

                            databaseLike = FirebaseDatabase.getInstance().getReference("likes").child(userF.getUid());
                            Query query2 = databaseLike;
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    likeList.clear();
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Like like = postSnapshot.getValue(Like.class);
                                        likeList.add(like);
                                        //Toast.makeText(MyMainActivity.thisActivity, postSnapshot.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                    //Toast.makeText(MyMainActivity.thisActivity, "Likes: " + likeList.size() + " " + likeList.get(0).getSwatch_id(), Toast.LENGTH_SHORT).show();


                                    databaseSwatches.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            swatchList.clear();
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                Swatch swatch = postSnapshot.getValue(Swatch.class);
                                                if (isInGroup(group, swatch.getColor())) {
                                                    swatchList.add(0,new SwatchEntry("username", swatch.getSwatch_name(), swatch.getImg_url(), swatch.getColor(), swatch.getUser_id(), swatch.getId()));
                                                } else {
                                                    swatchList.add(new SwatchEntry("username", swatch.getSwatch_name(), swatch.getImg_url(), swatch.getColor(), swatch.getUser_id(), swatch.getId()));
                                                }

                                            }
                                            if (swatchList.size() > 0) {
                                                //swatchTitle.setText("Swatches");
                                                swatchTitle.setVisibility(View.VISIBLE);
                                                noSwatchesFound.setVisibility(View.INVISIBLE);
                                            } else {
                                                //swatchTitle.setText("No swatches found");
                                                swatchTitle.setVisibility(View.INVISIBLE);
                                                noSwatchesFound.setVisibility(View.VISIBLE);
                                            }

                                            adapter=new ArrayAdapter<SwatchEntry>(MyProductActivity.context, R.layout.layout_swatch_entry, swatchList) {
                                                @Override
                                                public View getView(int position, View convertView, ViewGroup parent) {
                                                    SwatchEntry current = swatchList.get(position);

                                                    // Inflate only once
                                                    if(convertView == null) {
                                                        convertView = getLayoutInflater()
                                                                .inflate(R.layout.layout_swatch_entry, null, false);
                                                    }

                                                    final ImageView image1 = (ImageView) convertView.findViewById(R.id.productImage);
                                                    //image1.setImageResource(R.drawable.ic_home_black_24dp);
                                                    Picasso.with(MyProductActivity.context).load(current.imageURL).into(image1);
                                                    final ImageView colorSquare = (ImageView) convertView.findViewById(R.id.colorSquare);
                                                    colorSquare.setColorFilter(Color.parseColor("#" + current.color));


                                                    final ImageView lk = (ImageView) convertView.findViewById(R.id.like);

                                                    lk.setTag("");
                                                    lk.setImageResource(R.drawable.ic_liked_unfilled);
                                                    for (Like lke : likeList) {
                                                        //https://stackoverflow.com/questions/52435658/getting-a-specific-object-item-by-id-in-firebase
                                                        if (current.id.equals(lke.getSwatch_id())) {
                                                            lk.setTag(lke.getId());
                                                            lk.setImageResource(R.drawable.ic_liked_filled);
                                                        }
                                                    }

                                                    FrameLayout likeBox = (FrameLayout) convertView.findViewById(R.id.likePart);
                                                    likeBox.setTag(position);
                                                    likeBox.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            ImageView lk = (ImageView) v.findViewById(R.id.like);

                                                            if (!((String)lk.getTag()).equals("")) {
                                                                databaseLike.child((String)lk.getTag()).removeValue();

                                                                //TODO: check for response
                                                                Toast.makeText(MyMainActivity.thisActivity, "Removed from likes!", Toast.LENGTH_SHORT).show();
                                                                lk.setImageResource(R.drawable.ic_liked_unfilled);
                                                                lk.setTag("");
                                                            } else {
                                                                String id = databaseLike.push().getKey();
                                                                String swatchId = swatchList.get((int)v.getTag()).id;
                                                                Like like = new Like(id, swatchId);
                                                                databaseLike.child(id).setValue(like);

                                                                //TODO: check for response
                                                                Toast.makeText(MyMainActivity.thisActivity, "Liked!", Toast.LENGTH_SHORT).show();
                                                                lk.setImageResource(R.drawable.ic_liked_filled);
                                                                lk.setTag(like.getId());
                                                            }


                                                        }
                                                    });

                                                    return convertView;
                                                }
                                            };




                                            gridview.setAdapter(adapter);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); // Added for sorting








        // gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                // final String item = (String) parent.getItemAtPosition(position);
                // TODO: Expand image

                SwatchEntry current = swatchList.get(position);
                Picasso.with(MyProductActivity.context).load(current.imageURL).into(expandedImage);
                expandedImage.setVisibility(View.VISIBLE);
                expandedBlack.setVisibility(View.VISIBLE);
                showingExpandedImage = true;

                // Toast.makeText(getApplicationContext(), "Expand", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hideExpandedImage(View v) {
        expandedImage.setVisibility(View.GONE);
        expandedBlack.setVisibility(View.GONE);
        showingExpandedImage = false;
    }

    @Override
    public void onBackPressed()
    {
        if (showingExpandedImage) {
            expandedImage.setVisibility(View.GONE);
            expandedBlack.setVisibility(View.GONE);
            showingExpandedImage = false;
        } else {
            super.onBackPressed();  // optional depending on your needs
        }
    }


}
