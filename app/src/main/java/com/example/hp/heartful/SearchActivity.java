package com.example.hp.heartful;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Locale;

/**
 * Created by HP INDIA on 01-Nov-17.
 */

public class SearchActivity extends AppCompatActivity {
    private DatabaseReference searchbase;
    private EditText searchview;
    private RecyclerView recyclerView;
    private ImageView cleartext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        searchview = (EditText) findViewById(R.id.search_text);
        cleartext = (ImageView) findViewById (R.id.clear_text);
        cleartext.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search...");
        searchbase= FirebaseDatabase.getInstance().getReference();
        recyclerView=(RecyclerView)findViewById(R.id.ngo_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        //mNewsLists.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(linearLayoutManager);

        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String newText = searchview.getText().toString().toLowerCase(Locale.getDefault());
                Query Q = searchbase.child("NgoList").orderByChild("searchName").startAt(newText.toLowerCase()).endAt(newText.toLowerCase()+"\uf8ff");
                Log.v("SearchText",newText);
                if(newText.length()>0)
                    cleartext.setVisibility(View.VISIBLE);
                else
                    cleartext.setVisibility(View.GONE);
                cleartext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchview.setText("");
                    }
                });
                Log.v("search", String.valueOf(Q));

                FirebaseRecyclerAdapter<OrgInfo, OrgInfoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrgInfo , OrgInfoViewHolder>(
                        OrgInfo.class, R.layout.home_list_item, OrgInfoViewHolder.class, Q) {
                    @Override
                    protected void populateViewHolder(final OrgInfoViewHolder viewHolder, final OrgInfo model, int position) {
                        final String post_key=getRef(position).getKey();
                        viewHolder.setmOrginfo(model.getmOrginfo());
                        viewHolder.setmOrgname(model.getmOrgname());
                        viewHolder.setmCategory(model.getmCategory());
                        viewHolder.setmImage(SearchActivity.this.getApplicationContext(),model.getmImage());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v1) {
                                Intent intent=new Intent(SearchActivity.this,orgInsideActivity.class);
                                intent.putExtra("news_id",post_key);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    Transition transition_one = TransitionInflater.from(SearchActivity.this).inflateTransition(R.transition.transition_two);
//                                    SearchActivity.this.getWindow().setSharedElementEnterTransition(transition_one);
//                                    ImageView transitionViewOne = (ImageView) v1.findViewById(R.id.org_logo);
//                                    Bundle b = ActivityOptionsCompat
//                                            .makeSceneTransitionAnimation(SearchActivity.this, transitionViewOne, "orgimg").toBundle();
//                                    startActivity(intent, b);
//                                }
//                                else {
                                    Log.v("starting","");

                                    startActivity(intent);
//                                }
                            }
                        });

                    }
                };
                recyclerView.setAdapter(firebaseRecyclerAdapter);
                if(searchview.length()==0)
                    recyclerView.setVisibility(View.GONE);
                else
                    recyclerView.setVisibility(View.VISIBLE);
                return;
            }
        });

//        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                Query Q = searchbase.child("NgoList").orderByChild("searchName").startAt(newText.toLowerCase()).endAt(newText.toLowerCase()+"\uf8ff");
//                Log.v("SearchText",newText);
//                Log.v("search", String.valueOf(Q));
//                FirebaseRecyclerAdapter<OrgInfo, OrgInfoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrgInfo , OrgInfoViewHolder>(
//                        OrgInfo.class, R.layout.home_list_item, OrgInfoViewHolder.class, Q) {
//                    @Override
//                    protected void populateViewHolder(final OrgInfoViewHolder viewHolder, final OrgInfo model, int position) {
//                        final String post_key=getRef(position).getKey();
//                        viewHolder.setmOrginfo(model.getmOrginfo());
//                        viewHolder.setmOrgname(model.getmOrgname());
//                        viewHolder.setmCategory(model.getmCategory());
//                        viewHolder.setmImage(SearchActivity.this.getApplicationContext(),model.getmImage());
//                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v1) {
//                                Intent intent=new Intent(SearchActivity.this,orgInsideActivity.class);
//                                intent.putExtra("news_id",post_key);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    Transition transition_one = TransitionInflater.from(SearchActivity.this).inflateTransition(R.transition.transition_two);
//                                    getActivity().getWindow().setSharedElementEnterTransition(transition_one);
//                                    ImageView transitionViewOne = (ImageView) v1.findViewById(R.id.org_logo);
//                                    Bundle b = ActivityOptionsCompat
//                                            .makeSceneTransitionAnimation(SearchActivity.this, transitionViewOne, "orgimg").toBundle();
//                                    startActivity(intent, b);
//                                }
//                                else {
//                                    Log.v("starting","");
//
//                                    startActivity(intent);
//                                }
//                            }
//                        });
//
//                    }
//                };
//                recyclerView.setAdapter(firebaseRecyclerAdapter);
//                return false;
//            }
//        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static class OrgInfoViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public OrgInfoViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }


        public void setmOrginfo(String s) {
            TextView post_title=(TextView)mView.findViewById(R.id.org_about);
            post_title.setText(s);
        }

        public void setmOrgname(String s) {
            TextView post_title=(TextView)mView.findViewById(R.id.org_name);
            post_title.setText(s);
        }

        public void setmImage(Context applicationContext, String image) {
            final ImageView post_image=(ImageView)mView.findViewById(R.id.org_logo);
            Glide
                    .with(applicationContext)
                    .load(image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(post_image);

        }

        public void setmCategory(String s) {
            TextView post_title=(TextView)mView.findViewById(R.id.category);
            post_title.setText(s);
        }
    }
}
