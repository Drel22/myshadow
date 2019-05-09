package com.mas.ethan.mas_myshadow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.mas.ethan.mas_myshadow.models.Product;
import com.mas.ethan.mas_myshadow.models.Skin;
import com.mas.ethan.mas_myshadow.models.Swatch;
import com.mas.ethan.mas_myshadow.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBookmarksSkinTonesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBookmarksSkinTonesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBookmarksSkinTonesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context curr;
    private View thisView;
    public static MyBookmarksSkinTonesFragment thisFrag;


    DatabaseReference databaseSkin;

    public MyBookmarksSkinTonesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBookmarksSkinTonesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBookmarksSkinTonesFragment newInstance(String param1, String param2) {
        MyBookmarksSkinTonesFragment fragment = new MyBookmarksSkinTonesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFrag = this;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_bookmarks_skin_tones, container, false);
        thisView = v;
        return v;
    }

    public enum Skintype {
        OILY, DRY;
    }
    public enum Season {
        AUTUMN, SPRING, SUMMER, WINTER;
    }

    public class SkinEntry {
        public String name;
        public String color;
        public String skintype;
        public String season;
        public String id;
        public boolean selected;

        public SkinEntry(String name, String color, String skintype, String season, String id, boolean selected) {
            this.name =name;
            this.color = color;
            this.skintype = skintype;
            this.season = season;
            this.id = id;
            this.selected = selected;
        }
    }

    public static ArrayList<SkinEntry> mylist = new ArrayList<>();
    public static ArrayAdapter<SkinEntry> adapter;
    public static FirebaseUser userF;
    public static FirebaseAuth mAuth;

    public static DatabaseReference userInfo;
    public String selectedId;
    public boolean gotSelected;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        selectedId = "";
        gotSelected = false;

        FloatingActionButton fab = thisView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMainActivity.thisActivity.newSkinTone();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        userF = mAuth.getCurrentUser();
        databaseSkin = FirebaseDatabase.getInstance().getReference("skins").child(userF.getUid());
        // databaseSkin = FirebaseDatabase.getInstance().getReference("skins").child(MyLoginActivity.user.getUid());

