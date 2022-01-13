package com.example.itask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity_UsersTasks extends AppCompatActivity implements Serializable {

    private TextView tvUsersBackFunction, tvNoData;
    private ProgressBar progressBar;
    private String u_keys;

    private DatabaseReference mReference;

    private RecyclerView usersRecyclerView;
    private ClassTasksRecyclerViewAdapter UsersAdapter;
    private ArrayList<ClassTasksModel> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_tasks);

        tvUsersBackFunction = (TextView) findViewById(R.id.tvUsersBackFunction);
        usersRecyclerView = (RecyclerView) findViewById(R.id.rView_Users_Tasks);
        progressBar = (ProgressBar) findViewById(R.id.progressBarUserTasks);
        tvNoData = (TextView) findViewById(R.id.tvNoData);

        u_keys = getIntent().getStringExtra("Users_Unique_Key");
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(u_keys).child("Tasks");

        AdditionalStyling();

        recyclerViewData();

        tvUsersBackFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void recyclerViewData() {
        progressBar.setVisibility(View.VISIBLE);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksList = new ArrayList<>();
        UsersAdapter = new ClassTasksRecyclerViewAdapter(this, tasksList);
        usersRecyclerView.setAdapter(UsersAdapter);

        mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    ClassTasksModel tasksModel = dataSnapshot.getValue(ClassTasksModel.class);
                    tasksList.add(tasksModel);
                    progressBar.setVisibility(View.GONE);
                }
                UsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    private void AdditionalStyling() {
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.themeColor),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }


}





