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
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.poetryData.LyricsLoad;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignInViewModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.startActivities.SignUpViewModel;

import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by sangroklee on 2017. 12. 11..
 */

public class Adapter {
    @BindingAdapter({"onEditorActionSignIn"})
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

    @BindingAdapter({"onEditorActionSignUp"})
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

    @BindingAdapter({"onSelected"})
    public static void onSelected(View view, boolean s){
        view.setSelected(true);
    }

    @BindingAdapter({"loadImage"})
    public static void loadImage(ImageView view, String url){
        RequestOptions option = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo).transform(new CropCircleTransformation());
        Glide.with(view.getContext())
                .load(url)
                .apply(option)
                .into(view);
    }

    @BindingAdapter({"loadSquareImage"})
    public static void loadSquareImage(ImageView view, String url){
        RequestOptions option = new RequestOptions().placeholder(R.drawable.logo).error(R.drawable.logo);

        Glide.with(view.getContext())
                .load(url)
                .apply(option)
                .into(view);
    }

    @BindingAdapter({"setLikeCount"})
    public static void setLikeCount(TextView view, int count){
        view.setText(Integer.toString(count));
    }

    @BindingAdapter({"setStartIcon"})
    public static void setIcon(TextView view, Drawable drawable){
        drawable.setBounds(0, 0, view.getResources().getDimensionPixelSize(R.dimen.f34), view.getResources().getDimensionPixelSize(R.dimen.f60));
        view.setCompoundDrawables(drawable, null, null, null);
    }

    @BindingAdapter({"setFilter"})
    public static void setFilter(ImageView view, Boolean bool) {
        if (bool) {
            view.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        }
        else{
            view.setColorFilter(null);
        }
    }

    @BindingAdapter({"mode"})
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

    @BindingAdapter({"progressText"})
    public static void progressText(TextView view, int progress){
        view.setText(String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(progress),
                TimeUnit.MILLISECONDS.toSeconds(progress) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progress))
        ));
    }

    @BindingAdapter({"android:layout_height"})
    public static void setLayoutHeight(View view, float value){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) value;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter({"android:layout_height"})
    public static void setLayoutHeight(View view, int value){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = value;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter({"android:layout_width"})
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

    @BindingAdapter("android:layout_marginEnd")
    public static void setMarginEnd(View view, float value){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                layoutParams.rightMargin, Math.round(value));
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_marginStart")
    public static void setMarginStart(View view, float value){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                layoutParams.rightMargin, Math.round(value));
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

    @BindingAdapter("footerVisible")
    public static void setFooterVisible(View view, boolean visible){
        if (view.getTag() == null) {
            view.setTag(true);
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        } else {
            view.animate().cancel();
            if (visible) {
                view.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_bottom_up);
                view.setAnimation(anim);
            } else {
                Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_top_down);
                view.setAnimation(anim);
                view.setVisibility(View.GONE);
            }
        }
    }

    @BindingAdapter("expandableChildText")
    public static void setExpandableChildText(TextView view, String text){
        if(text.equals("")) return;
        view.setText(text);
        ExpandableLinearLayout exView = ((ExpandableLinearLayout) view.getParent());
        exView.initLayout();
        exView.expand();
    }

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
