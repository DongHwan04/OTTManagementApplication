            item.put("id", createUniqueId(items));
            item.put("name", name.trim());
            item.put("category", category.trim());
            item.put("monthlyPrice", monthlyPrice);
            item.put("paymentDay", paymentDay);

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

        if (deleted) {
            save(updated);
        }

        return deleted;
    }

 
    private boolean isValid(String name, String category,
                            int monthlyPrice, int paymentDay) {
        return name != null && !name.trim().isEmpty()
                && category != null && !category.trim().isEmpty()
                && monthlyPrice >= 0
                && paymentDay >= 1 && paymentDay <= 31;
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
