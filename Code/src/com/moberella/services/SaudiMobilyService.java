package com.moberella.services;

import android.net.Uri;

public class SaudiMobilyService extends SIMService {

	private String userIdentityNumber;
	
	public SaudiMobilyService(String cardNo, String number, String balance, String userIdentityNumber) {
		super(cardNo, number, balance);
		this.userIdentityNumber = userIdentityNumber;
	}

	@Override
	public String getChargeFormat() {
		String format = "*1400*" + cardNo + "*" + this.userIdentityNumber +  Uri.encode("#");
		return format;
	}

	@Override
	public String getCallMeFormat() {
		return "";
	}

	@Override
	public String getSendBalance() {
		return "";
	}

	@Override
	public String getCurrentBalance() {
		String format = "*1411" + Uri.encode("#");
		return format;
	}
	
	public String getUserIdentityNumber() {
		return this.userIdentityNumber;
	}

}
