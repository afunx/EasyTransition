package com.afunx.easytransition.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.widget.Toast;

import com.afunx.easytransition.R;
import com.afunx.easytransition.model.TransitionItems;

public class TransitionListActivity extends AppCompatActivity implements TransitionListFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_list);
    }

    @Override
    public void onItemSelected(int id) {

        TransitionItems.TransitionItem item = TransitionItems.ITEMS_MAP.get(id);

        Toast.makeText(this, item.getClassName(), Toast.LENGTH_SHORT).show();

        Transition transition = item.createTransition();
        transition.setDuration(500);


        Intent intent = new Intent(this, TransitionDestActivity.class);
        intent.putExtra("id", item.getId());
        getWindow().setExitTransition(transition);
        getWindow().setEnterTransition(transition);
        // don't forget ActivityOptions
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
