package com.example.hp.heartful;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleNewsDetail extends AppCompatActivity {
    private String post_key;
    private ImageView mNewsImage;
    private TextView mNewsTitle,mNewsDesc;
    private String loadImage;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNewsImage=(ImageView)findViewById(R.id.news_images);
        mNewsDesc=(TextView)findViewById(R.id.news_description);
        mNewsTitle=(TextView)findViewById(R.id.news_title);
        post_key=getIntent().getExtras().getString("news_id");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("News");
//        initCollapsingToolbar();
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title= (String) dataSnapshot.child("Title").getValue();
                String post_desc=(String)dataSnapshot.child("Description").getValue();
                String post_image=(String)dataSnapshot.child("Image").getValue();
                loadImage=post_image;
                mNewsTitle.setText(post_title);
                mNewsDesc.setText(post_desc);
//                supportPostponeEnterTransition();

                Glide
                        .with(SingleNewsDetail.this)
                        .load(post_image)

                        .override(1000,1000)

                        .dontAnimate()

                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                supportStartPostponedEnterTransition();
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                supportStartPostponedEnterTransition();
//                                return false;
//                            }
//                        })
                        .into(mNewsImage)

                ;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
