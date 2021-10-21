package com.example.brighterlife.page;

import android.os.Build;
import android.os.Bundle;

import com.example.brighterlife.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class addnotes extends AppCompatActivity {
FirebaseFirestore fstore;
FirebaseUser user;
    Calendar c;
    String todaysDate;



    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fstore =FirebaseFirestore.getInstance();
        EditText noteTitle,noteContent;
        ImageView frustrated,happy,sad,stressed;
        ProgressBar progressBarSave;
        noteContent=findViewById(R.id.addNoteContent);
        noteTitle=findViewById(R.id.EditPageTitle);
        frustrated=findViewById(R.id.frustrated);
        happy=findViewById(R.id.happy);
        sad=findViewById(R.id.sad);
        stressed=findViewById(R.id.stressed);
        progressBarSave=findViewById(R.id.progressBar);
        user= FirebaseAuth.getInstance().getCurrentUser();
        final int[] image=new int[1];
        frustrated.setOnClickListener((v)->{
            image[0]= 0;
            v.setOutlineAmbientShadowColor(0);

        });
        happy.setOnClickListener((v)->{
            image[0]= 1;

        });
        sad.setOnClickListener((v)->{
            image[0]=2;
        });
        stressed.setOnClickListener((v)->{
            image[0]=3;
        });

// set current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);







        FloatingActionButton fab = findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTitle = todaysDate+":"+noteTitle.getText().toString();
                String nContent = noteContent.getText().toString();
                int nimage = image[0];
                String toady=todaysDate;
                if (nContent.isEmpty() || nTitle.isEmpty()) {
                    Toast.makeText(addnotes.this, "Cannot save note with empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBarSave.setVisibility(view.VISIBLE);

                //saving note to firebase
                //journal>>pages>>title,content,date,owner,image
                DocumentReference docref =fstore.collection("Journal").document(user.getUid()).collection("myPages").document();
                Map<String,Object> page = new HashMap<>();
                page.put("title",nTitle);
                page.put("content",nContent);
                page.put("imageId",nimage);
                page.put("date",toady);


                docref.set(page).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(addnotes.this, "page added", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addnotes.this, "Error try again", Toast.LENGTH_SHORT).show();
                        progressBarSave.setVisibility(view.VISIBLE);
                    }
                });








            }});

        getSupportActionBar();
    }

    }
