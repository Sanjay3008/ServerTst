package com.example.servertst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Measurement extends AppCompatActivity {


    Button refresh,measure;
    TextView moisture, ifrared;
    private DatabaseReference databaseReference_m,databaseReference_i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        databaseReference_m = FirebaseDatabase.getInstance().getReference("sensordata/Moisture");
        databaseReference_i = FirebaseDatabase.getInstance().getReference("sensordata/Infrared");
        moisture=findViewById(R.id.moisture_v);
        ifrared=findViewById(R.id.ifrared_v);
        refresh=findViewById(R.id.refresh_data);
        measure=findViewById(R.id.measure_data);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference_m.setValue("0");
                databaseReference_i.setValue("0");
            }
        });
        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference_m.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String m=snapshot.getValue().toString()+" V";
                        moisture.setText(m);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                    }
                });
                databaseReference_i.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String i=snapshot.getValue().toString()+" V";
                        ifrared.setText(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
