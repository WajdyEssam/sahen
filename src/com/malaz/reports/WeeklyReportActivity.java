package com.malaz.reports;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.malaz.app.R;
import com.malaz.app.R.id;
import com.malaz.app.R.layout;
import com.malaz.database.HistoryDB;
import com.malaz.model.History;
import com.malaz.util.Constants;
import com.malaz.util.DateUtil;
import com.malaz.util.LangUtil;
import com.malaz.util.DateUtil.WeekRangeGenerator;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class WeeklyReportActivity extends Activity {
	
	private LinearLayout lay;
	private HorizontalListView listview;
	
	private double highest;
	
	private int[] chargingHight; 
	private int[] transfereHight;
	
	private String[] datelabel = {"Sun","Mon","Tue", "Wed","Thu","Fri","Sat"};
	
	private Double[] chargeAmounts = new Double[datelabel.length];
	private Double[] transfereAmounts  = new Double[datelabel.length];
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        System.out.println("CREATING WeeklyReportActivity");
        LangUtil.setLocale(this);		
        setContentView(R.layout.activity_weekly_report);
		//this.initializingActionBar();
   
        fillChartLables();
        initData();
        updateSizeInfo();
    }
    
    private void fillChartLables() {
    	HistoryDB db = HistoryDB.getInstance(this);
    	WeekRangeGenerator weekRange = DateUtil.WeekRangeGenerator.generate();
    	
    	String fromDate = weekRange.getFirstDate();
    	String lastDate = weekRange.getLastDate();
    	
    	System.out.println("Get Yearly: " + fromDate + " AND " + lastDate);
    	
    	List<History> histories = db.getHistoriesBetween(fromDate, lastDate);
    	for(History history: histories) {
    		addRow(String.valueOf(history.getAmount()), history.getOperation().getEnglishDescription(), history.getTime());
    	}
    	
    	for(int i=0; i<datelabel.length; i++) {
        	chargeAmounts[i] = getChargeForDay(i, histories);
        	transfereAmounts[i] = getTransfereForDay(i, histories);
        }    	
    }
    
    private double getChargeForDay(int dayIndex, List<History> histories) {
    	if ( histories.isEmpty() )
    		return 0;
    	
    	double charging = 0;
    	for(History history: histories) {
    		try {
				Date time = DateUtil.formatStringDate(history.getTime());
				int historyDay = DateUtil.getDayIndex(time);
				
				boolean chargingOperation = history.getOperation().getId().equals(Constants.CHARGING_OPERATION);
				boolean happenOnSameDay = dayIndex == historyDay-1;
				
				if ( chargingOperation &&  happenOnSameDay) {
					charging += history.getAmount();					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return charging;
    }
    
    private double getTransfereForDay(int dayIndex, List<History> histories) {
    	if ( histories.isEmpty() )
    		return 0;
    	
    	double transfere = 0;
    	for(History history: histories) {
    		try {
				Date time = DateUtil.formatStringDate(history.getTime());
				int historyDay = DateUtil.getDayIndex(time);

				boolean transfereOperation = history.getOperation().getId().equals(Constants.SENDING_BALANCE_OPERATION);
				boolean happenOnSameDay = dayIndex == historyDay-1;
				
				if ( transfereOperation &&  happenOnSameDay) {
					transfere += history.getAmount();					
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return transfere;
    }

    private void addRow(String cell1, String cell2, String cell3) {
    	TableLayout table = (TableLayout) findViewById(R.id.weekly_table);
    	TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.partial_table_row, null);

    	TextView view1 = (TextView) getLayoutInflater().inflate(R.layout.partial_text_view, null);
    	view1.setText(cell1);

    	TextView view2 = (TextView) getLayoutInflater().inflate(R.layout.partial_text_view, null);
    	view2.setText(cell2);    	
    	
    	TextView view3 = (TextView) getLayoutInflater().inflate(R.layout.partial_text_view, null);
    	view3.setText(cell3);
    	
    	row.addView(view1);
    	row.addView(view2);
    	row.addView(view3);
    	
    	table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));    	
    }    
    
    private void initData() {
    	lay = (LinearLayout)findViewById(R.id.weeklylinearlayout);
		listview = (HorizontalListView) findViewById(R.id.weekly_listview);
        
		// get the highest value
		List<Double> elements = new ArrayList<Double>();
		elements.addAll(Arrays.asList(chargeAmounts));
		elements.addAll(Arrays.asList(transfereAmounts));				
        highest = (Collections.max(elements));

        transfereHight = new int[transfereAmounts.length];
        chargingHight= new int[chargeAmounts.length];
    }
    
    private class WeeklyBaseAdapter extends BaseAdapter {
        Activity context;
        String[] array;
        
        public WeeklyBaseAdapter(Activity context,String[] arr) {
            this.context=context;
            this.array = arr;
        }

        public int getCount() {
            return array.length;
        }

        public Object getItem(int position) {
            return array[position];
        }

        public long getItemId(int position) {
            return array.length;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View row=null;
            LayoutInflater inflater=context.getLayoutInflater();
            row=inflater.inflate(R.layout.weekly_simplerow, null);
            
            DecimalFormat df = new DecimalFormat("#.#");
            final TextView title	=	(TextView)row.findViewById(R.id.weekly_title);
            TextView tvcol1	=	(TextView)row.findViewById(R.id.weekly_colortext01);
            TextView tvcol2	=	(TextView)row.findViewById(R.id.weekly_colortext02);
            
            TextView gt		=	(TextView)row.findViewById(R.id.weekly_text01);
            TextView nt		=	(TextView)row.findViewById(R.id.weekly_text02);
            
            tvcol1.setHeight(chargingHight[position]);
            tvcol2.setHeight(transfereHight[position]);
            title.setText(datelabel[position]);
            
            gt.setText(df.format(chargeAmounts[position])+" SD");
            nt.setText(df.format(transfereAmounts[position])+" SD");
            
            tvcol1.setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					Toast.makeText(WeeklyReportActivity.this, "Day: "+ title.getText().toString()+"\nCharing: "+chargeAmounts[position], Toast.LENGTH_SHORT).show();
				}
			});
            
            tvcol2.setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					Toast.makeText(WeeklyReportActivity.this, "Day: "+ title.getText().toString()+"\nTransfere: "+transfereAmounts[position], Toast.LENGTH_SHORT).show();
				}
			});
            
        return row;
        }
    }

	private void updateSizeInfo() {
		int h;
		
		if(getResources().getConfiguration().orientation == 1)
		{
			h = (int) (lay.getHeight()*0.5);
			if(h == 0)
			{
				h = 200;
			}
		}
		else
		{
			h = (int) (lay.getHeight()*0.3);
			if(h == 0)
			{
				h = 130;
			}
		}
		
		for(int i=0;i<transfereAmounts.length;i++)  {
			transfereHight[i] = (int)((h*transfereAmounts[i])/highest);
    		chargingHight[i] = (int)((h*chargeAmounts[i])/highest);
    		
    		System.out.println("Transfere Value [i] "+ transfereHight[i] +  "Charging Value[i] " + chargingHight[i]);
    	}		
		
    	listview.setAdapter(new WeeklyBaseAdapter(this,datelabel));
	}
}