package com.malaz.util;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class AlertUtil {
	private static CharSequence[] items = {"MTN", "Sudani", "Zain", "Nothing"};
	private static int selectedItem;
	
	public static void selectSIMTypeDialog(final Context context ) {
		new AlertDialog.Builder(context)
			.setIcon(R.drawable.ic_dialog_alert)
			.setTitle("Please choose SIM company type")
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(context, "OK clicked!", Toast.LENGTH_SHORT).show();
						}
					})
			.setNegativeButton("Cancel", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(context, "Cancel clicked!", Toast.LENGTH_SHORT).show();							
						}
					})
			.setSingleChoiceItems(items, selectedItem, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {						
							SharedPreferences appPrefs = context.getSharedPreferences("sahen_sudani", Activity.MODE_PRIVATE);
							SharedPreferences.Editor prefsEditor = appPrefs.edit();
							prefsEditor.putString("companyName", String.valueOf(which));
							prefsEditor.commit();								
															
						}
					})
			.show();					
	}
}
