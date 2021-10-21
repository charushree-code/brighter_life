
package com.example.brighterlife.page;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.brighterlife.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class journal extends AppCompatActivity {

    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();

        TextView content = findViewById(R.id.journalDetailContent);
        TextView title =findViewById(R.id.journalDetailTitle);
        ImageView contentImageView =findViewById(R.id.contentImageView);

        //data from main activity
        data =getIntent();
        title.setText(data.getStringExtra("title"));
        content.setText(data.getStringExtra("content"));
        content.setBackgroundColor(getResources().getColor(data.getIntExtra("code",0),null));
        contentImageView.setImageResource(data.getIntExtra("feelings",0));
        content.setMovementMethod(new ScrollingMovementMethod());







        FloatingActionButton fab = findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),editPage.class);
                i.putExtra("title",data.getStringExtra("title"));
                i.putExtra("content",data.getStringExtra("content"));
                i.putExtra("code",data.getIntExtra("code",0));
                i.putExtra("feelings",data.getIntExtra("feelings",0));
                i.putExtra("pageId",data.getStringExtra("pageId"));

                startActivity(i);
            }
        });
    }
}