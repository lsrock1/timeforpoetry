package com.timeofpoetry.timeofpoetry.timeofpoetry.adapter;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.naviView.SettingVersionViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignInViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignUpViewModel;

import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * databinding에 쓰이는 어댑터들을 모아놓은 클래스
 */

public class Adapter {
    // layout/activity_setting_version editText #like_session
    // 텍스트 에디터에서 키보드 done 버튼을 눌렀을 때 정보 업데이트
    @BindingAdapter("onEditorActionUserInfoUpdate")
    public static void onEditorActionUserInfoUpdate(EditText view, final SettingVersionViewModel viewModel){
        view.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    viewModel.userInfoUpdate();
                    return true;
                }
                return false;
            }
        });
    }

    // layout/fragment_sign_in editText
    // 텍스트 에디터에서 키보드 done 버튼을 눌렀을 때 로그인 작동
    @BindingAdapter("onEditorActionSignIn")
    public static void onEditorAction(EditText view, final SignInViewModel viewModel){
        view.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    viewModel.onSignInBtn();
                    return true;
                }
                return false;
            }
        });
    }

    // layout/fragment_sign_up editText
    // 텍스트 에디터에서 키보드 done 버튼을 눌렀을 때 회원가입 작동
    @BindingAdapter("onEditorActionSignUp")
    public static void onEditorAction(EditText view, final SignUpViewModel viewModel){
        view.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    viewModel.onSignUpBtn();
                    return true;
                }
                return false;
            }
        });
    }

    // layout/fragment_player
    // marquee 기능을 작동시키기 위해
    @BindingAdapter("onSelected")
    public static void onSelected(View view, boolean s){
        view.setSelected(s);
    }

    //원 이미지를 로드할 때는 이 함수 사용
    @BindingAdapter("loadImage")
    public static void loadImage(ImageView view, String url){
        RequestOptions option = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo).transform(new CropCircleTransformation());
        Glide.with(view.getContext())
                .load(url)
                .apply(option)
                .into(view);
    }

    //사각형 이미지를 로드할 때는 이 함수 사용
    @BindingAdapter("loadSquareImage")
    public static void loadSquareImage(ImageView view, String url){
        RequestOptions option = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo);

        Glide.with(view.getContext())
                .load(url)
                .apply(option)
                .into(view);
    }

    @BindingAdapter("setLikeCount")
    public static void setLikeCount(TextView view, int count){
        view.setText(Integer.toString(count));
    }

    @BindingAdapter("setStartIcon")
    public static void setIcon(TextView view, Drawable drawable){
        drawable.setBounds(0, 0, view.getResources().getDimensionPixelSize(R.dimen.f34), view.getResources().getDimensionPixelSize(R.dimen.f60));
        view.setCompoundDrawables(drawable, null, null, null);
    }

    //재생중인 시의 커버에 회색 필터를 씌우는 함수
    @BindingAdapter("setFilter")
    public static void setFilter(ImageView view, Boolean bool) {
        if (bool) {
            view.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        }
        else{
            view.setColorFilter(null);
        }
    }

    @BindingAdapter("mode")
    public static void mode(ImageView view, int mode){
        if(mode == MyPlayListModel.SHUFFLE){
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.player_random_off));
        }
        else if(mode == PlaybackStateCompat.REPEAT_MODE_ALL){
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_repeat_all));
        }
        else if(mode == PlaybackStateCompat.REPEAT_MODE_ONE){
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_repeat_one));
        }
        else if(mode == PlaybackStateCompat.REPEAT_MODE_NONE){
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_repeat_none));
        }
    }

    @BindingAdapter("progressText")
    public static void progressText(TextView view, int progress){
        view.setText(String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(progress),
                TimeUnit.MILLISECONDS.toSeconds(progress) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progress))
        ));
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, float value){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) value;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, float value){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) value;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, int value){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = value;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_weight")
    public static void setLayoutLast(View view, int value) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                params.width,
                params.height,
                (float) value);
        view.setLayoutParams(layoutParams);
    }

    //layout/board_item
    //content text의 변화를 observe해서
    //expandable layout을 초기화하고 보이게 expand 함
    @BindingAdapter("expandableChildText")
    public static void setExpandableChildText(TextView view, String text){
        if(text.equals("")) return;
        view.setText(text);
        ExpandableLinearLayout exView = ((ExpandableLinearLayout) view.getParent());
        exView.initLayout();
        exView.expand();
    }

    //layout/open_source_row
    //expandable layout을 토글
    @BindingAdapter("expandableToggle")
    public static void setExpandableToggle(View view, boolean bool){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExpandableLinearLayout) ((ViewGroup) view.getParent()).getChildAt(1)).toggle();
            }
        });
    }
}
