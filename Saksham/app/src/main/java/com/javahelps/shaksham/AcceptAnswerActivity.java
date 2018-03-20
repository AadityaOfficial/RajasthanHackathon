package com.javahelps.shaksham;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.javahelps.shaksham.R;

public class AcceptAnswerActivity extends AppCompatActivity {
    ImageView nextBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_answer);

        nextBt = (ImageView)findViewById(R.id.nextButton);
        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callingIntent = getIntent();
                int learn= callingIntent.getIntExtra("learn", 1);
                int caller = callingIntent.getIntExtra("caller", 0);
                int quesNo = callingIntent.getIntExtra("ques", 0);
                int q = callingIntent.getIntExtra("clickQ",0);

                Intent intent = new Intent(AcceptAnswerActivity.this, SpeachQuestionActivity.class);

                intent.putExtra("learn",learn);

                if(caller==1 || caller == 3){
                    intent = new Intent(AcceptAnswerActivity.this, SpeachQuestionActivity.class);
                    intent.putExtra("caller", caller);
                }
                else if(caller==4){
                    intent = new Intent(AcceptAnswerActivity.this, FindObject.class);
                    intent.putExtra("caller", caller);
                }
                intent.putExtra("learn", learn);
                intent.putExtra("ques", (quesNo)%4+1);
                startActivity(intent);
                finish();
            }
        });
    }
}
