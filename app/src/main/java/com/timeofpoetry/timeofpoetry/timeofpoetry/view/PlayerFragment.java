package com.timeofpoetry.timeofpoetry.timeofpoetry.view;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.timeofpoetry.timeofpoetry.timeofpoetry.R;
import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass;
import com.timeofpoetry.timeofpoetry.timeofpoetry.databinding.FragmentPlayerBinding;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.ActivityComponent;
import com.timeofpoetry.timeofpoetry.timeofpoetry.di.FragModule;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.model.PlayBackStateModel;
import com.timeofpoetry.timeofpoetry.timeofpoetry.musicPlayer.MediaPlaybackService;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.PlayListActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.view.footer.PlayerActivity;
import com.timeofpoetry.timeofpoetry.timeofpoetry.viewmodel.PlayerFragmentViewModel;

import javax.inject.Inject;

public class PlayerFragment extends Fragment {

    private MediaBrowserCompat mMediaBrowser;
    @Inject
    PlayerFragmentViewModel.PlayerFragmentViewModelFactory viewModelFactory;
    private MediaBrowserCompat.ConnectionCallback mConnectionCallbacks;
    private final MediaControllerCompat.Callback controllerCallback = new MediaControllerCompat.Callback(){};
    private PlayerFragmentViewModel viewModel;
    private boolean isWide;

    private OnFragmentInteractionListener mListener;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListener.getComponent()
                .plus(new FragModule())
                .inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerFragmentViewModel.class);
        FragmentPlayerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        viewModel.setIsWide(isWide);
        binding.setViewModel(viewModel);

        binding.playlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof PlayListActivity){
                    getActivity().onBackPressed();
                }
                else if(getActivity() instanceof PlayerActivity){
                    startActivity(new Intent(getActivity(), PlayListActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_bottom_up, R.anim.slide_top_down);
                    getActivity().finish();
                }
                else{
                    startActivity(new Intent(getActivity(), PlayListActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_bottom_up, R.anim.hold);
                }
            }
        });

        binding.playInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof PlayListActivity){
                    startActivity(new Intent(getActivity(), PlayerActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_bottom_up, R.anim.slide_top_down);
                    getActivity().finish();
                }
                else if(getActivity() instanceof PlayerActivity){
                    getActivity().onBackPressed();
                }
                else{
                    startActivity(new Intent(getActivity(), PlayerActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_bottom_up, R.anim.hold);
                }
            }
        });

        viewModel.getMode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer mode) {
                if(viewModel.setMode(mode)){
                    if(mode == MyPlayListModel.SHUFFLE){
                        Toast.makeText(getContext(), "랜덤으로 재생합니다", Toast.LENGTH_SHORT).show();
                    }
                    else if(mode == PlaybackStateCompat.REPEAT_MODE_ALL){
                        Toast.makeText(getContext(), "전체 시를 반복합니다", Toast.LENGTH_SHORT).show();
                    }
                    else if(mode == PlaybackStateCompat.REPEAT_MODE_ONE){
                        Toast.makeText(getContext(), "시 하나를 반복합니다", Toast.LENGTH_SHORT).show();
                    }
                    else if(mode == PlaybackStateCompat.REPEAT_MODE_NONE){
                        Toast.makeText(getContext(), "반복을 사용하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewModel.getState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                viewModel.setState(integer);
                if(integer == PlayBackStateModel.PLAYING || integer == PlayBackStateModel.BUFFERING){
                    ((Activity) mListener).setVolumeControlStream(AudioManager.STREAM_MUSIC);
                }
                else{
                    ((Activity) mListener).setVolumeControlStream(AudioManager.MODE_NORMAL);
                }
            }
        });

        viewModel.getCurrentMedia().observe(this, new Observer<PoetryClass.Poem>() {
            @Override
            public void onChanged(@Nullable PoetryClass.Poem poem) {
                if(poem == null || binding.getPoem() != null && binding.getPoem().getDatabaseId() == poem.getDatabaseId()) return;
                binding.setPoem(poem);
            }
        });

        buildTransportControls(binding);

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            isWide = context instanceof PlayerActivity;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        mConnectionCallbacks = new MediaBrowserCompat.ConnectionCallback() {
            @Override
            public void onConnected() {
                super.onConnected();
                try {
                    MediaSessionCompat.Token token = mMediaBrowser.getSessionToken();
                    MediaControllerCompat mediaController = new MediaControllerCompat(context, token);
                    MediaControllerCompat.setMediaController((Activity) context, mediaController);
                    MediaControllerCompat.getMediaController((Activity) context).registerCallback(controllerCallback);
                } catch (RemoteException e) {
                    //세션 토큰 초기화 시 발생하는 에러를 잡기 위함
                }
            }

            @Override
            public void onConnectionSuspended() {
                super.onConnectionSuspended();
                //서비스가 크래쉬됐을 때 컨트롤러를 자동 재열결 시 까지 보이지 않게 만듭니다
            }

            @Override
            public void onConnectionFailed() {
                super.onConnectionFailed();
                //서비스가 연결을 거부했을 때
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMediaBrowser = new MediaBrowserCompat((Activity) mListener,
                new ComponentName(getContext(), MediaPlaybackService.class),
                mConnectionCallbacks,
                null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowser.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (MediaControllerCompat.getMediaController((Activity) mListener) != null) {
            MediaControllerCompat.getMediaController((Activity) mListener).unregisterCallback(controllerCallback);
        }
        mMediaBrowser.disconnect();
    }

    private void buildTransportControls(FragmentPlayerBinding binding)
    {
        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaControllerCompat.getMediaController((Activity) mListener).getTransportControls().play();
            }
        });

        binding.pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {MediaControllerCompat.getMediaController((Activity) mListener).getTransportControls().pause();}
        });

        binding.rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {MediaControllerCompat.getMediaController((Activity) mListener).getTransportControls().rewind();}
        });

        binding.forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaControllerCompat.getMediaController((Activity) mListener).getTransportControls().fastForward();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        ActivityComponent getComponent();
    }
}
