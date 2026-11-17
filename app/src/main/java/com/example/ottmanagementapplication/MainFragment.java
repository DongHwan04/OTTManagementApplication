package com.example.ottmanagementapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    private View btnVideo;
    private View btnMusic;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 부모 XML의 FragmentContainerView에 자식 프래그먼트(MainStatesFragment)를 조립
        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_child_fragment_container, new MainStatesFragment())
                    .commit();
        }
        btnVideo = view.findViewById(R.id.button_transition_video); // 비디오 아이콘
        btnMusic = view.findViewById(R.id.button_transition_music); // 뮤직 아이콘

        if (btnVideo != null) {
            btnVideo.setOnClickListener(v -> toggleChildFragment(MainVideoFragment.class));
        }

        if (btnMusic != null) {
            btnMusic.setOnClickListener(v -> toggleChildFragment(MainMusicFragment.class));
        }

    }
    private void toggleChildFragment(@NonNull Class<? extends Fragment> targetClass) {
        Fragment currentFragment = getChildFragmentManager()
                .findFragmentById(R.id.home_child_fragment_container);

        Fragment nextFragment;
        boolean isReturningToStates = (currentFragment != null && currentFragment.getClass().equals(targetClass));

        if (isReturningToStates) {
            // 이미 해당 화면에 있는 상태에서 버튼을 다시 누름 -> 전체 통계 화면으로 복귀
            nextFragment = new MainStatesFragment();
        } else {
            // 다른 화면에서 버튼을 누름 -> 대상 프래그먼트 생성
            try {
                nextFragment = targetClass.newInstance();
            } catch (IllegalAccessException | java.lang.InstantiationException e) {
                e.printStackTrace();
                return;
            }
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.home_child_fragment_container, nextFragment)
                .commit();

        // 버튼 선택(활성) 시각 상태 동기화
        updateNavButtonState(isReturningToStates ? MainStatesFragment.class : targetClass, isReturningToStates);
    }

    private void updateNavButtonState(@NonNull Class<? extends Fragment> activeClass, boolean isreturn) {
        if (isreturn) {
            btnVideo.setAlpha(1.0f);
            btnMusic.setAlpha(1.0f);
            return;
        }
        if (btnVideo != null) {
            boolean isVideoActive = activeClass.equals(MainVideoFragment.class);
            btnVideo.setSelected(isVideoActive);
            btnVideo.setAlpha(isVideoActive ? 1.0f : 0.5f);
        }
        if (btnMusic != null) {
            boolean isMusicActive = activeClass.equals(MainMusicFragment.class);
            btnMusic.setSelected(isMusicActive);
            btnMusic.setAlpha(isMusicActive ? 1.0f : 0.5f);
        }


    }
}