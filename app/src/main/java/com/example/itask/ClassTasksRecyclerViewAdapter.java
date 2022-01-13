package com.example.itask;//package com.example.itask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassTasksRecyclerViewAdapter extends RecyclerView.Adapter<ClassTasksRecyclerViewAdapter.TasksViewHolder> {

    Context context;
    ArrayList<ClassTasksModel> tasksList;

    public ClassTasksRecyclerViewAdapter(Context context, ArrayList<ClassTasksModel> tasksList) {
        this.context = context;
        this.tasksList = tasksList;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_each_task, parent, false);
        return new TasksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        final ClassTasksModel tasks = tasksList.get(position);

        holder.taskName.setText(tasks.getTask());
        holder.taskDate.setText(tasks.getDate());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }


    public class TasksViewHolder extends RecyclerView.ViewHolder {

        TextView taskName, taskDate;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            taskName = itemView.findViewById(R.id.tvRetrievedTaskName);
            taskDate = itemView.findViewById(R.id.tvRetrievedTaskDate);


        }
    }

}
