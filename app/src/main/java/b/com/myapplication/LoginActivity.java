package b.com.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView dontHaveAcoount;

    private EditText login_tv_email_id,login_tv_password;
    private Button login;
    RegisterModal registerModal;
    DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ProgressBar progress_bar;
    private boolean doubleBackToExitPressedOnce;

    SessionManager sessionManager;

    private String body = "";
    private String title;
    private String uid;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findItemViewId();
    }

    /*............................find_Item_View_Id............................*/
    private void findItemViewId() {
        //Get Firebase auth instance

        auth = FirebaseAuth.getInstance();
        databaseReference  = FirebaseDatabase.getInstance().getReference("LoginUser");
        registerModal = new RegisterModal();
        login_tv_email_id = findViewById(R.id.login_tv_email_id);
        login_tv_password = findViewById(R.id.login_tv_password);
        login = findViewById(R.id.login);
        dontHaveAcoount = findViewById(R.id.dontHaveAcoount);
        progress_bar = findViewById(R.id.progress_bar);
        setOnclickListener();


        if (getIntent() != null) {
            body = getIntent().getStringExtra("body");
            title = getIntent().getStringExtra("title");
            uid = getIntent().getStringExtra("uid");
            type = getIntent().getStringExtra("type");
        }

    }
    private void setOnclickListener() {
        login.setOnClickListener(this);
        dontHaveAcoount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                String email = login_tv_email_id.getText().toString().trim();
                final String password = login_tv_password.getText().toString().trim();
//                String id = databaseReference.push().getKey();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progress_bar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progress_bar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        login_tv_password.setError("password atleast 8 digit");
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                break;

            case R.id.dontHaveAcoount:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        sessionManager = new SessionManager(this);
        if(sessionManager.isRegister()){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /*....................onBackPressed()....................................*/
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(getApplicationContext(), "press_back_again", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 1000);
            } else {
                super.onBackPressed();
            }
        }
    }
}
