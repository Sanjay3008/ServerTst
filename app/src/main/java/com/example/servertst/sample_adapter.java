package com.example.servertst;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class sample_adapter extends RecyclerView.Adapter<sample_adapter.sampleviewholder> {

    private Context context;
    private List<upload_data> upload_dataList;
    private OnItemClickListener mlistener;
    private AlertDialog dialog;

    public sample_adapter(Context context, List<upload_data> upload_dataList)
    {
        this.context = context;
        this.upload_dataList = upload_dataList;
    }
    @NonNull
    @Override
    public sampleviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_item,parent,false);
        return new sampleviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sampleviewholder holder, int position) {
            upload_data upload_data = upload_dataList.get(position);

            holder.t1.setText(upload_data.getSample_name());
            holder.t2.setText(upload_data.getLocation());
            holder.t3.setText(upload_data.getVoltage());
            holder.t4.setText(upload_data.getMoisture());
            holder.t5.setText(upload_data.getIfrared());
            holder.t6.setText(upload_data.getSodium());
            holder.t7.setText(upload_data.getMagesium());
            holder.t8.setText(upload_data.getCalcium());

        Picasso.with(context)
                .load(upload_data.getImageurl())
                .placeholder(R.drawable.ic_loading)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return upload_dataList.size();
    }

    public class sampleviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView t1,t2,t3,t4,t5,t6,t7,t8;
        public ImageView imageView,delete;

        public sampleviewholder(@NonNull View itemView) {
            super(itemView);

            t1=itemView.findViewById(R.id.sample_title);
            t2=itemView.findViewById(R.id.sample_location);
            t3=itemView.findViewById(R.id.VOLTAGE);
            t4=itemView.findViewById(R.id.MOISTURE);
            t5=itemView.findViewById(R.id.InFRARED);
            t6=itemView.findViewById(R.id.sodium_am);
            t7=itemView.findViewById(R.id.magesium_am);
            t8=itemView.findViewById(R.id.calcium_am);
            imageView = itemView.findViewById(R.id.sample_image);
            delete = itemView.findViewById(R.id.delete_data);

            delete.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {
            if(mlistener!=null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Confirm to delete this sample?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int pos = getAdapterPosition();
                                if(pos!=RecyclerView.NO_POSITION)
                                {
                                    mlistener.onItemDelete(pos);
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context,"Item Not Deleted",Toast.LENGTH_SHORT).show();
                            }
                        });

                dialog = builder.create();
                dialog.show();


            }
        }
    }
    public interface OnItemClickListener {
        void onItemDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mlistener = listener;

    }


}
