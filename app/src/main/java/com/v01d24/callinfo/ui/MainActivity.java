package com.v01d24.callinfo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.v01d24.callinfo.CallInfoService;
import com.v01d24.callinfo.R;
import com.v01d24.callinfo.Settings;

public class MainActivity extends AppCompatActivity {
    private RecentCallsAdapter recentCallsAdapter;
    private RecentCallsAsyncTask recentCallsAsyncTask;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listRecentCalls = findViewById(R.id.list_recent_calls);
        recentCallsAdapter = new RecentCallsAdapter(this);
        listRecentCalls.setOnItemClickListener(recentCallsAdapter);
        listRecentCalls.setAdapter(recentCallsAdapter);

        settings = new Settings(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recentCallsAsyncTask = new RecentCallsAsyncTask(this, recentCallsAdapter);
        recentCallsAsyncTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recentCallsAsyncTask != null) {
            recentCallsAsyncTask.cancel(true);
            recentCallsAsyncTask = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(1).setChecked(settings.isScamCallsRejectionEnabled());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_test_intent) {
            testIntent();
        }
        else if (itemId == R.id.menu_item_reject_scam_calls) {
            boolean checked = !item.isChecked();
            item.setChecked(checked);
            settings.setScamCallsRejectionEnabled(checked);
        }
        return true;
    }

    private void testIntent() {
        final EditText numberInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(R.string.test_intent)
                .setMessage(R.string.enter_phone_number)
                .setView(numberInput)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    String phoneNumber = numberInput.getText().toString();
                    CallInfoService.requestPhoneNumberLookup(getApplicationContext(), phoneNumber);
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }
}
