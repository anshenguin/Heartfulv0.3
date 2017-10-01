package com.example.hp.heartful;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

/**
 * Created by anshul on 17/9/17.
 */



public class Preferences extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout logout,changeName,changePic;
    private final static int GALLERY_REQUEST=1;
    private Uri imageUri=null;
    private ProgressDialog progress;
    private StorageReference mstorage;
    private FirebaseUser firebaseUser;
    private DatabaseReference forUsers;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        forUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseUser=mAuth.getCurrentUser();
        progress.setMessage("Updating Profile Picture, please wait...");
        mstorage= FirebaseStorage.getInstance().getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Preferences");
        changeName=(LinearLayout)findViewById(R.id.change_name);
        changePic=(LinearLayout)findViewById(R.id.change_pic);
        logout = (LinearLayout) findViewById(R.id.signout);
        logout.setOnClickListener(this);
        changePic.setOnClickListener(this);
        changeName.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == logout) {
            LoginManager.getInstance().logOut();
            mAuth.signOut();
            Toast.makeText(Preferences.this, "user has been sign out", Toast.LENGTH_LONG).show();
            finish();
        }
        if (v == changeName) {
            startActivity(new Intent(Preferences.this, UpdateName.class));
        }
        if (v == changePic){
            selectImage();
        }
    }
    private void selectImage() {
        Intent gallery=new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageUri=resultUri;
                progress.show();
                StorageReference filePath=mstorage.child("UsersProfilePics").child(imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") final
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                       final DatabaseReference imagePost= forUsers.child(firebaseUser.getUid());
                        forUsers.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                            imagePost.child("profilePicLink").setValue(downloadUrl.toString());
                                progress.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

//                userImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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