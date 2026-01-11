package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

	private long txnId;
	private long accountId;
	private String txnType;
	private BigDecimal amount;
	private LocalDate txnDate;
	private String status;
	private String reason;

	public Transaction(long txnId, long accountId, String txnType, BigDecimal amount, LocalDate txnDate, String status,
			String reason) {
		super();
		this.txnId = txnId;
		this.accountId = accountId;
		this.txnType = txnType;
		this.amount = amount;
		this.txnDate = txnDate;
		this.status = status;
		this.reason = reason;
	}

	public long getTxnId() {

		return txnId;

	}

	public void setTxnId(long txnId) {
		this.txnId = txnId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDate getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(LocalDate txnDate) {
		this.txnDate = txnDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
