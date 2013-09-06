package com.malaz.util;

import com.malaz.app.R;

import android.content.Context;
import android.content.Intent;

public class LunchEmailUtil {
	public static void launchEmailToIntent(Context context) {
	    Intent msg = new Intent(Intent.ACTION_SEND);

	    StringBuilder body = new StringBuilder("\n\n----------\n");
	    body.append(EnvironmentUtil.getApplicationInfo(context));

	    msg.putExtra(Intent.EXTRA_EMAIL, context.getString(R.string.mail_support_feedback_to).split(", "));
	    msg.putExtra(Intent.EXTRA_SUBJECT,context.getString(R.string.mail_support_feedback_subject));
	    msg.putExtra(Intent.EXTRA_TEXT, body.toString());
	    msg.setType("message/rfc822");
	    context.startActivity(Intent.createChooser(msg,context.getString(R.string.pref_sendemail_title)));
	  }
}
