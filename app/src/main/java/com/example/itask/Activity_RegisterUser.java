package com.example.itask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private Button BtnSignUp;
    private TextView BtnHaveAnAccount;
    private EditText EdtSignUpEmail, EdtMobileNumber, EdtSignUpPassword, EdtFullName;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_register_user);

        AssigningParameters();

        OnClickListeners();

        AdditionalStyling();


    }




    private void hideStatusBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void AssigningParameters() {
        EdtSignUpEmail = (EditText) findViewById(R.id.edtEmailAddress);
        EdtMobileNumber = (EditText) findViewById(R.id.edtMobileNumber);
        EdtSignUpPassword = (EditText) findViewById(R.id.edtPassword);
        EdtFullName = (EditText) findViewById(R.id.edtName);

        BtnHaveAnAccount = (TextView) findViewById(R.id.btnHaveAnAccount);

        BtnSignUp = (Button) findViewById(R.id.btnSignUp);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

    }


    private void OnClickListeners() {
        BtnSignUp.setOnClickListener(this);
        BtnHaveAnAccount.setOnClickListener(this);
    }


    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Toast.makeText(Activity_RegisterUser.this, "error: "+ e, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnSignUp:
                registerUser();
                hideKeyboard();
                break;

            case R.id.btnHaveAnAccount:
                finish();
                break;


        }
    }

    private void registerUser() {
        String FullName = EdtFullName.getText().toString().trim(),
                Email = EdtSignUpEmail.getText().toString().trim(),
                Number = EdtMobileNumber.getText().toString().trim(),
                Pass1 = EdtSignUpPassword.getText().toString().trim(),
                users_ID = "";

        if(FullName.isEmpty()){
            EdtFullName.setError("Name is required!");
            EdtFullName.requestFocus();
        }

        else if (Email.isEmpty()) {
            EdtSignUpEmail.setError("Email is required!");
            EdtSignUpEmail.requestFocus();
        }

        else if(Number.isEmpty() ){
            EdtMobileNumber.setError("Number is required!");
            EdtMobileNumber.requestFocus();
        }

        else if(Pass1.isEmpty()){
            EdtSignUpPassword.setError("Password is required!");
            EdtSignUpPassword.requestFocus();
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            EdtSignUpEmail.setError("Enter a valid email!");
            EdtSignUpEmail.requestFocus();
        }

        else if (Number.length() < 10) {
            EdtMobileNumber.setError("Enter a valid number!");
            EdtMobileNumber.requestFocus();
        }

        else if (Pass1.length()<8) {
            EdtSignUpPassword.setError("Password length must be atleast 8 characters!");
            EdtSignUpPassword.requestFocus();
        }

        else {
//                Send to fireBase here
            mAuth = FirebaseAuth.getInstance();
            String completeNumber = "+91" + " " + Number;

            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email, Pass1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                ClassUserInfo user = new ClassUserInfo(FullName, Email, completeNumber, Pass1, users_ID);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Activity_RegisterUser.this, "Signed Up Successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);

                                            Intent intent = new Intent(getApplicationContext(), Activity_Main.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            finishAffinity();
                                            startActivity(intent);

                                        } else{
                                            Toast.makeText(Activity_RegisterUser.this, "SignUp failed! " +
                                                    "Please try again after some time", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                String error = task.getException().toString().trim();
                                Toast.makeText(Activity_RegisterUser.this, "SignUp failed! \n" +
                                        error, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }

    }


    private void AdditionalStyling() {
        BtnHaveAnAccount.setPaintFlags(BtnHaveAnAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.themeColor),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}