package com.cpunisher.qrcodebeautifier.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.fragment.StyleListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            StyleListFragment styleListFragment = new StyleListFragment();
            styleListFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, styleListFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }
}