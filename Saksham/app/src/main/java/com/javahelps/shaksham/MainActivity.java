package com.javahelps.shaksham;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    LinearLayout ll_deaf, ll_mute, ll_blind ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("", "Refreshed token: " + refreshedToken);
    }

    private void init() {
        ll_deaf = (LinearLayout)findViewById(R.id.deaf);
        ll_mute = (LinearLayout)findViewById(R.id.mute);
        ll_blind = (LinearLayout)findViewById(R.id.blind);

        ll_mute.setOnClickListener(this);
        ll_deaf.setOnClickListener(this);
        ll_blind.setOnClickListener(this);
    }

    Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deaf:
                intent = new Intent(MainActivity.this, DeafActivity.class);
                startActivity(intent);
                break;
            case R.id.mute:
                intent = new Intent(MainActivity.this, MuteActivity.class);
                startActivity(intent);
                break;
            case R.id.blind:
                intent = new Intent(MainActivity.this, BlindActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
        super.onBackPressed();
    }
}
