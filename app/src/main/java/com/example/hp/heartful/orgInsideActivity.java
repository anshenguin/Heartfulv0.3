package com.example.hp.heartful;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by HP on 06-06-2017.
 */

public class orgInsideActivity extends AppCompatActivity{
    private String post_key;
    private ImageView mNgoLogo;
    private  String mContact,mDonate,mVolunteer;
    private TextView mNgoName,mNgoInfo;
    private  DatabaseReference mDatabase;
    private  DatabaseReference data;
    private DatabaseReference databaseReference;
    private String loadImage;
    private FirebaseAuth mAuth;
    private boolean doesFollowing;
    private FloatingActionButton following;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_inside_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        post_key=getIntent().getExtras().getString("news_id");
        following=(FloatingActionButton)findViewById(R.id.following);
        mNgoLogo=(ImageView)findViewById(R.id.backdrop);
        mNgoInfo=(TextView)findViewById(R.id.orgInfo);
        mNgoName=(TextView)findViewById(R.id.orgName);
//        initCollapsingToolbar();
        Log.v("follow", String.valueOf(doesFollowing));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("NgoList");
        data= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title= (String) dataSnapshot.child("mOrgname").getValue();
                String post_desc=(String)dataSnapshot.child("mOrginfo").getValue();
                String post_image=(String)dataSnapshot.child("mImage").getValue();
                String contact=(String)dataSnapshot.child("mContact").getValue();
                String donate=(String)dataSnapshot.child("mDonate").getValue();
                String volunteer=(String)dataSnapshot.child("mVolunteer").getValue();
                mContact=contact;
                mDonate=donate;
                mVolunteer=volunteer;
                loadImage=post_image;
                mNgoName.setText(post_title);
                mNgoInfo.setText(post_desc);
                Glide
                        .with(orgInsideActivity.this)
                        .load(post_image)
                        .override(450,450)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mNgoLogo);

                Glide.with(getApplicationContext()).load(loadImage)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mNgoLogo);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(mAuth.getCurrentUser()!= null) {
            databaseReference = data.child(mAuth.getCurrentUser().getUid());
            databaseReference.child("following").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("Datasnapshot", "works");
                    Log.v("Datasnapshot", String.valueOf(dataSnapshot.getChildrenCount()));
                    if (dataSnapshot.getChildrenCount() == 0) {
                        following.setImageResource(R.drawable.ic_person_add_black_24dp);
                        following.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        doesFollowing = true;
                    } else {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.v("Datasnapshot", snapshot.getKey());
                            Log.v("Datasnapshot", post_key);
                            if (snapshot.getKey().equals(post_key)) {
                                Log.v("Datasnapshot", " if statement works");
                                doesFollowing = false;
                                following.setImageResource(R.drawable.ic_check_black_24dp);
                                following.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.following)));
                                break;
                            } else {
                                Log.v("Datasnapshot", " else statement works");
                                following.setImageResource(R.drawable.ic_person_add_black_24dp);
                                following.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                                doesFollowing = true;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Chal rha ","hai");
                DatabaseReference follow;
                if (mAuth.getCurrentUser() != null) {
                    if (doesFollowing) {
                         follow = databaseReference.child("following");
                         follow.child(post_key).setValue(true);
                        following.setImageResource(R.drawable.ic_check_black_24dp);
                        following.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.following)));
                    Toast.makeText(orgInsideActivity.this, "You're following this NGO", Toast.LENGTH_SHORT).show();
                    doesFollowing = false;
                        Bundle bundle = new Bundle();
                        bundle.putString("edttext", post_key);
                        FragmentThree fragobj = new FragmentThree();
                        fragobj.setArguments(bundle);
//                following.setBackgroundColor(getResources().getColor(R.color.following));
                }
                else {
                        follow = databaseReference.child("following");
                        follow.child(post_key).removeValue();
                        following.setImageResource(R.drawable.ic_person_add_black_24dp);
                        following.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        Toast.makeText(orgInsideActivity.this, "You're Unfollowing this NGO", Toast.LENGTH_SHORT).show();
                        doesFollowing = true;
                    }
                }
                else {
                    Toast.makeText(orgInsideActivity.this, "Please SignIn to follow", Toast.LENGTH_SHORT).show();
                    doesFollowing = true;
                }

                }
        });
//        if (!doesFollowing)
//            following.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.following)));
        Log.v("Value ", String.valueOf(doesFollowing));
        findViewById(R.id.contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mContact)) {

                    Log.v("url", mContact);

                    finish();
                    openWebView(mContact);
                }
                else
                    Toast.makeText(orgInsideActivity.this,"Sorry no webPage is available for this ngo",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(orgInsideActivity.this,WebViewContents.class));
            }
        });
        findViewById(R.id.volunteer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mVolunteer)) {

                    Log.v("url", mVolunteer);

                    finish();
                    openWebView(mVolunteer);
                }
                else
                    Toast.makeText(orgInsideActivity.this,"Sorry no webPage is available for this ngo",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(orgInsideActivity.this,WebViewContents.class));
            }
        });
        findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mDonate)) {

                    Log.v("url", mDonate);

                    finish();
                    openWebView(mDonate);
                }
                else
                    Toast.makeText(orgInsideActivity.this,"Sorry no webPage is available for this ngo",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(orgInsideActivity.this,WebViewContents.class));
            }
        });
    }

   private void openWebView(String url) {
        Intent intent = new Intent(orgInsideActivity.this, WebViewContents.class);
        intent.putExtra("url", url);
        finish();
        startActivity(intent);
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

