package com.alrajab.week10_ramchat_sk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    public static final int DEFLT_MSG_LMT = 1000;
    private String mUsername;
    DatabaseReference mRef;
    ChildEventListener mChildEventListener;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser user;

    @BindView(R.id.sendButton) public Button mSendButton;
    @BindView(R.id.messageEditText) public EditText mMessageEditText;
    @BindView(R.id.messageListView) public ListView mLv;

    private MessageAdapter mMessageAdapter;
    private List<RamMessage> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRef = FirebaseDatabase.getInstance().getReference("Chats");
        mFirebaseAuth=FirebaseAuth.getInstance();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        mUsername = ANONYMOUS;

        msgs = new ArrayList<>();

        mMessageAdapter = new MessageAdapter(MainActivity.this,R.layout.note_list_item, msgs);
        mLv.setAdapter(mMessageAdapter);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0)
                    mSendButton.setEnabled(true);
                else
                    mSendButton.setEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFLT_MSG_LMT)});


        mLv.setAdapter(mMessageAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=mMessageEditText.getText().toString();
                mRef.push().setValue(new RamMessage(str,getFirebaseUser(),null));
                mMessageEditText.setText("");

            }
        });


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RamMessage msg= dataSnapshot.getValue(RamMessage.class);
                mMessageAdapter.add(msg);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };




        mRef.addChildEventListener(mChildEventListener);
    }

    public String getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public static String getCurrentTime(){
        SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm:ss");
        return format.format(Calendar.getInstance().getTime());
    }
}