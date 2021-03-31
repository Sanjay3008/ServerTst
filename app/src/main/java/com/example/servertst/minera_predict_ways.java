package com.example.servertst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class minera_predict_ways extends AppCompatActivity implements View.OnClickListener {

    Button w1, w2,w3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minera_predict_ways);

        w1=findViewById(R.id.acoustic_impedance_method);
        w2=findViewById(R.id.reflection_coeff_air_method);
        w3=findViewById(R.id.reflection_coeff_water_method);

        w1.setOnClickListener(this);
        w2.setOnClickListener(this);
        w3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.acoustic_impedance_method:
                startActivity(new Intent(this,mineral_predict.class));
                break;
            case R.id.reflection_coeff_air_method:
                startActivity(new Intent(this,reflect_air_predict.class));
                break;
            case R.id.reflection_coeff_water_method:
                startActivity(new Intent(this,reflect_predict_water.class));
                break;

        }
    }
}
