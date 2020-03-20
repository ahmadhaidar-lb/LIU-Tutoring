package com.example.joelle.liu_tutoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdministrationActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    ListView lv;
    ArrayList<String> listItems=new ArrayList<String>();
    boolean exists=false;
     ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);
        mFirebaseAuth=FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("Users");
        lv=findViewById(R.id.lv);
       adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        lv.setAdapter(adapter);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String type=datas.child("Type").getValue().toString();
                    if(type.equals("tutor"))
                    {

                    String verification=datas.child("Verified").getValue().toString();
                    if(verification.equals("no")) {
                        String email = datas.child("Email").getValue().toString();
                        String course = datas.child("Course given").getValue().toString();
                        for(int i=0; i<listItems.size(); i++) {
                            if (listItems.get(i).equals(email+System.getProperty("line.separator")+course))
                            {
                                exists=true;
                            }
                        }
                        if(!exists) {
                            listItems.add(email + System.getProperty("line.separator") + course);
                        }
                        adapter.notifyDataSetChanged();

                    }}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
              AlertDialog.Builder builder = new AlertDialog.Builder(AdministrationActivity.this);
              builder.setCancelable(true);
              builder.setTitle("Verification");
              builder.setMessage("Are you sure you want to verify that this user can teach this course ?");
              builder.setPositiveButton("Confirm",
                      new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {

                               String value=listItems.get(position);
                               final String email=value.substring(0,8);
                              ref.addValueEventListener(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      for(DataSnapshot datas: dataSnapshot.getChildren()){
                                          String email1=datas.child("Email").getValue().toString();
                                          if(email1.substring(0,8).equals(email))
                                          {
                                              datas.getRef().child("Verified").setValue("yes");
                                              adapter.notifyDataSetChanged();
                                              startActivity(new Intent(AdministrationActivity.this, AdministrationActivity.class));
                                          }

                                      }
                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                  }
                              });

                          }

                      });
              builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                  }
              });

              AlertDialog dialog = builder.create();
              dialog.show();
              adapter.notifyDataSetChanged();
          }
      });
    }
}
