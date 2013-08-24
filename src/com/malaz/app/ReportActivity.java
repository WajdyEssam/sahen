package com.malaz.app;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.malaz.util.LangUtil;

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

public class ReportActivity extends Activity {
	
	private LinearLayout lay;
	HorizontalListView listview;
	private double highest;
	private int[] grossheight; 
	private int[] netheight;
	private Double[] grossSal= {15000.0,15000.0,15000.25,15000.1,15000.0,15000.0,15000.0,15000.0,15000.25,15000.1,15000.0,15000.0};
	private Double[] netSal = {12000.0,13000.0,14000.25,10000.1,10000.0,9000.0,12000.0,13000.0,14000.25,10000.1,10000.0,9000.0};
	private String[] datelabel = {"Jan 12","Feb 12","Mar 12","Apr 12","May 12","Jun 12","Jul 12","Aug 12","Sep 12","Oct 12","Nov 12","Dec 12"};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LangUtil.setLocale(this);		
        setContentView(R.layout.activity_report);
		//this.initializingActionBar();
        
        addData();  
        initData();
    }
            
    private void addData() {
    	addRow("Wajdy Essam", "عملية سحب", new Date().toGMTString());
    	addRow("Ahmed Essam", "Charge", "2013/8/24");
    	addRow("Wajdy Essam", "عملية سحب", new Date().toGMTString());
    	addRow("Ahmed Essam", "Charge", "2013/8/24");
    	addRow("Wajdy Essam", "عملية سحب", new Date().toGMTString());
    	addRow("Ahmed Essam", "Charge", "2013/8/24");
    	addRow("Wajdy Essam", "عملية سحب", new Date().toGMTString());
    	addRow("Ahmed Essam", "Charge", "2013/8/24");
    	addRow("Wajdy Essam", "عملية سحب", new Date().toGMTString());
    	addRow("Ahmed Essam", "Charge", "2013/8/24");
    	addRow("Wajdy Essam", "عملية سحب", new Date().toGMTString());
    	addRow("Ahmed Essam", "Charge", "2013/8/24");
    }
    
    private void addRow(String cell1, String cell2, String cell3) {
    	TableLayout table = (TableLayout) findViewById(R.id.table);
    	
    	TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.partial_table_row, null);

    	TextView view1 = (TextView) getLayoutInflater().inflate(R.layout.partial_text_view, null);
    	view1.setText(cell1);

    	TextView view2 = (TextView) getLayoutInflater().inflate(R.layout.partial_text_view, null);
    	view2.setText(cell2);   
    	//view2.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
    	
    	TextView view3 = (TextView) getLayoutInflater().inflate(R.layout.partial_text_view, null);
    	view3.setText(cell3);
    	
    	row.addView(view1);
    	row.addView(view2);
    	row.addView(view3);
    	
    	table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));    	
    }
    
    
    // -----
    
    private void initData() {
    	lay = (LinearLayout)findViewById(R.id.linearlay);
		listview = (HorizontalListView) findViewById(R.id.listview);
        
		List<Double> b = Arrays.asList(grossSal);
        highest = (Collections.max(b));

        netheight = new int[netSal.length];
        grossheight= new int[grossSal.length];
    }
    
    public class bsAdapter extends BaseAdapter
    {
        Activity cntx;
        String[] array;
        public bsAdapter(Activity context,String[] arr)
        {
            // TODO Auto-generated constructor stub
            this.cntx=context;
            this.array = arr;

        }

        public int getCount()
        {
            // TODO Auto-generated method stub
            return array.length;
        }

        public Object getItem(int position)
        {
            // TODO Auto-generated method stub
            return array[position];
        }

        public long getItemId(int position)
        {
            // TODO Auto-generated method stub
            return array.length;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View row=null;
            LayoutInflater inflater=cntx.getLayoutInflater();
            row=inflater.inflate(R.layout.simplerow, null);
            
            DecimalFormat df = new DecimalFormat("#.##");
            final TextView title	=	(TextView)row.findViewById(R.id.title);
            TextView tvcol1	=	(TextView)row.findViewById(R.id.colortext01);
            TextView tvcol2	=	(TextView)row.findViewById(R.id.colortext02);
            
            TextView gt		=	(TextView)row.findViewById(R.id.text01);
            TextView nt		=	(TextView)row.findViewById(R.id.text02);
            
            tvcol1.setHeight(grossheight[position]);
            tvcol2.setHeight(netheight[position]);
            title.setText(datelabel[position]);
            
            gt.setText(df.format(grossSal[position]/1000)+" k");
            nt.setText(df.format(netSal[position]/1000)+" k");
            
            tvcol1.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Toast.makeText(ReportActivity.this, "Month/Year: "+title.getText().toString()+"\nGross Sal: "+grossSal[position], Toast.LENGTH_SHORT).show();
				}
			});
            
            tvcol2.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Toast.makeText(ReportActivity.this, "Month/Year: "+title.getText().toString()+"\nNet Sal: "+netSal[position], Toast.LENGTH_SHORT).show();
				}
			});
            
        return row;
        }
    }
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        updateSizeInfo();
    }
	
	private void updateSizeInfo() {		
		
		/** This is onWindowFocusChanged method is used to allow the chart to
		 * update the chart according to the orientation.
		 * Here h is the integer value which can be updated with the orientation
		 */
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
		for(int i=0;i<netSal.length;i++) 
    	{
			netheight[i] = (int)((h*netSal[i])/highest);
    		grossheight[i] = (int)((h*grossSal[i])/highest);
    		System.out.println("net width[i] "+netheight[i]+"gross width[i] "+grossheight[i]);
    	}
    	listview.setAdapter(new bsAdapter(this,datelabel));
	}
}