package com.example.administrator.guardian.ui.activity.Manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.administrator.guardian.R;
import com.example.administrator.guardian.utils.ConnectServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("deprecation")
public class ManagerSeniorInfoTabActivity extends AppCompatActivity {
    private static final String TAG = "ManagerInfoActivity";
    static final int Num_Tab = 4;


    private String senior_id;
    private String senior_name;
    private String senior_birthdate;
    private String senior_gender;
    private String senior_address;
    private String senior_tel;
    private String latitude;
    private String longitude;
    private int high_zone_2;
    private int high_zone_1;
    private int low_zone_1;

    private TabLayout tabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_senior_info_tab);


        Intent intent = getIntent();
        senior_id = intent.getExtras().getString("senior_id");
        Log.d(TAG, senior_id +"");


        getSeniorInfo();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context mContext;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext=context;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new ManagerManageInfoActivity(mContext,senior_id, senior_name, senior_birthdate, senior_gender, senior_address, senior_tel, latitude, longitude);
                case 1:
                    return new ManagerManageActiveActivity(mContext, senior_id);
                case 2:
                    return new ManagerManagePulseInfoActivity(mContext, senior_id, high_zone_2, low_zone_1);
                case 3:
                    return new ManagerManagePulseActivity(mContext, senior_id, high_zone_2, high_zone_1, low_zone_1);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return Num_Tab;
        }

        @SuppressWarnings("DefaultLocale")
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.mstitle_section1);
                case 1:
                    return mContext.getString(R.string.mstitle_section2);
                case 2:
                    return mContext.getString(R.string.mstitle_section3);
                case 3:
                    return mContext.getString(R.string.mstitle_section4);
            }
            return null;
        }
    }
    public void getSeniorInfo(){
        ConnectServer.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {


            @Override
            protected void onPreExecute() {
                toolbar = (Toolbar) findViewById(R.id.mstoolbar);
                setSupportActionBar(toolbar);
                mViewPager = (ViewPager) findViewById(R.id.mscontainer);
                tabLayout = (TabLayout) findViewById(R.id.mstabs);

            }
            protected void onPostExecute(Boolean params) {
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.

                mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager());
                mViewPager.setAdapter(mSectionsPagerAdapter);

                tabLayout.setupWithViewPager(mViewPager);

            }
            @Override
            protected Boolean doInBackground(String... params) {
                URL obj = null;
                try {
                    obj = new URL(ConnectServer.getInstance().getURL("Senior_Info"));
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json; charset=utf8");
                    con.setRequestProperty("Accept", "application/json");

                    con = ConnectServer.getInstance().setHeader(con);
                    con.setDoOutput(true);

                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("senior_id", senior_id);

                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(jsonObj.toString());

                    wr.flush();

                    BufferedReader rd =null;

                    if(con.getResponseCode() == 200){
                        rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                        String resultValues = rd.readLine();

                        JSONObject object = new JSONObject(resultValues);
                        Log.d(TAG, "success: "+object.toString());
                        JSONArray jArr  = object.getJSONArray("data");

                        JSONObject c = jArr.getJSONObject(0);
                        senior_name = c.getString("user_name");
                        senior_birthdate= c.getString("user_birthdate");
                        senior_gender= c.getString("user_gender");
                        senior_address= c.getString("user_address");
                        senior_tel= c.getString("user_tel");
                        latitude= c.getString("latitude");
                        longitude= c.getString("longitude");
                        high_zone_2= c.getInt("high_zone_2");
                        high_zone_1= c.getInt("high_zone_1");
                        low_zone_1= c.getInt("low_zone_1");
                    } else {
                        rd = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                        Log.d(TAG,"fail : " + rd.readLine());
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

        });
        ConnectServer.getInstance().execute();
    }




}
