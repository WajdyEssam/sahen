package com.malaz.util;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertUtil {	
	private static int selectedItem;

	public static void selectSIMTypeDialog(final Context context) {
		new AlertDialog.Builder(context)
			.setIcon(R.drawable.ic_dialog_alert)
			.setTitle("Please choose SIM company type")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
					})
			.setSingleChoiceItems(AppUtil.englishItems, selectedItem,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Preferences.setValue(context, Constants.COMPANY_NAME, String.valueOf(which));
					}
		}).show();
	}
}
