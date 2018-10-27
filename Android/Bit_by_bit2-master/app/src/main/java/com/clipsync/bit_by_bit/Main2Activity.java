package com.clipsync.bit_by_bit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private SignInButton signin;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog progressdialog;
    private Shared_pref shared_pref;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        signin = findViewById(R.id.signin);
        configure();
        signin.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(Main2Activity.this, MainActivity.class));
                    finish();
                }
            }
        };
        progressdialog = new ProgressDialog(this);
        progressdialog.setTitle("Logging In");
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setMessage("Please wait...");
        shared_pref = new Shared_pref(this);
    }

    private void configure() {
// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onClick(View view) {
        progressdialog.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 555);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 555) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                // Google Sign In was successful, save Token and a state then authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                Log.d("ABHI", account.getIdToken());
                Log.d("ABHI", account.getDisplayName());
                Log.d("ABHI", account.getEmail());
                //Log.d("ABHI", account.getServerAuthCode());
                Log.d("ABHI", account.getPhotoUrl().toString());
                //Log.d("ABHI", account.zab());
                //Log.d("ABHI", account.zac());
                shared_pref.put_email(account.getEmail(), account.getDisplayName(), account.getPhotoUrl().toString());


                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuthWithGoogle(credential);

            } else {
                // Google Sign In failed, update UI appropriately
                Log.e("ABHI", "Login Unsuccessful. ");
                Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {

                    Toast.makeText(Main2Activity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    shared_pref.reset_session();

                }else {
                    //createUserInFirebaseHelper();
                    Toast.makeText(Main2Activity.this, "Login successful",
                            Toast.LENGTH_SHORT).show();
                    shared_pref.set_session();
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("email").setValue(firebaseAuth.getCurrentUser().getEmail());
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("name").setValue(firebaseAuth.getCurrentUser().getDisplayName());
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("photo_url").setValue(firebaseAuth.getCurrentUser().getPhotoUrl().toString());
                    //startActivity(new Intent(Signin.this, Signout.class));
                    //finish();
                    String deviceName = android.os.Build.MODEL;
                    String deviceMan = android.os.Build.MANUFACTURER;
                    shared_pref.put_deviceid(deviceName+deviceMan);
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("devices").child(deviceName+deviceMan).child("device").setValue("Android");

                    progressdialog.dismiss();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Main2Activity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
