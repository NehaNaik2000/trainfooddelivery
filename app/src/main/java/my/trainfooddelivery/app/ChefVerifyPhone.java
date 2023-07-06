package my.trainfooddelivery.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ChefVerifyPhone extends AppCompatActivity {

    String verificationId;
    FirebaseAuth FAuth;
    Button verify,Resend;
    TextView txt;
    EditText entercode;
    String phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_verify_phone);
        phoneno=getIntent().getStringExtra("phonenumber").trim();

        entercode=(EditText) findViewById(R.id.code);
        txt=(TextView) findViewById(R.id.text);
        Resend=(Button) findViewById(R.id.Resendotp);
        verify=(Button) findViewById(R.id.verify);
        FAuth=FirebaseAuth.getInstance();
        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);
        sendverificatoncode(phoneno);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code =entercode.getText().toString().trim();
                Resend.setVisibility(View.INVISIBLE);

                if(code.isEmpty()&& code.length()<6)
                {
                    entercode.setError("Enter code");
                    entercode.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });
        new CountDownTimer(60000,1000)
        {


            @Override
            public void onTick(long millisUntilFinished) {
                txt.setVisibility(View.VISIBLE);
                txt.setText("Resend code in "+millisUntilFinished/1000+" Seconds");
            }

            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);

            }

        }.start();
           Resend.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Resend.setVisibility(View.INVISIBLE);
                   Resendotp(phoneno);
                   new CountDownTimer(60000,1000)
                   {


                       @Override
                       public void onTick(long millisUntilFinished) {
                           txt.setVisibility(View.VISIBLE);
                           txt.setText("Resend code in "+millisUntilFinished/1000+" Seconds");
                       }

                       @Override
                       public void onFinish() {
                           Resend.setVisibility(View.VISIBLE);
                           txt.setVisibility(View.INVISIBLE);

                       }

                   }.start();
               }
           });
    }

    private void Resendotp(String phonenum) {

          sendverificatoncode(phonenum);

    }

    private void sendverificatoncode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FAuth)
                        .setPhoneNumber(phoneno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mcallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                  String code =phoneAuthCredential.getSmsCode();
                  if(code!=null)
                  {
                      entercode.setText(code);
                      verifyCode(code);
                  }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ChefVerifyPhone.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
                linkCredential(credential);

    }

    private void linkCredential(PhoneAuthCredential credential) {
        FAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(ChefVerifyPhone.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent =new Intent(ChefVerifyPhone.this,MainMenu.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    ResuableCodeForAll.ShowAlert(ChefVerifyPhone.this,"error",task.getException().getMessage());
                }
            }
        });
    }
}