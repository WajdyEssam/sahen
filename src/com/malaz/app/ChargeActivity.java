package com.malaz.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.malaz.database.Database;
import com.malaz.services.SIMService;
import com.malaz.services.ServiceFactory;
import com.malaz.util.AlertUtil;
import com.malaz.util.CallUtil;
import com.malaz.util.LangUtil;

public class ChargeActivity extends BaseActivity {

	private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
	private static final String lang = "eng";
	private static final String TAG = "ChargeActivity";

	private static final int CAMERA_CAPTURE = 1;
	private static final int PIC_CROP = 2;
	private static final int EVENT_VOICE_CAPTURE = 3;
	
	protected Button ocrButton;
	protected EditText numberEditText;
	//protected String _path;
	protected boolean _taken;

	private Uri picUri;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LangUtil.setLocale(this);
		
		setContentView(R.layout.activity_charge);
		this.initializingActionBar();
		
		createImageAndInstallEngineFile();
		
		numberEditText = (EditText) findViewById(R.id.field);
		ocrButton = (Button) findViewById(R.id.button);		
		ocrButton.setOnClickListener(new ButtonClickHandler());
				
		Button voiceButton = (Button) findViewById(R.id.voice_button);
		voiceButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				captureVoice();
			}
		});
		
		//_path = DATA_PATH + "/ocr.jpg";
	}
	
	public void chargeButtonClicked(View view) {
		final String number = numberEditText.getText().toString();	
		
		SIMService service = new ServiceFactory(this).getChargeService(number);
		
		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(ChargeActivity.this);
			return;
		}
		
		boolean state = CallUtil.charge(this, service);
		
		if ( state ) {
			askForCharingAmount(number, this);			
		}
		else {
			Toast.makeText(this, "Error in Charging Balance", Toast.LENGTH_LONG).show();
		}
	}
	
	private void saveCharging(String number, int amount) {
		Database.saveChargingBalance(this, number, amount);
	}
	
	public void askForCharingAmount(final String number, final Context context) {
		final NumberPicker np = new NumberPicker(ChargeActivity.this);
		
		String[] nums = new String[1001];
        for(int i=0; i<nums.length; i++)
        	nums[i] = Integer.toString(i);

        np.setMinValue(1);
        np.setMaxValue(nums.length-1);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);
        np.setValue(5);
        
		AlertDialog.Builder alert = new AlertDialog.Builder(ChargeActivity.this);
		
		String title = this.getString(R.string.title_charge_dialog);
		String body = this.getString(R.string.body_charge_dialog);

		alert.setTitle(title);
		alert.setMessage(body);

		alert.setView(np);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int amount = np.getValue();
				saveCharging(number, amount);	
				Toast.makeText(ChargeActivity.this, "Charging Balance Done!", Toast.LENGTH_LONG).show();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(ChargeActivity.this, "Charging Balance Done!", Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				});

		alert.show();
	}
	
	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Log.v(TAG, "Starting Camera app");
			captureAndCrop();
		}
	}

	private void captureAndCrop() {
		try {
        	Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
    	}
        catch(ActivityNotFoundException anfe){
    		String errorMessage = "Whoops - your device doesn't support capturing images!";
    		Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
    		toast.show();
    	}
	}
	
	private void captureVoice() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, this.getString(R.string.voice_ask));
		
		try {
			startActivityForResult(intent, EVENT_VOICE_CAPTURE);
		} catch (ActivityNotFoundException e) {
			// If no recognizer exists, download one from Google Play
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Not Available");
			builder.setMessage("There is no recognition application installed."
					+ " Would you like to download one?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Download, for example, Google Voice Search
							Intent marketIntent = new Intent(Intent.ACTION_VIEW);
							marketIntent.setData(Uri.parse("market://details?id=com.google.android.voicesearch"));
						}
					});
			builder.setNegativeButton("No", null);
			builder.create().show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK) {
    		//user is returning from capturing an image using the camera
    		if(requestCode == CAMERA_CAPTURE){
    			//get the Uri for the captured image
    			picUri = data.getData();
    			//carry out the crop operation
    			performCrop();
    		}
    		//user is returning from cropping the image
    		else if(requestCode == PIC_CROP){
    			//get the returned data
    			Bundle extras = data.getExtras();
    			//get the cropped bitmap
    			Bitmap thePic = extras.getParcelable("data");
    			//retrieve a reference to the ImageView
    			ImageView picView = (ImageView)findViewById(R.id.picture);
    			//display the returned cropped image
    			picView.setImageBitmap(thePic);
    			
    			// convert to text
    			String recognizedText = "";
    			try {
    				
    				//recognizedText = runTessract(thePic);
    				
    				if ( recognizedText.length() != 0 ) {
    					numberEditText.setText(numberEditText.getText().toString().length() == 0 ? recognizedText : numberEditText.getText() + " " + recognizedText);
    					numberEditText.setSelection(numberEditText.getText().toString().length());
    				}
    				else {
    					Toast.makeText(this, "Zero Characters", Toast.LENGTH_LONG).show();
    				}
    			} catch (Exception e) {
    				Toast.makeText(this, "Error on OCR", Toast.LENGTH_LONG).show();
    				e.printStackTrace();
    			}	
    		}
    		else if ( requestCode == EVENT_VOICE_CAPTURE) {
    			final List<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				final CharSequence[] cs = matches.toArray(new CharSequence[matches.size()]);

				int selectedItem = 0;
				new AlertDialog.Builder(this)
				.setTitle("Please Select the text that match your voice")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {;
							}
						})
				.setSingleChoiceItems(cs, selectedItem,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								numberEditText.setText(matches.get(which));

							}
						}).show();
    		}
    	}
    	else {
			Toast.makeText(this, "Operation Canceled", Toast.LENGTH_SHORT).show();
		}
	}

