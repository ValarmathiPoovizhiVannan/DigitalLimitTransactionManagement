package model;

import java.math.BigDecimal;

public class Account {

	private String accountId;
	private long customerId;
	private String accountNumber;
	private String customerName;
	private BigDecimal balance;
	private BigDecimal dailyLimit;
	private BigDecimal monthlyLimit;

	public Account() {
		super();
	}

	public String getAccountId() {

		return accountId;

	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(BigDecimal dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public BigDecimal getMonthlyLimit() {

		return monthlyLimit;
	}

	public void setMonthlyLimit(BigDecimal monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}

	public String getCustomerName() {

		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
