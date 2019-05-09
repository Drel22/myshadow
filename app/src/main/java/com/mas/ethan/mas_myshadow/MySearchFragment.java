package com.mas.ethan.mas_myshadow;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MySearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MySearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public EditText userInput;
    public ImageView searchButtonImage;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context curr;
    private View thisView;
    private int showingFragment = 0;

    // Fragments?
    MySearchDefaultFragment defaultFragment;
    MySearchNotFoundFragment notFoundFragment;
    MySearchListFragment listFragment;
    MySearchReadyFragment readyFragment;

    private OnFragmentInteractionListener mListener;

    public MySearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySearchFragment newInstance(String param1, String param2) {
        MySearchFragment fragment = new MySearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer = null;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchButtonImage = (ImageView) thisView.findViewById(R.id.searchButtonImage);
        searchButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });
        userInput = (EditText) thisView.findViewById(R.id.searchEditText);
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                //Log.e("TextWatcherTest", "afterTextChanged:\t" +s.toString());
                changeToReadyFragment();

                textChanged();
            }
        });

        userInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToReadyFragment();
            }
        });





        defaultFragment = new MySearchDefaultFragment();
        notFoundFragment = new MySearchNotFoundFragment();
        readyFragment = new MySearchReadyFragment();
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (thisView.findViewById(R.id.fragment_container2) != null) {

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container2, defaultFragment).commit();

            showingFragment = 0;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_search, container, false);
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
    }


    public boolean textEmpty() {
        if (userInput.getText().toString().equals("") && showingFragment == 0) {
            return true;
        } else {
            userInput.setText("");
            changeToDefaultFragment();
            return false;
        }
    }

    public String prevText = "";

    CountDownTimer timer;
    CountDownTimer tempTimer;

    public void textChanged() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onFinish() {
                tempTimer = timer;
                timer = null;

                if (userInput.getText().toString().equals("")) {
                    changeToDefaultFragment();
                } else {
                    changeToListFragment();
                }

                tempTimer = null;
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();


    }

    public void searchButtonClicked() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        String input = userInput.getText().toString();

        changeToListFragment();
//        if (input.equals("product")) {
//            changeToListFragment();
//        } else {
//            changeToNotFoundFragment();
//        }
    }

    public void changeToDefaultFragment() {
        if (showingFragment != 0) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container2, defaultFragment);
            transaction.addToBackStack(null);
            // TODO: add transition?
            //transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            transaction.commit();
            showingFragment = 0;
        }
    }
    public void changeToReadyFragment() {
        if (showingFragment != 1) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container2, readyFragment);
            transaction.addToBackStack(null);
            // TODO: add transition?
            //transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            transaction.commit();
            showingFragment = 3;
        }
    }
    public void changeToNotFoundFragment() {
        if (showingFragment != 1) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container2, notFoundFragment);
            transaction.addToBackStack(null);
            // TODO: add transition?
            //transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            transaction.commit();
            showingFragment = 1;
        }
    }
    // TODO: search databse for listing before showing list
    public void changeToListFragment() {
        MySearchListFragment.hostFrag = this;
        // TODO: must instantiate list fragment before showing
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        String input = userInput.getText().toString();
        args.putString("search", input);
        listFragment = new MySearchListFragment();
        listFragment.setArguments(args);
        transaction.replace(R.id.fragment_container2, listFragment);
        transaction.addToBackStack(null);
        // TODO: add transition?
        //transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.commit();
        showingFragment = 2;
    }
}
