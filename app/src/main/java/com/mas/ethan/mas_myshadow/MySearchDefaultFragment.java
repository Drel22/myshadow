package com.mas.ethan.mas_myshadow;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mas.ethan.mas_myshadow.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MySearchDefaultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MySearchDefaultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySearchDefaultFragment extends Fragment {
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

    public MySearchDefaultFragment() {
        // Required empty public constructor
    }

    private class ColorSelection {
        public String color;
        public String name;

        public ColorSelection(String s0, String s1) {
            color = s0;
            name = s1;
        }
    }

    ArrayList<ColorSelection> colors = new ArrayList<>();
    ArrayList<ColorSelection> colors2 = new ArrayList<>();

    LinearLayout placeholder;
    LinearLayout placeholder2;


    LinearLayout container1;
    LinearLayout container2;
    LinearLayout container3;
    ArrayList<String> ids1 = new ArrayList<>();
    ArrayList<String> ids2 = new ArrayList<>();
    ArrayList<String> ids3 = new ArrayList<>();

    DatabaseReference databaseProducts;
    ArrayList<Product> products= new ArrayList<>();
    LayoutInflater inflater;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //create LayoutInflator class
        inflater = (LayoutInflater) curr.getSystemService(Context.LAYOUT_INFLATER_SERVICE);




        container1 = (LinearLayout) thisView.findViewById(R.id.container1);
        ids1.add("0");
        ids1.add("2");
        ids1.add("4");
        container2 = (LinearLayout) thisView.findViewById(R.id.container2);
        ids2.add("1");
        ids2.add("2");
        ids2.add("3");
        container3 = (LinearLayout) thisView.findViewById(R.id.container3);
        ids3.add("3");
        ids3.add("4");
        ids3.add("5");


        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        Query query = databaseProducts;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    products.add(product);
                }


                for (int i = 0; i < ids1.size(); i++) {
                    String prodId = ids1.get(i);
// create dynamic LinearLayout and set Image on it.
                    if (prodId != null) {

                        boolean found = false;
                        int j = 0;
                        while (!found && j < products.size()) {
                            if (products.get(j).getId().equals(prodId)) {
                                found = true;
                            } else {
                                j++;
                            }
                        }
                        if (found) {
                            LinearLayout productCol = (LinearLayout) inflater.inflate(
                                    R.layout.product_column, null);
                            productCol.setTag(products.get(j));
                            productCol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Product prod = (Product) v.getTag();
                                    MySearchListFragment.ProductEntry pEn= new MySearchListFragment.ProductEntry(prod.getId(),prod.getProduct_name(), prod.getBrand(), prod.getImg_url(), prod.getPrice(), prod.getRating());
                                    MyProductActivity.pEntry = pEn;
                                    //MyMainActivity.thisActivity.toastMessage(pEn.id);
                                    mListener.openProductPage();
                                }
                            });


                            ImageView thumbnailImage = (ImageView) productCol
                                    .findViewById(R.id.thumbnail_image);
                            Picasso.with(curr).load(products.get(j).getImg_url()).into(thumbnailImage);

                            container1.addView(productCol);
                        }


                    }

                }

                for (int i = 0; i < ids2.size(); i++) {
                    String prodId = ids2.get(i);
// create dynamic LinearLayout and set Image on it.
                    if (prodId != null) {

                        boolean found = false;
                        int j = 0;
                        while (!found && j < products.size()) {
                            if (products.get(j).getId().equals(prodId)) {
                                found = true;
                            } else {
                                j++;
                            }
                        }
                        if (found) {
                            LinearLayout productCol = (LinearLayout) inflater.inflate(
                                    R.layout.product_column, null);
                            productCol.setTag(products.get(j));
                            productCol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Product prod = (Product) v.getTag();
                                    MySearchListFragment.ProductEntry pEn= new MySearchListFragment.ProductEntry(prod.getId(),prod.getProduct_name(), prod.getBrand(), prod.getImg_url(), prod.getPrice(), prod.getRating());
                                    MyProductActivity.pEntry = pEn;
                                    //MyMainActivity.thisActivity.toastMessage(pEn.id);
                                    mListener.openProductPage();
                                }
                            });


                            ImageView thumbnailImage = (ImageView) productCol
                                    .findViewById(R.id.thumbnail_image);
                            Picasso.with(curr).load(products.get(j).getImg_url()).into(thumbnailImage);

                            container2.addView(productCol);
                        }


                    }

                }

                for (int i = 0; i < ids3.size(); i++) {
                    String prodId = ids3.get(i);
// create dynamic LinearLayout and set Image on it.
                    if (prodId != null) {

                        boolean found = false;
                        int j = 0;
                        while (!found && j < products.size()) {
                            if (products.get(j).getId().equals(prodId)) {
                                found = true;
                            } else {
                                j++;
                            }
                        }
                        if (found) {
                            LinearLayout productCol = (LinearLayout) inflater.inflate(
                                    R.layout.product_column, null);
                            productCol.setTag(products.get(j));
                            productCol.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Product prod = (Product) v.getTag();
                                    MySearchListFragment.ProductEntry pEn= new MySearchListFragment.ProductEntry(prod.getId(),prod.getProduct_name(), prod.getBrand(), prod.getImg_url(), prod.getPrice(), prod.getRating());
                                    MyProductActivity.pEntry = pEn;
                                    //MyMainActivity.thisActivity.toastMessage(pEn.id);
                                    mListener.openProductPage();
                                }
                            });


                            ImageView thumbnailImage = (ImageView) productCol
                                    .findViewById(R.id.thumbnail_image);
                            Picasso.with(curr).load(products.get(j).getImg_url()).into(thumbnailImage);

                            container3.addView(productCol);
                        }


                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySearchDefaultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySearchDefaultFragment newInstance(String param1, String param2) {
        MySearchDefaultFragment fragment = new MySearchDefaultFragment();
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
        View v = inflater.inflate(R.layout.fragment_my_search_default, container, false);
        thisView = v;
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        void openProductPage();
    }
}
