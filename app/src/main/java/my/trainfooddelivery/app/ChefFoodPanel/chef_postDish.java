package my.trainfooddelivery.app.ChefFoodPanel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import my.trainfooddelivery.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.UUID;



public class chef_postDish extends AppCompatActivity {

    private static final int PICK_IMAGE_CHOOSER_REQUEST_CODE = 0;
    ImageButton imageButton;
     ImageView IVPreviewImage;
    Button post_dish;
    Spinner Dishes;
    TextInputLayout desc,qty,pri;
    String descrption,quantity,price,dishes,restaurant;
    Uri imageuri;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,dataa;
    FirebaseAuth Fauth;
    StorageReference ref;
    String ChefId , RandomUID , rname, Code , Area;
    private DatabaseReference chefTokensRef;
    private DatabaseReference notificationsRef;
    private String chefId;
    private String channelId = "chefchannel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_post_dish);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Dishes = (Spinner)findViewById(R.id.dishes);
        desc = (TextInputLayout) findViewById(R.id.description);
        qty = (TextInputLayout) findViewById(R.id.Quantity);
        pri = (TextInputLayout) findViewById(R.id.price);
        post_dish = (Button) findViewById(R.id.post);
        Fauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("FoodDetails");

        // Initialize Firebase references
        chefTokensRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("tokens").child("chef");
        notificationsRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("notifications").child("chef");

        // Get chef ID
        chefId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        try {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataa = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
            dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Chef cheff = snapshot.getValue(Chef.class);
                    if (cheff !=null) {
                        Code = cheff.getState();
                        Area = cheff.getArea();
                        rname= cheff.getRestaurant();


                    }
                    else
                    {
                        Log.d("error","no fetch");

                    }
                    Log.d("error","code"+Code);
                    Log.d("error","area"+Area);
                    Log.d("error","rname"+rname);
                    imageButton = (ImageButton) findViewById(R.id.image_upload);
                    IVPreviewImage = findViewById(R.id.IVPreviewImage);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageChooser();
                        }
                    });
                    post_dish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dishes = Dishes.getSelectedItem().toString().trim();
                            descrption = desc.getEditText().getText().toString().trim();
                            quantity = qty.getEditText().getText().toString().trim();
                            price = pri.getEditText().getText().toString().trim();

                            if(isValid()){
                                uploadImage();
                                generateAndSaveToken();

                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("onDataChange: ", "Failed to read value.", error.toException());
                }
            });
        }catch (Exception e){
            Log.e("Error: ",e.getMessage());
        }

    }

    private void uploadImage() {
         Log.d("state","state"+Area);

        if(imageuri != null){
            final ProgressDialog progressDialog = new ProgressDialog(chef_postDish.this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();
            RandomUID = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUID);
            ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            restaurant=rname;
                            FoodDetails info = new FoodDetails(dishes,quantity,price,descrption,String.valueOf(uri),RandomUID,ChefId,restaurant);
                            FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("FoodDetails").child(Code).child(Area).child(rname).child(dishes)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            progressDialog.dismiss();
                                            Toast.makeText(chef_postDish.this,"Dish Posted Successfully!",Toast.LENGTH_SHORT).show();
                                            sendNotificationToChef();
                                        }
                                    });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //progressDialog.dismiss();
                    Toast.makeText(chef_postDish.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int) progress+"%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }

    }

    public boolean isValid() {

        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValidDescription = false,isValidPrice=false,isValidQuantity=false,isValid;
        if(TextUtils.isEmpty(descrption)){
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");
        }else{
            desc.setError(null);
            isValidDescription=true;
        }
        if(TextUtils.isEmpty(quantity)){
            qty.setErrorEnabled(true);
            qty.setError("Enter number of Plates or Items");
        }else{
            isValidQuantity=true;
        }
        if(TextUtils.isEmpty(price)){
            pri.setErrorEnabled(true);
            pri.setError("Please Mention Price");
        }else{
            isValidPrice=true;
        }
        isValid = (isValidDescription && isValidQuantity && isValidPrice)?true:false;
        return isValid;
    }

    // intent of the type image
    private void imageChooser()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        imageuri = data.getData();
                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    imageuri);
                            IVPreviewImage.setImageBitmap(
                                    selectedImageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            });

    private void generateAndSaveToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> saveTokenToFirebase(chefId, token))
                .addOnFailureListener(e -> Toast.makeText(chef_postDish.this, "Failed to generate token", Toast.LENGTH_SHORT).show());
    }
    private void saveTokenToFirebase(String chefId, String token) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        usersRef.child(chefId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String restaurantName = dataSnapshot.child("Restaurant").getValue(String.class);
                    if (restaurantName != null) {
                        DatabaseReference chefTokensRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("tokens").child("chef").child(restaurantName);
                        chefTokensRef.child(chefId).setValue(token)
                                .addOnSuccessListener(aVoid -> Toast.makeText(chef_postDish.this, "Token saved successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(chef_postDish.this, "Failed to save token", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(chef_postDish.this, "Restaurant name is null", Toast.LENGTH_SHORT).show();
                        Log.e("chef_postDish", "Restaurant name is null");
                    }
                } else {
                    Toast.makeText(chef_postDish.this, "Chef ID not found in users node", Toast.LENGTH_SHORT).show();
                    Log.e("chef_postDish", "Chef ID not found in users node");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(chef_postDish.this, "Failed to retrieve restaurant name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotificationToChef() {
        String channelId = "chefchannel";
        String channelName = "ChefChannel";

        String title = "Dish Posted!";
        String message = "Your dish has been posted successfully!";

        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Chef Notifications");
            channel.enableLights(true);

            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.train)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            // Show the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0, builder.build());
        }
    }

}
