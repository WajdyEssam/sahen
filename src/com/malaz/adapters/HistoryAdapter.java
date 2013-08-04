package com.malaz.adapters;

import java.util.List;

import com.malaz.app.R;
import com.malaz.model.History;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {

	private Context context;
	private List<History> histories;
	
	public HistoryAdapter(Context context, List<History> histories) {
		this.context = context;
		this.histories = histories;
	}
	
	private class ViewHolder {
		TextView operationView;
		TextView descriptionView;
		TextView timeView;
	}
	
	@Override
	public int getCount() {
		return this.histories.size();
	}

	@Override
	public Object getItem(int position) {
		return this.histories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return this.histories.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null ) {
			convertView = inflater.inflate(R.layout.history_list, null);
			holder = new ViewHolder();
			holder.operationView = (TextView) convertView.findViewById(R.id.type);
			holder.descriptionView = (TextView) convertView.findViewById(R.id.desc);
			holder.timeView = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		History history = (History) getItem(position);
		holder.operationView.setText(history.getOperation().getArabicDescription());
		holder.descriptionView.setText(history.getDetials());
		holder.timeView.setText(history.getTime());
		
		return convertView;
	}

}
