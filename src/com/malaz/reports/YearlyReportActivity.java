package com.malaz.reports;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.malaz.app.R;
import com.malaz.database.HistoryDB;
import com.malaz.model.History;
import com.malaz.util.Constants;
import com.malaz.util.DateUtil;
import com.malaz.util.DateUtil.DateRange;
import com.malaz.util.DateUtil.MonthlyRangeGenerator;
import com.malaz.util.DateUtil.WeekRangeGenerator;
import com.malaz.util.DateUtil.YearRangeGenerator;
import com.malaz.util.LangUtil;

public class YearlyReportActivity extends Activity {

	private LinearLayout lay;
	HorizontalListView listview;
	
	private double highest;
	
	private int[] chargingHight; 
	private int[] transfereHight;
	
	private String[] datelabel = {"2013","2014","2015", "2016","2017"};	
	private Double[] chargeAmounts = new Double[datelabel.length];
	private Double[] transfereAmounts  = new Double[datelabel.length];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		System.out.println("CREATING YearlyReportActivity");
		LangUtil.setLocale(this);		
		setContentView(R.layout.activity_yearly_report);
		
		fillChartLables();
        initData();
        updateSizeInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_yearly_report, menu);
		return true;
	}

	private void fillChartLables() {
    	HistoryDB db = HistoryDB.getInstance(this);
    	YearRangeGenerator yearlyGen = YearRangeGenerator.getInstance();    	
    	List<DateRange> ranges= yearlyGen.getRanges();
    	
    	int i=0;
    	for(DateRange range: ranges) {
    		System.out.println("Get Yearly: " + DateUtil.formatDate(range.firstDate) + " AND " + DateUtil.formatDate(range.endDate));
    		
    		List<History> histories = db.getHistoriesBetween(DateUtil.formatDate(range.firstDate), DateUtil.formatDate(range.endDate));
    		
    		double countOfCharge = 0;
    		double countOfTransfere = 0;
        	for(History history: histories) {        		
        		if ( history.getOperation().getId() == Constants.CHARGING_OPERATION ) {
        			countOfCharge += history.getAmount();
        		}
        		else if ( history.getOperation().getId() == Constants.SENDING_BALANCE_OPERATION ) {
        			countOfTransfere += history.getAmount();
        		}
        	}
        	
        	if ( countOfCharge != 0 ) {
        		String message = Constants.CHARGING_OPERATION_ENGLISH_MSG;
        		addRow(String.valueOf(countOfCharge), message, DateUtil.yearFormat(range.firstDate));
        	}
        	
        	if ( countOfTransfere != 0 ) {
        		String message = Constants.SENDING_BALANCE_OPERATION_ENGLISH_MSG;
        		addRow(String.valueOf(countOfTransfere), message, DateUtil.yearFormat(range.firstDate));        		
        	}
    		
        	chargeAmounts[i] = countOfCharge;
        	transfereAmounts[i] = countOfTransfere;
        	i++;
    	}           
    } 

    private void addRow(String cell1, String cell2, String cell3) {
    	TableLayout table = (TableLayout) findViewById(R.id.yearly_table);
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
    	lay = (LinearLayout)findViewById(R.id.yearlylinearlayout);
		listview = (HorizontalListView) findViewById(R.id.yearly_listview);
        
		// get the highest value
		List<Double> elements = new ArrayList<Double>();
		elements.addAll(Arrays.asList(chargeAmounts));
		elements.addAll(Arrays.asList(transfereAmounts));				
        highest = (Collections.max(elements));

        transfereHight = new int[transfereAmounts.length];
        chargingHight= new int[chargeAmounts.length];
    }
    
    private class YearlyBaseAdapter extends BaseAdapter {
        Activity context;
        String[] array;
        
        public YearlyBaseAdapter(Activity context,String[] arr) {
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
            row=inflater.inflate(R.layout.yearly_simplerow, null);
            
            DecimalFormat df = new DecimalFormat("#.#");
            final TextView title	=	(TextView)row.findViewById(R.id.yearly_title);
            TextView tvcol1	=	(TextView)row.findViewById(R.id.yearly_colortext01);
            TextView tvcol2	=	(TextView)row.findViewById(R.id.yearly_colortext02);
            
            TextView gt		=	(TextView)row.findViewById(R.id.yearly_text01);
            TextView nt		=	(TextView)row.findViewById(R.id.yearly_text02);
            
            tvcol1.setHeight(chargingHight[position]);
            tvcol2.setHeight(transfereHight[position]);
            title.setText(datelabel[position]);
            
            gt.setText(df.format(chargeAmounts[position])+" SD");
            nt.setText(df.format(transfereAmounts[position])+" SD");
            
            tvcol1.setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					Toast.makeText(YearlyReportActivity.this, "Day: "+ title.getText().toString()+"\nCharing: "+chargeAmounts[position], Toast.LENGTH_SHORT).show();
				}
			});
            
            tvcol2.setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					Toast.makeText(YearlyReportActivity.this, "Day: "+ title.getText().toString()+"\nTransfere: "+transfereAmounts[position], Toast.LENGTH_SHORT).show();
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
		
    	listview.setAdapter(new YearlyBaseAdapter(this,datelabel));
	}
}
