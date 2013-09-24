package com.moberella.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.res.AssetManager;

public class FileUtil {		
	
	public static boolean copyFileIfNotExists(Activity activity,  String destination, String source) {
		boolean result = false;
		
		if (!isExit(destination) ) {
			try {				
				copySourceToDestination(activity, destination, source);
				result = true;
			} catch (IOException e) {
				result = false;
			}
		}
		else {
			result = true;
		}
		
		return result;
	}
	
	public static boolean createDirectoryIfNotExists(String path) {
		if (!isExit(path) ) {
			return createDirectory(path);
		}
		
		return true;
	}
	
	private static boolean isExit(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}
	
	private static boolean createDirectory(String path) {
		File dir = new File(path);
		return dir.mkdirs();
	}
	
	private static void copySourceToDestination(Activity activity, String destination, String source) throws IOException {
		AssetManager assetManager = activity.getAssets();
		InputStream in = assetManager.open(source);
		OutputStream out = new FileOutputStream(destination);

		byte[] buf = new byte[1024];
		int len;

		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		
		in.close();
		out.close();
	}
}
