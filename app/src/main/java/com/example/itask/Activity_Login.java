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

public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    private EditText EdtSignInEmail, EdtSignInPassword;
    private TextView BtnCreateAccount;
    private Button BtnForgotPassword, BtnSignIn ;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_login);

        AssigningParameters();

        OnClickListeners();

        AlreadyLoggedIn();

        AdditionalStyling();
    }


    private void hideStatusBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void AssigningParameters(){
        EdtSignInEmail = (EditText) findViewById(R.id.edtEmailAddress);
        EdtSignInPassword = (EditText) findViewById(R.id.edtPassword);

        BtnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        BtnSignIn = (Button) findViewById(R.id.btnSignIn);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        BtnCreateAccount = (TextView) findViewById(R.id.btnCreateAccount);

        mAuth = FirebaseAuth.getInstance();

    }

    private void OnClickListeners() {

        BtnCreateAccount.setOnClickListener(this);
        BtnSignIn.setOnClickListener(this);
        BtnForgotPassword.setOnClickListener(this);
    }


    private void AlreadyLoggedIn() {
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Activity_Login.this, Activity_Main.class));
            finish();
        }
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Toast.makeText(Activity_Login.this, "error: "+ e, Toast.LENGTH_SHORT).show();
        }
    }

    private void AdditionalStyling() {
        BtnCreateAccount.setPaintFlags(BtnCreateAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.themeColor),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateAccount:
                Intent signUp = new Intent(Activity_Login.this, Activity_RegisterUser.class);
                startActivity(signUp);
//                finish();
                break;

            case R.id.btnSignIn:
                userLogin();
                hideKeyboard();
                break;

            case R.id.btnForgotPassword:
                Toast.makeText(getApplicationContext(), "Added for styling purpose", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void userLogin() {
        String inEmail = EdtSignInEmail.getText().toString().trim(),
                inPassword = EdtSignInPassword.getText().toString().trim();

        if (inEmail.isEmpty()) {
            EdtSignInEmail.setError(" Email is required!");
            EdtSignInEmail.requestFocus();
        }

        else if(inPassword.isEmpty()){
            EdtSignInPassword.setError("Password is required!");
            EdtSignInPassword.requestFocus();
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(inEmail).matches()) {
            EdtSignInEmail.setError("Enter a valid email!");
            EdtSignInEmail.requestFocus();
        }

        else if (inPassword.length()<8) {
            EdtSignInPassword.setError("Password length must be atleast 8 characters!");
            EdtSignInPassword.requestFocus();
        }

        else{
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(inEmail, inPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(Activity_Login.this, Activity_Main.class );
                                startActivity(intent);
                                finish();
                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(Activity_Login.this, "Failed to login! \n" + error
                                        , Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }


    }
}