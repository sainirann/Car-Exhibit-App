package com.example.cardetailsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Register user activity
 */
public class RegistrationActivity extends AppCompatActivity {

    UserInformationDB userInformationDB;
    EditText name, password, email, phone, retypePassword;
    Button btnSubmit;

    //Convenience variables for quick look up of values
    String nameVal, passwordVal, emailVal, phoneVal, retypePasswordVal;

    public static final String IS_SIGNUP_CONTEXT = "isUserSignedUp";
    public static final String USER_NAME = "Username";
    public static final String PASSWORD = "Pwd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        userInformationDB = new UserInformationDB(this);
        btnSubmit = findViewById(R.id.signupbutton);
        btnSubmit.setOnClickListener(v -> {
            setValuesOfUser();
            if (validateData()) {
                boolean isDataInserted = userInformationDB.insertData(name.getText().toString(), password.getText().toString(),
                        email.getText().toString(), phone.getText().toString());
                if (isDataInserted) {
                    Intent ind = new Intent(getApplicationContext(), MainActivity.class);
                    ind.putExtra(USER_NAME, nameVal);
                    ind.putExtra(PASSWORD, passwordVal);
                    ind.putExtra(IS_SIGNUP_CONTEXT, "Yes");
                    startActivity(ind);
                } else {
                    Toast.makeText(getApplicationContext(), "Existing User", Toast.LENGTH_LONG).show();
                    resetValues();
                }
            }
        });
    }

    /**
     * Reset all the fields if the user has already registered
     */
    public void resetValues() {
        name.setText("");
        password.setText("");
        retypePassword.setText("");
        password.setText("");
        email.setText("");
        phone.setText("");
    }

    /**
     * The values in all the fields are stored in corresponding string member fields
     */
    public void setValuesOfUser() {
        name = findViewById(R.id.usernamesignup);
        nameVal = name.getText().toString();
        password = findViewById(R.id.signuppassword);
        passwordVal = password.getText().toString();
        retypePassword = findViewById(R.id.signuprepassword);
        retypePasswordVal = retypePassword.getText().toString();
        email = findViewById(R.id.emailid);
        emailVal = email.getText().toString();
        phone = findViewById(R.id.phone);
        phoneVal = phone.getText().toString();
    }

    /**
     *The fields in the registration page are validated. All the fields are mandatory. Email ID and phone number should be
     *     in appropriate format. Username must be unique and the password should be between 6 to 32 characters.
     */
    public boolean validateData() {
        boolean isValid = true;
        if (nameVal.isEmpty()) {
            name.setError("Please enter username!!!");
            isValid = false;
        }
        if (passwordVal.isEmpty()) {
            password.setError("Please enter password!!!");
            isValid = false;
        } else if (password.length() < 6 || password.length() > 32) {
            password.setError("Password length should be between 6 and 32");
            isValid = false;
        }
        if (retypePasswordVal.isEmpty()) {
            retypePassword.setError("Please enter re-enter password!!!");
            isValid = false;
        } else if (retypePassword.length() < 6 || retypePassword.length() > 32) {
            password.setError("Password length should be between 6 and 32");
            isValid = false;
        }
        if (emailVal.isEmpty()) {
            email.setError("Please enter email id!!!");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()) {
            email.setError("Invalid Email ID");
            isValid = false;
        }

        if (phoneVal.isEmpty()) {
            phone.setError("Please enter phone number!!!");
            isValid = false;
        } else if (!Pattern.compile("[1-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]").matcher(phoneVal).matches()) {
            phone.setError("Invalid Phone Number!!!");
            isValid = false;
        }

        if (!passwordVal.isEmpty() && !retypePasswordVal.isEmpty() && !passwordVal.equals(retypePasswordVal)) {
            retypePassword.setError("Password Mismatch");
            isValid = false;
        }
        return isValid;
    }
}

