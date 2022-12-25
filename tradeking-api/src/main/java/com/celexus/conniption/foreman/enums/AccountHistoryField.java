package com.celexus.conniption.foreman.enums;

public enum AccountHistoryField {
    RANGE_PARAM("range"), TRANSACTIONS_PARAM("transactions"), RANGE_ALL("all"), RANGE_TODAY("today")
    , RANGE_CURRENT_WEEK("current_week"), RANGE_CURRENT_MONTH("current_month"), RANGE_LAST_MONTH("last_month")
    , TRANSACTIONS_TYPE_ALL("all"), TRANSACTIONS_TYPE_BOOKKEEPING("bookkeeping"), TRANSACTIONS_TYPE_TRADE("trade");

    private String tag;

    /**
     * Assign the string of a tag to the enum
     * string.
     *
     * @param tag
     */
    AccountHistoryField(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return tag;
    }
}
