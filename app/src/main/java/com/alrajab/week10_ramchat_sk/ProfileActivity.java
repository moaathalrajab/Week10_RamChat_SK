package com.alrajab.week10_ramchat_sk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ((TextView)  findViewById(R.id.userName_ID)).setText(getFirebaseUser().getCurrentUser()
                .getDisplayName().toString()+"");

         findViewById(R.id.signoutBtn).setOnClickListener(this::signOut);

        Glide.with(this)
                .load(  getFirebaseUser().getCurrentUser().getPhotoUrl()  )
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(((ImageView)  findViewById(R.id.profilePic)));
    }


    public FirebaseAuth getFirebaseUser() {
        return FirebaseAuth.getInstance();
    }
    public void signOut(View view) {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), FirebaseUIActivity.class));
                    }
                });
        // [END auth_fui_signout]mo
    }


}