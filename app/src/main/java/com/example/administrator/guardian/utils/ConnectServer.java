package com.example.administrator.guardian.utils;

import android.os.AsyncTask;
import java.net.HttpURLConnection;

/**
 * Created by KJPARK on 2016-05-06.
 *
 * @since 0.1
 */

public class ConnectServer {
    private AsyncTask<String, Void, Boolean> task;

    private String token = "";
    private static String URL = "http://citymouse.xyz";

    private String type;

    private static final ConnectServer instance = new ConnectServer();

    public ConnectServer() {
    }

    public static ConnectServer getInstance() {
        return instance;
    }

    public void setAsncTask(AsyncTask<String, Void, Boolean> task) {
        this.task = task;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void execute() {
        this.task.execute();
    }

    public HttpURLConnection setHeader(HttpURLConnection con) {
        con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
        con.setRequestProperty("Content-Type", "application/json");
        if(this.token == null){
            this.token = "";
        }
        con.setRequestProperty("token", this.token);

        return con;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getURL(String path) {
        String resultURL = null;

        switch (path) {
            case "Sign_Up":
                //The case which is Join Request
                resultURL = URL + "/" + "Sign_Up";
                break;
            case "Sign_In":
                resultURL = URL + "/" + "Sign_In";
                break;
            case "Sign_Out":
                resultURL = URL + "/" +"Sign_Out";
                break;
            case "Senior_Info":
                resultURL = URL + "/" + "Senior_Info";
                break;
            case "Senior_List":
                resultURL = URL + "/" + "Senior_List";
                break;
            case "Set_HR":
                resultURL = URL + "/" + "Set_HR";
                break;
            case "Receive_Activity_Log":
                resultURL = URL + "/" + "Receive_Activity_Log";
                break;
            case "Receive_Avg_Heartrate_Log":
                resultURL = URL + "/" +"Receive_Avg_Heartrate_Log";
                break;
            case "Receive_Heartrate_Log":
                resultURL = URL + "/" + "Receive_Heartrate_Log";
                break;
            case "Receive_Volunteer_Info":
                resultURL = URL + "/" + "Receive_Volunteer_Info";
                break;
            case "Total_Volunteer_Time":
                resultURL = URL + "/" + "Total_Volunteer_Time";
                break;
            case "Request":
                resultURL = URL + "/" + "Request";
                break;
            case "Request_List":
                resultURL = URL + "/" + "Request_List";
                break;
            case "Accept_Request":
                resultURL = URL + "/" + "Accept_Request";
                break;
            case "User_Info":
                resultURL = URL + "/" + "User_Info";
                break;
            case "Finish_Request":
                resultURL = URL + "/" +"Finish_Request";
                break;
            case "Get_Signature":
                resultURL = URL + "/" +"Get_Signature";
                break;
            case "TokenTest":
                resultURL = URL + "/" +"TokenTest";
                break;

        }
        return resultURL;
    }
}
