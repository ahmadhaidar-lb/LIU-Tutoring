package com.example.joelle.liu_tutoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;

public class RatingActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref;
    ImageView iv;
    TextView nameTv,emailTv,rateTv,typeTv;
    StorageReference storageReference;
    String id;
    Button btn;
    double rate=0;
    long counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        btn=findViewById(R.id.rateBtn);
        id=intent.getExtras().getString("id");
        ref=database.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference(id+".jpg");
        iv=findViewById(R.id.profileImage);
        nameTv=findViewById(R.id.nameTv);
        emailTv=findViewById(R.id.emailTv);
        rateTv=findViewById(R.id.rateTv);
        typeTv=findViewById(R.id.typeTv);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference(id+".jpg");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(RatingActivity.this).load(uri.toString()).into(iv);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RatingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String type=""+dataSnapshot.child("Type").getValue().toString();
                typeTv.setText(type);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fname = "" + dataSnapshot.child("First name").getValue().toString();

                nameTv.setText(fname);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lname = "" + dataSnapshot.child("Last name").getValue().toString();

                nameTv.append(" "+lname);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email=""+dataSnapshot.child("Email").getValue().toString();

                emailTv.setText(email);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RatingActivity.this);
                builder.setTitle("Rating");
                btn.setEnabled(false);
               // builder.setMessage("Give a Rate ");
                 final String[] rateNum={"1","2","3","4","5"};
                builder.setItems(rateNum, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        ref.child(id).child("Rate").push().setValue(which+1);
                        ref.child(id+"/Rate").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                counter=dataSnapshot.getChildrenCount();
                                for(DataSnapshot datas: dataSnapshot.getChildren()){
                                    double d=datas.getValue(Double.class);
                                    rate=rate+d/counter;

                                }
                                rateTv.append(" "+rate);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                builder.show();

            }
        });



    }
}
