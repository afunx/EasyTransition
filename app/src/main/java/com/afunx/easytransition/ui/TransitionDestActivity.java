package com.afunx.easytransition.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afunx.easytransition.R;
import com.afunx.easytransition.model.TransitionItems;

public class TransitionDestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_dest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra("id", -1);
        String title = TransitionItems.ITEMS_MAP.get(id).getItemName();
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
