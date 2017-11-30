package com.example.hp.heartful;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP INDIA on 19-Nov-17.
 */

public class FragmentThreeProfile extends Fragment implements View.OnClickListener {
    View view_pro;
    private CircleImageView profilePic;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private TextView userName;
    private String profileName;
    private DatabaseReference forUsers;
    private String profilePicLink;
    private ImageView edit;
    private ArrayList<String>key= new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view_pro = inflater.inflate(R.layout.profile_layout,container, false);
        profilePic=(CircleImageView) view_pro.findViewById(R.id.profile_pic);
        userName=(TextView)view_pro.findViewById(R.id.user_name);
        forUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        edit=(ImageView)view_pro.findViewById(R.id.edit);
        mAuth= FirebaseAuth.getInstance();
        edit.setOnClickListener(this);
        firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null) {
            forUsers.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("chal", "ondatachange");
                    Users user = dataSnapshot.getValue(Users.class);
                    profilePicLink = user.getProfilePicLink();
                    profileName = user.getUserName();
                    Glide
                            .with(getApplicationContext())
                            .load(profilePicLink)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profilePic);
                    userName.setText(profileName);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        forUsers.child(firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key.add(String.valueOf(dataSnapshot.getValue()));
                Log.v("fragment", String.valueOf(key));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view_pro;
    }

    @Override
    public void onClick(View view) {
        if(mAuth.getCurrentUser()!=null) {
            if (view == edit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    presentActivity(view);
                else {
                    Intent i = new Intent(getActivity(), Preferences.class);
                    i.setFlags(i.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }

            }
        }
    }

    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(getActivity(), Preferences.class);
        intent.putExtra(Preferences.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(Preferences.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}
