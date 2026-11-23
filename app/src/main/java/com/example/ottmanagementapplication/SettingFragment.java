package com.example.ottmanagementapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
}