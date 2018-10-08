package b.com.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;

import b.com.myapplication.notification.FcmNotificationBuilder;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference myRef, myRef2;

    FirebaseDatabase database;
    ArrayList<ChetModal> chatModalLisrt;
    RegisterModal getCheteUserDetails;
    private EditText messageArea;
    private ChatAdapter chatAdapter;
    private RecyclerView recycler_view_chat;
    private String userName;
    private ImageView iv_block;
    private DatabaseReference blockedUserRef;
    private String noticiationStaus;
    private SessionManager sessionManager;
    private String otherUserTocan;
    private String isBlocked = "";
    private MessageCountModal messageCountModal;
    private DatabaseReference megCountOther,megCounterMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sessionManager = new SessionManager(this);

        messageArea = findViewById(R.id.messageArea);
        iv_block = findViewById(R.id.iv_block);
        ImageView sendButton = findViewById(R.id.sendButton);
        recycler_view_chat = findViewById(R.id.recycler_view_chat);
        ImageView iv_group_back = findViewById(R.id.iv_group_back);
        iv_group_back.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        iv_block.setOnClickListener(this);
        chatModalLisrt = new ArrayList<>();
        messageCountModal = new MessageCountModal();

        if (getIntent().getSerializableExtra("id") != null) {
            getCheteUserDetails = (RegisterModal) getIntent().getSerializableExtra("id");
        }

        if (getIntent().getStringExtra("userName") != null) {
            userName = getIntent().getStringExtra("userName");
            Log.e("userName", userName);
        }


        if (getIntent().getStringExtra("tocan") != null) {
            otherUserTocan = getIntent().getStringExtra("tocan");
            Log.e("tocan", otherUserTocan);
        }


        database = FirebaseDatabase.getInstance();

        String name1 = userName;
        int myName = name1.charAt(0);

        String name = getCheteUserDetails.name;
        int otherUser = name.charAt(0);

        if (myName > otherUser) {
            blockedUserRef = FirebaseDatabase.getInstance().getReference().child("BlockedUser").child(userName + "_" + getCheteUserDetails.name);
        } else {
            blockedUserRef = FirebaseDatabase.getInstance().getReference().child("BlockedUser").child(getCheteUserDetails.name + "_" + userName);
        }

        // onr to one chatuser
        myRef = database.getReference().child("ChatUsers").child(userName + "_" + getCheteUserDetails.name);
        myRef2 = database.getReference().child("ChatUsers").child(getCheteUserDetails.name + "_" + userName);


        megCountOther = FirebaseDatabase.getInstance().getReference().child("MessgCount").child(userName + "_" + getCheteUserDetails.name);
        megCounterMy = FirebaseDatabase.getInstance().getReference().child("MessgCount").child(getCheteUserDetails.name + "_" + userName);

        MessageCountModal messageCountModal = new MessageCountModal();
        messageCountModal.unread = "0";
        megCountOther.setValue(messageCountModal);
        megCounterMy.setValue(messageCountModal);

        chatAdapter = new ChatAdapter(userName, chatModalLisrt);
        recycler_view_chat.setAdapter(chatAdapter);
        checkUserBlockOrNot();
        getList1();

    }

    private void checkUserBlockOrNot() {

        blockedUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                isBlocked = dataSnapshot.getValue(String.class);

                assert isBlocked != null;
                if (isBlocked.equals("")) {
                    Toast.makeText(ChatActivity.this, "Unblock by:" + userName, Toast.LENGTH_SHORT).show();
                } else if (isBlocked.equals(userName)) {
                    Toast.makeText(ChatActivity.this, "User blocked", Toast.LENGTH_SHORT).show();
                } else if (isBlocked.equals(getCheteUserDetails.name)) {
                    Toast.makeText(ChatActivity.this, "you are blocked by him", Toast.LENGTH_SHORT).show();
                } else if (isBlocked.equals("Both")) {
                    Toast.makeText(ChatActivity.this, "you both blocked each other", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                isBlocked = dataSnapshot.getValue(String.class);

                assert isBlocked != null;
                if (isBlocked.equals("")) {
                    Toast.makeText(ChatActivity.this, "Unblock by:" + userName, Toast.LENGTH_SHORT).show();
                } else if (isBlocked.equals(userName)) {
                    Toast.makeText(ChatActivity.this, "User blocked", Toast.LENGTH_SHORT).show();
                } else if (isBlocked.equals(getCheteUserDetails.name)) {
                    Toast.makeText(ChatActivity.this, "you are blocked by him", Toast.LENGTH_SHORT).show();
                } else if (isBlocked.equals("Both")) {
                    Toast.makeText(ChatActivity.this, "you both blocked each other", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /*..............................................................*/
    private void getList1() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    ChetModal messageOutput = dataSnapshot.getValue(ChetModal.class);
                    chatModalLisrt.add(messageOutput);
                    chatAdapter.notifyDataSetChanged();
                    recycler_view_chat.scrollToPosition(chatModalLisrt.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    ChetModal messageOutput = dataSnapshot.getValue(ChetModal.class);
                    chatModalLisrt.add(messageOutput);
                    chatAdapter.notifyDataSetChanged();
                    recycler_view_chat.scrollToPosition(chatModalLisrt.size() - 1);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("users").child(userName).child("notificationStatus").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (
                        dataSnapshot.getValue() != null) {
                    noticiationStaus = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendButton:
                ChetModal chetModal = new ChetModal();
                chetModal.msg = messageArea.getText().toString().trim();
                chetModal.usename = userName;

                if (isBlocked.equals("")) {
                    if (!chetModal.msg.equals("")) {
                        meassgCount();
                        myRef.push().setValue(chetModal);
                        myRef2.push().setValue(chetModal);
                        messageArea.setText("");
                        sendPushNotificationToReceiver(userName, chetModal.msg, getCheteUserDetails.name, sessionManager.getTocan(), otherUserTocan);
                    }
                } else {

                    Toast.makeText(this, "Sorry you are blocked", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.iv_group_back:
                onBackPressed();
                break;

            case R.id.iv_block:
                pleaseletUsknow(this);
                break;
        }
    }

    private void meassgCount() {


    }
/*

    private void meassgCount() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                MessageCountModal messageCountModal = dataSnapshot.getValue(MessageCountModal.class);

                assert messageCountModal != null;
                Log.w("messageCountModal",messageCountModal.unread);
                int msgCount = Integer.valueOf(messageCountModal.unread);
                msgCount += 1;
                megCountOther.setValue(msgCount);
                msgCount  = 0 ;
                megCounterMy.setValue(msgCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("messageCountModal", "loadPost:onCancelled", databaseError.toException());
            }
        };
        megCounterMy.addValueEventListener(postListener);
    }

*/

    private void sendPushNotificationToReceiver(String name, String message, String
            userID, String token, String otherFirebaseToken) {
        FcmNotificationBuilder.initialize().title(name)
                .message(message).clickaction("ChatActivity")
                .firebaseToken(token)
                .receiverFirebaseToken(otherFirebaseToken)
                .uid(userID).chatNode(userName + "_" + getCheteUserDetails.name)
                .send();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /*........................pleaseletUsknow().............................*/
    public void pleaseletUsknow(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dilog);

        final TextView tv_user = dialog.findViewById(R.id.tv_user);
        TextView chat = dialog.findViewById(R.id.chat);
        TextView block = dialog.findViewById(R.id.block);
        String dialog_heading = "";

        final BlockUserModal blockUserModal = new BlockUserModal();
        if (isBlocked.equals("")) {
            blockUserModal.userName = userName;
            dialog_heading = "you want to block";
        } else if (isBlocked.equals("Both")) {
            blockUserModal.userName = userName;
            dialog_heading = "are you want to unblock";
        } else if (isBlocked.equals(userName)) {
            blockUserModal.userName = "";
            dialog_heading = "are you want to unblock";
        } else if (isBlocked.equals(getCheteUserDetails.name)) {
            blockUserModal.userName = "Both";
            dialog_heading = "Do you want to block this user";
        }

        //UserName.setText(name);
        final String finalDialog_heading = dialog_heading;
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_user.setText(finalDialog_heading);
                blockedUserRef.setValue(blockUserModal);
                dialog.dismiss();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
