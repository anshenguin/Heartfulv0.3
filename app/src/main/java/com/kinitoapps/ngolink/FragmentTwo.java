package com.kinitoapps.ngolink;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HP INDIA on 08-Apr-17.
 */

public class FragmentTwo extends Fragment {
    public FragmentTwo(){}
  android.support.design.widget.FloatingActionButton button;
    ImageView imageView;
    boolean canPost;
    View view;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference forUsers;
    private RecyclerView mNewsLists;
    private DatabaseReference mDatabase;
//    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        view =inflater.inflate(com.kinitoapps.ngolink.R.layout.tab_two, container, false);
        super.onCreate(savedInstanceState);
        imageView = (ImageView) view.findViewById(com.kinitoapps.ngolink.R.id.news_images);
        button=(android.support.design.widget.FloatingActionButton) view.findViewById(com.kinitoapps.ngolink.R.id.button);
        button.bringToFront();
        firebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    Log.v("can u post2", String.valueOf(canPost));
                     forUsers= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("userInfo");
                    forUsers.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users user = dataSnapshot.getValue(Users.class);
                            canPost = user.isCanPost();
                            Log.v("can u post", String.valueOf(canPost));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canPost){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        presentActivity(v);
                        else
                startActivity(new Intent(getActivity(),NewsPost.class));

            }
            else {
                    Log.v("canPost", String.valueOf(canPost));
                    Toast.makeText(getActivity(),"If NGO ,login to post news", Toast.LENGTH_LONG).show();
                }
            }

        });

        mDatabase= FirebaseDatabase.getInstance().getReference().child("News");
        mDatabase.keepSynced(true);
        mNewsLists=(RecyclerView)view.findViewById(com.kinitoapps.ngolink.R.id.new_lists);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mNewsLists.setItemViewCacheSize(20);
        mNewsLists.setDrawingCacheEnabled(true);
        mNewsLists.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        mNewsLists.setLayoutManager(linearLayoutManager);
        if(firebaseAuth.getCurrentUser()==null){
            FirebaseRecyclerAdapter<News,NewsViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<News, NewsViewHolder>(News.class,
                    com.kinitoapps.ngolink.R.layout.news_list_sample,
                    NewsViewHolder.class,
                    mDatabase){

                @Override
                protected void populateViewHolder(final NewsViewHolder viewHolder, final News model, final int position) {
                    final String post_key = getRef(position).getKey();

                        viewHolder.setDateAndTime(model.getDateAndTime());

                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent=new Intent(getActivity(),SingleNewsDetail.class);
                            intent.putExtra("news_id",post_key);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Transition transition_two = TransitionInflater.from(getActivity()).inflateTransition(com.kinitoapps.ngolink.R.transition.transition_two);
                                getActivity().getWindow().setSharedElementEnterTransition(transition_two);
                                ImageView transitionView = (ImageView) v.findViewById(com.kinitoapps.ngolink.R.id.news_images);
                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(getActivity(), transitionView, getString(com.kinitoapps.ngolink.R.string.fragmenttwo_image_trans));
                                startActivity(intent, options.toBundle());
                            }
                            else
                                startActivity(intent);
                        }
                    });
                }



            };
            mNewsLists.setAdapter(firebaseRecyclerAdapter);
        }

        else{
            class NGOIdClass{

                private String Title,Image,DateAndTime,post_key;

                public NGOIdClass(String title,String image, String dateandtime,String postkey){
                    Title = title;
                    Image = image;
                    DateAndTime = dateandtime;
                    post_key = postkey;
                }

                public String getDateAndTime() {
                    return DateAndTime;
                }

                public String getTitle() {
                    return Title;
                }
                public String getImage() {
                    return Image;
                }
                public String getPostKey(){return post_key;}

            }

            class NGOIdClassAdapter extends RecyclerView.Adapter<NGOIdClassAdapter.ViewHolder>{


                class ViewHolder extends RecyclerView.ViewHolder{
                    TextView Title;
                    String post_key;
                    TextView DateAndTime;
                    View mView;
                    ImageView Image;

                    public ViewHolder(View itemView) {
                        super(itemView);
                        mView = itemView;
                        Title = (TextView)itemView.findViewById(com.kinitoapps.ngolink.R.id.news_title);
                        DateAndTime = (TextView)itemView.findViewById(com.kinitoapps.ngolink.R.id.date_time);
                        Image = (ImageView)itemView.findViewById(com.kinitoapps.ngolink.R.id.news_images);
                    }
                }
                private List<NGOIdClass> mNGOIDs;
                private Context mContext;
                public NGOIdClassAdapter(Context context,List<NGOIdClass> ngoIdClassList){
                    mNGOIDs = ngoIdClassList;
                    mContext = context;
                }
                private Context getContext() {
                    return mContext;
                }
                @Override
                public NGOIdClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    Context context = parent.getContext();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View singleItem = inflater.inflate( com.kinitoapps.ngolink.R.layout.news_list_sample,parent,false) ;
                    ViewHolder viewHolder = new ViewHolder(singleItem);
                    return viewHolder;
                }

                @Override
                public void onBindViewHolder(NGOIdClassAdapter.ViewHolder holder, int position) {
                    NGOIdClass singleNewsItem = mNGOIDs.get(position);
                    TextView title = holder.Title;
                    title.setText(singleNewsItem.getTitle());
                    TextView datentime = holder.DateAndTime;
                    datentime.setText(singleNewsItem.getDateAndTime());
                    ImageView img = holder.Image;
                    Glide
                            .with(getActivity().getApplicationContext())
                            .load(singleNewsItem.getImage())
                            .placeholder(com.kinitoapps.ngolink.R.drawable.placeholder)
                            .override(1000,1000)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);
                    final String post_key = singleNewsItem.getPostKey();

                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent=new Intent(getActivity(),SingleNewsDetail.class);
                            intent.putExtra("news_id",post_key);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Transition transition_two = TransitionInflater.from(getActivity()).inflateTransition(com.kinitoapps.ngolink.R.transition.transition_two);
                                getActivity().getWindow().setSharedElementEnterTransition(transition_two);
                                ImageView transitionView = (ImageView) v.findViewById(com.kinitoapps.ngolink.R.id.news_images);
                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(getActivity(), transitionView, getString(com.kinitoapps.ngolink.R.string.fragmenttwo_image_trans));
                                startActivity(intent, options.toBundle());
                            }
                            else
                                startActivity(intent);
                        }
                    });

                }



                @Override
                public int getItemCount() {
                    return mNGOIDs.size();
                }
            }


            final ArrayList<NGOIdClass> arrayEntries = new ArrayList<NGOIdClass>();
            final ArrayList<String> NGOIdsInFollowing = new ArrayList<String>();
            DatabaseReference forFollowing= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(firebaseAuth.getCurrentUser().getUid()).child("Following");
            forFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot p : dataSnapshot.getChildren()){
                        NGOIdsInFollowing.add(p.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                            Log.v("DataSnapshotValue", String.valueOf(dataSnapshot));
                    for (DataSnapshot p : dataSnapshot.getChildren()) {
                        //TODO: CAN REMOVE NGOID EXIST CHECK ONCE ALL NGOS HAVE AN ID I.E. ON DATA RESET
                        if (p.child("NGOId").exists())
                        {
                            for(String NGOIdInFollowing:NGOIdsInFollowing){
                                if(p.child("NGOId").getValue().toString().equals(NGOIdInFollowing))
                                {
                                    arrayEntries.add(new NGOIdClass(p.child("Title").getValue().toString(),p.child("Image").getValue().toString(),p.child("DateAndTime").getValue().toString(),p.getKey()));
                                }
                            }
                        }
                    }

                    NGOIdClassAdapter firebaseRecyclerAdapter = new NGOIdClassAdapter(getActivity(),arrayEntries);
                    mNewsLists.setAdapter(firebaseRecyclerAdapter);

                }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });


        }

        return view;
          }
    @Override
    public void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public NewsViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }


        public void setTitle(String title) {
            TextView post_title=(TextView)mView.findViewById(com.kinitoapps.ngolink.R.id.news_title);
            post_title.setText(title);
        }


        public void setDateAndTime(String desc) {
            TextView post_time=(TextView)mView.findViewById(com.kinitoapps.ngolink.R.id.date_time);
            post_time.setText(desc);
        }
        public void setImage(Context applicationContext, String image) {
            final ImageView post_image=(ImageView)mView.findViewById(com.kinitoapps.ngolink.R.id.news_images);
            Glide
                    .with(applicationContext)
                    .load(image)
                    .override(1000,1000)
                    .dontAnimate()
                    .placeholder(R.drawable.for_single)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(post_image);

        }
    }

    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(getActivity(), NewsPost.class);
        intent.putExtra(NewsPost.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(NewsPost.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }


}



