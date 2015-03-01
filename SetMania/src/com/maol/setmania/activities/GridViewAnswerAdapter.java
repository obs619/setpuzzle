package com.maol.setmania.activities;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maol.setmania.R;
import com.maol.setmania.models.Answer;

public class GridViewAnswerAdapter extends BaseAdapter{

	private List<Answer> items;
	private Context context;
	private Typeface tfGadugib;
	
	public GridViewAnswerAdapter(Context context, List<Answer> items) {
		super();
		this.context = context;
		this.items = items;
		this.tfGadugib = Typeface.createFromAsset(context.getAssets(),
			      "fonts/gadugib.ttf");
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.gridview_answer_item, parent, false);

			viewHolder = new ViewHolder();
			
			viewHolder.solvedLayout = (LinearLayout) convertView.findViewById(R.id.solved_layout);
			viewHolder.unsolvedLayout = (LinearLayout) convertView.findViewById(R.id.unsolved_layout);
			 
			viewHolder.firstans = (TextView) convertView.findViewById(R.id.txtfirstans);
			viewHolder.secondans = (TextView) convertView.findViewById(R.id.txtsecondans);
			viewHolder.thirdans = (TextView) convertView.findViewById(R.id.txtthirdans);
			
			viewHolder.firstans.setTypeface(tfGadugib);
			viewHolder.secondans.setTypeface(tfGadugib);
			viewHolder.thirdans.setTypeface(tfGadugib);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.firstans.setText(items.get(position).getAnswer()[0] + 1 + "");
		viewHolder.secondans.setText(items.get(position).getAnswer()[1] + 1 + "");
		viewHolder.thirdans.setText(items.get(position).getAnswer()[2] + 1 + "");
		
		
		if(items.get(position).isSolved()) {
			viewHolder.solvedLayout.setVisibility(View.VISIBLE);
			viewHolder.unsolvedLayout.setVisibility(View.GONE);
		}
		else {
			viewHolder.unsolvedLayout.setVisibility(View.VISIBLE);
			viewHolder.solvedLayout.setVisibility(View.GONE);
		}
			
		
		return convertView;
	}

	class ViewHolder {
		LinearLayout solvedLayout;
		LinearLayout unsolvedLayout;
		TextView firstans;
		TextView secondans;
		TextView thirdans;
	}
	
}