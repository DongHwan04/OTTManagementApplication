   package com.example.ottmanagementapplication;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SubscriptionManager {
    private static final String PREFS = "ott_subscriptions";
    private static final String KEY = "items";

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_PAUSED = "PAUSED";
    public static final String STATUS_CANCELED = "CANCELED";

    private final SharedPreferences prefs;

    public SubscriptionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // 구독 등록
    public boolean addSubscription(String name, String category,
                                   int monthlyPrice, int paymentDay) {
        return addSubscription(name, category, monthlyPrice, paymentDay, "");
    }

    // 메모를 포함한 구독 등록
    public boolean addSubscription(String name, String category,
                                   int monthlyPrice, int paymentDay, String memo) {
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
            item.put("memo", memo == null ? "" : memo.trim());
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
        return updateSubscription(id, name, category, monthlyPrice, paymentDay, null);
    }

    // 메모를 포함한 구독 정보 수정
    public boolean updateSubscription(long id, String name, String category,
                                       int monthlyPrice, int paymentDay, String memo) {
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

                    if (memo != null) {
                        item.put("memo", memo.trim());
                    } else if (!item.has("memo")) {
                        item.put("memo", "");
                    }

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

            // 기존 데이터에 status가 없으면 ACTIVE로 간주
            if (status.equals(getStatus(item))) {
                result.put(item);
            }
        }

        return result;
    }

    // 특정 구독의 상태 조회
    public String getSubscriptionStatus(long id) {
        JSONObject item = findSubscription(id);
        return item == null ? null : getStatus(item);
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

    // 다음 결제일의 timestamp 반환. 구독이 없거나 활성 상태가 아니면 -1 반환.
    public long getNextPaymentDate(long id) {
        JSONObject item = findSubscription(id);

        if (item == null || !STATUS_ACTIVE.equals(getStatus(item))) {
            return -1L;
        }

        Calendar nextPayment = calculateNextPayment(item, Calendar.getInstance());
        return nextPayment == null ? -1L : nextPayment.getTimeInMillis();
    }

    // 다음 결제일을 yyyy-MM-dd 형식으로 반환
    public String getNextPaymentDateText(long id) {
        long nextPayment = getNextPaymentDate(id);

        if (nextPayment < 0) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        return format.format(new Date(nextPayment));
    }

    // 다음 결제일까지 남은 일수. 구독이 없거나 활성 상태가 아니면 -1 반환.
    public int getDaysUntilNextPayment(long id) {
        long nextPayment = getNextPaymentDate(id);

        if (nextPayment < 0) {
            return -1;
        }

        Calendar today = getStartOfDay(Calendar.getInstance());
        Calendar paymentDate = Calendar.getInstance();
        paymentDate.setTimeInMillis(nextPayment);
        paymentDate = getStartOfDay(paymentDate);

        long difference = paymentDate.getTimeInMillis() - today.getTimeInMillis();
        return (int) (difference / (24L * 60L * 60L * 1000L));
    }

    // 지정한 기간 내 결제 예정인 활성 구독 조회
    public JSONArray getUpcomingPayments(int withinDays) {
        JSONArray result = new JSONArray();

        if (withinDays < 0) {
            return result;
        }

        JSONArray items = getSubscriptions();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);

            if (item == null || !STATUS_ACTIVE.equals(getStatus(item))) {
                continue;
            }

            Calendar nextPayment = calculateNextPayment(item, Calendar.getInstance());
            if (nextPayment == null) {
                continue;
            }

            Calendar today = getStartOfDay(Calendar.getInstance());
            Calendar paymentDate = getStartOfDay(nextPayment);
            long difference = paymentDate.getTimeInMillis() - today.getTimeInMillis();
            int daysLeft = (int) (difference / (24L * 60L * 60L * 1000L));

            if (daysLeft >= 0 && daysLeft <= withinDays) {
                try {
                    JSONObject resultItem = new JSONObject(item.toString());
                    resultItem.put("nextPaymentDate",
                            formatDate(nextPayment.getTimeInMillis()));
                    resultItem.put("daysUntilPayment", daysLeft);
                    result.put(resultItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    // 7일 이내 결제 예정 구독 조회
    public JSONArray getUpcomingPayments() {
        return getUpcomingPayments(7);
    }

    // 활성 구독의 월간 구독료 합계
    public int getMonthlySubscriptionTotal() {
        int total = 0;
        JSONArray activeItems = getSubscriptionsByStatus(STATUS_ACTIVE);

        for (int i = 0; i < activeItems.length(); i++) {
            JSONObject item = activeItems.optJSONObject(i);

            if (item != null) {
                total += item.optInt("monthlyPrice", 0);
            }
        }

        return total;
    }

    // 연간 예상 지출: 월간 구독료 합계 x 12
    public int getYearlySubscriptionTotal() {
        return getMonthlySubscriptionTotal() * 12;
    }

    // 특정 카테고리의 월간 구독료 합계
    public int getMonthlyTotalByCategory(String category) {
        if (category == null) {
            return 0;
        }

        int total = 0;
        JSONArray activeItems = getSubscriptionsByStatus(STATUS_ACTIVE);

        for (int i = 0; i < activeItems.length(); i++) {
            JSONObject item = activeItems.optJSONObject(i);

            if (item != null && category.equals(item.optString("category"))) {
                total += item.optInt("monthlyPrice", 0);
            }
        }

        return total;
    }

    // 카테고리별 월간 구독료 합계를 JSONObject로 반환
    public JSONObject getCategoryMonthlyTotals() {
        JSONObject totals = new JSONObject();
        JSONArray activeItems = getSubscriptionsByStatus(STATUS_ACTIVE);

        for (int i = 0; i < activeItems.length(); i++) {
            JSONObject item = activeItems.optJSONObject(i);

            if (item == null) {
                continue;
            }

            String category = item.optString("category", "기타");
            int currentTotal = totals.optInt(category, 0);

            try {
                totals.put(category,
                        currentTotal + item.optInt("monthlyPrice", 0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return totals;
    }

    // 서비스별 월간 구독료 합계를 JSONObject로 반환
    public JSONObject getServiceMonthlyTotals() {
        JSONObject totals = new JSONObject();
        JSONArray activeItems = getSubscriptionsByStatus(STATUS_ACTIVE);

        for (int i = 0; i < activeItems.length(); i++) {
            JSONObject item = activeItems.optJSONObject(i);

            if (item == null) {
                continue;
            }

            String serviceName = item.optString("name", "이름 없음");
            int currentTotal = totals.optInt(serviceName, 0);

            try {
                totals.put(serviceName,
                        currentTotal + item.optInt("monthlyPrice", 0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return totals;
    }

    // 서비스별 월간 구독료 지출 비중(%)을 JSONObject로 반환
    // 예: {"넷플릭스": 34.0, "FLO": 16.4}
    public JSONObject getServiceSpendingRatios() {
        JSONObject ratios = new JSONObject();

        int total = getMonthlySubscriptionTotal();
        if (total <= 0) {
            return ratios;
        }

        JSONObject serviceTotals = getServiceMonthlyTotals();
        java.util.Iterator<String> keys = serviceTotals.keys();

        while (keys.hasNext()) {
            String serviceName = keys.next();
            int serviceTotal = serviceTotals.optInt(serviceName, 0);
            double ratio = (serviceTotal * 100.0) / total;

            try {
                ratios.put(serviceName, Math.round(ratio * 10.0) / 10.0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ratios;
    }

    // 특정 서비스의 월간 구독료 지출 비중(%) 조회
    public double getServiceSpendingRatio(String serviceName) {
        if (serviceName == null) {
            return 0.0;
        }

        int total = getMonthlySubscriptionTotal();
        if (total <= 0) {
            return 0.0;
        }

        int serviceTotal = getMonthlyTotalByService(serviceName);
        return Math.round((serviceTotal * 1000.0) / total) / 10.0;
    }

    // 특정 서비스의 월간 구독료 조회
    public int getMonthlyTotalByService(String serviceName) {
        if (serviceName == null) {
            return 0;
        }

        int total = 0;
        JSONArray activeItems = getSubscriptionsByStatus(STATUS_ACTIVE);

        for (int i = 0; i < activeItems.length(); i++) {
            JSONObject item = activeItems.optJSONObject(i);

            if (item != null && serviceName.equals(item.optString("name"))) {
                total += item.optInt("monthlyPrice", 0);
            }
        }

        return total;
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

    private String getStatus(JSONObject item) {
        return item.optString("status", STATUS_ACTIVE);
    }

    private JSONObject findSubscription(long id) {
        JSONArray items = getSubscriptions();

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.optJSONObject(i);

            if (item != null && item.optLong("id") == id) {
                return item;
            }
        }

        return null;
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

   
    private Calendar calculateNextPayment(JSONObject item, Calendar today) {
        int paymentDay = item.optInt("paymentDay", 0);

        if (paymentDay < 1 || paymentDay > 31) {
            return null;
        }

        Calendar base = getStartOfDay((Calendar) today.clone());

        Calendar candidate = createClampedDate(
                base.get(Calendar.YEAR),
                base.get(Calendar.MONTH),
                paymentDay);

        if (candidate.before(base)) {
            Calendar nextMonth = (Calendar) base.clone();
            nextMonth.add(Calendar.MONTH, 1);

            candidate = createClampedDate(
                    nextMonth.get(Calendar.YEAR),
                    nextMonth.get(Calendar.MONTH),
                    paymentDay);
        }

        return candidate;
    }

    // 날짜 보정
    private Calendar createClampedDate(int year, int zeroBasedMonth, int day) {
        Calendar date = Calendar.getInstance();
        date.clear();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, zeroBasedMonth);
        date.set(Calendar.DAY_OF_MONTH, 1);

        int lastDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        date.set(Calendar.DAY_OF_MONTH, Math.min(day, lastDay));

        return date;
    }

    private Calendar getStartOfDay(Calendar source) {
        Calendar result = (Calendar) source.clone();
        result.set(Calendar.HOUR_OF_DAY, 0);
        result.set(Calendar.MINUTE, 0);
        result.set(Calendar.SECOND, 0);
        result.set(Calendar.MILLISECOND, 0);
        return result;
    }

    private String formatDate(long timeMillis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        return format.format(new Date(timeMillis));
    }

    private void save(JSONArray items) {
        prefs.edit().putString(KEY, items.toString()).apply();
    }
}
