package com.moberella.services;

public abstract class SIMService {
	protected final String cardNo;
	protected final String number;
	protected final String balance;
	
	public SIMService(String cardNo, String number, String balance) {
		this.cardNo = cardNo;
		this.number = number;
		this.balance = balance;
	}
	
	protected String getCardNo() { return this.cardNo; }
	protected String getNumber() { return this.number; }
	protected String getBalance() { return this.balance; }
	
	public abstract String getChargeFormat();
	public abstract String getCallMeFormat();
	public abstract String getSendBalance();
	public abstract String getCurrentBalance();
}
