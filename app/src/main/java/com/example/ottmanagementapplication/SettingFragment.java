package com.example.ottmanagementapplication;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Locale;

public class SettingFragment extends Fragment {

    private TextView tvProfileName;
    private TextView tvNameValue;
    private TextView tvAlarmTimeValue;
    private TextView tvRetentionValue;
    private TextView tvSoundValue;

    private int alarmHour = 13;
    private int alarmMinute = 0;
    private int selectedRetentionIndex = 1; // 기본값: "7일"
    private boolean isSoundEnabled = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnCalendar = view.findViewById(R.id.btn_setting_calendar);
        FrameLayout btnNotification = view.findViewById(R.id.btn_setting_notification);

        tvProfileName = view.findViewById(R.id.tv_setting_profile_name);
        tvNameValue = view.findViewById(R.id.tv_setting_name_value);
        tvAlarmTimeValue = view.findViewById(R.id.tv_setting_alarm_time_value);
        tvRetentionValue = view.findViewById(R.id.tv_setting_retention_value);
        tvSoundValue = view.findViewById(R.id.tv_setting_sound_value);

        LinearLayout rowName = view.findViewById(R.id.row_setting_name);
        FrameLayout btnLogout = view.findViewById(R.id.btn_setting_logout);
        FrameLayout btnDeleteAccount = view.findViewById(R.id.btn_setting_delete_account);
        LinearLayout rowAlarmTime = view.findViewById(R.id.row_setting_alarm_time);
        LinearLayout rowRetentionPeriod = view.findViewById(R.id.row_setting_retention_period);
        LinearLayout rowSound = view.findViewById(R.id.row_setting_sound);

        // 1. 상단 툴바 이벤트
        btnCalendar.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_setting_to_calendar)
        );
        btnNotification.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_setting_to_notification)
        );

        // 2. 계정 섹션 이벤트
        rowName.setOnClickListener(v -> showEditNameDialog());
        btnLogout.setOnClickListener(v -> showConfirmDialog(
                "로그아웃",
                "현재 계정에서 로그아웃하시겠습니까?",
                "로그아웃되었습니다."
        ));
        btnDeleteAccount.setOnClickListener(v -> showConfirmDialog(
                "계정 삭제",
                "계정을 삭제하면 저장된 모든 구독 내역과 통계가 영구적으로 삭제됩니다. 계속하시겠습니까?",
                "계정 삭제 요청이 처리되었습니다."
        ));

        // 3. 알림 설정 섹션 이벤트
        rowAlarmTime.setOnClickListener(v -> showAlarmTimePicker());
        rowRetentionPeriod.setOnClickListener(v -> showRetentionPeriodDialog());

        // 4. 일반 설정 섹션 이벤트 (On / Off 토글)
        rowSound.setOnClickListener(v -> {
            isSoundEnabled = !isSoundEnabled;
            tvSoundValue.setText(isSoundEnabled ? "On" : "Off");
            tvSoundValue.setTextColor(isSoundEnabled
                    ? Color.parseColor("#4E5968")
                    : Color.parseColor("#8B95A1"));
        });
    }

    private void showEditNameDialog() {
        EditText input = new EditText(requireContext());
        input.setText(tvNameValue.getText());
        input.setSelection(input.getText().length());

        new AlertDialog.Builder(requireContext())
                .setTitle("이름 변경")
                .setView(input)
                .setPositiveButton("저장", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        tvProfileName.setText(newName);
                        tvNameValue.setText(newName);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }
    private void showAlarmTimePicker() {
        new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    alarmHour = hourOfDay;
                    alarmMinute = minute;
                    tvAlarmTimeValue.setText(String.format(Locale.KOREA, "%02d:%02d", alarmHour, alarmMinute));
                },
                alarmHour,
                alarmMinute,
                true
        ).show();
    }

    private void showRetentionPeriodDialog() {
        final String[] options = {"3일", "7일", "14일", "30일"};
        new AlertDialog.Builder(requireContext())
                .setTitle("알림 저장 기간 선택")
                .setSingleChoiceItems(options, selectedRetentionIndex, (dialog, which) -> {
                    selectedRetentionIndex = which;
                    tvRetentionValue.setText(options[which]);
                    dialog.dismiss();
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void showConfirmDialog(String title, String message, String toastMessage) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", (dialog, which) ->
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                )
                .setNegativeButton("취소", null)
                .show();
    }
}