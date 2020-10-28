package company.photoshooto_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import company.photoshooto_task.Helper.FontType;

public class ForgotPassword extends AppCompatActivity {

    private EditText passwordEmail;
    private FrameLayout resetPassword;
    private FirebaseAuth firebaseAuth;

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ViewGroup root = (ViewGroup)findViewById(R.id.root);
        FontType fontType=new FontType(ForgotPassword.this,root);

        passwordEmail=(EditText)findViewById(R.id.etEmail);
        resetPassword=(FrameLayout)findViewById(R.id.login_button);
        firebaseAuth=FirebaseAuth.getInstance();

        img = (ImageView)findViewById(R.id.icon);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail=passwordEmail.getText().toString().trim();

                if(useremail.equals("")){
                    //Toast.makeText(password.this,"Please enter your register Email id",Toast.LENGTH_SHORT).show();
                    passwordEmail.setError("Enter Email address");
                    passwordEmail.requestFocus();
                }else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Toast.makeText(password.this," Password reset email sent",Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder=new AlertDialog.Builder(ForgotPassword.this);
                                builder.setMessage("Password reset link is sent to your registered Email id")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                startActivity(new Intent(ForgotPassword.this, Login.class));
                                            }
                                        }).setCancelable(false);
                                AlertDialog alertDialog=builder.create();
                                alertDialog.show();
                            }else{
                                Toast.makeText(ForgotPassword.this," Error in sending password reset email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}