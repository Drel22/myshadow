package com.mas.ethan.mas_myshadow;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileTabsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileTabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileTabsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentTabHost tabHost;
    private Context curr;
    private View thisView;

    private OnFragmentInteractionListener mListener;

    public MyProfileTabsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileTabsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileTabsFragment newInstance(String param1, String param2) {
        MyProfileTabsFragment fragment = new MyProfileTabsFragment();
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
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_my_profile_tabs);

        Bundle arg1 = new Bundle();
        arg1.putInt("param1", 0);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("Recent"),
                MyProfileHistoryFragment.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putInt("param1", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Uploads"),
                MyProfileUploadsFragment.class, arg2);

        Bundle arg3 = new Bundle();
        arg3.putInt("param1", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("Likes"),
                MyProfileLikesFragment.class, arg3);

        thisView = tabHost;

        return tabHost;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View v = tabHost.getTabWidget().getChildAt(i);
            //v.setBackgroundResource(R.drawable.tabs);
            //v.setBackgroundColor(0xFF96101f);


            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.black));
            v.setBackgroundResource(R.drawable.tab_selector_black);
        }
    }

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
        // TODO: Update argument type and name
    }
}
