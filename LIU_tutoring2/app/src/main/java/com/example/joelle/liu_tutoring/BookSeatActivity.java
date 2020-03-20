package com.example.joelle.liu_tutoring;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookSeatActivity extends AppCompatActivity {

    ToggleButton btn1,btn2,btn3,btn4;
    Button book;
    long counter=0;
    FirebaseAuth mFirebaseAuth;
    String reference;
    String name;
    String id;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_seat);
        btn1=findViewById(R.id.button);
        btn2=findViewById(R.id.button2);
        btn3=findViewById(R.id.button3);
        btn4=findViewById(R.id.button4);
        book=findViewById(R.id.button5);
        tv=findViewById(R.id.tvProfile);

        mFirebaseAuth=FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("TutorsBook");
        Intent intent = getIntent();
          reference = intent.getExtras().getString("references");
          name=intent.getExtras().getString("name");
          id=intent.getExtras().getString("id");
        tv.setText("Instructor profile : "+name);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookSeatActivity.this,RatingActivity.class);

                i.putExtra("id", id);
                startActivity(i);
            }
        });

        ref.child(reference).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long seatsCounter=dataSnapshot.child("seat").getChildrenCount();
              if(seatsCounter==1)
              {
                  btn4.setEnabled(false);
                  btn4.setChecked(true);
              }
              else if(seatsCounter==2)
              {
                  btn3.setEnabled(false);
                  btn4.setEnabled(false);
                  btn4.setChecked(true);
                  btn3.setChecked(true);
              }
              else if(seatsCounter==3)
              {
                  btn3.setEnabled(false);
                  btn4.setEnabled(false);
                  btn2.setEnabled(false);
                  btn4.setChecked(true);
                  btn3.setChecked(true);
                  btn2.setChecked(true);

              }
              else if(seatsCounter==4)
              {
                  btn3.setEnabled(false);
                  btn4.setEnabled(false);
                  btn2.setEnabled(false);
                  btn1.setEnabled(false);
                  btn4.setChecked(true);
                  btn3.setChecked(true);
                  btn2.setChecked(true);
                  btn1.setChecked(true);

                  Toast.makeText(BookSeatActivity.this, "All Seat are reserved ! Please choose another Time Slot", Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(btn1.isChecked()&&btn1.isEnabled())
               {

                   counter=counter+1;

               }
               else if(btn1.isEnabled()&&!btn1.isChecked()){
                   counter=counter-1; }
           }
       });
        btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(btn2.isChecked()&&btn2.isEnabled())
                {

                    counter=counter+1;

                }
                else if(btn2.isEnabled()&&!btn2.isChecked()){
                    counter=counter-1; }
            }
        });
        btn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(btn3.isChecked()&&btn3.isEnabled())
                {

                    counter=counter+1;

                }
                else if(btn3.isEnabled()&&!btn3.isChecked()){
                    counter=counter-1; }
            }
        });

        btn4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(btn4.isChecked()&&btn4.isEnabled())
                {

                    counter=counter+1;

                }
                else if(btn4.isEnabled()&&!btn4.isChecked()) {
                    counter=counter-1; }
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(reference).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long seatsCounter=dataSnapshot.child("seat").getChildrenCount();
                        if(seatsCounter+counter<=4)
                        {
                         for (int i=0;i<counter;i++)
                          {
                            ref.child(reference + "/seat").push().setValue(mFirebaseAuth.getCurrentUser().getUid());
                            seatsCounter=seatsCounter+1;
                          }

                        }
                        else if(seatsCounter==4)
                            Toast.makeText(BookSeatActivity.this, "All seats are reserved ! please select another section", Toast.LENGTH_SHORT).show();
                        else{
                            Toast.makeText(BookSeatActivity.this, "There is only "+(4-seatsCounter)+ " seats available.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(BookSeatActivity.this,ProfileActivity.class));
            }
        });
    }
}
