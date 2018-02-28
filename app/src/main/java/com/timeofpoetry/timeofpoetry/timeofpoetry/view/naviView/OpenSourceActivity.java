package com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviView;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityOpenSourceBinding;

public class OpenSourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOpenSourceBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_open_source);
        binding.boardBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
