package com.example.lopic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.lopic.Adapter.ItemsAdapter;
import com.example.lopic.Model.InputModel;
import com.example.lopic.Model.JsonData;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Context context;

    //Initialize Variables
    String jsonValue;
    public ItemsAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    //Initialize Views
    @BindView(R.id.rv_items)
    RecyclerView rv_items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        jsonValue = getJsonStringData(context.getResources().openRawResource(R.raw.sample_data));
        JsonData inputModel = new Gson().fromJson(jsonValue, JsonData.class);
        initializeRecyclerView(inputModel);

    }

    private void initializeRecyclerView(JsonData data) {
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new ItemsAdapter(data, context);
        adapter.setHasStableIds(true);
        rv_items.setLayoutManager(linearLayoutManager);
        rv_items.setAdapter(adapter);
    }


    public String getJsonStringData(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (adapter != null) {
            adapter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (adapter != null) {
            adapter.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.button_item);
        Button btn = item.getActionView().findViewById(R.id.button);
        btn.setOnClickListener(view -> {
            adapter.printDataToLogcat();
        });
        return true;
    }

}
