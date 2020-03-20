package com.example.joelle.liu_tutoring;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    EditText emailIn,passwordIn;
    Button signIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    String type="";
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth=FirebaseAuth.getInstance();
        emailIn=findViewById(R.id.email);
        passwordIn=findViewById(R.id.password);
        signIn=findViewById(R.id.signIn);
        tvSignUp=findViewById(R.id.signUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp_Form.class));
            }
        });


        // bshufon ba3den
        /*mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null && email verified...)
                {
                    Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                }
                else {
                    Toast.makeText(MainActivity.this, "please Login" , Toast.LENGTH_SHORT).show();
                }
            }
        }; */



        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailIn.getText().toString();
                String password=passwordIn.getText().toString();
                if(email.equals("admin@liu.edu.lb")&&password.equals("administration"))
                {
                    startActivity(new Intent(MainActivity.this, AdministrationActivity.class));
                }
                else
                if(email.isEmpty())
                {
                    emailIn.setError("Please enter your LIU-Email address");
                    emailIn.requestFocus();

                }
                else if(password.isEmpty())
                {
                    passwordIn.setError("Please enter your password");
                    passwordIn.requestFocus();

                }


                else{
                    mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                if(mFirebaseAuth.getCurrentUser().isEmailVerified()) {

                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    final DatabaseReference ref = database.getReference("Users");


                                    ref.child(mFirebaseAuth.getCurrentUser().getUid()).child("Type").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            type = dataSnapshot.getValue().toString();
                                            if(type.equals("tutor"))
                                            {
                                                ref.child(mFirebaseAuth.getCurrentUser().getUid()).child("Verified").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String verified = dataSnapshot.getValue().toString();
                                                        if(verified.equals("yes"))
                                                        {
                                                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                                        }
                                                        else{
                                                            Toast.makeText(MainActivity.this, "You are not verified yet by administration", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                            else {
                                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            System.out.println("The read failed: " + databaseError.getCode());
                                        }
                                    });

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Please verify your Email address", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(MainActivity.this, "Check your email and password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
