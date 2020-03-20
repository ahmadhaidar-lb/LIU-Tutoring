package com.example.joelle.liu_tutoring;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class SignUp_Profile2 extends AppCompatActivity {
    private static final int CHOOSE_IMAGE=1;
    ImageView iv;
    Uri uriProfilePic;
    String profileImageUrl;
    Button signUpbtn;
    FirebaseAuth mAuth;
    String type;
    EditText phoneN,birth,firstname,familyname;
    private Bitmap bitmap;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__profile2);
        mAuth=FirebaseAuth.getInstance();
        iv=findViewById(R.id.profilePic2);
        signUpbtn=findViewById(R.id.signUp);
        firstname=findViewById(R.id.fnTv);
        familyname=findViewById(R.id.familyNtv);
        birth=findViewById(R.id.birthTv);
        phoneN=findViewById(R.id.phoneTv);
        spinner=findViewById(R.id.spinner);
        spinner.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        type = intent.getExtras().getString("types");
        if(type.equals("tutor"))
            spinner.setVisibility(View.VISIBLE);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showGallery();

            }
        });

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    saveUserInformation();
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("Users");
                String fname=firstname.getText().toString();
                String lname=familyname.getText().toString();
                String phone=phoneN.getText().toString();
                String birthdate=birth.getText().toString();
                if(fname.isEmpty())
                {
                    firstname.requestFocus();
                    return;
                }
                else if(lname.isEmpty())
                {

                    familyname.requestFocus();
                    return;
                }
                else  if(phone.isEmpty())
                {

                    phoneN.requestFocus();
                    return;
                }
                else if(phone.length()!=8)
                {
                    phoneN.setError("Enter a valid phone number");
                    phoneN.requestFocus();
                }
                else if(birthdate.isEmpty())
                {

                    birth.requestFocus();
                    return;
                }
                else if(spinner.getSelectedItem().toString().equals(null))
                {
                    spinner.requestFocus();

                }
                else {
                    uploadImageToFirebase();
                    FirebaseUser user=mAuth.getCurrentUser();
                    HashMap<Object,String> hashmap=new HashMap<>();
                    hashmap.put("Email",user.getEmail());
                    hashmap.put("First name",fname);
                    hashmap.put("Last name",lname);
                    hashmap.put("Phone number",phone);
                    hashmap.put("Type",type);

                    hashmap.put("Image Url",profileImageUrl);
                    if(type.equals("tutor")) {
                        String course = spinner.getSelectedItem().toString();
                        hashmap.put("Course given",course);
                        hashmap.put("Verified","no");
                    }

                    ref.child(user.getUid()).setValue(hashmap);
                    mAuth.getCurrentUser().sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SignUp_Profile2.this, "SignUp successful, Please Verify your Email Address", Toast.LENGTH_SHORT).show();
                                }
                            });
                    startActivity(new Intent(SignUp_Profile2.this,MainActivity.class));
                }
            }
        });




    }

    private void saveUserInformation() {

        String fname=firstname.getText().toString();
        String lname=familyname.getText().toString();
        String phone=phoneN.getText().toString();
        String birthdate=birth.getText().toString();

        if(fname.isEmpty())
        {
            firstname.requestFocus();
            return;
        }
        else if(lname.isEmpty())
        {

            familyname.requestFocus();
            return;
        }
       else  if(phone.isEmpty())
        {

            phoneN.requestFocus();
            return;
        }
        else if(birthdate.isEmpty())
        {

            birth.requestFocus();
            return;
        }
        else {
            FirebaseUser user=mAuth.getCurrentUser();
            String email=user.getEmail();
            HashMap<Object,String> hashmap=new HashMap<>();
            hashmap.put("email",email);
            hashmap.put("first name",fname);
            hashmap.put("last name",lname);
            hashmap.put("phone number",phone);

        }

    }





    private void uploadImageToFirebase() {

        StorageReference profileImage= FirebaseStorage.getInstance().getReference(mAuth.getCurrentUser().getUid()+".jpg");
        if(uriProfilePic!=null)
        {
            profileImage.putFile(uriProfilePic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    profileImageUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp_Profile2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //to open gallery
   private void showGallery(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),CHOOSE_IMAGE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!=null)
        {
            uriProfilePic=data.getData();



            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfilePic);
            } catch (IOException e) {
                e.printStackTrace();
            }
            iv.setImageBitmap(bitmap);
            uploadImageToFirebase();

        }
    }
}
