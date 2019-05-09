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
import com.mas.ethan.mas_myshadow.models.Skin;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.mas.ethan.mas_myshadow.models.Product;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MySearchListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MySearchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySearchListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String searchString;
    public static MySearchFragment hostFrag;

    public static String promotedId = "0";

    private static FragmentActivity parent;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context curr;
    private View thisView;

    public ArrayAdapter<ProductEntry> adapter;
    public ArrayList<ProductEntry> productList = new ArrayList<>();
    // TODO: do this more elegantly
    public boolean added = false;

    public MySearchListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySearchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySearchListFragment newInstance(String param1, String param2) {
        MySearchListFragment fragment = new MySearchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        searchString = args.getString("search");
        View v = inflater.inflate(R.layout.fragment_my_search_list, container, false);
        thisView = v;
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }


    public static class ProductEntry {
        public String product;
        public String brand;
        public String imageURL;
        public String id;
        public String price;
        public int rating;

        public ProductEntry(String s0, String s1, String s2, String s3, String s4, int i) {
            id = s0;
            product = s1;
            brand = s2;
            imageURL = s3;
            price = s4;
            rating = i;
        }
    }

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Product> mylist = new ArrayList<>();

    private ArrayList<Bookmark> bookmarkList = new ArrayList<>();

    DatabaseReference databaseBookmark;
    DatabaseReference databaseProduct;
    Product promoted = null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*parent.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(curr, "tabnum:" + tabNum, Toast.LENGTH_SHORT).show();
            }
        });*/

        databaseProduct = FirebaseDatabase.getInstance().getReference("products");
        Query query = databaseProduct.orderByChild("id").equalTo(promotedId).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mylist.clear();

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    promoted = singleSnapshot.getValue(Product.class);
                    // mylist.add(singleSnapshot.getValue(Product.class));
                }


                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser userF = mAuth.getCurrentUser();
                databaseBookmark = FirebaseDatabase.getInstance().getReference("bookmarks").child(userF.getUid());

                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                //Query query = mDatabaseReference.child("products").orderByChild("brand").equalTo("ColourPop");
                //Query query = mDatabaseReference.child("products").orderByChild("brand").equalTo(searchString);

                //databaseBookmark.addValueEventListener(new ValueEventListener() {
                Query query = databaseBookmark;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        bookmarkList.clear();
                        productList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Bookmark bookmark = postSnapshot.getValue(Bookmark.class);
                            bookmarkList.add(bookmark);
                            //Toast.makeText(MyMainActivity.thisActivity, postSnapshot.toString(), Toast.LENGTH_SHORT).show();
                        }

                        Query query = mDatabaseReference.child("products");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    mylist.add(singleSnapshot.getValue(Product.class));
                                }

                                if (mylist.size() > 0) {
                                    productList.add(new ProductEntry(promoted.getId(), promoted.getProduct_name(), promoted.getBrand(), promoted.getImg_url(), promoted.getPrice(), promoted.getRating()));

                                    for (int i = 0; i < mylist.size(); ++i) {
                                        if(mylist.get(i).getProduct_name().replaceAll("\\s+","").equalsIgnoreCase(searchString.replaceAll("\\s+",""))) {
                                            // this will also take care of spaces like tabs etc.
                                            productList.add(new ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url(), mylist.get(i).getPrice(), mylist.get(i).getRating()));
                                        } else if (mylist.get(i).getBrand().replaceAll("\\s+","").equalsIgnoreCase(searchString.replaceAll("\\s+",""))) {
                                            productList.add(new ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url(), mylist.get(i).getPrice(), mylist.get(i).getRating()));
                                        }
                                        // productList.add(new ProductEntry(mylist.get(i).getId(), mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), mylist.get(i).getImg_url()));
                                    }
                                    added = true;

                                    final ListView listview = (ListView) thisView.findViewById(R.id.listview);

                                    adapter = new ArrayAdapter<ProductEntry>(curr, R.layout.layout_search_entry, productList) {
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent) {
                                            ProductEntry current = productList.get(position);

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

                                            final RelativeLayout promo = (RelativeLayout) convertView.findViewById(R.id.promo);
                                            promo.setVisibility(View.GONE);
                                            if (position == 0) {
                                                promo.setVisibility(View.VISIBLE);
                                            }

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
                                    hostFrag.changeToNotFoundFragment();
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        /*final ListView listview = (ListView) thisView.findViewById(R.id.listview);
        if (!added) {



            String[] values1 = new String[] { "Product0", "Product1", "Product2", "Product3",
                    "Product4"};
            String[] values2 = new String[] { "Brand0", "Brand1", "Brand2", "Brand3",
                    "Brand4"};
            String[] values3 = new String[] { "URL0", "URL1", "URL2", "URL3",
                    "URL4"};

//            for (int i = 0; i < values1.length; ++i) {
//                productList.add(new ProductEntry(values1[i], values2[i], values3[i]));
//            }
//            added = true;


            parent.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(curr, "mylist size:" + mylist.size(), Toast.LENGTH_SHORT).show();
                }
            });

            for (int i = 0; i < mylist.size(); ++i) {
                productList.add(new ProductEntry(mylist.get(i).getProduct_name(), mylist.get(i).getBrand(), "img"));
            }
            added = true;
        }
//        final ArrayList<String> list1 = new ArrayList<String>();
//        final ArrayList<String> list2 = new ArrayList<String>();
//        final ArrayList<String> list3 = new ArrayList<String>();
//        final ArrayList<String> list4 = new ArrayList<String>();
//        for (int i = 0; i < values1.length; ++i) {
//            list1.add(values1[i]);
//        }
//        for (int i = 0; i < values2.length; ++i) {
//            list2.add(values2[i]);
//        }
//        for (int i = 0; i < values3.length; ++i) {
//            list3.add(values3[i]);
//        }
//        for (int i = 0; i < values4.length; ++i) {
//            list4.add(values4[i]);
//        }
//        final StableArrayAdapter adapter = new StableArrayAdapter(curr,
//                R.layout.layout_history_entry, list1, list2, list3, list4);

        adapter=new ArrayAdapter<ProductEntry>(curr, R.layout.layout_search_entry, productList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ProductEntry current = productList.get(position);

                // Inflate only once
                if(convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.layout_search_entry, null, false);
                }

                final TextView text1 = (TextView) convertView.findViewById(R.id.productName);
                final TextView text2 = (TextView) convertView.findViewById(R.id.brandName);

                text1.setText(current.product);
//                text1.setTextSize(bodySize + fontChange);
//                text1.setLayoutParams(new LinearLayout.LayoutParams(350, 55 + fontChange * 4));
                text2.setText(current.brand);

                return convertView;
            }
        };

        listview.setOnItemClickListener(messageClickedHandler);

        listview.setAdapter(adapter);*/

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view,
//                                    int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//                view.animate().setDuration(2000).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                list.remove(item);
//                                adapter.notifyDataSetChanged();
//                                view.setAlpha(1);
//                            }
//                        });
//            }
//
//        });


    }

    private AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Do something in response to the click
            // TODO: navigate to the relevant product page/instantiate the relevant product page

            // Get product name and pass through here
            if (mListener != null) {
                MyProductActivity.pEntry = productList.get(position);
                mListener.openProductPage();
            }
        }
    };

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
