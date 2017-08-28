package com.example.hp.heartful;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    private TextView mNgoName,mNgoInfo;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_inside_layout);
        post_key=getIntent().getExtras().getString("news_id");
        mNgoLogo=(ImageView)findViewById(R.id.orgLogo);
        mNgoInfo=(TextView)findViewById(R.id.orgInfo);
        mNgoName=(TextView)findViewById(R.id.orgName);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("NgoList");
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title= (String) dataSnapshot.child("mOrgname").getValue();
                String post_desc=(String)dataSnapshot.child("mOrginfo").getValue();
                String post_image=(String)dataSnapshot.child("mImage").getValue();
                mNgoName.setText(post_title);
                mNgoInfo.setText(post_desc);
                Glide
                        .with(orgInsideActivity.this)
                        .load(post_image)
                        .override(450,450)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mNgoLogo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
