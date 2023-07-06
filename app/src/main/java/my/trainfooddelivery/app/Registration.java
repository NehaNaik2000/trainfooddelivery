package my.trainfooddelivery.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;


import java.util.ArrayList;
import java.util.HashMap;



public class Registration extends AppCompatActivity {
    String[] Maharashtra = {"Mumbai","Pune","Nashik"};
    String[] Madhyapradesh = {"Bhopal","Indore","Ujjain"};

    TextInputLayout Fname,Lname,Email,Pass,cpass,mobileno,area;
    Spinner Statespin,Cityspin;
    Button signup, Emaill, Phone;
    CountryCodePicker Cpp;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname,lname,emailid,password,confpassword,mobile,Area,statee,cityy;
    String role="Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Fname = (TextInputLayout)findViewById(R.id.Fname);
        Lname = (TextInputLayout)findViewById(R.id.Lname);
        Email = (TextInputLayout)findViewById(R.id.mail);
        Pass = (TextInputLayout)findViewById(R.id.Pawd);
        cpass = (TextInputLayout)findViewById(R.id.Cppass);
        mobileno = (TextInputLayout)findViewById(R.id.mobno);
        Statespin = (Spinner) findViewById(R.id.States);
        Cityspin = (Spinner) findViewById(R.id.Cityss);
        area = (TextInputLayout)findViewById(R.id.area);

        signup = (Button)findViewById(R.id.sup);
        Emaill = (Button)findViewById(R.id.em);
        Phone = (Button)findViewById(R.id.ph);

        Cpp = (CountryCodePicker)findViewById(R.id.concode);

        Statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object value = parent.getItemAtPosition(position);
                statee = value.toString().trim();
                if(statee.equals("Maharashtra")){
                    ArrayList<String> list = new ArrayList<>();
                    for (String cities : Maharashtra){
                        list.add(cities);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Registration.this,android.R.layout.simple_spinner_item,list);
                    Cityspin.setAdapter(arrayAdapter);
                }
                if(statee.equals("Madhyapradesh")){
                    ArrayList<String> list = new ArrayList<>();
                    for (String cities : Madhyapradesh){
                        list.add(cities);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Registration.this,android.R.layout.simple_spinner_item,list);
                    Cityspin.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                cityy = value.toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference = firebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                emailid = Email.getEditText().getText().toString().trim();
                mobile = mobileno.getEditText().getText().toString().trim();
                password = Pass.getEditText().getText().toString().trim();
                confpassword = cpass.getEditText().getText().toString().trim();
                Area = area.getEditText().getText().toString().trim();


                if (isValid()){
                    final ProgressDialog mDialog = new ProgressDialog(Registration.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registration in progress please wait......");
                    mDialog.show();



                    mAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                String useridd =FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("customer").child(useridd);
                                HashMap<String , String> hashMap = new HashMap<>();
                                hashMap.put("Role",role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        HashMap<String , String> hashMap1 = new HashMap<>();

                                        hashMap1.put("MobileNo",mobile);
                                        hashMap1.put("FirstName",fname);
                                        hashMap1.put("LastName",lname);
                                        hashMap1.put("EmailId",emailid);
                                        hashMap1.put("City",cityy);
                                        hashMap1.put("Area",Area);
                                        hashMap1.put("Password",password);

                                        hashMap1.put("State",statee);
                                        hashMap1.put("Confirm Password",confpassword);


                                        firebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDialog.dismiss();

                                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful()){
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                                                                    builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                                    builder.setCancelable(false);
                                                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            dialog.dismiss();
                                                                            String phonenumber=Cpp.getDefaultCountryCodeWithPlus()+mobile;
                                                                            Intent b= new Intent(Registration.this,ChefVerifyPhone.class);
                                                                            b.putExtra("phonenumber",phonenumber);
                                                                            startActivity(b);

                                                                        }
                                                                    });
                                                                    AlertDialog Alert = builder.create();
                                                                    Alert.show();
                                                                }else{
                                                                    mDialog.dismiss();
                                                                    ResuableCodeForAll.ShowAlert(Registration.this,"Error",task.getException().getMessage());
                                                                }
                                                            }
                                                        });

                                                    }
                                                });

                                    }
                                });
                            }
                            else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(Registration.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }

            }
        });

        Emaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Registration.this,Login.class));
                finish();
            }
        });
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Loginphone.class));
                finish();
            }
        });




    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String npattern  ="[a-zA-Z]+";
    public boolean isValid(){
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        mobileno.setErrorEnabled(false);
        mobileno.setError("");
        cpass.setErrorEnabled(false);
        cpass.setError("");
        area.setErrorEnabled(false);
        area.setError("");


        boolean isValid=false,isValidlname=false,isValidname=false,isValidemail=false,isValidpassword=false,isValidconfpassword=false,isValidmobilenum=false,isValidarea=false;
        if(TextUtils.isEmpty(fname)){
            Fname.setErrorEnabled(true);
            Fname.setError("Enter First Name");
        }else{
            if(fname.matches(npattern)){
                isValidname = true;
            }else{
                Fname.setErrorEnabled(true);
                Fname.setError("Enter a Valid Name");
            }
        }
        if(TextUtils.isEmpty(lname)){
            Lname.setErrorEnabled(true);
            Lname.setError("Enter First Name");
        }else{
            if(lname.matches(npattern)){
                isValidlname = true;
            }else{
                Lname.setErrorEnabled(true);
                Lname.setError("Enter a Valid Name");
            }
        }
        if(TextUtils.isEmpty(emailid)){
            Email.setErrorEnabled(true);
            Email.setError("Email Is Required");
        }else{
            if(emailid.matches(emailpattern)){
                isValidemail = true;
            }else{
                Email.setErrorEnabled(true);
                Email.setError("Enter a Valid Email Id");
            }
        }
        if(TextUtils.isEmpty(password)){
            Pass.setErrorEnabled(true);
            Pass.setError("Enter Password");
        }else{
            if(password.length()<8){
                Pass.setErrorEnabled(true);
                Pass.setError("Password is Weak");
            }else{
                isValidpassword = true;
            }
        }
        if(TextUtils.isEmpty(confpassword)){
            cpass.setErrorEnabled(true);
            cpass.setError("Enter Password Again");
        }else{
            if(!password.equals(confpassword)){
                cpass.setErrorEnabled(true);
                cpass.setError("Password Dosen't Match");
            }else{
                isValidconfpassword = true;
            }
        }
        if(TextUtils.isEmpty(mobile)){
            mobileno.setErrorEnabled(true);
            mobileno.setError("Mobile Number Is Required");
        }else{
            if(mobile.length()<10){
                mobileno.setErrorEnabled(true);
                mobileno.setError("Invalid Mobile Number");
            }else{
                isValidmobilenum = true;
            }
        }
        if(TextUtils.isEmpty(Area)){
            area.setErrorEnabled(true);
            area.setError("Area Is Required");
        }else{
            isValidarea = true;
        }


        isValid = (isValidarea && isValidconfpassword && isValidpassword && isValidemail && isValidmobilenum && isValidname  && isValidlname) ? true : false;
        return isValid;


    }
}