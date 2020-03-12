package com.example.realapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String Userid;
    EditText user,email;
    Button btnsubbmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.nametxt);
        email = findViewById(R.id.emailtxt);
        btnsubbmit = findViewById(R.id.btnsubmit);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseInstance.setPersistenceEnabled(true);
        mFirebaseDatabase = mFirebaseInstance.getReference("DataUsers");
        mFirebaseDatabase.keepSynced(true);//syncing data offline
        //show database connection status
        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("disconnectmessage");
// Write a string when this client loses connection
        presenceRef.onDisconnect().setValue("I disconnected!");



        Userid = mFirebaseDatabase.push().getKey();
    }

    public void addUser(String username, String email){
        User users = new User(username,email);
        mFirebaseDatabase.child("Users").child(Userid).setValue(users);
    }

    public void updateUser(String username, String email){

        mFirebaseDatabase.child("Users").child(Userid).child("username").setValue(username);
        mFirebaseDatabase.child("Users").child(Userid).child("email").setValue(email);
    }
    public void insertData(View view){
        addUser(user.getText().toString().trim(),email.getText().toString().trim());
    }
    public void updateData(View view){
        updateUser(user.getText().toString().trim(),user.getText().toString().trim());
    }

    public void readData(View view){
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String dbuser = ds.child("username").getValue(String.class);
                        String dbemail = ds.child("email").getValue(String.class);
                        Toast.makeText(MainActivity.this,dbuser+"/"+dbemail, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