//	private String runTessract(Bitmap bitmap) throws IOException {
//		//bitmap = rotateImage(bitmap);
//		bitmap = ImageUtil.toGrayscale(bitmap);
//		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//		
//		TessBaseAPI baseApi = new TessBaseAPI();		
//		//baseApi.setDebug(true);
//		baseApi.init(DATA_PATH, lang);
//		baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE );
//		baseApi.setImage(bitmap);
//		
//		String recognizedText = baseApi.getUTF8Text();
//		
//		baseApi.end();
//		bitmap.recycle();
//		Runtime.getRuntime().gc();		
//		
//		Log.v(TAG, "OCRED TEXT: " + recognizedText);
//
//		Toast.makeText(this, "TEXT OCRED: " + recognizedText, Toast.LENGTH_LONG).show();
//		
//		if ( lang.equalsIgnoreCase("eng") ) {
//			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", "");
//		}
//		
//		recognizedText = recognizedText.trim();
//		return recognizedText;
//	}
	
	 /**
     * Helper method to carry out crop operation
     */
    private void performCrop(){
    	//take care of exceptions
    	try {
    		//call the standard crop action intent (the user device may not support it)
	    	Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
	    	//indicate image type and Uri
	    	cropIntent.setDataAndType(picUri, "image/*");
	    	//set crop properties
	    	cropIntent.putExtra("crop", "true");
	    	//retrieve data on return
	    	cropIntent.putExtra("return-data", true);
	    	//start the activity - we handle returning in onActivityResult
	        startActivityForResult(cropIntent, PIC_CROP);  
    	}
    	//respond to users whose devices do not support the crop action
    	catch(ActivityNotFoundException anfe){
    		//display an error message
    		String errorMessage = "Whoops - your device doesn't support the crop action!";
    		Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
    		toast.show();
    	}
    }
	
	private void createImageAndInstallEngineFile() {
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		
		// lang.traineddata file with the app (in assets folder)
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
		// This area needs work and optimization
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/eng.traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/eng.traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();
				
				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}
	}

}
