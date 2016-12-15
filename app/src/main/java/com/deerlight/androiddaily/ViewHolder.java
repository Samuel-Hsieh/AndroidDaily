package com.deerlight.androiddaily;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by taaze on 2016/12/9.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
	public TextView titleName;
	public CardView mCardView;

	public ViewHolder(View view) {
		super(view);
		titleName = (TextView) view.findViewById(R.id.titleName);
		mCardView = (CardView) view.findViewById(R.id.mCardView);
	}
}

