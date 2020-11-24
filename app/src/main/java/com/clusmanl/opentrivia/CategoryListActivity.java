package com.clusmanl.opentrivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryListActivity extends AppCompatActivity {

    private String categoryLabels[];
    private String categoryIds[];
    private static SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        this.getItems();
    }

    /**
     * Get items from the api database
     */
    public void getItems(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // Following code gets the categories from the api
                    URL url = new URL("https://opentdb.com/api_category.php");
                    URLConnection connection = url.openConnection();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String content = "";
                    String inputLine;
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        content += inputLine;
                    }
                    bufferedReader.close();

                    JSONObject output = new JSONObject(content);
                    JSONArray results = output.getJSONArray("trivia_categories");

                    categoryLabels = new String[results.length()];
                    categoryIds = new String[results.length()];

                    for(int i = 0; i < results.length(); i++){
                        categoryLabels[i] = ((JSONObject) results.get(i)).getString("name");
                        categoryIds[i] = String.valueOf(((JSONObject) results.get(i)).getInt("id"));
                    }

                    getListViewAdapter();
                    setListViewAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    /**
     * Get the list view adapter
     */
    public void getListViewAdapter(){
        List<Map<String, String>> itemDataList = new ArrayList<>();

        for(int i = 0; i < categoryLabels.length; i++){
            Map<String,String> listItemMap = new HashMap<>();
            listItemMap.put("name", categoryLabels[i]);
            listItemMap.put("id", categoryIds[i]);
            itemDataList.add(listItemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, itemDataList, android.R.layout.simple_list_item_2,
                new String[]{"name","id"}, new int[]{android.R.id.text1, android.R.id.text2});

        CategoryListActivity.adapter = simpleAdapter;
    }

    /**
     * Sets the list view adapter
     */
    public void setListViewAdapter(){

        runOnUiThread(new Runnable(){

            @Override
            public void run(){

                ListView categoryList = findViewById(R.id.categoryList);
                categoryList.setAdapter(CategoryListActivity.adapter);

                categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                        Object clickItemObj = adapterView.getAdapter().getItem(index);
                        int id = Integer.valueOf(((HashMap) clickItemObj).get("id").toString());

                        Intent intentGame = new Intent(getApplicationContext(), QuestionActivity.class);
                        intentGame.putExtra("category", id);
                        startActivity(intentGame);
                    }
                });
            }
        });
    }
}
