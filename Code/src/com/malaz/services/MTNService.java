package com.malaz.services;

import android.net.Uri;

public class MTNService extends SIMService {
	
	public MTNService(String cardNo, String number, String balance) {
		super(cardNo, number, balance);
	}

	@Override
	public String getChargeFormat() {
		String format = "*131*" + cardNo +  Uri.encode("#");
		return format;
	}

	@Override
	public String getCallMeFormat() {
		return "";
	}

	@Override
	public String getSendBalance() {
		String format = "*121*" + number + "*" + balance + "*00000" +  Uri.encode("#");
		return format;
	}
	
	@Override
	public String getCurrentBalance() {
		String format = "*141" + Uri.encode("#");
		return format;
	}

}
