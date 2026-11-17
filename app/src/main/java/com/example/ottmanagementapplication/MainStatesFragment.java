package com.example.ottmanagementapplication;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainStatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainStatesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int selectedPosition = -1;

    public MainStatesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainStatesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainStatesFragment newInstance(String param1, String param2) {
        MainStatesFragment fragment = new MainStatesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_main_states, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvIcons = view.findViewById(R.id.rv_subscription_icons);


        // UI 확인용 더미 아이콘 리스트 (drawable에 있는 실제 아이콘 이름으로 변경)
        List<Integer> dummyIcons = Arrays.asList(
                R.drawable.ic_watcha,
                R.drawable.ic_coupangplay,
                R.drawable.ic_disneyplus,
                R.drawable.ic_genie,
                R.drawable.ic_laftel
        );
        View cardTotalStats = view.findViewById(R.id.include_total_stats);
        TextView tvTotalEfficientUnit = cardTotalStats.findViewById(R.id.tv_stats_total_efficient_KRW_h);
        TextView tvTotalFeeUnit = cardTotalStats.findViewById(R.id.tv_stats_total_amount_KRW);

        View cardIndividualStats = view.findViewById(R.id.include_individual_stats);
        ImageView ivDetailIcon = cardIndividualStats.findViewById(R.id.iv_detail_service_icon);
        TextView tvServiceName = cardIndividualStats.findViewById(R.id.tv_detail_service_name);
        TextView tvEfficientValue = cardIndividualStats.findViewById(R.id.tv_value_detail_monthly_efficient);
        TextView tvFeeValue = cardIndividualStats.findViewById(R.id.tv_value_detail_monthly_fee);
        TextView tvEfficientUnit = cardIndividualStats.findViewById(R.id.tv_text_detail_monthly_efficient);
        TextView tvFeeUnit = cardIndividualStats.findViewById(R.id.tv_text_detail_monthly_fee);

        applyTextGradient(tvEfficientUnit, R.color.eff_gradient_start, R.color.eff_gradient_end);
        applyTextGradient(tvFeeUnit, R.color.krw_gradient_start, R.color.krw_gradient_end);
        applyTextGradient(tvTotalEfficientUnit, R.color.eff_gradient_start, R.color.eff_gradient_end);
        applyTextGradient(tvTotalFeeUnit, R.color.krw_gradient_start, R.color.krw_gradient_end);


        List<String> dummyNames = Arrays.asList("왓챠", "쿠팡플레이", "디즈니+", "지니뮤직", "라프텔");
        List<String> dummyEfficients = Arrays.asList("7,900", "7,890", "9,900", "8,400", "14,900");
        List<String> dummyFees = Arrays.asList("7,900", "7,890", "9,900", "8,400", "14,900");



        SubscriptionIconAdapter adapter = new SubscriptionIconAdapter(dummyIcons, position -> {
            if (selectedPosition == position && cardIndividualStats.getVisibility() == View.VISIBLE) {
                // 이미 선택된 아이콘을 다시 누르면 접기
                cardIndividualStats.setVisibility(View.GONE);
                selectedPosition = -1;
            } else {
                // 다른 아이콘 선택 시 데이터 갱신 후 펼치기
                ivDetailIcon.setImageResource(dummyIcons.get(position));
                tvServiceName.setText(dummyNames.get(position));
                tvEfficientValue.setText(dummyEfficients.get(position));
                tvFeeValue.setText(dummyFees.get(position));

                cardIndividualStats.setVisibility(View.VISIBLE);
                selectedPosition = position;
            }
            rvIcons.getAdapter(); // null 방지 확인용
            ((SubscriptionIconAdapter) rvIcons.getAdapter()).setSelectedPosition(selectedPosition);
        });
        rvIcons.setAdapter(adapter);


    }
    private void applyTextGradient(TextView textView, @ColorRes int startColorRes, @ColorRes int endColorRes) {
        TextPaint paint = textView.getPaint();
        String text = textView.getText().toString();
        float width = paint.measureText(text);

        // R.color 리소스 ID를 실제 색상 정수값으로 변환
        int startColor = ContextCompat.getColor(textView.getContext(), startColorRes);
        int endColor = ContextCompat.getColor(textView.getContext(), endColorRes);

        Shader textShader = new LinearGradient(
                0f, 0f, width, 0f,
                new int[]{startColor, endColor},
                null,
                Shader.TileMode.CLAMP
        );

        paint.setShader(textShader);
        textView.invalidate();
    }

}