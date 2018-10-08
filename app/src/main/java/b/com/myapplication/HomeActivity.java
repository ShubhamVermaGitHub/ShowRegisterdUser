package b.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ArrayList<RegisterModal> registerModalList;
    private UserListAdapter userListAdapter;
    private TextView name_user;
    private RegisterModal registerModal;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        registerModalList = new ArrayList<>();
        RecyclerView recycler_view = findViewById(R.id.recycler_view);
        name_user = findViewById(R.id.name_user);
        TextView logout = findViewById(R.id.logout);
        sessionManager = new SessionManager(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });

        if (getIntent().getSerializableExtra("registerModal") != null) {
            registerModal = (RegisterModal) getIntent().getSerializableExtra("registerModal");
            Log.i("registerModal", registerModal.name);
            //name_user.setText(registerModal.name);
        }

        userListAdapter = new UserListAdapter(registerModal.name, registerModalList, HomeActivity.this);
        recycler_view.setAdapter(userListAdapter);

        //all register user list
        FirebaseDatabase.getInstance().getReference("registerdUser").child("allRegisterUser").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                RegisterModal newPost = dataSnapshot.getValue(RegisterModal.class);
                assert newPost != null;
                System.out.println("Author: " + newPost.email);
                System.out.println("Title: " + newPost.name);
                System.out.println("Title: " + newPost.id);
                System.out.println("Previous Post ID: " + prevChildKey);
                registerModalList.add(newPost);
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}

