package com.example.animesocialapp.loginManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.animesocialapp.R;
import com.example.animesocialapp.mainManagement.MainActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("onClick Login Button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.i("onClick Sign Up Button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signupUser(username, password);
            }
        });
    }

    private void signupUser(String username, String password) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Timber.e("Sign Up Success!");
                    loginUser(username,password);
                } else {
                    Timber.e("Sign Up Failed " + e);
                    Toast.makeText(LoginActivity.this, R.string.sign_up_failed, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loginUser(String username, String password) {
        Timber.i("Attempting to login user: " + username);
        // Calling Log in in background so that the app doesn't stop working while this is taking place
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null){
                    goMainActivity();
                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                } else {
                    Timber.e("Issue with login " + e);
                    if (username.isEmpty() && password.isEmpty()){
                        Toast.makeText(LoginActivity.this, R.string.username_password_empty, Toast.LENGTH_LONG).show();
                    } else if (username.isEmpty()) {
                        Toast.makeText(LoginActivity.this, R.string.username_empty, Toast.LENGTH_LONG).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, R.string.password_empty, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.username_password_incorrect, Toast.LENGTH_LONG).show();
                    }
                    return;
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}