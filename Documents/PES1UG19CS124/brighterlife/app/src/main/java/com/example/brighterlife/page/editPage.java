package com.example.brighterlife.page;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.brighterlife.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class editPage extends AppCompatActivity {
Intent data;
EditText editPageTitle,editPageContent;

FirebaseFirestore fStore;
FirebaseUser user;



    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);
        data=getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ImageView frustrated,happy,sad,stressed;
        ProgressBar progressBarSave;
        editPageTitle=findViewById(R.id.EditPageTitle);
        editPageContent=findViewById(R.id.EditpageContent);
        frustrated=findViewById(R.id.frustrated);
        happy=findViewById(R.id.happy);
        sad=findViewById(R.id.sad);
        stressed=findViewById(R.id.stressed);
        progressBarSave=findViewById(R.id.progressBarSave);
        fStore=FirebaseFirestore.getInstance();


        String pageTitle=data.getStringExtra("title");
        String pageContent =data.getStringExtra("content");
        editPageTitle.setText(pageTitle.substring(9));
        editPageContent.setText(pageContent);
        //editPageContent.setBackgroundColor(getResources().getColor(data.getIntExtra("code",0),null));
        final int[] image = {data.getIntExtra("feelings", 0)};


       if(image[0] ==0) {
           frustrated.setOutlineAmbientShadowColor(4);
       }
        if(image[0] ==1) {
            happy.setOutlineAmbientShadowColor(4);
        }
        if(image[0] ==2) {
            sad.setOutlineAmbientShadowColor(4);
        }
        if(image[0] ==3) {
            stressed.setOutlineAmbientShadowColor(4);
        }
        frustrated.setOnClickListener((v)->{
                    image[0] = 1;
                v.setOutlineAmbientShadowColor(0);
          });
        happy.setOnClickListener((v)->{
            image[0] = 1;
            v.setOutlineAmbientShadowColor(0);

        });
        sad.setOnClickListener((v)->{
            image[0] =2;
            v.setOutlineAmbientShadowColor(0);
        });
        stressed.setOnClickListener((v)->{
            image[0] =3;
            v.setOutlineAmbientShadowColor(0);
        });



        FloatingActionButton fab = findViewById(R.id.editNotesave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTitle = editPageTitle.getText().toString();
                String nContent = editPageContent.getText().toString();
                int nimage = image[0];
                if (nContent.isEmpty() || nTitle.isEmpty()) {
                    Toast.makeText(editPage.this, "Cannot save page with empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBarSave.setVisibility(view.VISIBLE);

                //saving note to firebase
                //journal>>pages>>title,content,date,owner,image
                DocumentReference docref = fStore.collection("Journal").document(user.getUid()).collection("myPages").document(data.getStringExtra("pageId"));
                Map<String, Object> page = new HashMap<>();
                page.put("title", nTitle);
                page.put("content", nContent);
                page.put("imageId", nimage);


                docref.update(page).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editPage.this, "page is successfully edited", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editPage.this, "Error try again", Toast.LENGTH_SHORT).show();
                        progressBarSave.setVisibility(view.VISIBLE);
                    }
                });
            }});
    }


}