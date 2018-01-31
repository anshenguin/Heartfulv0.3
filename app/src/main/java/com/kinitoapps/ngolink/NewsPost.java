package com.kinitoapps.ngolink;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class NewsPost extends AppCompatActivity {
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;

    private int revealX;
    private int revealY;

    private ImageView userImage;
    private EditText title,userDesc;
    private Button submitbtn;
    private DatabaseReference mdatabase;
    private Uri imageUri=null;
    private ProgressDialog progress;
    private String ngoId;
    private StorageReference newsPhotos;
    private FirebaseStorage mstorage;
    private  String newsPostBy;
    private   long currentDateTimeString = 	System.currentTimeMillis();
    private final static int GALLERY_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kinitoapps.ngolink.R.layout.activity_news_post);
        final Intent intent = getIntent();
        rootLayout = findViewById(com.kinitoapps.ngolink.R.id.rootlayout);
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(com.kinitoapps.ngolink.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Post News");
        mstorage=FirebaseStorage.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("News");
        newsPhotos=mstorage.getReference().child("NewsImages");
        submitbtn=(Button)findViewById(com.kinitoapps.ngolink.R.id.submit_button);
        title=(EditText)findViewById(com.kinitoapps.ngolink.R.id.title);
        userDesc=(EditText)findViewById(com.kinitoapps.ngolink.R.id.user_des);
        progress=new ProgressDialog(this);
        userImage=(ImageView)findViewById(com.kinitoapps.ngolink.R.id.user_image);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/jpeg");
                gallery.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(gallery, "Complete action using"),GALLERY_REQUEST);
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("News Posting, please wait...");
                userPost();


            }
        });
    }
    private void userPost(){
        final String postTitle=title.getText().toString();
        final String postDes= userDesc.getText().toString();
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userInfo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("chal raha hai","ondatachange");
                ngoId = String.valueOf(dataSnapshot.child("NGOId").getValue());
                newsPostBy = String.valueOf(dataSnapshot.child("userName").getValue()) ;
//                            Log.v("value", String.valueOf(dataSnapshot.child("NGOId").getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        if (!TextUtils.isEmpty(postTitle)&&!TextUtils.isEmpty(postDes)/*&&imageUri!=null*/){
            progress.show();
           StorageReference filePath=newsPhotos.child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(this,new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();


                    DatabaseReference newPost=mdatabase.push();
                    newPost.child("Title").setValue(postTitle);
                    newPost.child("Description").setValue(postDes);
                    newPost.child("DateAndTime").setValue(String.valueOf(currentDateTimeString) );
                    newPost.child("Image").setValue(downloadUrl.toString());
//                    Log.v("value1",ngoId);
                    Log.v("chal raha hai","newspost set wala");
//                    Log.v("NGOId",ngoId);
                    newPost.child("NGOId").setValue(ngoId);
                    newPost.child("NewsPostBy").setValue(newsPostBy);
                    progress.dismiss();
                    finish();

                }
            });

        }
        else{
            Toast.makeText(NewsPost.this,"Please fill all the blanks",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setFixAspectRatio(true)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 Uri resultUri = result.getUri();
                imageUri=resultUri;
                userImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(NewsPost.this, (CharSequence) error,Toast.LENGTH_LONG).show();
            }
        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

        protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }
}
