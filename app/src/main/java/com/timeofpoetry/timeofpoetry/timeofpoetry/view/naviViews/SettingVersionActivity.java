package com.timeofpoetry.timeofpoetry.timeofpoetry.view.naviViews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.NetworkTasks;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.concerns.SharedPreferenceController;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.MainActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

public class SettingVersionActivity extends AppCompatActivity {
    private SharedPreferenceController msp;
    private NetworkTasks.Setting setting;
    private EditText poet;
    private EditText poem;
    private EditText season;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_version);
        setting = new NetworkTasks.Setting(this);
        msp = new SharedPreferenceController(getApplicationContext());
        poet = (EditText) findViewById(R.id.setting_version_poet);
        poem = (EditText) findViewById(R.id.setting_version_poem);
        season = (EditText) findViewById(R.id.setting_version_season);

        final Button update = (Button) findViewById(R.id.setting_version_update);

        loginVisibility();

        findViewById(R.id.setting_version_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.setting_version_logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msp.setLogin(false);
                onBackPressed();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원정보 업데이트
                setting.infoUpdate();
            }
        });

        season.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    update.callOnClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void loginVisibility(){
        View view = findViewById(R.id.setting_version_login);
        if(msp.isLogin()){
            view.setVisibility(View.VISIBLE);
            TextView myId = (TextView) findViewById(R.id.setting_version_my_id);
            myId.setText(msp.getUserId());
        }
        else{
            view.setVisibility(View.GONE);
        }
    }

    public String[] getData(){
        return new String[] {poet.getText().toString(), poem.getText().toString(), season.getText().toString()};
    }

    public void networkFail(){
        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다", Toast.LENGTH_SHORT).show();
    }

    public void successUpdate(){
        Toast.makeText(getApplicationContext(), "정보 업데이트에 성공했습니다", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    public void failUpdate(){
        Toast.makeText(getApplicationContext(), "정보 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show();
    }
}
