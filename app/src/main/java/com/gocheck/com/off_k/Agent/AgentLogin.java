package com.gocheck.com.off_k.Agent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gocheck.com.off_k.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AgentLogin extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private Button mLogin;
    private ProgressDialog loadingDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        loadingDialog = new ProgressDialog(this);
        mEmail = findViewById(R.id.agent_email_login);
        mPassword = findViewById(R.id.agent_password_login);
        mLogin = findViewById(R.id.agent_login);

        mAuth = FirebaseAuth.getInstance();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccount();
            }
        });
    }

    private void LoginAccount() {
        final String inputEmail = mEmail.getText().toString();
        final String inputPassword = mPassword.getText().toString();

        if (!inputEmail.equals("") && !inputPassword.equals("")) {

            loadingDialog.setTitle("Agent Login");
            loadingDialog.setMessage("Please wait, Checking credentials...");
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();

            mAuth.signInWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingDialog.dismiss();
                                Toast.makeText(AgentLogin.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AgentLogin.this, AgentHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }else
            Toast.makeText(AgentLogin.this, "Fill in information", Toast.LENGTH_SHORT).show();
    }
}
