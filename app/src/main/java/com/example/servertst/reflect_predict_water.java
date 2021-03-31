package com.example.servertst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class reflect_predict_water extends AppCompatActivity {


    Report_Receiver broadcast = new Report_Receiver(this);

    TextInputEditText editText;
    TextView result, progress;
    Button predict, refresh, connect;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect_predict_water);

        editText = findViewById(R.id.impedance_value_water);
        result = findViewById(R.id.result_mineral_water);
        predict = findViewById(R.id.predict_water);
        refresh = findViewById(R.id.refresh_water);
        progress = findViewById(R.id.progress_text_water);
        progressBar = findViewById(R.id.processing_water);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(null);
                result.setText("");
                editText.requestFocus();
            }
        });

        requestQueue = Volley.newRequestQueue(reflect_predict_water.this);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                final String data = editText.getText().toString();
                String Url = "https://mineral-predict.herokuapp.com/predict_water";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("data_i", data);
                    jsonObject.put("min_name", "");
                    Toast.makeText(getApplicationContext(),jsonObject.getString("data_i"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, Url, jsonObject, new
                        Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String x = "";
                                String y = "";
                                try {
                                    x = response.getString("min_name");
                                    y = response.getString("data_i");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String res = "The Mineral corresponding to Reflection Coefficient of " + y + " is " + x.substring(2, x.length() - 2);
                                result.setText(res);
                                progress.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Please Try again"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(request1);
            }
        });



    }
    Snackbar snackbar;
    boolean first =false;
    public  void alert(boolean noconnectivity){
        if(noconnectivity){
            LinearLayout dl = findViewById(R.id.linear_mineral);
            snackbar= Snackbar
                    .make(dl,"Check Your Internet....",Snackbar.LENGTH_LONG);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
            snackbar.show();
            Toast.makeText(getApplicationContext(),"Enable Internet! App cannot function since it requires Internet service",Toast.LENGTH_SHORT).show();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            first=true;
        }
        else{
            onResume();
            LinearLayout dl = findViewById(R.id.linear_mineral);
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
