package com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.concerns;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by sangroklee on 2017. 11. 8..
 */

public class BackPressCloseHandler {
    private long mBackKeyPressedTime = 0;
    private Toast mToast;
    private Activity mActivity;

    public BackPressCloseHandler(Activity context) { this.mActivity = context; }

    public void onBackPressed() {
        if (System.currentTimeMillis() > mBackKeyPressedTime + 2000){
            mBackKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }

        if (System.currentTimeMillis() <= mBackKeyPressedTime + 2000) {
            mActivity.finish();
            mToast.cancel();
        }
    }

    private void showGuide() {
        mToast = Toast.makeText(mActivity, "뒤로 버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT);
        mToast.show();
    }

}
