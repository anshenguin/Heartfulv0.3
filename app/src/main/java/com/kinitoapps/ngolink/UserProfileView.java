package com.kinitoapps.ngolink;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfileView extends AppCompatActivity {
   private String userName;
   private  String userPro,userDes;
   private ImageView avtaar;
   private String uid;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    private DatabaseReference forUsers;
    private TextView user_desc_text;
    CollapsingToolbarLayout collapsingToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        uid=bundle.getString("uid");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid    ).child("RecentActivities");
        mDatabase.keepSynced(true);

        setContentView(R.layout.activity_user_profile_view);
        recyclerView=(RecyclerView)findViewById(R.id.user_activities_view);
        FragmentThreeProfile.CustomGridLayoutManager linearLayoutManager = new FragmentThreeProfile.CustomGridLayoutManager(this);
        linearLayoutManager.setScrollEnabled(false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(linearLayoutManager);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        forUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        user_desc_text=(TextView)findViewById(R.id.user_desc);
        collapsingToolbar =

                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        avtaar=(ImageView)findViewById(R.id.avatar);
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        forUsers.child(uid).child("userInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                userPro = user.getProfilePicLink();
                userName = user.getUserName();
                userDes=user.getUserDesc();
                user_desc_text.setText(userDes);

                Glide
                        .with(getApplicationContext())
                        .load(userPro)

                        .dontAnimate()

                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(avtaar);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setSupportActionBar(toolbar);
        collapsingToolbar.setTitle(userName);
        FirebaseRecyclerAdapter<RecentActivity,FragmentThreeProfile.RecentActivityHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<RecentActivity, FragmentThreeProfile.RecentActivityHolder>
                (RecentActivity.class,
                        com.kinitoapps.ngolink.R.layout.profile_list_item,
                        FragmentThreeProfile.RecentActivityHolder.class,
                        mDatabase) {
            @Override
            protected void populateViewHolder(FragmentThreeProfile.RecentActivityHolder viewHolder, RecentActivity model, int position) {
                final String push_id = getRef(position).getKey();
                Log.v("msg pushid", String.valueOf(getRef(position)));
                String string= model.getmText();
                string = string.replaceFirst("You've",userName);
                viewHolder.setText(string);
                viewHolder.setImage(getApplicationContext(),model.getmImageLink());
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
                                    Intent intent = new Intent(UserProfileView.this, orgInsideActivity.class);

                                    intent.putExtra("news_id", String.valueOf(dataSnapshot.child("postkey").getValue()));
                                    startActivity(intent);
                                } else {

                                    Intent intent = new Intent(UserProfileView.this, SingleNewsDetail.class);
                                    intent.putExtra("news_id", String.valueOf(dataSnapshot.child("postkey").getValue()));
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
