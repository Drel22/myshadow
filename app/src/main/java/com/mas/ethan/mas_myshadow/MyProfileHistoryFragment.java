package com.mas.ethan.mas_myshadow;

import android.content.Context;
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
import com.mas.ethan.mas_myshadow.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileHistoryFragment extends Fragment {
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

    public MyProfileHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileHistoryFragment newInstance(String param1, String param2) {
        MyProfileHistoryFragment fragment = new MyProfileHistoryFragment();
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
        View v = inflater.inflate(R.layout.fragment_my_profile_history, container, false);
        thisView = v;
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    public ArrayAdapter<MySearchListFragment.ProductEntry> adapter;
    public ArrayList<MySearchListFragment.ProductEntry> productList = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Product> mylist = new ArrayList<>();

    public ArrayList<String> recentProducts = new ArrayList<>();
    public static DatabaseReference userInfo;
    public static FirebaseUser userF;

    private ArrayList<Bookmark> bookmarkList = new ArrayList<>();

    DatabaseReference databaseBookmark;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userF = mAuth.getCurrentUser();
        userInfo = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        databaseBookmark = FirebaseDatabase.getInstance().getReference("bookmarks").child(userF.getUid());

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

                    Query query = databaseBookmark;
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            bookmarkList.clear();
                            productList.clear();
                            productList.add(null);
                            productList.add(null);
                            productList.add(null);
                            productList.add(null);
                            productList.add(null);
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Bookmark bookmark = postSnapshot.getValue(Bookmark.class);
                                bookmarkList.add(bookmark);
                            }

                            Query query = mDatabaseReference.child("products");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mylist.clear();
                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        mylist.add(singleSnapshot.getValue(Product.class));
                                    }

                                    if (mylist.size() > 0) {

                                        for (int i = 0; i < mylist.size(); ++i) {
                                            if (mylist.get(i).getId().equals(recentProducts.get(0))) {
                                                // this will also take care of spaces like tabs etc.
                                                productList.set(0, new MySearchListFragment.ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url(), mylist.get(i).getPrice(), mylist.get(i).getRating()));
                                            } else if (mylist.get(i).getId().equals(recentProducts.get(1))) {
                                                productList.set(1, new MySearchListFragment.ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url(), mylist.get(i).getPrice(), mylist.get(i).getRating()));
                                            } else if (mylist.get(i).getId().equals(recentProducts.get(2))) {
                                                productList.set(2, new MySearchListFragment.ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url(), mylist.get(i).getPrice(), mylist.get(i).getRating()));
                                            } else if (mylist.get(i).getId().equals(recentProducts.get(3))) {
                                                productList.set(3, new MySearchListFragment.ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url(), mylist.get(i).getPrice(), mylist.get(i).getRating()));
                                            } else if (mylist.get(i).getId().equals(recentProducts.get(4))) {
                                                productList.set(4, new MySearchListFragment.ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url(), mylist.get(i).getPrice(), mylist.get(i).getRating()));
                                            }
                                            // productList.add(new ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url()));
                                        }

                                        for (int i = productList.size() - 1; i > -1; i--) {
                                            if (productList.get(i) == null) {
                                                productList.remove(i);
                                            }
                                        }

                                        final ListView listview = (ListView) thisView.findViewById(R.id.listview);

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

                                                bm.setTag("");
                                                bm.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                                                for (Bookmark bkm : bookmarkList) {
                                                    //https://stackoverflow.com/questions/52435658/getting-a-specific-object-item-by-id-in-firebase
                                                    if (current.id.equals(bkm.getProduct_id())) {
                                                        bm.setTag(bkm.getId());
                                                        bm.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                                    }
                                                }


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
                                                RelativeLayout part3 = (RelativeLayout) convertView.findViewById(R.id.otherPart);
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

                                        // TODO: if this is still needed, remove the onclicklisteners above
                                        // listview.setOnItemClickListener(messageClickedHandler);

                                        listview.setAdapter(adapter);
                                    } else {
//                                        hostFrag.changeToNotFoundFragment();
                                    }

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
        });
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
        void openProductPage();
    }
}
