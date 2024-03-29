package com.example.administrator.guardian.utils;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by 경태 on 2016-06-01.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

	@Override
	public void onTokenRefresh() {
		Log.e("refresh","refresh");
		// Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
		Intent intent = new Intent(this, RegistrationIntentService.class);
		startService(intent);
	}
}