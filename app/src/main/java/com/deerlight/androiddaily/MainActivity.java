package com.deerlight.androiddaily;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    AsyncTask mAsyncTask;
    ArrayList<DailyItem> dailyList;
    RecyclerView titleRecyclerview;
    LinearLayout RSS_loading;
    DailyItem dailyItem;
    String extra_data = "";
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleRecyclerview = (RecyclerView) findViewById(R.id.titleRecyclerview);
        RSS_loading = (LinearLayout) findViewById(R.id.RSS_loading);
        setSupportActionBar(toolbar);
        FloatingActionButton fab_next = (FloatingActionButton) findViewById(R.id.fab_next);
        FloatingActionButton fab_forward = (FloatingActionButton) findViewById(R.id.fab_forward);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopTheDaily();
                if (page < 5) {
                    page++;
                    extra_data += "/index.php/Index/index/p/" + page;
                    RunTheDaily();
                }
            }
        });
        fab_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopTheDaily();
                if (page > 1) {
                    page--;
                    extra_data += "/index.php/Index/index/p/" + page;
                    RunTheDaily();
                }
            }
        });
    }

    public class getTitleData extends AsyncTask<String, Void, ArrayList<DailyItem>> {

        @Override
        protected ArrayList<DailyItem> doInBackground(String... urlPath) {
            try {
                String title;
                String url = urlPath[0];
                String pageUrl = "http://androidblog.cn";
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.select("li");
                for (Element e : links) {
                    dailyItem = new DailyItem();
                    title = e.select("p.l-name").toString();
                    String link = pageUrl + e.select("a").attr("href").toString();
                    int startStr = title.indexOf('>')+1; //’>‘的下一個開始
                    int endStr = title.lastIndexOf('<');
                    title = title.substring(startStr, endStr);
                    dailyItem.setTitle(title);
                    dailyItem.setLink(link);
                    dailyList.add(dailyItem);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dailyList;
        }

        @Override
        protected void onPreExecute() {
            RSS_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<DailyItem> linklist) {
            titleRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            titleRecyclerview.setAdapter(new RecyclerViewAdpter(MainActivity.this, dailyList));
            RSS_loading.setVisibility(View.GONE);
            super.onPostExecute(linklist);
        }
    }

    @Override
    protected void onStop() {
        StopTheDaily();
        super.onStop();
    }

    @Override
    protected void onResume() {
        if (isConnected()) {
            RunTheDaily();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("網路無法連線")
                    .setMessage("請檢查網路是否已經連線")
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction("android.settings.WIFI_SETTINGS");
                            startActivityForResult(intent, MainActivity.RESULT_OK);
                        }
                    })
                    .show();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void RunTheDaily() {
        Log.v("page","page: "+page);
        if (mAsyncTask == null) {
            dailyList = new ArrayList<>();
            String[] url = {"http://androidblog.cn" + extra_data};
            Log.v("url","url: "+ url[0]);
            mAsyncTask = new getTitleData();
            mAsyncTask.execute(url);
        }
    }

    private void StopTheDaily() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
            dailyList = null;
            dailyItem = null;
            extra_data = "";
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
