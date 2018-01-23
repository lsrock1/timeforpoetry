package com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.ActivityAuthorityBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

public class AuthorityActivity extends AppCompatActivity {

    ActivityAuthorityBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authority);

        binding.passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "로그인하지 않습니다", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }
        });

        binding.toSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                intent.putExtra("inorup", false);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_center, R.anim.slide_center_left);
            }
        });

        binding.toSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                intent.putExtra("inorup", true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_center, R.anim.slide_center_left);
            }
        });
    }

    private void startMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "로그인하지 않습니다", Toast.LENGTH_SHORT).show();
        startMainActivity();
    }
}
