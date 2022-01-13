package com.example.itask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton btnFab;
    private ImageButton btnMenu, btnSearch, btnSignOut, btnDarkMode;
    private CardView selfCardView;
    private TextView tvAccountName, tvAccountNumber, tvAccountEmail, tvNoDataAvailable;
    private ProgressBar progressBar;

    private FirebaseUser userAccount;
    private DatabaseReference reference;

    private String userAccountID;

    private RecyclerView recyclerView;
    private ClassRecyclerViewAdapter adapter;
    private ArrayList<ClassUserInfo> list;
    private String db_name, db_number, db_email;

    boolean isDark=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_ITask);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssigningParameters();

        AdditionalStyling();

        OnClickListeners();

        FireBaseData();

        recyclerViewData();

        nightModeData();



    }






    private void AssigningParameters() {
        btnMenu = (ImageButton) findViewById(R.id.id_menu);
        btnSearch = (ImageButton) findViewById(R.id.id_search);
        btnSignOut = (ImageButton) findViewById(R.id.id_signout);
        btnDarkMode = (ImageButton) findViewById(R.id.id_darkMode);

        selfCardView = (CardView) findViewById(R.id.id_self_cardView);
        tvAccountName = (TextView) findViewById(R.id.id_self_name);
        tvAccountNumber = (TextView) findViewById(R.id.id_self_number);
        tvAccountEmail = (TextView) findViewById(R.id.id_self_email);

        btnFab = (FloatingActionButton) findViewById(R.id.fabButton);
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerView);

        progressBar = (ProgressBar) findViewById(R.id.progressBarMainPage);
        tvNoDataAvailable = (TextView) findViewById(R.id.tvNoData);

    }

    private void OnClickListeners() {
        btnMenu.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnDarkMode.setOnClickListener(this);
        btnFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.id_signout):
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Activity_Main.this, Activity_Login.class);
                startActivity(intent);
                finish();
                Toast.makeText(Activity_Main.this, "Signed out Successfully", Toast.LENGTH_SHORT).show();
                break;

            case (R.id.fabButton):
                startActivity(new Intent(getApplicationContext(), Activity_SelfTasks.class));
                break;

            case (R.id.id_menu):
            case (R.id.id_search):
                Toast.makeText(getApplicationContext(), "Added for styling purpose", Toast.LENGTH_SHORT).show();

        }
    }


    private void FireBaseData() {
        userAccount = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userAccountID = userAccount.getUid();

        String u_key = userAccountID;
        HashMap<String, Object> updateKey = new HashMap<>();
        updateKey.put("u_key", userAccountID);
        reference.child(u_key).updateChildren(updateKey);

        reference.child(userAccountID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassUserInfo userProfile = snapshot.getValue(ClassUserInfo.class);

                if (userProfile!=null){
                    db_name = userProfile.f_name;
                    db_number = userProfile.mob_number;
                    db_email = userProfile.e_mail;

                    tvAccountName.setText(db_name);
                    tvAccountNumber.setText(db_number);
                    tvAccountEmail.setText(db_email);

                    selfCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), Activity_SelfTasks.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_Main.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });








    }

    private void recyclerViewData() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new ClassRecyclerViewAdapter(this, list);
        recyclerView.setAdapter(adapter);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ClassUserInfo info = dataSnapshot.getValue(ClassUserInfo.class);
                    if(!info.getE_mail().equals(db_email) && dataSnapshot.exists()){
                        list.add(info);
                        tvNoDataAvailable.setText("");
                        progressBar.setVisibility(View.GONE);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        tvNoDataAvailable.setText("No data available");
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }


    private void nightModeData() {
        isDark = getNightMode();
        if (isDark){
            btnDarkMode.setImageResource(R.drawable.icon_day_mode);

        }else {
            btnDarkMode.setImageResource(R.drawable.icon_nights_mode);
        }
        btnFab.setColorFilter(Color.WHITE);

        btnDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDark){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    isDark=false;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    isDark=true;
                }
            }
        });

    }

    private boolean getNightMode(){
        int nightModeFlags = Activity_Main.this.getResources()
                .getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags){
            case Configuration.UI_MODE_NIGHT_YES:
                return true;
            case Configuration.UI_MODE_NIGHT_NO:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                return false;
        }

        return false;
    }


    private void AdditionalStyling() {
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.themeColor),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);

    }

}