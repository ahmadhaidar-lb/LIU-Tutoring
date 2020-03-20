package com.example.joelle.liu_tutoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp_Profile extends AppCompatActivity {

    Button Tutorbtn,Studentbtn;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__profile);
        Tutorbtn=findViewById(R.id.tutor);
        Studentbtn=findViewById(R.id.student);



        /*FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Users");
        FirebaseUser user=mAuth.getCurrentUser();
        hashmap.put("type","tutor");
        ref.child(user.getUid()).setValue(hashmap);

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Users");
        FirebaseUser user=mAuth.getCurrentUser();
        hashmap.put("type","student");
        ref.child(user.getUid()).setValue(hashmap);
*/
        Tutorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="tutor";
                Intent i = new Intent(SignUp_Profile.this,SignUp_Profile2.class);
                i.putExtra("types", type);
                startActivity(i);

            }
        });
        Studentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="student";
                Intent i = new Intent(SignUp_Profile.this,SignUp_Profile2.class);
                i.putExtra("types", type);
                startActivity(i);
            }
        });

    }
}
