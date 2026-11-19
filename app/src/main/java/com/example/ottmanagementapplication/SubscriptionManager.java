        package com.example.ottmanagementapplication;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubscriptionManager {
    private static final String PREFS = "ott_subscriptions";
    private static final String KEY = "items";

    // 구독 상태
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_PAUSED = "PAUSED";
    public static final String STATUS_CANCELED = "CANCELED";

    private final SharedPreferences prefs;

    public SubscriptionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // 구독 등록: 입력값이 유효하면 true 반환
    public boolean addSubscription(String name, String category,
                                   int monthlyPrice, int paymentDay) {
        if (!isValid(name, category, monthlyPrice, paymentDay)) {
            return false;
        }

        JSONArray items = getSubscriptions();
        JSONObject item = new JSONObject();

        try {
            item.put("id", createUniqueId(items));
            item.put("name", name.trim());
            item.put("category", category.trim());
            item.put("monthlyPrice", monthlyPrice);
            item.put("paymentDay", paymentDay);
            item.put("status", STATUS_ACTIVE);

            items.put(item);
            save(items);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 전체 구독 목록 조회
    public JSONArray getSubscriptions() {
        String saved = prefs.getString(KEY, "[]");

        try {
            return new JSONArray(saved);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    // 구독 정보 수정
    public boolean updateSubscription(long id, String name, String category,
                                      int monthlyPrice, int paymentDay) {
        if (!isValid(name, category, monthlyPrice, paymentDay)) {
            return false;
        }

        JSONArray items = getSubscriptions();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);

            if (item != null && item.optLong("id") == id) {
                try {
                    item.put("name", name.trim());
                    item.put("category", category.trim());
                    item.put("monthlyPrice", monthlyPrice);
                    item.put("paymentDay", paymentDay);

                    save(items);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        return false;
    }

    // 구독 상태 변경
    public boolean updateSubscriptionStatus(long id, String status) {
        if (!isValidStatus(status)) {
            return false;
        }

        JSONArray items = getSubscriptions();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);

            if (item != null && item.optLong("id") == id) {
                try {
                    item.put("status", status);
                    save(items);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        return false;
    }

    // 특정 상태의 구독 목록 조회
    public JSONArray getSubscriptionsByStatus(String status) {
        JSONArray result = new JSONArray();

        if (!isValidStatus(status)) {
            return result;
        }

        JSONArray items = getSubscriptions();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);
            if (item == null) {
                continue;
            }

            // 기존에 저장된 데이터에 status가 없으면 ACTIVE로 간주
            String itemStatus = item.optString("status", STATUS_ACTIVE);

            if (status.equals(itemStatus)) {
                result.put(item);
            }
        }

        return result;
    }

    // 특정 구독의 상태 조회
    public String getSubscriptionStatus(long id) {
        JSONArray items = getSubscriptions();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);

            if (item != null && item.optLong("id") == id) {
                // 기존 데이터 호환: status가 없으면 ACTIVE 반환
                return item.optString("status", STATUS_ACTIVE);
            }
        }

        return null;
    }

    // 구독 삭제
    public boolean deleteSubscription(long id) {
        JSONArray items = getSubscriptions();
        JSONArray updated = new JSONArray();
        boolean deleted = false;

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);
            if (item == null) {
                continue;
            }

            if (item.optLong("id") == id) {
                deleted = true;
            } else {
                updated.put(item);
            }
        }

        if (deleted) {
            save(updated);
        }

        return deleted;
    }

    // 입력값 검증
    private boolean isValid(String name, String category,
                            int monthlyPrice, int paymentDay) {
        return name != null && !name.trim().isEmpty()
                && category != null && !category.trim().isEmpty()
                && monthlyPrice >= 0
                && paymentDay >= 1 && paymentDay <= 31;
    }

    // 허용된 구독 상태인지 확인
    private boolean isValidStatus(String status) {
        return STATUS_ACTIVE.equals(status)
                || STATUS_PAUSED.equals(status)
                || STATUS_CANCELED.equals(status);
    }

    // 기존 ID와 겹치지 않는 ID 생성
    private long createUniqueId(JSONArray items) {
        long id = System.currentTimeMillis();
        boolean exists = true;

        while (exists) {
            exists = false;

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.optJSONObject(i);

                if (item != null && item.optLong("id") == id) {
                    id++;
                    exists = true;
                    break;
                }
            }
        }

        return id;
    }

    // 변경된 구독 목록 저장
    private void save(JSONArray items) {
        prefs.edit().putString(KEY, items.toString()).apply();
    }
}
