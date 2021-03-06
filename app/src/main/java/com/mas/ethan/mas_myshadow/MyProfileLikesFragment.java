package com.mas.ethan.mas_myshadow;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
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
import com.mas.ethan.mas_myshadow.models.Bookmark;
import com.mas.ethan.mas_myshadow.models.Like;
import com.mas.ethan.mas_myshadow.models.Product;
import com.mas.ethan.mas_myshadow.models.Skin;
import com.mas.ethan.mas_myshadow.models.Swatch;
import com.mas.ethan.mas_myshadow.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileLikesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileLikesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileLikesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context curr;
    private View thisView;

    private OnFragmentInteractionListener mListener;

    public MyProfileLikesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileLikesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileLikesFragment newInstance(String param1, String param2) {
        MyProfileLikesFragment fragment = new MyProfileLikesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile_likes, container, false);
        thisView = v;
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }


    public ArrayAdapter<MyProductActivity.SwatchEntry> adapter;
    public ArrayList<MyProductActivity.SwatchEntry> swatchList = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Like> mylist = new ArrayList<>();

    DatabaseReference databaseLike;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*parent.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(curr, "tabnum:" + tabNum, Toast.LENGTH_SHORT).show();
            }
        });*/


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser userF = mAuth.getCurrentUser();
        databaseLike = FirebaseDatabase.getInstance().getReference("likes").child(userF.getUid());

        //Query query = mDatabaseReference.child("products").orderByChild("brand").equalTo("ColourPop");
        //Query query = mDatabaseReference.child("products").orderByChild("brand").equalTo(searchString);
        final GridView gridview = (GridView) thisView.findViewById(R.id.gridview);
        gridview.setNumColumns(2);


        swatchList.clear();
        databaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                swatchList.clear();
                mylist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Like like = postSnapshot.getValue(Like.class);
                    mylist.add(like);
                }

                mDatabaseReference = FirebaseDatabase.getInstance().getReference("swatches");

                Query query = mDatabaseReference;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        swatchList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            for (DataSnapshot furtherSnapshot : postSnapshot.getChildren()) {
                                Swatch swatch= furtherSnapshot.getValue(Swatch.class);
                                for (Like lkm : mylist) {
                                    //https://stackoverflow.com/questions/52435658/getting-a-specific-object-item-by-id-in-firebase
                                    if (swatch.getId().equals(lkm.getSwatch_id())) {
                                        swatchList.add(new MyProductActivity.SwatchEntry("username", swatch.getSwatch_name(), swatch.getImg_url(), swatch.getColor(), swatch.getUser_id(), swatch.getId()));
                                    }
                                }
                            }
                        }


                        adapter = new ArrayAdapter<MyProductActivity.SwatchEntry>(curr, R.layout.layout_swatch_entry, swatchList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                MyProductActivity.SwatchEntry current = swatchList.get(position);

                                // Inflate only once
                                if(convertView == null) {
                                    convertView = getLayoutInflater()
                                            .inflate(R.layout.layout_swatch_entry, null, false);
                                }

                                final ImageView image1 = (ImageView) convertView.findViewById(R.id.productImage);
                                //image1.setImageResource(R.drawable.ic_home_black_24dp);
                                Picasso.with(curr).load(current.imageURL).into(image1);
                                final ImageView colorSquare = (ImageView) convertView.findViewById(R.id.colorSquare);
                                colorSquare.setColorFilter(Color.parseColor("#" + current.color));


                                final ImageView lk = (ImageView) convertView.findViewById(R.id.like);

                                lk.setTag(current.id);
                                lk.setImageResource(R.drawable.ic_liked_filled);

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

        // TODO: if this is still needed, remove the onclicklisteners above
        // listview.setOnItemClickListener(messageClickedHandler);



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
        // TODO: Update argument type and name
    }
}
