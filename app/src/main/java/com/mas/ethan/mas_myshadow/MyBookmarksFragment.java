package com.mas.ethan.mas_myshadow;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBookmarksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBookmarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBookmarksFragment extends Fragment {

    private FragmentTabHost tabHost;
    private MyBookmarksFragment.OnFragmentInteractionListener mListener;
    private Context curr;
    private View thisView;

    public MyBookmarksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyBookmarksFragment.
     */
    public static MyBookmarksFragment newInstance() {
        MyBookmarksFragment fragment = new MyBookmarksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_my_bookmarks);

        Bundle arg1 = new Bundle();
        arg1.putInt("param1", 0);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("product"),
                MyBookmarksProductsFragment.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putInt("param1", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("skin tone"),
                MyBookmarksSkinTonesFragment.class, arg2);

        thisView = tabHost;

        return tabHost;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        curr = context;
        if (context instanceof MyBookmarksFragment.OnFragmentInteractionListener) {
            mListener = (MyBookmarksFragment.OnFragmentInteractionListener) context;
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
}