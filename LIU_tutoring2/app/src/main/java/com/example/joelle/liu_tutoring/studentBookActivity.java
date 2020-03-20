package com.example.joelle.liu_tutoring;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class studentBookActivity extends AppCompatActivity {
    ListView lv;
    Spinner spinner;
    FirebaseAuth mFirebaseAuth;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayList<String> refItems=new ArrayList<String>();
    ArrayList<String> names=new ArrayList<String>();
    ArrayList<String> uids=new ArrayList<String>();
    ArrayList<String> datee=new ArrayList<String>();
    ArrayList<String> idOf=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String course="";
    String uid="";
    String name="";
    String date="";
    String time="";
    boolean exists=false;
    String referencee;
    boolean existss=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book);
        lv=findViewById(R.id.lv1);
        spinner=findViewById(R.id.spin);
        mFirebaseAuth=FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("TutorsBook");
        final DatabaseReference userRef = database.getReference().child("Users");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        lv.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listItems.clear();
                course=spinner.getSelectedItem().toString();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot datas: dataSnapshot.getChildren()){
                            date=datas.getKey();
                            for(DataSnapshot datass: datas.getChildren()){
                                time=datass.getKey();
                                datee.add(date + "/"+time);

                                for(DataSnapshot datasss: datass.getChildren()){
                                    uid=datasss.getKey();
                                    uids.add(uid);
                                }





                            }







                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot datasa: dataSnapshot.getChildren()) {
                            for(int i=0; i<uids.size(); i++) {
                                if(datasa.getKey().equals(uids.get(i)))
                                {
                                    String coursee=datasa.child("Course given").getValue().toString();
                                    if(course.equals(coursee)) {
                                        String fname = datasa.child("First name").getValue().toString();
                                        String lname = datasa.child("Last name").getValue().toString();
                                        name = fname + " " + lname;
                                        for(int j=0;j<listItems.size();j++)
                                        {
                                            if(listItems.get(j).equals(datee.get(i) + System.getProperty("line.separator")+"instructor : " +name.toString()+ "      course : "+course))
                                            {
                                                exists=true;
                                            }
                                        }
                                        if (!exists) {
                                            listItems.add(datee.get(i) + System.getProperty("line.separator")+"instructor : " +name.toString()+ "      course : "+course );
                                            refItems.add(datee.get(i)+"/"+uids.get(i));
                                            names.add(name);
                                            idOf.add(uids.get(i));
                                        }



                                    }
                                }
                            }

                            adapter.notifyDataSetChanged();

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

             referencee =refItems.get(position);
             String nam=names.get(position);
                Intent i = new Intent(studentBookActivity.this,BookSeatActivity.class);
                i.putExtra("references", referencee);
                i.putExtra("name", nam);
                i.putExtra("id", idOf.get(position));
                startActivity(i);
            }
        });

    }
}
