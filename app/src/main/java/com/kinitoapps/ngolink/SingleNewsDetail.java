package com.kinitoapps.ngolink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
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
    private  String commentPostKey;
    private ImageView mNewsImage;
    private TextView mNewsTitle,mNewsDesc;
    private    FirebaseAuth mAuth;
    private String profilePicLink;
     private String profileName;
     private   String post_title;
     private   String post_image;
     private DatabaseReference comments;
    private EditText commentedText;
    private String commentTaken;
    private String loadImage;
     int noOfComments;
     private String uid;
     String userName , userPro;
    private RecyclerView recyclerView;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kinitoapps.ngolink.R.layout.activity_single_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(com.kinitoapps.ngolink.R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
        recyclerView=(RecyclerView)findViewById(com.kinitoapps.ngolink.R.id.comment_section);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNewsImage=(ImageView)findViewById(com.kinitoapps.ngolink.R.id.news_images);
        commentedText=(EditText)findViewById(com.kinitoapps.ngolink.R.id.commented_text);
        mNewsDesc=(TextView)findViewById(com.kinitoapps.ngolink.R.id.news_description);
        mNewsTitle=(TextView)findViewById(com.kinitoapps.ngolink.R.id.news_title);
        post_key=getIntent().getExtras().getString("news_id");
        FragmentThreeProfile.CustomGridLayoutManager linearLayoutManager = new FragmentThreeProfile.CustomGridLayoutManager(this);
        recyclerView.setItemViewCacheSize(20);
        final TextView numberOfCmts = (TextView)findViewById( com.kinitoapps.ngolink.R.id.number_of_comments);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(linearLayoutManager);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("News");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("News").child(post_key);
        final DatabaseReference forUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        if(mAuth.getCurrentUser()!=null) {
            forUsers.child(mAuth.getCurrentUser().getUid()).child("userInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
                    profilePicLink = user.getProfilePicLink();
                    profileName = user.getUserName();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

     findViewById(com.kinitoapps.ngolink.R.id.send_comments).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             commentTaken = commentedText.getText().toString();
             if (TextUtils.isEmpty(commentTaken)) {
                 // email is empty
                 Toast.makeText(SingleNewsDetail.this, "please type something", Toast.LENGTH_SHORT).show();
                 return;// to stop the function from execution.
             }

           else
               {
                   Log.v("Comments", commentTaken);
             commentedText.setText(" ");
             mAuth = FirebaseAuth.getInstance();

             if (mAuth.getCurrentUser() != null) {

                 DatabaseReference forUserActivity=forUsers.child(mAuth.getCurrentUser().getUid()).child("RecentActivities").push();
                 forUsers.child(mAuth.getCurrentUser().getUid()).child("RecentActivities").addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if(dataSnapshot.getChildrenCount()==5){
                             for (DataSnapshot p : dataSnapshot.getChildren()) {
                                 p.getRef().removeValue();
                                 return;
                             }

                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
                 forUserActivity.child("mText").setValue("You've  commented On "+ post_title);
                 forUserActivity.child("mImageLink").setValue(post_image);
                 forUserActivity.child("postkey").setValue(post_key);

                 DatabaseReference whileComment = databaseReference.child("comments").push();

                 whileComment.child("comments").setValue(commentTaken);
                 whileComment.child("userName").setValue(profileName);
                 whileComment.child("profilePicLink").setValue(profilePicLink);
                 whileComment.child("uid").setValue(mAuth.getCurrentUser().getUid());

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

                Glide
                        .with(getApplicationContext())
                        .load(post_image)
                        .placeholder(com.kinitoapps.ngolink.R.drawable.for_single)

                        .override(1000,1000)

                        .dontAnimate()

                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .into(mNewsImage)

                ;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
  mDatabase.child(post_key).child("comments").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
          noOfComments= (int) dataSnapshot.getChildrenCount();
          Log.v("numberOfComments", String.valueOf(noOfComments));
          numberOfCmts.setText(String.valueOf(noOfComments));
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
  });
        FirebaseRecyclerAdapter<postComments,postCommentsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<postComments, postCommentsViewHolder>
                (postComments.class,
                com.kinitoapps.ngolink.R.layout.comment_layout,
                postCommentsViewHolder.class,
                databaseReference.child("comments"))
        {

            @Override
            protected void populateViewHolder(postCommentsViewHolder viewHolder,final postComments model, int position) {
                 final String comment_post_key=getRef(position).getKey();

                viewHolder.setComments(model.getComments());
                viewHolder.setCommentatorName(model.getUserName());
                viewHolder.setmImage(getApplicationContext(),model.getProfilePicLink());
                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        commentPostKey= comment_post_key;
                        uid = model.getUid();
                        userName = model.getUserName();
                        userPro = model.getProfilePicLink();
//                        Toast.makeText(SingleNewsDetail.this,"Long Press working", Toast.LENGTH_LONG).show();
                        registerForContextMenu(recyclerView);
                        return false;
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Report");
        menu.add(0, v.getId(), 0, "View Profile");    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

         if(item.getTitle()=="Delete"){
             mAuth=FirebaseAuth.getInstance();
          if (mAuth.getCurrentUser()!= null){
              if (uid.equals(mAuth.getCurrentUser().getUid()))
                  mDatabase.child(post_key).child("comments").child(commentPostKey).removeValue();
              else
                  Toast.makeText(SingleNewsDetail.this, "You're not allow to delete other's comments", Toast.LENGTH_SHORT).show();
          }
          else {
              Toast.makeText(SingleNewsDetail.this, "You're not Logged In", Toast.LENGTH_SHORT).show();
          }

         }
        else if(item.getTitle()=="Report"){

         }
        else if(item.getTitle()=="View Profile"){
             Log.v("Name",userName);

             Intent i =  new Intent(SingleNewsDetail.this,UserProfileView.class);
             Bundle bundle = new Bundle();
             bundle.putString("userName",userName);
             bundle.putString("userPro",userPro);
             i.putExtras(bundle);
             startActivity(i);

         }
        else{
            return false;
        }
        return super.onContextItemSelected(item);
    }



    public static class postCommentsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public postCommentsViewHolder(View itemView) {


            super(itemView);
            mView = itemView;
        }

        public void setComments(String comments) {
            TextView post_title=(TextView)mView.findViewById(com.kinitoapps.ngolink.R.id.comment);
            post_title.setText(comments);
        }

        public void setCommentatorName(String userName) {
            TextView post_title=(TextView)mView.findViewById(com.kinitoapps.ngolink.R.id.commenter_name);
            post_title.setText(userName);
        }

        public void setmImage(Context applicationContext, String profilePicLink) {
            final ImageView post_image=(ImageView)mView.findViewById(com.kinitoapps.ngolink.R.id.commenter_pic);
            Glide
                    .with(applicationContext)
                    .load(profilePicLink)
                    .placeholder(com.kinitoapps.ngolink.R.drawable.for_single)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(post_image);
        }
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
