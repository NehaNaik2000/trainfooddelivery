package my.trainfooddelivery.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseOne extends AppCompatActivity {

    Button Chef,Customer,DeliveryPerson;
    Intent intent;
    String type;
    ConstraintLayout bgimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);



        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        Chef = (Button)findViewById(R.id.chef);
        DeliveryPerson = (Button)findViewById(R.id.delivery);
        Customer = (Button)findViewById(R.id.customer);


        Chef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Email")){
                    Intent loginemail  = new Intent(ChooseOne.this,Cheflogin.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(ChooseOne.this,Chefloginphone.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Register  = new Intent(ChooseOne.this,ChefRegistration.class);
                    startActivity(Register);
                }
            }
        });

        Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){
                    Intent loginemailcust  = new Intent(ChooseOne.this,Login.class);
                    startActivity(loginemailcust);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphonecust  = new Intent(ChooseOne.this,Loginphone.class);
                    startActivity(loginphonecust);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Registercust  = new Intent(ChooseOne.this,Registration.class);
                    startActivity(Registercust);
                }

            }
        });

        DeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){
                    Intent loginemail = new Intent(ChooseOne.this,Delivery_Login.class);
                    startActivity(loginemail);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphone  = new Intent(ChooseOne.this,Delivery_Loginphone.class);
                    startActivity(loginphone);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Registerdelivery  = new Intent(ChooseOne.this,Delivery_Registration.class);
                    startActivity(Registerdelivery);
                }

            }
        });

    }
}
