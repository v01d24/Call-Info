package com.v01d24.callinfo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.v01d24.callinfo.db.CallInfo;
import com.v01d24.callinfo.registry.PhoneNumberRegistryFactory;

import java.util.Collections;
import java.util.List;

public class RecentCallsAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private static class ViewHolder {
        public TextView numberView;
        public TextView summaryView;
    }

    private final Context context;
    private final LayoutInflater inflater;
    private List<CallInfo> recentCalls;

    public RecentCallsAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recentCalls = Collections.emptyList();
    }

    @Override
    public int getCount() {
        return recentCalls.size();
    }

    @Override
    public Object getItem(int i) {
        return recentCalls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return recentCalls.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_2, null);
            holder = new ViewHolder();
            holder.numberView = (TextView) view.findViewById(android.R.id.text1);
            holder.summaryView = (TextView) view.findViewById(android.R.id.text2);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CallInfo callInfo = recentCalls.get(i);
        holder.numberView.setText(callInfo.getPhoneNumber());
        holder.summaryView.setText(callInfo.getSummary());
        return view;
    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int i, long l) {
        CallInfo callInfo = recentCalls.get(i);
        Uri webPageUri = PhoneNumberRegistryFactory.createDefault()
                .getWebPageUri(callInfo.getPhoneNumber());
        Intent webPageIntent = new Intent(Intent.ACTION_VIEW, webPageUri);
        context.startActivity(webPageIntent);
    }

    public void setRecentCalls(@Nullable List<CallInfo> recentCalls) {
        this.recentCalls = (recentCalls != null) ? recentCalls : Collections.emptyList();
        notifyDataSetChanged();
    }
}