//        mylist.clear();
//        mylist.add(new SkinEntry("Ethan", "0000FF", "DRY", "SPRING"));
//        mylist.add(new SkinEntry("Anisha", "FF0000", "DRY", "AUTUMN"));
//        mylist.add(new SkinEntry("Johnny", "00FF00", "DRY", "WINTER"));

        final ListView listview = (ListView) thisView.findViewById(R.id.listview);

        adapter = new ArrayAdapter<SkinEntry>(curr, R.layout.layout_skin_entry, mylist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                SkinEntry current = mylist.get(position);

                // Inflate only once
                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.layout_skin_entry, null, false);
                }

                final TextView name = (TextView) convertView.findViewById(R.id.name);
                final ImageView skincolor = (ImageView) convertView.findViewById(R.id.color);
                final ImageView skintypeimage = (ImageView) convertView.findViewById(R.id.skintype);
                final ImageView weatherimage = (ImageView) convertView.findViewById(R.id.season);

                final ImageView check = (ImageView) convertView.findViewById(R.id.check);
                check.setImageResource(R.drawable.ic_check_off);
                if (current.selected) {
                    check.setImageResource(R.drawable.ic_check_on);
                }

                RelativeLayout checkbox = (RelativeLayout) convertView.findViewById(R.id.checkbox);
                //checkbox.setTag(0, current.id);
                checkbox.setTag(position);
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sid = mylist.get((int)v.getTag()).id;
                        for (int i = 0; i < mylist.size(); i++) {
                            if (mylist.get(i).selected && !mylist.get(i).id.equals(sid)) {
                                mylist.get(i).selected = false;
                            }
                        }
                        mAuth = FirebaseAuth.getInstance();
                        userF = mAuth.getCurrentUser();
                        userInfo = FirebaseDatabase.getInstance().getReference("users");
                        if (mylist.get((int)v.getTag()).selected) {
                            //MyMainActivity.thisActivity.toastMessage("true");
                            userInfo.child(userF.getUid()).child("selected_skin").setValue("");
                            mylist.get((int)v.getTag()).selected = false;
                        } else {
                            //MyMainActivity.thisActivity.toastMessage("false: " + sid);
                            userInfo.child(userF.getUid()).child("selected_skin").setValue(sid);
                            mylist.get((int)v.getTag()).selected = true;
                        }
                        MyBookmarksSkinTonesFragment.thisFrag.refreshViews();
                    }
                });

                name.setText(current.name);

                skincolor.setColorFilter(Color.parseColor("#" + current.color));


                if (current.skintype.equals("OILY")) {
                    skintypeimage.setImageResource(R.drawable.ic_oily);
                } else if (current.skintype.equals("DRY")) {
                    skintypeimage.setImageResource(R.drawable.ic_dry);
                } else if (current.skintype.equals("NORMAL")) {
                    skintypeimage.setImageResource(R.drawable.ic_normal);
                } else if (current.skintype.equals("COMBINATION")) {
                    skintypeimage.setImageResource(R.drawable.ic_combinationsvg);
                }

                if (current.season.equals("AUTUMN")) {
                    weatherimage.setImageResource(R.drawable.ic_autumn);
                } else if (current.season.equals("SPRING")) {
                    weatherimage.setImageResource(R.drawable.ic_spring);
                } else if (current.season.equals("SUMMER")) {
                    weatherimage.setImageResource(R.drawable.ic_summer);
                } else if (current.season.equals("WINTER")) {
                    weatherimage.setImageResource(R.drawable.ic_winter);
                }

                ImageView box = (ImageView) convertView.findViewById(R.id.box);
                box.setTag(current.id);
                box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyMainActivity.thisActivity.editSkinTone((String)v.getTag());
                    }
                });


                /*ConstraintLayout part1 = (ConstraintLayout) convertView.findViewById(R.id.imagePart);
                LinearLayout part2 = (LinearLayout) convertView.findViewById(R.id.textPart);
                RelativeLayout part3 = (RelativeLayout) convertView.findViewById(R.id.bookmarkPart);
                part1.setTag(position);
                part2.setTag(position);
                part3.setTag(position);

                part1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            MyProductActivity.pEntry = productList.get((int)v.getTag());
                            mListener.openProductPage();
                        }
                    }
                });
                part2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            MyProductActivity.pEntry = productList.get((int)v.getTag());
                            mListener.openProductPage();
                        }
                    }
                });
                part3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView bm = (ImageView) v.findViewById(R.id.bookmark);
                        bm.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    }
                });*/

                return convertView;
            }
        };


        mAuth = FirebaseAuth.getInstance();
        userF = mAuth.getCurrentUser();
        userInfo = FirebaseDatabase.getInstance().getReference("users");
        //userInfo.addValueEventListener(new ValueEventListener() {
        Query query = userInfo.orderByChild("user_id").equalTo(userF.getUid()).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user= postSnapshot.getValue(User.class);


                    if (!user.getSelected_skin().equals("")) {
                        selectedId = user.getSelected_skin();
                    }

                    databaseSkin.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mylist.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Skin skin = postSnapshot.getValue(Skin.class);
                                boolean sel = false;
                                if (skin.getId().equals(selectedId)) {
                                    sel = true;
                                }
                                mylist.add(new SkinEntry(skin.getName(), skin.getColor(), skin.getType(), skin.getSeason(), skin.getId(), sel));
                            }
                            if (mylist.size() > 0) {
                                //swatchTitle.setText("Swatches");
//                    swatchTitle.setVisibility(View.VISIBLE);
//                    noSwatchesFound.setVisibility(View.INVISIBLE);
                            } else {
                                //swatchTitle.setText("No swatches found");
//                    swatchTitle.setVisibility(View.INVISIBLE);
//                    noSwatchesFound.setVisibility(View.VISIBLE);
                            }
                            listview.setAdapter(adapter);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                //userInfo.child(userF.getUid()).child("product_id_5").setValue(recentProducts.get(4));










            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // TODO: if this is still needed, remove the onclicklisteners above
        // listview.setOnItemClickListener(messageClickedHandler);

        listview.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Do something in response to the click
            // TODO: navigate to the relevant product page/instantiate the relevant product page

            // Get product name and pass through here
