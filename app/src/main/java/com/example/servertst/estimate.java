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

public class estimate extends AppCompatActivity {

    Report_Receiver broadcast = new Report_Receiver(this);

    TextInputEditText editText1, editText2, editText3;
    TextView result1,result2,result3, progress;
    Button predict, refresh, connect;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);

        editText1 = findViewById(R.id.reflected_voltage);
        editText2 = findViewById(R.id.moisture_voltage);
        editText3 = findViewById(R.id.infrared_voltage);
        result1 = findViewById(R.id.sodium);
        result2 = findViewById(R.id.MAG);
        result3 = findViewById(R.id.calcium);
        predict = findViewById(R.id.estimate_p);
        refresh = findViewById(R.id.estimate_r);
        progress = findViewById(R.id.progress_text_estimate);
        progressBar = findViewById(R.id.processing_estimate);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1.setText(null);
                editText2.setText(null);
                editText3.setText(null);
                result1.setText("");
                result2.setText("");
                result3.setText("");
                editText1.requestFocus();
            }
        });

        requestQueue = Volley.newRequestQueue(estimate.this);

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                final String data1 = editText1.getText().toString();
                final String data2 = editText2.getText().toString();
                final String data3 = editText3.getText().toString();
                String Url = "https://mineral-predict.herokuapp.com/estimate";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("reflected", data1);
                    jsonObject.put("moisture", data2);
                    jsonObject.put("infrared", data3);
                    jsonObject.put("Sodium", "");
                    jsonObject.put("Magnesium", "");
                    jsonObject.put("Calcium", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, Url, jsonObject, new
                        Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String na = "";
                                String p = "";
                                String ca = "";

                                try {
                                    na = response.getString("Sodium");
                                    p = response.getString("Magnesium");
                                    ca = response.getString("Calcium");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String res1 = "Sodium : " + na;
                                String res2 = "Magnesium : " + p;
                                String res3 = "Calcium : " + ca;
                                Toast.makeText(estimate.this, res1+" "+res2+" "+res3, Toast.LENGTH_SHORT).show();
                                result1.setText(res1);
                                result2.setText(res2);
                                result3.setText(res3);
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
            LinearLayout dl = findViewById(R.id.linear_estimate);
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
            LinearLayout dl = findViewById(R.id.linear_estimate);
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
