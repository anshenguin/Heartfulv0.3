package com.example.hp.heartful;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP INDIA on 08-Apr-17.
 */

public  class FragmentOne extends Fragment implements AdapterView.OnItemSelectedListener {
    public FragmentOne() {}
    private DatabaseReference mDatabase;
//    private ArrayList<OrgInfo>orgInfoArrayList;
//    private OrgInfoAdapter adapter;
    private SearchView searchview;
    private RecyclerView recyclerView;
    private ImageView searchimagebutton;
    int check = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_one, container, false);
        super.onCreate(savedInstanceState);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("NgoList");
            mDatabase.keepSynced(true);
        searchimagebutton = (ImageView) rootView.findViewById(R.id.searchimagebutton);
        searchimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        recyclerView=(RecyclerView)rootView.findViewById(R.id.ngo_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        //mNewsLists.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseRecyclerAdapter<OrgInfo,OrgInfoViewHolder>firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<OrgInfo, OrgInfoViewHolder>(
                OrgInfo.class,
                R.layout.home_list_item,
                OrgInfoViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(OrgInfoViewHolder viewHolder, OrgInfo model, int position) {
                final String post_key=getRef(position).getKey();
                viewHolder.setmOrginfo(model.getmOrginfo());
                viewHolder.setmOrgname(model.getmOrgname());
                viewHolder.setmCategory(model.getmCategory());
                viewHolder.setmImage(getActivity().getApplicationContext(),model.getmImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        Intent intent=new Intent(getActivity(),orgInsideActivity.class);
                        intent.putExtra("news_id",post_key);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Transition transition_one = TransitionInflater.from(getActivity()).inflateTransition(R.transition.transition_two);
                            getActivity().getWindow().setSharedElementEnterTransition(transition_one);
                            ImageView transitionViewOne = (ImageView) v1.findViewById(R.id.org_logo);
                            Bundle b = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(getActivity(), transitionViewOne, "orgimg").toBundle();
                            startActivity(intent, b);
                        }
                        else
                            startActivity(intent);
                    }
                });
            }

        };
        Spinner spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("All");
        categories.add("Education");
        categories.add("Physical Health");
        categories.add("Mental Health");
        categories.add("Poverty");
        categories.add("Children");
        categories.add("Women");
        categories.add("Clothes");
        categories.add("Shelter");
////      adapter= new OrgInfoAdapter(getActivity(),orgInfoArrayList);
//// Create an ArrayAdapter using the string array and a default spinner layout
//
//        ArrayAdapter<CharSequence> spinadapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        ArrayAdapter<String> spinadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(spinadapter);
        spinner.setOnItemSelectedListener(this);
       recyclerView.setAdapter(firebaseRecyclerAdapter);
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
// On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
//        if(++check > 1) {
//            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
            setAccordingToselected(item);
//        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void setAccordingToselected(String item) {
        String all = "All";
        if (item != all) {
            Query Q = mDatabase.orderByChild("mCategory").equalTo(item);
            FirebaseRecyclerAdapter<OrgInfo, OrgInfoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrgInfo, OrgInfoViewHolder>(
                    OrgInfo.class, R.layout.home_list_item, OrgInfoViewHolder.class, Q) {
                @Override
                protected void populateViewHolder(final OrgInfoViewHolder viewHolder, final OrgInfo model, int position) {
                    final String post_key = getRef(position).getKey();
                    viewHolder.setmOrginfo(model.getmOrginfo());
                    viewHolder.setmOrgname(model.getmOrgname());
                    viewHolder.setmCategory(model.getmCategory());
                    viewHolder.setmImage(getActivity().getApplicationContext(), model.getmImage());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v1) {
                            Intent intent = new Intent(getActivity(), orgInsideActivity.class);
                            intent.putExtra("news_id", post_key);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Transition transition_one = TransitionInflater.from(getActivity()).inflateTransition(R.transition.transition_two);
                                getActivity().getWindow().setSharedElementEnterTransition(transition_one);
                                ImageView transitionViewOne = (ImageView) v1.findViewById(R.id.org_logo);
                                Bundle b = ActivityOptionsCompat
                                        .makeSceneTransitionAnimation(getActivity(), transitionViewOne, "orgimg").toBundle();
                                startActivity(intent, b);
                            } else {
                                Log.v("starting", "");

                                startActivity(intent);
                            }
                        }
                    });

                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        }
        else
        {
            FirebaseRecyclerAdapter<OrgInfo,OrgInfoViewHolder>firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<OrgInfo, OrgInfoViewHolder>(
                    OrgInfo.class,
                    R.layout.home_list_item,
                    OrgInfoViewHolder.class,
                    mDatabase

            ) {
                @Override
                protected void populateViewHolder(OrgInfoViewHolder viewHolder, OrgInfo model, int position) {
                    final String post_key=getRef(position).getKey();
                    viewHolder.setmOrginfo(model.getmOrginfo());
                    viewHolder.setmOrgname(model.getmOrgname());
                    viewHolder.setmCategory(model.getmCategory());
                    viewHolder.setmImage(getActivity().getApplicationContext(),model.getmImage());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v1) {
                            Intent intent=new Intent(getActivity(),orgInsideActivity.class);
                            intent.putExtra("news_id",post_key);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Transition transition_one = TransitionInflater.from(getActivity()).inflateTransition(R.transition.transition_two);
                                getActivity().getWindow().setSharedElementEnterTransition(transition_one);
                                ImageView transitionViewOne = (ImageView) v1.findViewById(R.id.org_logo);
                                Bundle b = ActivityOptionsCompat
                                        .makeSceneTransitionAnimation(getActivity(), transitionViewOne, "orgimg").toBundle();
                                startActivity(intent, b);
                            }
                            else
                                startActivity(intent);
                        }
                    });
                }

            };

            recyclerView.setAdapter(firebaseRecyclerAdapter);


        }
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









