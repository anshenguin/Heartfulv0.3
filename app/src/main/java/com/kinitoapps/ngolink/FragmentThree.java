package com.kinitoapps.ngolink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP INDIA on 08-Apr-17.
 */
public class FragmentThree extends Fragment implements View.OnClickListener ,ConnectivityReceiver.ConnectivityReceiverListener{
    private boolean shouldRefreshOnResume = false;
    private boolean justRefreshed = false;
    private boolean usingSignIn = false;
    private EditText email_Id;
    private EditText password;
    private SignInButton mgoogleSign;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private EditText User_Name;
    private Button Sign_Up;
    private FirebaseUser firebaseUser;
    private DatabaseReference forUsers;
    private TextView login_Text;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog progress;
    private ProgressDialog fbProgress;
    private static String TAG="FragmentThree";
    private LoginButton mFbLogin;
    private CallbackManager callbackManager;
    View view;
    boolean isConnected = ConnectivityReceiver.isConnected();
//    View view_pro;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(com.kinitoapps.ngolink.R.layout.sign_up_page,container, false);
        fbProgress= new ProgressDialog(getActivity());
        fbProgress.setMessage("Connecting to Facebook Account, Please wait...");
        mAuth=FirebaseAuth.getInstance();
        forUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getActivity());
        callbackManager= CallbackManager.Factory.create();
        progressDialog=new ProgressDialog(getActivity());
        progress=new ProgressDialog(getActivity());
        email_Id=(EditText)view.findViewById(com.kinitoapps.ngolink.R.id.email_id);
        progress.setMessage("Connecting to Google Account, please wait...");
        password=(EditText)view.findViewById(com.kinitoapps.ngolink.R.id.password);
        User_Name=(EditText)view.findViewById(com.kinitoapps.ngolink.R.id.User_name);
        Sign_Up=(Button)view.findViewById(com.kinitoapps.ngolink.R.id.sign_up);
        login_Text=(TextView)view.findViewById(com.kinitoapps.ngolink.R.id.login_text);
        Sign_Up.setOnClickListener(this);
