package com.example.servertst;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class upload_activity extends AppCompatActivity {

    Report_Receiver broadcast = new Report_Receiver(this);

    ImageView sample_photo;
    Button upload, capture;
    TextInputEditText sample_name, sample_location, reflected, moisture, ifrared, sodium, calcium, magesium;
    private Bitmap image;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    ProgressBar progressBar;
    private int image_choosen = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_activity);


        progressBar = findViewById(R.id.progressbar);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("samples");
        capture = findViewById(R.id.capture_photo);
        sample_photo = findViewById(R.id.sample_pic);
        sample_name = findViewById(R.id.sample_name);
        reflected = findViewById(R.id.reflected_voltage_upload);
        sample_location = findViewById(R.id.sample_location);
        moisture = findViewById(R.id.moisture_upload);
        ifrared = findViewById(R.id.ifrared_upload);
        sodium = findViewById(R.id.sodium_a);
        calcium = findViewById(R.id.calcium_a);
        magesium = findViewById(R.id.magnesium_a);
        upload = findViewById(R.id.upload_data);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image_choosen == 1) {
                    progressBar.setVisibility(View.VISIBLE);
                    final String sample = sample_name.getText().toString().toUpperCase();
                    final String location = sample_location.getText().toString().toUpperCase();
                    final String vol = reflected.getText().toString().toUpperCase();
                    final String moi = moisture.getText().toString().toUpperCase();
                    final String ifra = ifrared.getText().toString().toUpperCase();
                    final String sod = sodium.getText().toString().toUpperCase();
                    final String cal = calcium.getText().toString().toUpperCase();
                    final String mag = magesium.getText().toString().toUpperCase();


                    if (!sample.isEmpty() && !vol.isEmpty() && !location.isEmpty() && !moi.isEmpty()
                            && !ifra.isEmpty()&& !sod.isEmpty()&& !cal.isEmpty()&& !mag.isEmpty()) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                        String id = sample;
                        StorageReference imageref = storageReference.child("samples/" + id);

                        byte[] b = stream.toByteArray();
                        String url = "";
                        imageref.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri download = uri;
                                        upload_data data = new upload_data(sample, location,download.toString(),moi,vol,ifra,sod,mag,cal);
                                        String id_ = sample;
                                        databaseReference.child(id_).setValue(data);
                                    }
                                });
//                                upload_data data = new upload_data(sample_name,location,ac_imp,min_name,)
                                Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                image_choosen = 0;
                                sample_name.setText(null);
                                reflected.setText(null);
                                moisture.setText(null);
                                ifrared.setText(null);
                                sample_location.setText(null);
                                sodium.setText(null);
                                magesium.setText(null);
                                calcium.setText(null);
                                sample_photo.setImageResource(R.drawable.ic_add_sample_pic);
                                sample_name.requestFocus();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                                Toast.makeText(getApplicationContext(), "Uploaded Failed", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Check... Some Fields are Missing..", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Check If You took picture of sample", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            image_choosen = 1;
            image = (Bitmap) data.getExtras().get("data");
            sample_photo.setImageBitmap(image);

        }
    }
    Snackbar snackbar;
    boolean first =false;
    public  void alert(boolean noconnectivity){
        if(noconnectivity){
            ScrollView dl = findViewById(R.id.upload_scroll);
            snackbar= Snackbar
                    .make(dl,"Check Your Internet....",Snackbar.LENGTH_LONG);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.show();
            Toast.makeText(getApplicationContext(),"Enable Internet! App cannot function since it requires Internet service",Toast.LENGTH_SHORT).show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            first=true;
        }
        else{
            onResume();
            ScrollView dl = findViewById(R.id.upload_scroll);
            if(first){
                snackbar= Snackbar
                        .make(dl,"Internet Connected Back!!!",Snackbar.LENGTH_SHORT);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.setBackgroundTint(Color.parseColor("#FF4E5E30"));
                snackbar.show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(getApplicationContext(),"App Service Enabled",Toast.LENGTH_SHORT).show();

            }
            first=false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcast,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcast);
    }
}
