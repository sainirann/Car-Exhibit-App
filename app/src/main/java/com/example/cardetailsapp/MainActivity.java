package com.example.cardetailsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Main login/signing up activity
 */
public class MainActivity extends AppCompatActivity {

    Button signUpPage, signInPage;
    EditText userName, password;
    UserInformationDB db;

    String nameVal = "";
    String passwordVal = "";

    public static final String IS_SIGNUP_CONTEXT = "isUserSignedUp";
    public static final String USER_NAME = "Username";
    public static final String PASSWORD = "Pwd";
    public static final String NAME = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new UserInformationDB(this);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signUpPage = findViewById(R.id.register);
        signInPage = findViewById(R.id.submit);
        Intent i = getIntent();
        if (i.hasExtra(IS_SIGNUP_CONTEXT)) {
            String indicator = i.getStringExtra(IS_SIGNUP_CONTEXT);
            if (indicator != null && indicator.equals("Yes")) {
                userName.setText(i.getStringExtra(USER_NAME));
                password.setText(i.getStringExtra(PASSWORD));
            }
        }

        signUpPage.setOnClickListener(v -> openSignUpPageActivity());
        signInPage.setOnClickListener(v -> openSignIn());
    }

    /**
     * Activate registration page new user
     */
    public void openSignUpPageActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     *  For the existing user, the credentials would be cross verified with database
     *     and if the user is the authenticated person, it will redirect to Welcome page
     *      If the user is not an authenticated person, they will pop up an error message
     */
    public void openSignIn() {
        if (validateData()) {
            if (db.queryData(nameVal, passwordVal)) {
                Intent intent = new Intent(this, CarSelectionActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                password.setText("");
            }
        }
    }

    /**
     * Validate the username and password data
     * @return
     */
    public boolean validateData() {
        nameVal = userName.getText().toString();
        passwordVal = password.getText().toString();
        boolean isValid = true;
        if (nameVal.isEmpty()) {
            userName.setError("Please enter username!!!");
            isValid = false;
        }
        if (passwordVal.isEmpty()) {
            password.setError("Please enter password!!!");
            isValid = false;
        } else if (password.length() < 6 || password.length() > 32) {
            password.setError("Password length should be between 6 and 32");
            isValid = false;
        }
        return isValid;
    }

    public void passwordVisibility(View view) {
        ImageView visibility = findViewById(view.getId());
        if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            visibility.setImageResource(R.drawable.ic_visibility_black_24dp);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            visibility.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}

