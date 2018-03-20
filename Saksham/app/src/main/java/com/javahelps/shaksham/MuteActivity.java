package com.javahelps.shaksham;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.javahelps.shaksham.R;

public class MuteActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout trace_aplha  , trace_digits , draw_alpha , draw_digits, prac_gesture, learn_gesture ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mute);
        init();
    }
    private void init() {
        trace_aplha = (LinearLayout)findViewById(R.id.tracing_alpha);
        trace_digits = (LinearLayout)findViewById(R.id.trac_digit);
        draw_alpha = (LinearLayout)findViewById(R.id.draw_alpha);
        draw_digits = (LinearLayout)findViewById(R.id.draw_dig);
        prac_gesture = (LinearLayout)findViewById(R.id.pracGesture);
        learn_gesture = (LinearLayout)findViewById(R.id.learnGesture);
        trace_digits.setOnClickListener(this);
        trace_aplha.setOnClickListener(this);
        draw_digits.setOnClickListener(this);
        draw_alpha.setOnClickListener(this);
        prac_gesture.setOnClickListener(this);
        learn_gesture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tracing_alpha:
                intent = new Intent(MuteActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 3);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;

            case R.id.trac_digit:
                intent = new Intent(MuteActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 1);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;
            case R.id.draw_alpha:
                Log.i("tag", "onClick: draw alpha ");
                intent = new Intent(MuteActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 6);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;
            case R.id.draw_dig:
                Log.i("tag", "onClick: draw digit ");
                intent = new Intent(MuteActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 1);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;
            case R.id.pracGesture:
                Log.i("tag", "onClick: draw digit ");
                intent = new Intent(MuteActivity.this, PracGesture.class);
                intent.putExtra("caller", 7);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;
            case R.id.learnGesture:
                Log.i("tag", "onClick: draw digit ");
                intent = new Intent(MuteActivity.this, LearnGesture.class);
                intent.putExtra("caller", 8);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;
        }

        }
}
