package com.example.joelle.liu_tutoring;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TutorBookActivity extends AppCompatActivity {
    CalendarView cal;
    RadioGroup group;
    String date;
    String time;
    RadioButton r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12;
    Button book;
    long counter=0;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("TutorsBook");
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_book);
        book=findViewById(R.id.book);
        r1=findViewById(R.id.radioButton);
        r2=findViewById(R.id.radioButton2);
        r3=findViewById(R.id.radioButton3);
        r4=findViewById(R.id.radioButton4);
        r5=findViewById(R.id.radioButton5);
        r6=findViewById(R.id.radioButton6);
        r7=findViewById(R.id.radioButton7);
        r8=findViewById(R.id.radioButton8);
        r9=findViewById(R.id.radioButton9);
        r10=findViewById(R.id.radioButton10);
        r11=findViewById(R.id.radioButton11);
        r12=findViewById(R.id.radioButton12);
        group=findViewById(R.id.radioGroup);
        group.setVisibility(View.INVISIBLE);
        cal=findViewById(R.id.calendarView);
        cal.setDate(System.currentTimeMillis(),false,true);
        cal.setMinDate(System.currentTimeMillis()+(1000 * 60 * 60 * 24 ));
        mFirebaseAuth=FirebaseAuth.getInstance();
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date=dayOfMonth+"-"+(month+1)+"-"+year;
                Toast.makeText(TutorBookActivity.this, ""+date, Toast.LENGTH_SHORT).show();
                cal.setVisibility(View.INVISIBLE);
                group.setVisibility(View.VISIBLE);
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(r1.isChecked())
                    time="8 am";
                if(r2.isChecked())
                    time="9 am";
                if(r3.isChecked())
                    time="10 am";
                if(r4.isChecked())
                    time="11 am";
                if(r5.isChecked())
                    time="12 pm";
                if(r6.isChecked())
                    time="1 pm";
                if(r7.isChecked())
                    time="2 pm";
                if(r8.isChecked())
                    time="3 pm";
                if(r9.isChecked())
                    time="4 pm";
                if(r10.isChecked())
                    time="5 pm";
                if(r11.isChecked())
                    time="6 pm";
                if(r12.isChecked())
                    time="6 pm";

            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ref.child(date+"/"+time).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            counter=dataSnapshot.getChildrenCount();

                        if(counter>=3)
                        {
                            Toast.makeText(TutorBookActivity.this, "All places are reserved, Please Choose another time", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            String uid = mFirebaseAuth.getCurrentUser().getUid();

                            String a = date + "/" + time + "/" + uid;
                           // String b = date + "/" + time + "/" + uid+ "/seat";



                            ref.child(a).push().setValue("");
                           // ref.child(b).setValue("");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
                startActivity(new Intent(TutorBookActivity.this,ProfileActivity.class));

            }
        });
    }
}
