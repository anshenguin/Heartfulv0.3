package com.example.hp.heartful;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP INDIA on 19-Nov-17.
 */

public class FragmentThreeProfile extends Fragment implements View.OnClickListener {

    public static class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }
    View view_pro;
    boolean isNgo;
    private CircleImageView profilePic;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private TextView userName;
    private TextView desc;
    private String profileName;
    private DatabaseReference forUsers;
    private String profilePicLink;
    private ImageView edit;
    private String userDes;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view_pro = inflater.inflate(R.layout.profile_layout,container, false);
        profilePic=(CircleImageView) view_pro.findViewById(R.id.profile_pic);
        userName=(TextView)view_pro.findViewById(R.id.user_name);
        desc=(TextView)view_pro.findViewById(R.id.textView);
        forUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        edit=(ImageView)view_pro.findViewById(R.id.edit);
        mAuth= FirebaseAuth.getInstance();
        edit.setOnClickListener(this);
        firebaseUser=mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("RecentActivities");
        mDatabase.keepSynced(true);
        recyclerView=(RecyclerView)view_pro.findViewById(R.id.user_activity);
        CustomGridLayoutManager linearLayoutManager = new CustomGridLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setReverseLayout(true
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(linearLayoutManager);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        Log.v("FragmentThree", String.valueOf(mDatabase));
        if(firebaseUser!=null) {
            forUsers.child(firebaseUser.getUid()).child("userInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("chal", "ondatachange");
                    Users user = dataSnapshot.getValue(Users.class);
                    profilePicLink = user.getProfilePicLink();
                    profileName = user.getUserName();
                    userDes=user.getUserDesc();
                    Glide
                            .with(getApplicationContext())
                            .load(profilePicLink)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profilePic);
                    userName.setText(profileName);
                    desc.setText(userDes);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        FirebaseRecyclerAdapter<RecentActivity,RecentActivityHolder>firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<RecentActivity, RecentActivityHolder>
                (RecentActivity.class,
                        R.layout.profile_list_item,
                        RecentActivityHolder.class,
                        mDatabase) {
            @Override
            protected void populateViewHolder(RecentActivityHolder viewHolder, RecentActivity model, int position) {
                final String push_id = getRef(position).getKey();
                Log.v("msg pushid", String.valueOf(getRef(position)));
//                String post_key = mDatabase.child(push_id).g
//                Log.v("msg postkey", String.valueOf(mDatabase.child(push_id).getKey()));
                viewHolder.setText(model.getmText());
                viewHolder.setImage(getActivity().getApplicationContext(),model.getmImageLink());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Log.v("KyA button click pe","YES");
                         Query query =  mDatabase.child(push_id);
                         query.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 Log.v("actual", String.valueOf(dataSnapshot));
                                     if (dataSnapshot.child("isNgo").exists()) {
                                         Intent intent = new Intent(getActivity(), orgInsideActivity.class);

                                         intent.putExtra("news_id", String.valueOf(dataSnapshot.child("postkey").getValue()));
                                         startActivity(intent);
                                     } else {

                                         Intent intent = new Intent(getActivity(), SingleNewsDetail.class);
                                         intent.putExtra("news_id", String.valueOf(dataSnapshot.child("postkey").getValue()));
                                         startActivity(intent);
                                     }
                                 }
//                                 Log.v("msg", String.valueOf(dataSnapshot.getValue()));
//                                 if (dataSnapshot.exists()){
//                                     Intent intent = new Intent(getActivity(), orgInsideActivity.class);
//                                     intent.putExtra("news_id", post_key);
//                                     startActivity(intent);
//                                 }
//                                 else {
//
//                                     Intent intent = new Intent(getActivity(), SingleNewsDetail.class);
//                                     intent.putExtra("news_id", post_key);
//                                     startActivity(intent);
//                                 }


                             @Override
                             public void onCancelled(DatabaseError databaseError) {

                             }
                         });

                     }
                 });

            }
        };
       recyclerView.setAdapter(firebaseRecyclerAdapter);
        return view_pro;
    }

    public static class RecentActivityHolder extends RecyclerView.ViewHolder {
        View mView;
        public RecentActivityHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setText(String text) {
            TextView post_title=(TextView)mView.findViewById(R.id.actionPerformedText);
            post_title.setText(text);
            Log.v("Fragment", String.valueOf(text));

        }

        public void setImage(Context applicationContext, String imageLink) {
            final ImageView post_image=(ImageView) mView.findViewById(R.id.actionPerformedImage);
            Glide
                    .with(applicationContext)
                    .load(imageLink)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(post_image);
        }
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
