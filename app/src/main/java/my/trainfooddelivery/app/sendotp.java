package my.trainfooddelivery.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class sendotp extends AppCompatActivity {

    String verificationId;
    FirebaseAuth FAuth;
    Button verify,Resend;
    TextView txt;
    EditText entercode;
    String phoneno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendotp);

        phoneno=getIntent().getStringExtra("Phonenumber").trim();

        entercode=(EditText) findViewById(R.id.Code);
        txt=(TextView) findViewById(R.id.texxt);
        Resend=(Button) findViewById(R.id.resendotp);
        verify=(Button) findViewById(R.id.Verify);
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
                txt.setText("Resend code in"+millisUntilFinished/1000+" Seconds");
            }

            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);

            }

        }.start();

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
            Toast.makeText(sendotp.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }
    };
    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code);
        signInWithPhone(credential);

    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        FAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(sendotp.this,ChefFoodPanel_BottomNavigation.class));
                    finish();
                }
                else
                {
                    ResuableCodeForAll.ShowAlert(sendotp.this,"ERROR",task.getException().getMessage());
                }

            }
        });
    }


}
