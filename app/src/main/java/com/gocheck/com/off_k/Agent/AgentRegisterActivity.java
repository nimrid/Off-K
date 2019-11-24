package com.gocheck.com.off_k.Agent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gocheck.com.off_k.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AgentRegisterActivity extends AppCompatActivity {
    private EditText mName, mPhoneNumber, mAddress, mEmail, mPassword, mConfirmPassword;
    private Button mAgentLogin, mAgentRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_register);


//register as Home owner or agent
        loadingDialog = new ProgressDialog(this);
        mAgentLogin = findViewById(R.id.agent_already_have_acc);
        mAgentRegister = findViewById(R.id.agent_register);
        mName = findViewById(R.id.agent_name);
        mPhoneNumber = findViewById(R.id.agent_phone);
        mAddress = findViewById(R.id.agent_address);
        mEmail = findViewById(R.id.agent_email_login);
        mPassword = findViewById(R.id.agent_password_login);
        mConfirmPassword = findViewById(R.id.confirm_agent_password);

        mAuth = FirebaseAuth.getInstance();

        mAgentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgentRegisterActivity.this, AgentLogin.class);
                startActivity(intent);
            }
        });

        mAgentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAgent();
            }
        });
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            Intent intent = new Intent(AgentRegisterActivity.this ,AgentHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
*/
    private void registerAgent() {
        final String inputName = mName.getText().toString();
        final String inputEmail = mEmail.getText().toString();
        final String inputPhoneNumber = mPhoneNumber.getText().toString();
        final String inputAddress = mAddress.getText().toString();
        String inputPassword = mPassword.getText().toString();
        final String inputConfirmPassword = mConfirmPassword.getText().toString();

        if (inputName.isEmpty()){
            mName.setError("Name required");
            mName.requestFocus();
        }

        if (inputEmail.isEmpty()){
            mName.setError("Email required");
            mName.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
            mEmail.setError("Enter Valid Email");
            mEmail.requestFocus();
            return;
        }

        if (inputPhoneNumber.isEmpty()){
            mName.setError("Phone Number required");
            mName.requestFocus();
        }

        if (inputAddress.isEmpty()){
            mName.setError("Address required");
            mName.requestFocus();
        }

        if (inputPassword.isEmpty()){
            mName.setError("Password required");
            mName.requestFocus();
        }

        if (inputConfirmPassword.isEmpty()){
            mName.setError("Password required");
            mName.requestFocus();
        }

        if (!inputPassword.equals(inputConfirmPassword)){
            mConfirmPassword.setError("Password mismatch, retype");
            mConfirmPassword.requestFocus();
        }

        loadingDialog.setTitle("Registering New Agent");
        loadingDialog.setMessage("Please wait, Registering...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        mAuth.createUserWithEmailAndPassword(inputEmail, inputConfirmPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Agent");

                    String agentId = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> agentMap = new HashMap<>();
                    agentMap.put("agentId", agentId);
                    agentMap.put("Name", inputName);
                    agentMap.put("Email", inputEmail);
                    agentMap.put("PhoneNumber", inputPhoneNumber);
                    agentMap.put("Address", inputAddress);
                    agentMap.put("Password", inputConfirmPassword);

                    reference.child(agentId).updateChildren(agentMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        loadingDialog.dismiss();
                                        Toast.makeText(AgentRegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AgentRegisterActivity.this, AgentHomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                            Toast.makeText(AgentRegisterActivity.this, "Network Error, Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}
