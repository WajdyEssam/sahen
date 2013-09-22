package com.moberella.util;

import android.content.Context;
import android.content.res.Configuration;

public class LangUtil {
	
	public enum Languages {
        Arabic, English
    };

    public static void setLocale(Context context) {
        int language = Integer.valueOf(Preferences.getValue(context, Constants.APPLICATION_LANGUAGE ,  Constants.DEFAULT_APPLICATION_LANGUAGE));
        java.util.Locale locale = java.util.Locale.getDefault();

        switch (language) {
            case 1: // Default System Locale
                locale = java.util.Locale.getDefault();
                break;

            case 2: // Arabic Preference
                locale = new java.util.Locale("ar");
                break;

            case 3: // English Preference
                locale = new java.util.Locale("en");
                break;
        }

        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    public static Languages getCurrentLanguage(Context context) {
        String defaultLocale = context.getResources().getConfiguration().locale.getLanguage(); 

        if (defaultLocale.contains("ar"))
            return Languages.Arabic;
        else
            return Languages.English;
    }
}
