package com.kinitoapps.ngolink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email_Id;
    private EditText password;
    private TextView sign_Up;
    private Button sign_In;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kinitoapps.ngolink.R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(com.kinitoapps.ngolink.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login to your Account");
        progressDialog=new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();
        email_Id=(EditText)findViewById(com.kinitoapps.ngolink.R.id.email_id);
        password=(EditText)findViewById(com.kinitoapps.ngolink.R.id.password);
        sign_In=(Button)findViewById(com.kinitoapps.ngolink.R.id.sign_In);
        sign_Up=(TextView)findViewById(com.kinitoapps.ngolink.R.id.new_user);
        sign_In.setOnClickListener(this);
        sign_Up.setOnClickListener(this);

    }

    private void user_Login(){
        firebaseAuth= FirebaseAuth.getInstance();
        String email=email_Id.getText().toString().trim();
        String pass_word=password.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            // email is empty
            Toast.makeText(this,"please enter email",Toast.LENGTH_SHORT).show();
            return;// to stop the function from executation.
        }
        if(TextUtils.isEmpty(pass_word)){
            // email is empty
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        // here if everything ok the user will be register
        progressDialog.setMessage("Signing,please wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,pass_word)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // start user profile activity
                            progressDialog.dismiss();
                            Intent intent = new Intent(loginActivity.this, Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(loginActivity.this," "+ task.getException(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if (view==sign_In){
            // start user profile activity
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            user_Login();
        }
        if (view==sign_Up){
            finish();
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
