package com.celexus.conniption.foreman.enums;

public enum TopList {
    LOSERS_DOLLAR(Market.TOPLISTS_LOSERS_DOLLAR),
    LOSERS_PERCENTAGE(Market.TOPLISTS_LOSERS_PERCENTAGE),
    VOLUME(Market.TOPLISTS_VOLUME),
    ACTIVE(Market.TOPLISTS_ACTIVE),
    GAINERS_DOLLAR(Market.TOPLISTS_GAINERS_DOLLAR_AMT),
    GAINERS_PERCENTAGE(Market.TOPLISTS_GAINERS_PERCENTAGE),
    GAINERS_ACTIVE(Market.TOPLISTS_GAINERS_ACTIVE_DOLLAR_AMT);
    private Market link;

    TopList(Market link) {
        this.link = link;
    }

    public Market getLink() {
        return link;
    }
}
