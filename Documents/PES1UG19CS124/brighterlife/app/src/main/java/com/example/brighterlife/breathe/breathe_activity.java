package com.example.brighterlife.breathe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.brighterlife.MainActivity;
import com.example.brighterlife.R;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.text.MessageFormat;

public class breathe_activity extends AppCompatActivity {
    private ImageView imageView;
    private TextView breaths, time, session, guide;
    private Button startButton;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe_activity);
        imageView = findViewById(R.id.iv_flower);
        breaths = findViewById(R.id.tv_breathsTaken);
        time = findViewById(R.id.tv_lastBreath);
        session = findViewById(R.id.tv_todayMinutes);
        guide = findViewById(R.id.tv_breathe);
        prefs = new Prefs(this);

        startIntroAnimation();

        breaths.setText(MessageFormat.format("{0} breaths", prefs.getBreaths()));
        time.setText("Last at " + prefs.getDate());
        session.setText(MessageFormat.format("{0} minutes", prefs.getSessions()));

        startButton = findViewById(R.id.btn_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
    }

    private void startIntroAnimation() {
        ViewAnimator
                .animate(imageView)
                .translationX(1000, 0)
                .translationY(1000, 0)
                .alpha(0,1)
                .andAnimate(guide)
                .scale(0, 1)
                .duration(1500)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guide.setText("Breathe");
                    }
                })
                .start();
    }

    private void startAnimation() {
        ViewAnimator
                .animate(imageView)
                .alpha(0, 1)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guide.setText("Inhale ... Exhale");
                    }
                })
                .decelerate()
                .duration(1000)
                .thenAnimate(imageView)
                .scale(0.02f, 1.5f, 0.02f)
                //rotate animation clockwise & anti-clockwise later (inhale & exhale)
                .rotation(-360)
                .repeatCount(4)
                .accelerate()
                .duration(5000)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        guide.setText("Excellent Job!");
                        imageView.setScaleX(1.0f);
                        imageView.setScaleY(1.0f);

                        prefs.setBreaths(prefs.getBreaths() + 1);
                        prefs.setDate(System.currentTimeMillis());
                        prefs.setSessions(prefs.getSessions() + 1);

                        //refresh activity
                        new CountDownTimer(2000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                //put code to show ticking ... 1, 2, 3, ...
                            }
                            @Override
                            public void onFinish() {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }.start();
                    }
                }).start();
    }
    }
