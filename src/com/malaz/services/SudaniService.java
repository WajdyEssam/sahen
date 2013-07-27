package com.malaz.services;

import android.net.Uri;

public class SudaniService extends SIMService {

	public SudaniService(String cardNo, String number, String balance) {
		super(cardNo, number, balance);
	}

	@Override
	public String getChargeFormat() {
		String format = "*222*" + cardNo +  Uri.encode("#");
		return format;
	}

	@Override
	public String getCallMeFormat() {
		return "";
	}

	@Override
	public String getSendBalance() {
		String format = "*333*" + number + "*" + balance + "*0000" +  Uri.encode("#");
		return format;
	}
	
	@Override
	public String getCurrentBalance() {
		String format = "*222" + Uri.encode("#");
		return format;
	}
}
