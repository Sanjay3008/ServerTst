package com.example.servertst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class database_collection extends AppCompatActivity implements sample_adapter.OnItemClickListener {

    Report_Receiver broadcast = new Report_Receiver(this);

    private RecyclerView recyclerView;
    private sample_adapter adapter;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ProgressBar progressBar;

    private List<upload_data> uploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_collection);

        progressBar = findViewById(R.id.progress_database);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads = new ArrayList<>();
        adapter = new sample_adapter(database_collection.this,uploads);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(database_collection.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("samples");
        firebaseStorage = FirebaseStorage.getInstance();

        valueEventListener= databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploads.clear();
                for(DataSnapshot postsnapshoit: snapshot.getChildren()){
                    upload_data upload_data_item = postsnapshoit.getValue(upload_data.class);
                    upload_data_item.setKey(postsnapshoit.getKey());
                    uploads.add(upload_data_item);
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(database_collection.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    Snackbar snackbar;
    boolean first =false;
    public  void alert(boolean noconnectivity){
        if(noconnectivity){
            RelativeLayout dl = findViewById(R.id.database_relative);
            snackbar= Snackbar
                    .make(dl,"Check Your Internet....",Snackbar.LENGTH_LONG);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
            snackbar.show();
            Toast.makeText(getApplicationContext(),"Enable Internet! App cannot function since it requires Internet service",Toast.LENGTH_SHORT).show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            first=true;
        }
        else{
            RelativeLayout dl = findViewById(R.id.database_relative);
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

    @Override
    public void onItemDelete(int position) {
        upload_data select_item = uploads.get(position);
        final String key = select_item.getKey();

        StorageReference imageref = firebaseStorage.getReferenceFromUrl(select_item.getImageurl());
        imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(key).removeValue();
                Toast.makeText(database_collection.this, "Item Deleted Successfully from Database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        databaseReference.removeEventListener(valueEventListener);
    }
}
