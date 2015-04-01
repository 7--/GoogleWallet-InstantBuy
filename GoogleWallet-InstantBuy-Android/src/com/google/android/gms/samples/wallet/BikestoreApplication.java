/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.wallet;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class BikestoreApplication extends Application {

    private static final String USER_PREFS = "com.google.android.gms.samples.wallet.USER_PREFS";
    private static final String KEY_USERNAME = "com.google.android.gms.samples.wallet.KEY_USERNAME";
    private String mUserName;

    // Not being saved in shared preferences to let users try new addresses
    // between app invocations
    private boolean mAddressValidForPromo;

    private SharedPreferences mPrefs;

    private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        GLOBAL_TRACKER, // Tracker used only in this app.
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        mUserName = mPrefs.getString(KEY_USERNAME, null);
    }

    public boolean isLoggedIn() {
        return mUserName != null;
    }

    public void login(String userName) {
        mUserName = userName;
        mPrefs.edit().putString(KEY_USERNAME, mUserName).commit();
    }

    public void logout() {
        mUserName = null;
        mPrefs.edit().remove(KEY_USERNAME).commit();
    }

    public String getAccountName() {
        return mPrefs.getString(KEY_USERNAME, null);
    }

    public boolean isAddressValidForPromo() {
        return mAddressValidForPromo;
    }

    public void setAddressValidForPromo(boolean addressValidForPromo) {
        this.mAddressValidForPromo = addressValidForPromo;
    }

}
