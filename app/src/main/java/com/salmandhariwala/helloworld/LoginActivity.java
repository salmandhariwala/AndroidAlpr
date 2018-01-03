package com.salmandhariwala.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // LogCat tag
    private static final String TAG = LoginActivity.class.getSimpleName();

    EditText loginId;
    EditText password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginId = findViewById(R.id.et_login_id);
        password = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View view) {

        String userName = loginId.getText().toString();
        String pass = password.getText().toString();

        if (userName.equals("admin") && pass.equals("admin")) {
            // start activity
            startActivity(new Intent(this,MainActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Invalid username password!", Toast.LENGTH_LONG).show();
        }

    }


}

