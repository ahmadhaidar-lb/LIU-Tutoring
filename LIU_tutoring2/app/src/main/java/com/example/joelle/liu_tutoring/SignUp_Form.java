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

public class SignUp_Form extends AppCompatActivity {

    EditText emailIn,passwordIn,password2In;
    Button signUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__form);
        mFirebaseAuth=FirebaseAuth.getInstance();
        emailIn=findViewById(R.id.Email);
        passwordIn=findViewById(R.id.Password);
        password2In=findViewById(R.id.password2);
        signUp=findViewById(R.id.signUp);
        tvSignIn=findViewById(R.id.signUp);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp_Form.this,MainActivity.class));
            }
        });
       // ((EditText)emailIn).getText();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailIn.getText().toString();
                String password=passwordIn.getText().toString();
                String password2=password2In.getText().toString();
                if(email.isEmpty())
                {
                    emailIn.setError("Please enter your LIU-Email address");
                    emailIn.requestFocus();

                }
                else if(password.isEmpty())
                {
                    passwordIn.setError("Please enter a password");
                    passwordIn.requestFocus();

                }
                else if(password.length()<6)
                {
                    passwordIn.setError("Password is too short");
                    passwordIn.requestFocus();
                }
                else if(password2.isEmpty())
                {
                    password2In.setError("repeat your password");
                    password2In.requestFocus();

                }
                else if(!password2.equals(password))
                {
                    password2In.setError("your password doesn't match");
                    password2In.requestFocus();
                    passwordIn.requestFocus();

                }
                else if(!(email.endsWith("@students.b-iu.edu.lb")||email.endsWith("@students.liu.edu.lb")))
                {
                    emailIn.setError("Please enter a valid LIU-Email");
                    emailIn.requestFocus();
                }
                else{
                    mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUp_Form.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {


                                startActivity(new Intent(SignUp_Form.this,SignUp_Profile.class));

                            }
                            else
                            {
                                Toast.makeText(SignUp_Form.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });
    }
}
