package com.moberella.app;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.moberella.app.R;
import com.moberella.database.Database;
import com.moberella.image.ImageUtil;
import com.moberella.services.SIMService;
import com.moberella.services.ServiceFactory;
import com.moberella.util.AlertUtil;
import com.moberella.util.CallUtil;
import com.moberella.util.Constants;
import com.moberella.util.LangUtil;
import com.moberella.util.Logger;

public class ChargeActivity extends BaseActivity {	
	private final static String lang = "eng";
	private final static String TAG = "ChargeActivity";

	private final static int CAMERA_CAPTURE = 1;
	private final static int CROP_IMAGE = 2;
	private final static int EVENT_VOICE_CAPTURE = 3;
	private final static int CHARGING_BALANCE = 4;
	
	private Button ocrButton;
	private EditText numberEditText;
	private Uri picUri;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LangUtil.setLocale(this);
		
		setContentView(R.layout.activity_charge);
		this.initializingActionBar();
				
		
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
	}
	
	public void chargeButtonClicked(View view) {
		final String number = numberEditText.getText().toString().trim().replaceAll(" ", "");
		
		if ( number.isEmpty() ) {
			Toast.makeText(this, getString(R.string.toast_charge_validation_number), Toast.LENGTH_LONG).show();
			return;
		}
		
		SIMService service = new ServiceFactory(this).getChargeService(number);

		if ( service == null ) {
			AlertUtil.selectSIMTypeDialog(ChargeActivity.this);
			return;
		}
		
		CallUtil.charge(this, service, CHARGING_BALANCE);
	}
	
	private void saveCharging(String number, int amount) {
		Database.saveChargingBalance(this, number, amount);
	}
	
	public void askForCharingAmount(final String number, final Context context) {
		final NumberPicker np = new NumberPicker(this);
		
        np.setMinValue(1);
        np.setMaxValue(200);
        np.setWrapSelectorWheel(false);
 
        np.setValue(5);
        
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		String title = this.getString(R.string.title_charge_dialog);
		String body = this.getString(R.string.body_charge_dialog);

		alert.setTitle(title);
		alert.setMessage(body);

		alert.setView(np);

		alert.setPositiveButton(getString(R.string.ok_message), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int amount = np.getValue();
				saveCharging(number, amount);	
				Toast.makeText(ChargeActivity.this,String.format(getString(R.string.toast_chargeing), number, amount), Toast.LENGTH_LONG).show();
			}
		});

		alert.setNegativeButton(getString(R.string.cancel_message),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});

		alert.show();
	}
	
	public class ButtonClickHandler implements View.OnClickListener {
		public void onClick(View view) {
			Logger.showInDebugOnly(TAG, "Starting Camera app");
			captureImage();
		}
	}

	private void captureImage() {
		try {
        	Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
    	}
        catch(ActivityNotFoundException anfe){
    		Toast toast = Toast.makeText(this, getString(R.string.toast_chargeing_no_capture), Toast.LENGTH_SHORT);
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
			builder.setTitle(getString(R.string.not_available));
			builder.setMessage(getString(R.string.dialog_no_voice_reg_app));
			builder.setPositiveButton(R.string.yes_message,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent marketIntent = new Intent(Intent.ACTION_VIEW);
							marketIntent.setData(Uri.parse("market://details?id=com.google.android.voicesearch"));
						}
					});
			builder.setNegativeButton(getString(R.string.no_message), null);
			builder.create().show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK) {
    		if(requestCode == CAMERA_CAPTURE){
    			picUri = data.getData();
    			performCrop();
    		}
    		else if(requestCode == CROP_IMAGE){
    			Bundle extras = data.getExtras();
    			Bitmap thePic = extras.getParcelable("data");
    			
    			ImageView picView = (ImageView)findViewById(R.id.picture);
    			picView.setVisibility(View.VISIBLE);
    			picView.setImageBitmap(thePic);
    			
    			String recognizedText = "";
    			try {
    				recognizedText = runTessract(thePic);
    				
    				if ( recognizedText.length() != 0 ) {
    					numberEditText.setText(numberEditText.getText().toString().length() == 0 ? recognizedText : numberEditText.getText() + " " + recognizedText);
    					numberEditText.setSelection(numberEditText.getText().toString().length());
    				}
    				else {
    					Toast.makeText(this, getString(R.string.toast_no_digit_by_ocr), Toast.LENGTH_LONG).show();
    				}
    			} catch (Exception e) {
    				Toast.makeText(this, getString(R.string.toast_ocr_error), Toast.LENGTH_LONG).show();
    				e.printStackTrace();
    			}	
    		}
    		else if ( requestCode == EVENT_VOICE_CAPTURE) {
    			final List<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				final CharSequence[] cs = matches.toArray(new CharSequence[matches.size()]);

				int selectedItem = 0;
				new AlertDialog.Builder(this)
				.setTitle(getString(R.string.toast_select_voice))
				.setPositiveButton(getString(R.string.ok_message),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {;
							}
						})
				.setNegativeButton(getString(R.string.cancel_message),
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
    		else if ( requestCode == CHARGING_BALANCE ) {
    			final String number = numberEditText.getText().toString().trim().replaceAll(" ", "");
    			askForCharingAmount(number, this);					
				ImageView picView = (ImageView)findViewById(R.id.picture);
				picView.setVisibility(View.INVISIBLE);
    		}
    	}
    	else {
    		if ( requestCode == CHARGING_BALANCE ) {
    			final String number = numberEditText.getText().toString().trim().replaceAll(" ", "");
    			Toast.makeText(this, getString(R.string.toast_charge_error) + number, Toast.LENGTH_LONG).show();
    		}
    		else {
    			Toast.makeText(this, getString(R.string.operation_not_done), Toast.LENGTH_SHORT).show();
    		}
		}
	}

	private String runTessract(Bitmap bitmap) throws IOException {
		//bitmap = rotateImage(bitmap);
		bitmap = ImageUtil.toGrayscale(bitmap);
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		TessBaseAPI baseApi = new TessBaseAPI();		
		//baseApi.setDebug(true);
		baseApi.init(Constants.APPLICATION_FOLDER, lang);
		baseApi.setPageSegMode(TessBaseAPI.PSM_SINGLE_LINE );
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();
		bitmap.recycle();
		Runtime.getRuntime().gc();		
		
		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", "");
		}
		
		recognizedText = recognizedText.trim();
		return recognizedText;
	}

    private void performCrop(){
    	try {
	    	Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
	    	cropIntent.setDataAndType(picUri, "image/*");
	    	cropIntent.putExtra("crop", "true");
	    	cropIntent.putExtra("return-data", true);
	        startActivityForResult(cropIntent, CROP_IMAGE);  
    	}
    	catch(ActivityNotFoundException anfe){    		
    		Toast toast = Toast.makeText(this, getString(R.string.toast_chargeing_no_crop), Toast.LENGTH_SHORT);
    		toast.show();
    	}
    }
}
