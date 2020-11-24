package com.clusmanl.opentrivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView resultText = findViewById(R.id.resultId);

        if(getIntent().getBooleanExtra("result", false)){
            resultText.setText("Good answer !");
        } else {
            resultText.setText("Bad answer !");
            findViewById(R.id.goodAnswerWasId).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.goodAnswerId)).setText(getIntent().getStringExtra("goodAnswer"));
        }

    }

    public void onClickScreen(View v){
        finish();
    }
}
