package com.deerlight.androiddaily;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Created by taaze on 2016/12/9.
 */
public class RecyclerViewAdpter extends RecyclerView.Adapter<ViewHolder>{

	ArrayList<DailyItem> dailyList;
	Context context;
//	private static final int DELETE_USELESS_STRING_ON_TITLE  = 18;

	public RecyclerViewAdpter(Context context, ArrayList<DailyItem> dailyList) {
		this.context = context;
		this.dailyList = dailyList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.title_item, parent, false);
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		final WebViewDialog webViewDialog = new WebViewDialog(context);
		final String titleItem = dailyList.get(position).getTitle();
		final String linkItem = dailyList.get(position).getLink();
		Log.v("日報","title: "+titleItem);
		Log.v("日報","link: "+linkItem);
		holder.titleName.setText(titleItem);
		holder.mCardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 webViewDialog.popUpWebView(titleItem,linkItem);
			}
		});
	}

	@Override
	public int getItemCount() {
		return dailyList.size();
	}
}