//            if (mListener != null) {
//                MyProductActivity.pEntry = productList.get(position);
//                mListener.openProductPage();
//            }
        }
    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        curr = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        curr = null;
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }

    public void refreshViews() {
        final ListView listview = (ListView) thisView.findViewById(R.id.listview);
        adapter = new ArrayAdapter<SkinEntry>(curr, R.layout.layout_skin_entry, mylist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                SkinEntry current = mylist.get(position);

                // Inflate only once
                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.layout_skin_entry, null, false);
                }

                final TextView name = (TextView) convertView.findViewById(R.id.name);
                final ImageView skincolor = (ImageView) convertView.findViewById(R.id.color);
                final ImageView skintypeimage = (ImageView) convertView.findViewById(R.id.skintype);
                final ImageView weatherimage = (ImageView) convertView.findViewById(R.id.season);

                final ImageView check = (ImageView) convertView.findViewById(R.id.check);
                check.setImageResource(R.drawable.ic_check_off);
                if (current.selected) {
                    check.setImageResource(R.drawable.ic_check_on);
                }

                RelativeLayout checkbox = (RelativeLayout) convertView.findViewById(R.id.checkbox);
                //checkbox.setTag(0, current.id);
                checkbox.setTag(position);
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sid = mylist.get((int)v.getTag()).id;
                        for (int i = 0; i < mylist.size(); i++) {
                            if (mylist.get(i).selected && !mylist.get(i).id.equals(sid)) {
                                mylist.get(i).selected = false;
                            }
                        }
                        mAuth = FirebaseAuth.getInstance();
                        userF = mAuth.getCurrentUser();
                        userInfo = FirebaseDatabase.getInstance().getReference("users");
                        if (mylist.get((int)v.getTag()).selected) {
                            //MyMainActivity.thisActivity.toastMessage("true");
                            userInfo.child(userF.getUid()).child("selected_skin").setValue("");
                            mylist.get((int)v.getTag()).selected = false;
                        } else {
                            //MyMainActivity.thisActivity.toastMessage("false: " + sid);
                            userInfo.child(userF.getUid()).child("selected_skin").setValue(sid);
                            mylist.get((int)v.getTag()).selected = true;
                        }
                        MyBookmarksSkinTonesFragment.thisFrag.refreshViews();
                    }
                });

                name.setText(current.name);

                skincolor.setColorFilter(Color.parseColor("#" + current.color));


                if (current.skintype.equals("OILY")) {
                    skintypeimage.setImageResource(R.drawable.ic_oily);
                } else if (current.skintype.equals("DRY")) {
                    skintypeimage.setImageResource(R.drawable.ic_dry);
                } else if (current.skintype.equals("NORMAL")) {
                    skintypeimage.setImageResource(R.drawable.ic_normal);
                } else if (current.skintype.equals("COMBINATION")) {
                    skintypeimage.setImageResource(R.drawable.ic_combinationsvg);
                }

                if (current.season.equals("AUTUMN")) {
                    weatherimage.setImageResource(R.drawable.ic_autumn);
                } else if (current.season.equals("SPRING")) {
                    weatherimage.setImageResource(R.drawable.ic_spring);
                } else if (current.season.equals("SUMMER")) {
                    weatherimage.setImageResource(R.drawable.ic_summer);
                } else if (current.season.equals("WINTER")) {
                    weatherimage.setImageResource(R.drawable.ic_winter);
                }

                ImageView box = (ImageView) convertView.findViewById(R.id.box);
                box.setTag(current.id);
                box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyMainActivity.thisActivity.editSkinTone((String)v.getTag());
                    }
                });


                return convertView;
            }
        };
        listview.setAdapter(adapter);
    }
}
