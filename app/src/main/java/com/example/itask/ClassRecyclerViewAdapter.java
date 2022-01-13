package com.example.itask;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassRecyclerViewAdapter extends RecyclerView.Adapter<ClassRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<ClassUserInfo> list;

    public ClassRecyclerViewAdapter(Context context, ArrayList<ClassUserInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_users_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ClassUserInfo user = list.get(position);

        holder.u_name.setText(user.getF_name());
        holder.u_mail.setText(user.getE_mail());
        holder.u_number.setText(user.getMob_number());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_UsersTasks.class);
                intent.putExtra("Users_Unique_Key", user.getU_key());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView u_name, u_mail, u_number;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            u_name = itemView.findViewById(R.id.id_user_name);
            u_mail = itemView.findViewById(R.id.id_user_mail);
            u_number = itemView.findViewById(R.id.id_user_number);

        }
    }
}
