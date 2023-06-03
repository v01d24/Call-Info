package com.v01d24.callinfo.ui;

import android.content.Context;
import android.os.AsyncTask;

import com.v01d24.callinfo.db.CallInfo;
import com.v01d24.callinfo.db.DatabaseOpener;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecentCallsAsyncTask extends AsyncTask<Void, Void, List<CallInfo>> {

    private final WeakReference<Context> contextReference;
    private final WeakReference<RecentCallsAdapter> adapterReference;

    public RecentCallsAsyncTask(Context context, RecentCallsAdapter adapter) {
        this.contextReference = new WeakReference<>(context);
        this.adapterReference = new WeakReference<>(adapter);
    }

    @Override
    protected List<CallInfo> doInBackground(Void... voids) {
        Context context = contextReference.get();
        if (context == null) {
            return null;
        }
        return new DatabaseOpener(context)
                .openReadable()
                .getCallInfoRepository()
                .getRecent(10);
    }

    @Override
    protected void onPostExecute(List<CallInfo> result) {
        RecentCallsAdapter adapter = adapterReference.get();
        if (adapter != null) {
            adapter.setRecentCalls(result);
        }
    }
}
