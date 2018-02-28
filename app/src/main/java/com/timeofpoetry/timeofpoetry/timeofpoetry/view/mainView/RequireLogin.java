package com.timeofpoetry.timeofpoetry.timeofpoetry.view.mainView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.startActivities.SignActivity;

public class RequireLogin extends Fragment {

    public RequireLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_require_login, container, false);
        view.findViewById(R.id.my_poem_empty_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignActivity.class);
                intent.putExtra("mainactivity", "comeback");
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

}
