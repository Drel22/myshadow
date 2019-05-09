package com.mas.ethan.mas_myshadow;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
import com.mas.ethan.mas_myshadow.models.Product;
import com.mas.ethan.mas_myshadow.models.Skin;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBookmarksProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBookmarksProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBookmarksProductsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private Context curr;
    private View thisView;

    public ArrayAdapter<MySearchListFragment.ProductEntry> adapter;
    public ArrayList<MySearchListFragment.ProductEntry> productList = new ArrayList<>();
    // TODO: do this more elegantly
    public boolean added = false;
    private OnFragmentInteractionListener mListener;

    public MyBookmarksProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBookmarksProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBookmarksProductsFragment newInstance(String param1, String param2) {
        MyBookmarksProductsFragment fragment = new MyBookmarksProductsFragment();
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
        View v = inflater.inflate(R.layout.fragment_my_bookmarks_products, container, false);
        thisView = v;
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Bookmark> mylist = new ArrayList<>();

    DatabaseReference databaseBookmark;




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*parent.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(curr, "tabnum:" + tabNum, Toast.LENGTH_SHORT).show();
            }
        });*/


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser userF = mAuth.getCurrentUser();
        databaseBookmark = FirebaseDatabase.getInstance().getReference("bookmarks").child(userF.getUid());

        //Query query = mDatabaseReference.child("products").orderByChild("brand").equalTo("ColourPop");
        //Query query = mDatabaseReference.child("products").orderByChild("brand").equalTo(searchString);
        final ListView listview = (ListView) thisView.findViewById(R.id.listview);


        productList.clear();
        databaseBookmark.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                mylist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Bookmark bookmark = postSnapshot.getValue(Bookmark.class);
                    mylist.add(bookmark);
                }

                mDatabaseReference = FirebaseDatabase.getInstance().getReference("products");

                Query query = mDatabaseReference;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        productList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                            Product product = postSnapshot.getValue(Product.class);

                            for (Bookmark bm : mylist) {
                                //https://stackoverflow.com/questions/52435658/getting-a-specific-object-item-by-id-in-firebase
                                if (product.getId().equals(bm.getProduct_id())) {
                                    productList.add(new MySearchListFragment.ProductEntry(product.getId(), product.getProduct_name(), product.getBrand(), product.getImg_url(), product.getPrice(), product.getRating()));
                                }
                            }



                        }



                        adapter = new ArrayAdapter<MySearchListFragment.ProductEntry>(curr, R.layout.layout_search_entry, productList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                MySearchListFragment.ProductEntry current = productList.get(position);

                                // Inflate only once
                                if (convertView == null) {
                                    convertView = getLayoutInflater()
                                            .inflate(R.layout.layout_search_entry, null, false);
                                }

                                final TextView text1 = (TextView) convertView.findViewById(R.id.productName);
                                final TextView text2 = (TextView) convertView.findViewById(R.id.brandName);
                                final TextView text3 = (TextView) convertView.findViewById(R.id.price);
                                final ImageView img = (ImageView) convertView.findViewById(R.id.productImage);

                                final ImageView r0 = (ImageView) convertView.findViewById(R.id.review0);
                                final ImageView r1 = (ImageView) convertView.findViewById(R.id.review1);
                                final ImageView r2 = (ImageView) convertView.findViewById(R.id.review2);
                                final ImageView r3 = (ImageView) convertView.findViewById(R.id.review3);
                                final ImageView r4 = (ImageView) convertView.findViewById(R.id.review4);

                                final ImageView bm = (ImageView) convertView.findViewById(R.id.bookmark);
                                bm.setImageResource(R.drawable.ic_close_black_24dp);
                                bm.setColorFilter(R.color.darkRed);


                                r0.setImageResource(R.drawable.ic_review_circle_clean);
                                r1.setImageResource(R.drawable.ic_review_circle_clean);
                                r2.setImageResource(R.drawable.ic_review_circle_clean);
                                r3.setImageResource(R.drawable.ic_review_circle_clean);
                                r4.setImageResource(R.drawable.ic_review_circle_clean);

                                if (current.rating > 0) {
                                    r0.setImageResource(R.drawable.ic_review_circle_filled);
                                }
                                if (current.rating > 1) {
                                    r1.setImageResource(R.drawable.ic_review_circle_filled);
                                }
                                if (current.rating > 2) {
                                    r2.setImageResource(R.drawable.ic_review_circle_filled);
                                }
                                if (current.rating > 3) {
                                    r3.setImageResource(R.drawable.ic_review_circle_filled);
                                }
                                if (current.rating > 4) {
                                    r4.setImageResource(R.drawable.ic_review_circle_filled);
                                }

                                text1.setText(current.product);
                                //                text1.setTextSize(bodySize + fontChange);
                                //                text1.setLayoutParams(new LinearLayout.LayoutParams(350, 55 + fontChange * 4));
                                text2.setText(current.brand);
                                text3.setText("$" + current.price + " USD");

                                //Picasso.with(curr).load("https://firebasestorage.googleapis.com/v0/b/myshadow-ef37d.appspot.com/o/burgundytimesnine.jpg?alt=media&token=50e1afee-e4ea-476a-b997-81696d2f5cbd").into(img);
                                Picasso.with(curr).load(current.imageURL).into(img);

                                ConstraintLayout part1 = (ConstraintLayout) convertView.findViewById(R.id.imagePart);
                                LinearLayout part2 = (LinearLayout) convertView.findViewById(R.id.textPart);
                                FrameLayout part3 = (FrameLayout) convertView.findViewById(R.id.bookmarkPart);
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

                                        if (mListener != null) {
                                            MyProductActivity.pEntry = productList.get((int)v.getTag());
                                            mListener.openProductPage();
                                        }
                                    }
                                });

                                return convertView;
                            }
                        };
                        listview.setAdapter(adapter);


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
        //void onFragmentInteraction(Uri uri);
        void openProductPage();
    }


}
