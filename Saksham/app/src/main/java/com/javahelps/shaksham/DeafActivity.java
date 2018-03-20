package com.javahelps.shaksham;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.javahelps.shaksham.R;


public class DeafActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout trace_aplha  , trace_digits , draw_digits , find_obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deaf);
        init();
    }

    private void init() {
        trace_aplha = (LinearLayout)findViewById(R.id.tracing_alpha);
        trace_digits = (LinearLayout)findViewById(R.id.trac_digit);
        draw_digits = (LinearLayout)findViewById(R.id.draw_dig);
        find_obj = (LinearLayout)findViewById(R.id.find_obj);

        trace_digits.setOnClickListener(this);
        trace_aplha.setOnClickListener(this);
        draw_digits.setOnClickListener(this);
        find_obj.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tracing_alpha:
                intent = new Intent(DeafActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 3);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;

            case R.id.trac_digit:
                intent = new Intent(DeafActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 1);
                intent.putExtra("learn", 1);
                startActivity(intent);
                break;
            case R.id.draw_dig:
                Log.i("tag", "onClick: draw digit ");
                intent = new Intent(DeafActivity.this, SpeachQuestionActivity.class);
                intent.putExtra("caller", 1);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;
            case R.id.find_obj:
                Log.i("tag", "onClick: find obj ");
                intent = new Intent(DeafActivity.this, FindObject.class);
                intent.putExtra("caller", 4);
                intent.putExtra("learn", 0);
                startActivity(intent);
                break;
        }
    }
}
