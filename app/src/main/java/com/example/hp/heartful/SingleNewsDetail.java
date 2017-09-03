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

public class SingleNewsDetail extends AppCompatActivity {
    private String post_key;
    private ImageView mNewsImage;
    private TextView mNewsTitle,mNewsDesc;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news_detail);
        mNewsImage=(ImageView)findViewById(R.id.news_images);
        mNewsDesc=(TextView)findViewById(R.id.news_description);
        mNewsTitle=(TextView)findViewById(R.id.news_title);
        post_key=getIntent().getExtras().getString("news_id");
       mDatabase= FirebaseDatabase.getInstance().getReference().child("News");
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title= (String) dataSnapshot.child("Title").getValue();
                String post_desc=(String)dataSnapshot.child("Description").getValue();
                String post_image=(String)dataSnapshot.child("Image").getValue();
                mNewsTitle.setText(post_title);
                mNewsDesc.setText(post_desc);
                Glide
                        .with(SingleNewsDetail.this)
                        .load(post_image)
                      .override(450,450)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mNewsImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
