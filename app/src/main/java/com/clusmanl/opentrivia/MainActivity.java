package com.clusmanl.opentrivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickQuickGame(View v){
        Intent intentGame = new Intent(getApplicationContext(), QuestionActivity.class);
        startActivity(intentGame);
    }

    public void onClickCategoryGame(View v){
        Intent intentCategoryGame = new Intent(getApplicationContext(), CategoryListActivity.class);
        startActivity(intentCategoryGame);
    }
}
