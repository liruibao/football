package com.dugames.tablefootball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dugames.tablefootball.view.FootBallView2;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        FootBallView2 fbv = (FootBallView2) findViewById(R.id.foot_ball_view);
        fbv.shopPoup("");

    }
}
