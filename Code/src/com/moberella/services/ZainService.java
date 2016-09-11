package com.moberella.services;

import android.net.Uri;

public class ZainService extends SIMService {

	private String currentSIMPassword;
	
	public ZainService(String cardNo, String number, String balance, String currentSIMPassword) {
		super(cardNo, number, balance);
		this.currentSIMPassword = currentSIMPassword;
	}
	
	@Override
	public String getChargeFormat() {
		String format = "*888*" + cardNo +  Uri.encode("#");
		return format;
	}

	@Override
	public String getCallMeFormat() {
		String format = "*121*" + this.number +  Uri.encode("#");
		return format;
	}

	@Override
	public String getSendBalance() {
		String format = "*200*" + this.currentSIMPassword + "*" + balance + "*" + number +  Uri.encode("#");
		return format;
	}
	
	@Override
	public String getCurrentBalance() {
		String format = "*888" + Uri.encode("#");
		return format;
	}
	
	public String getSIMPassword() {
		return this.currentSIMPassword;
	}
	
//	public abstract boolean isValidChargeFormat();
//	public abstract boolean isValidTranfereFormat();
//	public abstract boolean isValidBalanceCheckingFormat();
//	public abstract boolean isValidCallMeFormat();

}
