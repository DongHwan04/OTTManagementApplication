package com.example.ottmanagementapplication;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubscriptionManager {
    private static final String PREFS = "ott_subscriptions";
    private static final String KEY = "items";

    private final SharedPreferences prefs;

    public SubscriptionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // 구독 등록
    public void addSubscription(String name, String category,
                                int monthlyPrice, int paymentDay) {
        JSONArray items = getSubscriptions();

        JSONObject item = new JSONObject();
        try {
            item.put("id", System.currentTimeMillis());
            item.put("name", name);
            item.put("category", category);
            item.put("monthlyPrice", monthlyPrice);
            item.put("paymentDay", paymentDay);
            items.put(item);
            save(items);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 전체 구독 목록 조회
    public JSONArray getSubscriptions() {
        String saved = prefs.getString(KEY, "[]");
        try {
            return new JSONArray(saved);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    // 구독 삭제
    public boolean deleteSubscription(long id) {
        JSONArray items = getSubscriptions();
        JSONArray updated = new JSONArray();
        boolean deleted = false;

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);
            if (item == null) continue;

            if (item.optLong("id") == id) {
                deleted = true;
            } else {
                updated.put(item);
            }
        }

        if (deleted) save(updated);
        return deleted;
    }

    private void save(JSONArray items) {
        prefs.edit().putString(KEY, items.toString()).apply();
    }
}
