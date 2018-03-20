package com.javahelps.shaksham;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;import android.support.design.widget.FloatingActionButton;

public class StartActivity extends AppCompatActivity {

    FloatingActionButton bt_Next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    private void init() {
        bt_Next = (FloatingActionButton)findViewById(R.id.btNext);
        bt_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);

                startActivity(intent);
            }
        });
    }
}
