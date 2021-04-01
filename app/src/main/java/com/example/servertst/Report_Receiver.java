package com.example.servertst;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class Report_Receiver extends BroadcastReceiver {
    mineral_predict mineral_predict_;
    upload_activity upload_activity_;
    database_collection database_collection_;
    MainActivity mainActivity;
    reflect_air_predict reflect_air_predict_;
    reflect_predict_water reflect_predict_water_;
    estimate estimate_;
    Measurement measurement_;


    private Activity calledactivity;

    public Report_Receiver(mineral_predict callingactivity){
        this.calledactivity=callingactivity;
        mineral_predict_=callingactivity;
    }

    public Report_Receiver(reflect_air_predict callingactivity){
        this.calledactivity=callingactivity;
        reflect_air_predict_=callingactivity;
    }
    public Report_Receiver(reflect_predict_water callingactivity){
        this.calledactivity=callingactivity;
        reflect_predict_water_=callingactivity;
    }
    public Report_Receiver(MainActivity callingactivity1){
        this.calledactivity=callingactivity1;
        mainActivity=callingactivity1;
    }
    public Report_Receiver(database_collection callingactivity2){
        this.calledactivity=callingactivity2;
        database_collection_=callingactivity2;
    }
    public Report_Receiver(upload_activity callingactivity2){
        this.calledactivity=callingactivity2;
        upload_activity_=callingactivity2;
    }
    public Report_Receiver(estimate callingactivity2){
        this.calledactivity=callingactivity2;
        estimate_=callingactivity2;
    }
    public Report_Receiver(Measurement callingactivity2){
        this.calledactivity=callingactivity2;
        measurement_=callingactivity2;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()== ConnectivityManager.CONNECTIVITY_ACTION){
            boolean noconnectvity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
            if(noconnectvity){
                if(this.calledactivity instanceof upload_activity){
                    upload_activity_.alert(true);
                }
                else if(this.calledactivity instanceof MainActivity){
                    mainActivity.alert(true);
                }
                else if(this.calledactivity instanceof  database_collection){
                    database_collection_.alert(true);
                }
                else if(this.calledactivity instanceof mineral_predict)
                {
                    mineral_predict_.alert(true);
                }
                else if(this.calledactivity instanceof reflect_air_predict)
                {
                    reflect_air_predict_.alert(true);
                }
                else if(this.calledactivity instanceof reflect_predict_water)
                {
                    reflect_predict_water_.alert(true);
                }
                else if(this.calledactivity instanceof estimate)
                {
                    estimate_.alert(true);
                }
                else if(this.calledactivity instanceof Measurement)
                {
                    measurement_.alert(true);
                }
            }
            else{
                if(this.calledactivity instanceof upload_activity){
                    upload_activity_.alert(false);
                }
                else if(this.calledactivity instanceof MainActivity){
                    mainActivity.alert(false);
                }
                else if(this.calledactivity instanceof  database_collection){
                    database_collection_.alert(false);
                }
                else if(this.calledactivity instanceof mineral_predict)
                {
                    mineral_predict_.alert(false);
                }
                else if(this.calledactivity instanceof reflect_air_predict)
                {
                    reflect_air_predict_.alert(false);
                }
                else if(this.calledactivity instanceof reflect_predict_water)
                {
                    reflect_predict_water_.alert(false);
                }
                else if(this.calledactivity instanceof estimate)
                {
                    estimate_.alert(false);
                }
                else if(this.calledactivity instanceof Measurement)
                {
                    measurement_.alert(false);
                }
            }
        }
    }
}
