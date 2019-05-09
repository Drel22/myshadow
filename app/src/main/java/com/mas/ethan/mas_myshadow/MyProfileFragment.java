package com.mas.ethan.mas_myshadow;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mas.ethan.mas_myshadow.models.Skin;
import com.mas.ethan.mas_myshadow.models.User;
import com.mas.ethan.mas_myshadow.models.UserAccountSettings;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentTabHost tabHost;
    private OnFragmentInteractionListener mListener;
    private Context curr;
    private View thisView;
    public static MyProfileFragment thisFragment;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = this;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        thisView = v;
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    public TextView username;
    public ImageView skinColor;
    public static UserAccountSettings currentSettings;
    public static String userID;
    MyProfileTabsFragment tabFragment;
    DatabaseReference databaseUserAccountSettings;
    DatabaseReference databaseUsers;
    DatabaseReference databaseSkins;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tabFragment = new MyProfileTabsFragment();

        TextView signOutText = (TextView) view.findViewById(R.id.signOutText);
        signOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(curr,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Signing Out...");
                progressDialog.show();

                //mProgressBar.setVisibility(View.VISIBLE);
                //tvSigningOut.setVisibility(View.VISIBLE);
                setupFirebaseAuth();
                mAuth.signOut();
                progressDialog.dismiss();

                MyMainActivity.thisActivity.finish();


            }
        });


        RelativeLayout editBox = (RelativeLayout) thisView.findViewById(R.id.editBox);
        editBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfilePopUp();
            }
        });
        ImageView editButton = (ImageView) thisView.findViewById(R.id.round1);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfilePopUp();
            }
        });
        TextView textEdit = (TextView) thisView.findViewById(R.id.editProfile);
        textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfilePopUp();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userF = mAuth.getCurrentUser();
        databaseUserAccountSettings = FirebaseDatabase.getInstance().getReference("user_account_settings");

        username = (TextView) view.findViewById(R.id.username);
        skinColor = (ImageView) view.findViewById(R.id.skinColor);

        userID = userF.getUid();
        username.setText("Loading...");
        Query query = databaseUserAccountSettings.orderByChild("user_id").equalTo(userF.getUid()).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //MyMainActivity.thisActivity.toastMessage("Got data");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserAccountSettings uas = postSnapshot.getValue(UserAccountSettings.class);
                    currentSettings = uas;

                    name = uas.getDisplay_name();
                    username.setText(name);



                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser userF = mAuth.getCurrentUser();
                    databaseUsers = FirebaseDatabase.getInstance().getReference("users");
                    Query query = databaseUsers.orderByChild("user_id").equalTo(userF.getUid()).limitToFirst(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                User user = postSnapshot.getValue(User.class);
                                String skinId = user.getSelected_skin();
                                //MyMainActivity.thisActivity.toastMessage("Got data2: " + skinId);

                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser userF = mAuth.getCurrentUser();
                                databaseSkins = FirebaseDatabase.getInstance().getReference("skins").child(userF.getUid());
                                Query query = databaseSkins.orderByChild("id").equalTo(skinId).limitToFirst(1);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Skin skin = postSnapshot.getValue(Skin.class);
                                            //MyMainActivity.thisActivity.toastMessage("Got data3: " + skin.getColor());
                                            skinColor.setColorFilter(Color.parseColor("#" + skin.getColor()));

                                        }
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (view.findViewById(R.id.profileFragmentFrame) != null) {

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.profileFragmentFrame, tabFragment).commit();

        }
    }


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private void setupFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                } else {
                    Intent intent = new Intent(getActivity(), MyLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                // ...
            }
        };
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

    public static String name;


    // Show dialog that allows user to set Bluetooth module name and choose which vital sign data to show
    public void showProfilePopUp() {
        // create a Dialog component
        final Dialog dialog = new Dialog(curr);

        //tell the Dialog to use the dialog.xml as it's layout description
        dialog.setContentView(R.layout.popup_edit_profile_dialogue);
        dialog.setTitle("Profile Settings");

        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        Button saveButton = (Button) dialog.findViewById(R.id.saveButton);
        final EditText nameEdit = (EditText) dialog.findViewById(R.id.name);

        nameEdit.setText(name);
        nameEdit.setSelection(nameEdit.getText().length());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: update database info and shown name

                name = nameEdit.getText().toString();

                if (currentSettings != null) {
                    String id = userID;
                    UserAccountSettings newUAS= new UserAccountSettings(currentSettings.getDescription(),
                            name, currentSettings.getFollowers(), currentSettings.getFollowing(), currentSettings.getPosts(),
                            currentSettings.getProfile_photo(), currentSettings.getUsername(), currentSettings.getUser_id());
                    databaseUserAccountSettings.child(id).setValue(newUAS);
                    username.setText(name);
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser userF = mAuth.getCurrentUser();
                    userID = userF.getUid();
                    Query query = databaseUserAccountSettings.orderByChild("user_id").equalTo(userF.getUid()).limitToFirst(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //MyMainActivity.thisActivity.toastMessage("Got data");
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                UserAccountSettings uas = postSnapshot.getValue(UserAccountSettings.class);
                                currentSettings = uas;

                                String id = userID;
                                UserAccountSettings newUAS= new UserAccountSettings(currentSettings.getDescription(),
                                        name, currentSettings.getFollowers(), currentSettings.getFollowing(), currentSettings.getPosts(),
                                        currentSettings.getProfile_photo(), currentSettings.getUsername(), currentSettings.getUser_id());
                                databaseUserAccountSettings.child(id).setValue(newUAS);
                                username.setText(name);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
