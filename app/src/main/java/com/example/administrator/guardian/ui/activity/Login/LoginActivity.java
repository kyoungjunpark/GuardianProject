package com.example.administrator.guardian.ui.activity.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.guardian.R;
import com.example.administrator.guardian.ui.activity.Manager.ManagerMainActivity;
import com.example.administrator.guardian.ui.activity.Senior.SeniorTabActivity;
import com.example.administrator.guardian.ui.activity.Volunteer.VolunteerTabActivity;
import com.example.administrator.guardian.utils.ConnectServer;
import com.example.administrator.guardian.utils.GlobalVariable;
import com.example.administrator.guardian.utils.RegistrationIntentService;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Context mContext;
    private Button loginbutton;
    private Button joinbutton;
    public EditText idEditText;
    public EditText pwEditText;
    private GlobalVariable globalVariable;
    private Handler messageHandler;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int responseStatus = 0;
    String user_type;
    String user_name;
    private final int LOGIN_PERMITTED = 200;

    public void onStop(){
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        globalVariable = (GlobalVariable)getApplication();

        Intent serviceIntent = new Intent(getApplicationContext(), RegistrationIntentService.class);
        startService(serviceIntent);
        Log.d("bugfix0", "doInBackground: "+globalVariable.getToken());
        Log.d("bugfix0", "doInBackground: "+globalVariable.getToken());

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);

        Log.d("bugfix00", "doInBackground: "+pref.getString("token", ""));
        Log.d("bugfix00", "doInBackground: "+pref.getString("token", ""));

        mContext = this;
        messageHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    new LovelyInfoDialog(mContext)
                            .setTopColorRes(R.color.wallet_holo_blue_light)
                            .setIcon(R.mipmap.ic_not_interested_black_24dp)
                            //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                            .setTitle("서버와의 통신 문제")
                            .setMessage((String)msg.obj)
                            .show();
                }
            }};

        idEditText = (EditText) findViewById(R.id.idEditText);
        idEditText.setText(pref.getString("idEditText", ""));
        pwEditText = (EditText) findViewById(R.id.pwEditText);
        pwEditText.setText(pref.getString("pwEditText", ""));

        loginbutton = (Button)findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent firstMainActivity = new Intent(getApplicationContext(), SeniorMainActivity.class);
                if(idEditText.getText().toString().equals("")
                        || pwEditText.getText().toString().equals("")){
                    new LovelyInfoDialog(v.getContext())
                            .setTopColorRes(R.color.wallet_holo_blue_light)
                            .setIcon(R.mipmap.ic_not_interested_black_24dp)
                            .setTitle("경고")
                            .setMessage("아이디와 비밀번호를 입력해주세요.")
                            .show();
                } else{
                    sendLoginInfoToServer();
                }

            }
        });

        joinbutton = (Button)findViewById(R.id.joinbutton);
        joinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Go_joinActivity = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(Go_joinActivity);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void sendLoginInfoToServer()
    {
        //Send join data to server
        ConnectServer.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {
            String login_id;
            String login_pw;

            @Override
            protected void onPreExecute() {
                //Collect the input data



                login_id = idEditText.getText().toString();
                login_pw = pwEditText.getText().toString();
            }
            @Override
            protected void onPostExecute(Boolean params) {
                if(responseStatus == 1){
                    getUserInfo();

                }

            }

            @Override
            protected Boolean doInBackground(String... params) {
                URL obj = null;
                try {
                    obj = new URL(ConnectServer.getInstance().getURL("Sign_In"));
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("login_id", login_id);
                    jsonObj.put("login_pw", login_pw);
                    String token;
                    if(pref.getString("token","").compareTo("")!= 0 ){
                        token = new String(pref.getString("token",""));
                        Log.d("bugfix1", "doInBackground: "+token);
                    }else {
                        token = globalVariable.getToken();
                        Log.d("bugfix2", "doInBackground: "+token);

                    }

                    jsonObj.put("token", token);

                    Log.d(TAG, "doInBackground: "+pref.getString("token",""));
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(jsonObj.toString());
                    wr.flush();

                    BufferedReader rd =null;

                    if(con.getResponseCode() == LOGIN_PERMITTED){


                        responseStatus = 1;
                        //Sign up Success
                        rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                        String resultValues = rd.readLine();
                        Log.d(TAG,"Login Success: " + resultValues);
                        JSONObject object = new JSONObject(resultValues);
                        Boolean login_status = (Boolean) object.get("login_status");
                        //String token = (String) object.get("token");
                        user_type = (String) object.get("user_type");
                        Log.d(TAG,"Received Data: " + login_status + "//" + user_type);
                        Log.d("bugfix3", "doInBackground: "+globalVariable.getToken());
                        ConnectServer.getInstance().setToken(globalVariable.getToken());
                        ConnectServer.getInstance().setType(user_type);

                        if(user_type.equals("senior")){
                            globalVariable.setLoginType(0);
                            Intent firstMainActivity = new Intent(getApplicationContext(), SeniorTabActivity.class);
                            startActivity(firstMainActivity);
                            finish();
                        } else if(user_type.equals("volunteer")){
                            globalVariable.setLoginType(1);
                            Intent firstMainActivity = new Intent(getApplicationContext(), VolunteerTabActivity.class);
                            startActivity(firstMainActivity);
                            finish();
                        } else if(user_type.equals("manager")){
                            globalVariable.setLoginType(2);
                            Intent firstMainActivity = new Intent(getApplicationContext(), ManagerMainActivity.class);
                            startActivity(firstMainActivity);
                            finish();
                        } else {
                            globalVariable.setLoginType(-1);
                            //error case
                            Log.d(TAG, "UNEXPECTED PATH!!!");
                        }
                }else {
                        responseStatus = 0;
                        //Login Fail
                        rd = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                        String returnMessage = rd.readLine();
                        Log.d(TAG,"Login Fail: " + returnMessage);
                        Message message = messageHandler.obtainMessage(0, returnMessage);
                        message.sendToTarget();
                    }
                } catch(IOException | JSONException e){
                    e.printStackTrace();
                }
                return null;
            }
        });
        ConnectServer.getInstance().execute();

    }

    private void getUserInfo()
    {
        //Send join data to server
        ConnectServer.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {


            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(Boolean params) {
                pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                editor = pref.edit();
                editor.putString("idEditText", idEditText.getText().toString());
                editor.putString("pwEditText", pwEditText.getText().toString());
                editor.putString("token", globalVariable.getToken());
                editor.putString("userType",user_type);
                editor.putString("userName", user_name);
                Log.d(TAG, "onPostExecute: "+editor.toString());
                editor.commit();
            }
            @Override
            protected Boolean doInBackground(String... params) {
                URL obj = null;
                try {
                    obj = new URL(ConnectServer.getInstance().getURL("User_Info"));
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con = ConnectServer.getInstance().setHeader(con);

                    con.setDoOutput(true);

                    JSONObject jsonObj = new JSONObject();
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(jsonObj.toString());
                    wr.flush();

                    BufferedReader rd =null;

                    if(con.getResponseCode() == LOGIN_PERMITTED){
                        responseStatus = 1;
                        //Sign up Success
                        rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                        String resultValues = rd.readLine();
                        JSONObject object = new JSONObject(resultValues);
                        JSONArray jArr  = object.getJSONArray("data");

                        JSONObject c = jArr.getJSONObject(0);
                        globalVariable.setUser_name(c.getString("user_name"));

                    }else {
                        responseStatus = 0;
                        //Login Fail
                        rd = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                        String returnMessage = rd.readLine();
                        Log.d(TAG,"Login Fail: " + returnMessage);
                    }
                } catch(IOException | JSONException e){
                    e.printStackTrace();
                }
                return null;
            }
        });
        ConnectServer.getInstance().execute();

    }
}
