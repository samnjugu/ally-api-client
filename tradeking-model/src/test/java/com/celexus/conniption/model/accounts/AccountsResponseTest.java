package com.celexus.conniption.model.accounts;

import com.celexus.conniption.model.XmlParsingTest;
import com.celexus.conniption.model.accounts.AccountsResponse.*;
import com.celexus.conniption.model.accounts.AccountsResponse;
import com.celexus.conniption.model.util.JAXBUtils;
import java.io.FileInputStream;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.xml.sax.InputSource;

public class AccountsResponseTest extends XmlParsingTest {

    @Test public void accountsParseTest() throws Exception {
	AccountsResponse resp = parse(source("xml/accounts.xml"),
				      "com.celexus.conniption.model.accounts",
				      AccountsResponse.class);
	assertThat(resp, notNullValue());
	assertThat(resp.getElapsedtime(), is(0));
	Accounts accounts = resp.getAccounts();
	assertThat(accounts, notNullValue());
	List<AccountSummary> summaries = accounts.getAccountsummary();
	assertThat(summaries.size(), is(1));
	for (AccountSummary summary : summaries) {
	    if (summary.getAccount() == 88963310) {
		AccountBalance accountBalance = summary.getAccountbalance();
		assertThat(accountBalance, notNullValue());
		assertThat(accountBalance.getAccountvalue(), is(285072.75));
		assertThat(accountBalance.getFedcall(), is(0d));
		assertThat(accountBalance.getHousecall(), is(0d));
		/*
		  protected BuyingPower buyingpower;
		  protected Money money;
		  protected Securities securities;
		*/
		AccountHoldings accountHoldings = summary.getAccountholdings();
		assertThat(accountHoldings, notNullValue());
		assertThat(accountHoldings.getTotalsecurities(), is(284201.3));
		/*
		  protected AccountHoldingsDisplayData displaydata;
		*/
		List<Holding> holdings = accountHoldings.getHolding();
		assertThat(holdings.size(), is(1));
	    }
	}
	assertThat(resp.getError(), is("Success"));
    }

}