//        edit.setOnClickListener(this);
        mgoogleSign=(SignInButton)view.findViewById(com.kinitoapps.ngolink.R.id.google_login);
        login_Text.setOnClickListener(this);
        mFbLogin=(LoginButton)view.findViewById(com.kinitoapps.ngolink.R.id.fb_login);
        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                justRefreshed = false;
                if(usingSignIn)
                    shouldRefreshOnResume = true;

            }

        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.kinitoapps.ngolink.R.string.default_web_client_id))
                .requestEmail()
                .build();
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            try {
                mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                        .enableAutoManage(getActivity(),new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Toast.makeText(getActivity(),"You get an error", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mgoogleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isConnected) {
                   progress.show();
                   signIn();
               }
               else   Toast.makeText(getActivity(),"No Internet Connection, Please Connect to network first", Toast.LENGTH_LONG).show();

            }
        });


       mFbLogin.setFragment(FragmentThree.this);
       mFbLogin.setReadPermissions("public_profile", "email", "user_friends");
       try {
           mFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
               @Override
               public void onSuccess(LoginResult loginResult) {
                   Log.v(" FragmentFacebook", "Dekhte hai chal rha hai ki nhi");
                   fbProgress.show();
                   handleFacebookAccessToken(loginResult.getAccessToken());

                   // App code
                   //userProfile();

               }

               @Override
               public void onCancel() {
                   Log.v("FragmentThree 2", "Dekhte hai chal rha hai ki nhi");
                   // App code
                   Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onError(FacebookException exception) {
                   Log.v("FragmentThree 3", "Dekhte hai chal rha hai ki nhi");
                   Log.v("Exception", String.valueOf(exception));
                   // App code
               }
           });
       } catch (Exception e) {
           Log.v("FACEBOOK", "Error in the loginButton facebook");
           e.printStackTrace();
       }

        //                }

            return view;

    }



    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }


    private void signIn() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                //progress.dismiss();
            }

        }

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()==null) {
            if (visible) {

                FragmentManager fm = getFragmentManager();
                login_dialogbox dialogFragment = new login_dialogbox();
                dialogFragment.show(fm, "LoginPopup");

            }

            super.setMenuVisibility(visible);
        }
    }


    @Override
    public void onClick(View view) {
        if(view==Sign_Up){
            // user will register here
   if (isConnected)
            registerUser();
            else   Toast.makeText(getActivity(),"No Internet Connection, Please Connect to network first", Toast.LENGTH_LONG).show();
        }
        if(view==login_Text) {
            //    login user
            if (isConnected) {
                usingSignIn = true;
                login();
//            shouldRefreshOnResume=true;
            }
            else   Toast.makeText(getActivity(),"No Internet Connection, Please Connect to network first", Toast.LENGTH_LONG).show();

        }

    }
    //
    private void login(){

        try{
            startActivity(new Intent(getActivity(), loginActivity.class));
        }catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
        }

    }

    private  void registerUser(){
        String email=email_Id.getText().toString().trim();
        String pass_word=password.getText().toString().trim();
        final   String user_name=User_Name.getText().toString();
        if(TextUtils.isEmpty(user_name)){
            // email is empty
            Toast.makeText(getActivity(),"please select  name",Toast.LENGTH_SHORT).show();
            return;// to stop the function from executation.
        }


        if(TextUtils.isEmpty(email)){
            // email is empty
            Toast.makeText(getActivity(),"please enter email",Toast.LENGTH_SHORT).show();
            return;
            // to stop the function from executation.
        }
        if(TextUtils.isEmpty(pass_word)){
            // email is empty
            Toast.makeText(getActivity(),"please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        // here if everything ok the user will be register
        progressDialog.setMessage("Registering User, please wait...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,pass_word)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //show user profile
                            Toast.makeText(getActivity(),"Registered successfully",Toast.LENGTH_SHORT).show();
                            firebaseUser=mAuth.getCurrentUser();
                            DatabaseReference userData = forUsers.child(firebaseUser.getUid()).child("userInfo");
                                boolean canPost = false;
                            String userDesc ="Yesterday is history, tomorrow's a mystery, today is a gift, that's why we call it 'present'";
                                String personName = user_name;
                                String personPhoto = "https://firebasestorage.googleapis.com/v0/b/heartful-dc3ac.appspot.com/o/profilepic.png?alt=media&token=5b98dc2e-1e36-4eb8-86e9-54d10222120e";
                                Users user = new Users(personName, personPhoto, canPost,userDesc);
                                userData.setValue(user);

                            justRefreshed = false;
//                            reLoad();
                            Intent intent = new Intent(getActivity(), Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(getActivity(),"could not register, pls try again Error is"+ task.getException(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });


    }

    private void handleFacebookAccessToken(AccessToken token) {
        shouldRefreshOnResume = false;
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.v("Facebook","Calling");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String HEARTFUL_USER ="HeartfulUser";
                            firebaseUser=mAuth.getCurrentUser();
                            final DatabaseReference userData = forUsers.child(firebaseUser.getUid()).child("userInfo");
                            SharedPreferences pref = getApplicationContext().getSharedPreferences(HEARTFUL_USER, Activity.MODE_PRIVATE);

                            if (!pref.contains(HEARTFUL_USER)) {
                                Log.v("chal","fb");
                                boolean canPost = false;
                                String personName = firebaseUser.getDisplayName();
                                Uri personPhoto = firebaseUser.getPhotoUrl();
                                String userDesc ="Yesterday is history, tomorrow's a mystery, today is a gift, that's why we call it 'present'";
                                Users user = new Users(personName, personPhoto.toString(), canPost,userDesc);
                                userData.setValue(user);
                                pref.edit().putBoolean(HEARTFUL_USER, true).commit();
                            }

                            Query query = userData.orderByKey().equalTo("canPost");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){}
                                    else{

                                        boolean canPost = false;
                                        String userDesc ="Yesterday is history, tomorrow's a mystery, today is a gift, that's why we call it 'present'";
                                        String personName = firebaseUser.getDisplayName();
                                        Uri personPhoto = firebaseUser.getPhotoUrl();
                                        Users user = new Users(personName, personPhoto.toString(), canPost,userDesc);
                                        userData.setValue(user);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Log.v(TAG, "signInWithCredential:success");
                            justRefreshed = false;
//                            reLoad();
                            Intent intent = new Intent(getActivity(), Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            fbProgress.dismiss();
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed,"+task.getException(),Toast.LENGTH_LONG    ).show();
                            LoginManager.getInstance().logOut();
                            fbProgress.dismiss();

                        }


                    }
                });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        shouldRefreshOnResume = false;
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String HEARTFUL_USER ="GoogleUser";
                            firebaseUser=mAuth.getCurrentUser();
                            final DatabaseReference userData = forUsers.child(firebaseUser.getUid()).child("userInfo");
                            SharedPreferences pref = getApplicationContext().getSharedPreferences(HEARTFUL_USER, Activity.MODE_PRIVATE);

                            if (!pref.contains(HEARTFUL_USER)) {
                                Log.v("chal","google");
                                boolean canPost = false;
                                String userDesc ="Yesterday is history, tomorrow's a mystery, today is a gift, that's why we call it 'present'";
                                String personName = firebaseUser.getDisplayName();
                                Uri personPhoto = firebaseUser.getPhotoUrl();
                                Users user = new Users(personName, personPhoto.toString(), canPost,userDesc);
                                userData.setValue(user);
                                pref.edit().putBoolean(HEARTFUL_USER, true).commit();
                            }

                            Query query = userData.orderByKey().equalTo("canPost");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){}
                                    else{

                                boolean canPost = false;
                                        String userDesc ="Yesterday is history, tomorrow's a mystery, today is a gift, that's why we call it 'present'";
                                        String personName = firebaseUser.getDisplayName();
                                Uri personPhoto = firebaseUser.getPhotoUrl();
                                Users user = new Users(personName, personPhoto.toString(), canPost,userDesc);
                                userData.setValue(user);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Log.v(TAG, "signInWithCredential:success");
                            justRefreshed = false;

                            progress.dismiss();
                            Intent intent = new Intent(getActivity(), Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            fbProgress.dismiss();
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                            progress.dismiss();

                        }

                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.v("reload", "onStop");

        if(mAuth.getCurrentUser()!=null)
            shouldRefreshOnResume = true;
        else
            shouldRefreshOnResume = false;
        Log.v("value of should refresh", String.valueOf(shouldRefreshOnResume));
//        Log.v("reload", "onStop");

    }

    @Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        ForCache.getInstance().setConnectivityListener(this);
        // Check should we need to refresh the fragment
        Log.v("reload ing through", "onresume");
        Log.v(String.valueOf(shouldRefreshOnResume), "onresume");

        if(shouldRefreshOnResume){
            // refresh fragment
            Log.v("value of should refresh", String.valueOf(shouldRefreshOnResume));
//            reLoad();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.v("Network","change occur");
        this. isConnected = isConnected ;

    }
}