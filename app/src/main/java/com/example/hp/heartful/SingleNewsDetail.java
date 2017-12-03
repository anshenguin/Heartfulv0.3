package com.example.hp.heartful;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

 public class SingleNewsDetail extends AppCompatActivity {
    private String post_key;
    private ImageView mNewsImage;
//    public static final int DEFAULT_MSG_LENGTH_LIMIT = 100;
    private TextView mNewsTitle,mNewsDesc;
    private    FirebaseAuth mAuth;
     private String profilePicLink;
     private String profileName;
     private   String post_title;
     private   String post_image;

     private DatabaseReference comments;
    private ProgressDialog progressDialog;
    private EditText commentedText;
    private String commentTaken;
    private String loadImage;
    private RecyclerView recyclerView;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("commenting");
        recyclerView=(RecyclerView)findViewById(R.id.comment_section);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNewsImage=(ImageView)findViewById(R.id.news_images);
        commentedText=(EditText)findViewById(R.id.commented_text);
        mNewsDesc=(TextView)findViewById(R.id.news_description);
        mNewsTitle=(TextView)findViewById(R.id.news_title);
        post_key=getIntent().getExtras().getString("news_id");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        //mNewsLists.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(linearLayoutManager);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("News");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("News").child(post_key);
        final DatabaseReference forUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        if(mAuth.getCurrentUser()!=null) {

            forUsers.child(mAuth.getCurrentUser().getUid()).child("userInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.v("chal","ondatachange");
                    Users user = dataSnapshot.getValue(Users.class);
                    profilePicLink = user.getProfilePicLink();
                    profileName = user.getUserName();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

//        initCollapsingToolbar();
//        commentedText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.toString().trim().length() > 0) {
//                    sendCommentsBtn.setEnabled(true);
//
//                } else {
//                    sendCommentsBtn.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        commentedText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
     findViewById(R.id.send_comments).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             commentTaken = commentedText.getText().toString();
             if (TextUtils.isEmpty(commentTaken)) {
                 // email is empty
                 Toast.makeText(SingleNewsDetail.this, "please type something", Toast.LENGTH_SHORT).show();
                 return;// to stop the function from executation.
             }

           else
               {
                   Log.v("Comments", commentTaken);
             commentedText.setText(" ");
             mAuth = FirebaseAuth.getInstance();
                   DatabaseReference forUserActivity=forUsers.child(mAuth.getCurrentUser().getUid()).child("RecentActivities").push();
                   forUserActivity.child("mText").setValue("You've  commented On "+ post_title);
                   forUserActivity.child("mImageLink").setValue(post_image);

             if (mAuth.getCurrentUser() != null) {
                 DatabaseReference whileComment = databaseReference.child("comments").push();
                 whileComment.child("comments").setValue(commentTaken);
                 whileComment.child("userName").setValue(profileName);
                 whileComment.child("profilePicLink").setValue(profilePicLink);
//                    DatabaseReference fromPath=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
//                    DatabaseReference toPath=FirebaseDatabase.getInstance().getReference().child("comments").push().child("userName");
//                    copyRecord(fromPath,toPath);
//                    DatabaseReference fromPath=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
//                    DatabaseReference toPath=FirebaseDatabase.getInstance().getReference().child("Comments").child(mAuth.getCurrentUser().getUid());
//                    copyRecord(fromPath,toPath);
//                    progressDialog.dismiss();
//                    progressDialog.show();
//                    comments=FirebaseDatabase.getInstance().getReference().child("Comments").child(mAuth.getCurrentUser().getUid());
//                    String personName = mAuth.getCurrentUser().getDisplayName();
//                    Uri personPhoto = mAuth.getCurrentUser().getPhotoUrl();
//                    postComments postComments = new postComments(personName, personPhoto.toString(), commentTaken);
//                    comments.setValue(postComments);
             } else
                 Toast.makeText(SingleNewsDetail.this, "Please logged in for Comments", Toast.LENGTH_SHORT).show();
         }
     }
        });

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 post_title= (String) dataSnapshot.child("Title").getValue();
                String post_desc=(String)dataSnapshot.child("Description").getValue();
                 post_image=(String)dataSnapshot.child("Image").getValue();
                loadImage=post_image;
                mNewsTitle.setText(post_title);
                mNewsDesc.setText(post_desc);
//                supportPostponeEnterTransition();

                Glide
                        .with(getApplicationContext())
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

        FirebaseRecyclerAdapter<postComments,postCommentsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<postComments, postCommentsViewHolder>
                (postComments.class,
                R.layout.comment_layout,
                postCommentsViewHolder.class,
                databaseReference.child("comments"))
        {

            @Override
            protected void populateViewHolder(postCommentsViewHolder viewHolder, postComments model, int position) {
                viewHolder.setComments(model.getComments());
                viewHolder.setCommentatorName(model.getUserName());
                viewHolder.setmImage(getApplicationContext(),model.getProfilePicLink());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class postCommentsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public postCommentsViewHolder(View itemView) {


            super(itemView);
            mView = itemView;
        }

        public void setComments(String comments) {
            TextView post_title=(TextView)mView.findViewById(R.id.comment);
            post_title.setText(comments);
        }

        public void setCommentatorName(String userName) {
            TextView post_title=(TextView)mView.findViewById(R.id.commenter_name);
            post_title.setText(userName);
        }

        public void setmImage(Context applicationContext, String profilePicLink) {
            final ImageView post_image=(ImageView)mView.findViewById(R.id.commenter_pic);
            Glide
                    .with(applicationContext)
                    .load(profilePicLink)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(post_image);
        }
    }
//     public void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
//        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                postComments postComments= dataSnapshot.getValue(postComments.class);
//                toPath.setValue(postComments.getUserName(), new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//    }
//    public void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
//        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                toPath.child("comments").setValue(commentTaken, new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                    }
//                });
//                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//    }


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
