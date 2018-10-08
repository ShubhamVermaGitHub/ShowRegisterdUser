package b.com.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    RegisterModal registerModal;
    DatabaseReference databaseReference;
    private EditText register_tv_name, register_tv_email_id, register_tv_password, register_tv_mobile;
    private Button update;
    private TextView alradyregister;
    private FirebaseAuth auth;
    private ProgressBar progress_barsignup;
    private String deviceToken;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findItemViewId();
    }

    /*............................find_Item_View_Id............................*/
    private void findItemViewId() {
        //Get Firebase auth instance

        auth = FirebaseAuth.getInstance();
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        databaseReference = FirebaseDatabase.getInstance().getReference("registerdUser");
        register_tv_email_id = findViewById(R.id.register_tv_email_id);
        register_tv_password = findViewById(R.id.register_tv_password);
        register_tv_name = findViewById(R.id.register_tv_name);
        register_tv_mobile = findViewById(R.id.register_tv_mobile);
        update = findViewById(R.id.update);
        alradyregister = findViewById(R.id.alradyregister);
        progress_barsignup = findViewById(R.id.progress_barsignup);
        setOnclickListener();
    }

    private void setOnclickListener() {
        update.setOnClickListener(this);
        alradyregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update:
                final String email = register_tv_email_id.getText().toString().trim();
                final String password = register_tv_password.getText().toString().trim();
                final String mobile = register_tv_mobile.getText().toString().trim();
                final String name = register_tv_name.getText().toString().trim();
                 id = databaseReference.push().getKey();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progress_barsignup.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progress_barsignup.setVisibility(View.GONE);

                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    SessionManager sessionManager = new SessionManager(RegisterActivity.this);
                                    RegisterModal registerModal = new RegisterModal();
                                    registerModal.email = email;
                                    registerModal.password = password;
                                    registerModal.name = name;
                                    registerModal.mobile = mobile;
                                    registerModal.id = id;
                                    registerModal.deviceToken = deviceToken;
                                    registerModal.isRegister = true;
                                    sessionManager.createSession(registerModal);
                                    writeUserData(registerModal);
                                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                    intent.putExtra("registerModal", registerModal);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                break;

            case R.id.alradyregister:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void writeUserData(final RegisterModal registerModal) {
        databaseReference.child("allRegisterUser").child(registerModal.id).setValue(registerModal);
    }
}
