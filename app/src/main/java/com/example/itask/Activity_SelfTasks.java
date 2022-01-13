package com.example.itask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class Activity_SelfTasks extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button btnAddTask;
    private ImageButton imgBtn;
    private TextView tvBack;
    private RecyclerView recyclerView;
    private String strGetTaskTitle, strGettingTaskDate;
    private DatePickerDialog.OnDateSetListener datePicker;

    private DatabaseReference mReference;
    private String mId;

    TextView sampleTextView;
    TextView tvSetDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        tvBack = (TextView) findViewById(R.id.tvBackFunction);
        imgBtn = (ImageButton) findViewById(R.id.id_search);
        recyclerView = (RecyclerView) findViewById(R.id.rView_User_Tasks);
        btnAddTask = (Button) findViewById(R.id.btnAddTask);

        sampleTextView = (TextView) findViewById(R.id.sampleTV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(mId).child("Tasks");

        tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Added only for styling purpose",
                            Toast.LENGTH_SHORT).show();
                }
            });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogBuilder();
                    btnAddTask.setVisibility(View.GONE);
                }
            });





    }



    private void alertDialogBuilder(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.layout_add_task, null);
        dialogBuilder.setView(myView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);

        final EditText edtCreateTask = (EditText) myView.findViewById(R.id.edt_AddTask);
        tvSetDueDate = (TextView) myView.findViewById(R.id.tv_setDueDate);
        Button btnCreateTask = (Button) myView.findViewById(R.id.btnCreateTask);
        TextView tvCancelTask = (TextView) myView.findViewById(R.id.tvCancelTask);

        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strGetTaskTitle = edtCreateTask.getText().toString().trim();
                if (strGetTaskTitle.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Task cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (tvSetDueDate.getText().toString().equals("  Choose Date")){
                    Toast.makeText(getApplicationContext(), "Select due date", Toast.LENGTH_SHORT).show();
                } else {
                    //strGettingTaskTitle
                    //strGettingTaskDate
                    String strId = mReference.push().getKey();
                    ClassTasksModel model = new ClassTasksModel(strGetTaskTitle, strGettingTaskDate, strId);
                    mReference.child(strId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Task added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                String error = task.getException().toString();
                                Toast.makeText(getApplicationContext(), "Error: "+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    alertDialog.dismiss();
                    btnAddTask.setVisibility(View.VISIBLE);
                }
            }
        });

        tvSetDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new ClassDatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

        tvCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                btnAddTask.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Task canceled", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvSetDueDate.setText(" " + currentDate);
        strGettingTaskDate = tvSetDueDate.getText().toString().trim();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ClassTasksModel> recyclerOptions = new FirebaseRecyclerOptions.Builder<ClassTasksModel>()
                .setQuery(mReference, ClassTasksModel.class).build();
        FirebaseRecyclerAdapter<ClassTasksModel, getTasksViewHolder> adapter = new FirebaseRecyclerAdapter<ClassTasksModel,
                getTasksViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull getTasksViewHolder holder, int position, @NonNull ClassTasksModel model) {
                holder.setTaskName(model.getTask());
                holder.setTaskDate(model.getDate());
            }

            @NonNull
            @Override
            public getTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_each_task, parent, false);
                return new getTasksViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    public static class getTasksViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public getTasksViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTaskName(String task) {
            TextView tvRetrievedTasName = mView.findViewById(R.id.tvRetrievedTaskName);
            tvRetrievedTasName.setText(task);
        }

        public void setTaskDate(String task) {
            TextView tvRetrievedTaskDate = mView.findViewById(R.id.tvRetrievedTaskDate);
            tvRetrievedTaskDate.setText(task);

        }
    }



}
