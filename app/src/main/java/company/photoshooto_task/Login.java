package company.photoshooto_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import company.photoshooto_task.Common.Common;
import company.photoshooto_task.Helper.FontType;
import company.photoshooto_task.Model.Sign_Up;

public class Login extends AppCompatActivity {

    TextView forgotPass,txt8,openSignup;
    FrameLayout btnLogin;
    EditText userName,password;

    LinearLayout googleSignIn,otpSignIn;

    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    String dummyName,dummyEmail,dummyPassword,dummyNumber;

    private final static int RC_SIGN_IN=2;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ViewGroup root = (ViewGroup)findViewById(R.id.root);
        FontType fontType=new FontType(Login.this,root);

        forgotPass = (TextView) findViewById(R.id.forgotpass);
        btnLogin = (FrameLayout) findViewById(R.id.login_button);
        txt8 = (TextView) findViewById(R.id.txt1);
        openSignup = (TextView) findViewById(R.id.txt2);


        userName = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPass);

        googleSignIn = (LinearLayout)findViewById(R.id.google_login_layout);

        otpSignIn = (LinearLayout)findViewById(R.id.otp_login_layout);

        progressDialog = new ProgressDialog(this);

        //firebase init
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        openSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

        //username password login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    if(userName.getText().toString().equals("") ){
                        userName.setError("Enter Email address");
                        userName.requestFocus();
                    }
                    else if(password.getText().toString().equals("")){
                        password.setError("Enter password");
                        password.requestFocus();
                    }else{
                        validate(userName.getText().toString(), password.getText().toString());
                    }

                }
                else {
                    Toast.makeText(Login.this,"Please check your internet Connection !!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            private void validate(String s, String s1) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(s,s1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseAuthException e = (FirebaseAuthException)task.getException();
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            //Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            checkEmailVerification();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Login Failed"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        otpSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Login.this);
                bottomSheetDialog.setContentView(R.layout.otp_signin_layout);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                //CountryCodePicker countryCodePicker = bottomSheetDialog.findViewById(R.id.ccp);
                TextView phoneTxt = bottomSheetDialog.findViewById(R.id.phonenumber_text);
                EditText number = bottomSheetDialog.findViewById(R.id.etNumber);
                FrameLayout sendOtp = bottomSheetDialog.findViewById(R.id.buttonContinue);
                TextView textOtp = bottomSheetDialog.findViewById(R.id.textOtp);

                Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Gelion-Regular.ttf");
                number.setTypeface(typeface);
                textOtp.setTypeface(typeface);
                phoneTxt.setTypeface(typeface);
                sendOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mobile = number.getText().toString().trim();

                        if(mobile.isEmpty() || mobile.length() < 10){
                            number.setError("Enter a valid mobile number");
                            number.requestFocus();
                            return;
                        }
                        Intent intent = new Intent(Login.this,Otp.class);
                        intent.putExtra("mobile", number.getText().toString());
                        startActivity(intent);
                    }
                });
                bottomSheetDialog.show();
            }
        });

        //google signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if(user!= null){
            finish();
            startActivity(new Intent(Login.this, Home.class));
        }

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    signIn();
                }
                else {
                    Toast.makeText(Login.this,"Please check your internet Connection !!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();

        if(emailflag){
            finish();
            Intent intent=new Intent(Login.this,Home.class);
            // intent.putExtra("Title","Update Profile");
            startActivity(intent);
        }else{
            //Toast.makeText(this,"Verify your email",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
            builder.setMessage("First verify your email")
                    .setPositiveButton("ok",null).setCancelable(false);
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
            firebaseAuth.signOut();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser isNewUser = firebaseAuth.getCurrentUser();
                        // if(isNewUser.equals == 0){
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            sendUserGoogleData();
                           // String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Intent intent=new Intent(Login.this,Home.class);
                            //intent.putExtra("Title","Update Profile");
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                    }
                });
    }

    private void sendUserGoogleData(){
        final GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        // Person profile=plus.peopleApi.getCurrentPerson(mGoogleSignInClient);
        if(account!=null){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            final DatabaseReference myRef=firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
            //String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                    }else{
                        dummyName=account.getDisplayName();
                        dummyEmail=account.getEmail();
                        dummyNumber="+91";

                        //Uri photo=account.getPhotoUrl();
                        Sign_Up userProfile=new Sign_Up(dummyName,dummyNumber,dummyEmail);
                        myRef.setValue(userProfile);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Login.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}