package com.javahelps.shaksham;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class LearnGesture extends AppCompatActivity {

    ImageView img;
    HashMap<Integer, Model> questions;
    int caller, q, learn, quesNo;
    TextView title;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_gesture);
        img = (ImageView)findViewById(R.id.imageView3);
        questions = new HashMap<>();
        populateQuestions();

        title = (TextView)findViewById(R.id.Title3);
        submitButton = (Button)findViewById(R.id.submitButton3);

        caller = getIntent().getIntExtra("caller", 0);
        q = getIntent().getIntExtra("clickQ",0);
        learn = getIntent().getIntExtra("learn",0);
        quesNo = getIntent().getIntExtra("ques", 1);

        title.setText(questions.get(quesNo).question.toString());
        img.setImageResource(Integer.parseInt(questions.get(quesNo).answer));
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnGesture.this, LearnGesture.class);
                intent.putExtra("ques", quesNo+1);
                startActivity(intent);
                finish();
            }
        });
    }

    private void populateQuestions() {
        Model model = new Model();
        model.question = "This is two.";
        model.answer = R.drawable.two + "";
        questions.put(1, model);
        model = new Model();
        model.question = "This is up";
        model.answer = R.drawable.up + "";
        questions.put(2, model);
        model = new Model();
        model.question = "This is down";
        model.answer = R.drawable.down + "";
        questions.put(3, model);
    }
}
