package my.trainfooddelivery.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Cheflogin extends AppCompatActivity {

    TextInputLayout email,pass;
    Button Signin,Signinphone;
    TextView Forgotpassword,signup;
    FirebaseAuth Fauth;
    String emailid;
    String pwd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheflogin);

     try {
         email = (TextInputLayout) findViewById(R.id.Lemail);
         pass = (TextInputLayout) findViewById(R.id.Lpassword);
         Signin = (Button) findViewById(R.id.button4);
         Forgotpassword = (TextView) findViewById(R.id.forgotpass);
         Signinphone = (Button)findViewById(R.id.btnphone);
         signup=(TextView) findViewById(R.id.textView3);
         Fauth = FirebaseAuth.getInstance();

         Signin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 emailid = email.getEditText().getText().toString().trim();
                 pwd = pass.getEditText().getText().toString().trim();

                 if (isValid()) {
                     final ProgressDialog mDialog = new ProgressDialog(Cheflogin.this);
                     mDialog.setCanceledOnTouchOutside(false);
                     mDialog.setCancelable(false);
                     mDialog.setMessage("SIGN IN PLEASE WAIT..");
                     mDialog.show();

                     Fauth.signInWithEmailAndPassword(emailid, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 mDialog.dismiss();

                                 if (Fauth.getCurrentUser().isEmailVerified()) {
                                     mDialog.dismiss();
                                     Toast.makeText(Cheflogin.this, "Successful login", Toast.LENGTH_SHORT).show();
                                     Intent z = new Intent(Cheflogin.this, ChefFoodPanel_BottomNavigation.class);
                                     startActivity(z);
                                     finish();
                                 } else {
                                     ResuableCodeForAll.ShowAlert(Cheflogin.this, "Verification failed", "You have not verified your email");
                                 }
                             } else {
                                 mDialog.dismiss();
                                 ResuableCodeForAll.ShowAlert(Cheflogin.this, "ERROR", task.getException().getMessage());
                             }

                         }
                     });
                 }


             }
         });

         signup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(Cheflogin.this, ChefRegistration.class));
                 finish();
             }
         });

         Forgotpassword.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(Cheflogin.this,ChefForgotPassword.class));
                 finish();
             }
         });

         Signinphone.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                Intent i = new Intent(Cheflogin.this,Chefloginphone.class);
                startActivity(i);
                 finish();
             }
         });
     } catch (Exception e) {
         Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
     }
    }




    String emailpattern ="[a-zA-Z0-9.-_]+@[a-z]+\\.+[a-z]+";

    public boolean isValid(){
        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalid=false,isvalidemail=false,isvalidpassword=false;
        if(TextUtils.isEmpty(emailid))
        {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        }
        else{
            if(emailid.matches(emailpattern))
            {
                isvalidemail=true;
            }
            else
            {
                email.setErrorEnabled(true);
                email.setError("invalid email address");
            }
        }

        if(TextUtils.isEmpty(pwd))
        {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        }
        else
        {
            isvalidpassword=true;
        }

        isvalid=(isvalidemail && isvalidpassword)?true:false;
        return isvalid;


    }


}