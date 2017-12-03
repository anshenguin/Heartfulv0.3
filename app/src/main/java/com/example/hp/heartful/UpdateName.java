package com.example.hp.heartful;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateName extends AppCompatActivity {
 private EditText enterName;
    private String changedName;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private DatabaseReference forUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Enter your name");
        enterName=(EditText)findViewById(R.id.changed_name);
        mAuth= FirebaseAuth.getInstance();
        forUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseUser=mAuth.getCurrentUser();
            forUsers.child(firebaseUser.getUid()).child("userInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
                    enterName.setText(user.getUserName());
                    enterName.selectAll();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

            findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changedName= enterName.getText().toString();
                    if(TextUtils.isEmpty(changedName)){
                        // email is empty
                        Toast.makeText(UpdateName.this,"please select  name",Toast.LENGTH_SHORT).show();
                        return;// to stop the function from executation.
                    }
                    Log.v("New name",changedName);
                 DatabaseReference name=forUsers.child(firebaseUser.getUid()).child("userInfo");
                    name.child("userName").setValue(changedName);
//                    Log.v("name ",mAuth.getCurrentUser().getDisplayName());
                    finish();

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
