package com.easyconnect.easyconnectap.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * A pack of helpful getter and setter methods for reading/writing to {@link SharedPreferences}.
 *
 * Values are persisted using {@link EncryptedSharedPreferences} so that sensitive data
 * (the DPP session token and the DPP URI) is encrypted at rest.
 */
final public class SharedPrefsUtils {

    private static SharedPrefsUtils sharedPrefsUtils;
    private String easyconnectTAG = "Easyconnect";
    private String easyConnectDPPTAG = "Easyconnect_dpp";


    public static synchronized SharedPrefsUtils getInstance(){

        if(sharedPrefsUtils == null){

            sharedPrefsUtils = new SharedPrefsUtils();
        }

        return sharedPrefsUtils;
    }

    /**
     * Builds an {@link EncryptedSharedPreferences} instance backed by an AES-256
     * master key from the Android Keystore, so both keys and values are encrypted
     * at rest.
     *
     * A user upgrading from a build that stored these preferences in plaintext (or
     * whose keyset can no longer be decrypted) has an existing file that cannot be
     * opened as encrypted data. Rather than crashing, discard that unreadable file
     * and retry once; any stale value is re-established on the next onboarding.
     */
    private SharedPreferences getEncryptedPreferences(Context context, String name) {
        try {
            return createEncryptedPreferences(context, name);
        } catch (GeneralSecurityException | IOException e) {
            deleteSharedPreferencesFile(context, name);
            try {
                return createEncryptedPreferences(context, name);
            } catch (GeneralSecurityException | IOException retryError) {
                throw new IllegalStateException("Unable to create encrypted shared preferences", retryError);
            }
        }
    }

    private SharedPreferences createEncryptedPreferences(Context context, String name)
            throws GeneralSecurityException, IOException {
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        return EncryptedSharedPreferences.create(
                name,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
    }

    private void deleteSharedPreferencesFile(Context context, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.deleteSharedPreferences(name);
        } else {
            context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
            File prefsFile = new File(context.getApplicationInfo().dataDir + "/shared_prefs/" + name + ".xml");
            if (prefsFile.exists()) {
                prefsFile.delete();
            }
        }
    }

    /**
     * Helper method to clear {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     */
    public void clearSharedPreference(Context context) {

        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Helper method to retrieve a String value from {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    public  String getStringPreference(Context context, String key) {
        String value = null;
        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }


    /**
     * Helper method to write a String value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public  boolean setStringPreference(Context context, String key, String value) {
        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to write a String value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public  boolean setTokenPreference(Context context, String key, String value) {
        SharedPreferences preferences = getEncryptedPreferences(context, easyConnectDPPTAG);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a String value from {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    public  String getTokenPreference(Context context, String key) {
        String value = null;
        SharedPreferences preferences = getEncryptedPreferences(context, easyConnectDPPTAG);
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }


    /**
     * Helper method to retrieve a float value from {@link SharedPreferences}.
     *
     * @param context      a {@link Context} object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public  float getFloatPreference(Context context, String key, float defaultValue) {
        float value = defaultValue;
        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        if (preferences != null) {
            value = preferences.getFloat(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a float value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public  boolean setFloatPreference(Context context, String key, float value) {
        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a long value from {@link SharedPreferences}.
     *
     * @param context      a {@link Context} object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public  long getLongPreference(Context context, String key, long defaultValue) {
        long value = defaultValue;
        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a long value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public  boolean setLongPreference(Context context, String key, long value) {
        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve an integer value from {@link SharedPreferences}.
     *
     * @param context      a {@link Context} object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public int getIntegerPreference(Context context, String key, int defaultValue) {
        int value = defaultValue;
        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write an integer value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setIntegerPreference(Context context, String key, int value) {
        SharedPreferences preferences = getEncryptedPreferences(context, easyconnectTAG);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }
        return false;
    }

    /**
     * Helper method to retrieve a boolean value from {@link SharedPreferences}.
     *
     * @param context      a {@link Context} object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    public boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
        boolean value = defaultValue;
        SharedPreferences preferences = getEncryptedPreferences(context, easyConnectDPPTAG);
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to write a boolean value to {@link SharedPreferences}.
     *
     * @param context a {@link Context} object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    public boolean setBooleanPreference(Context context, String key, boolean value) {
        SharedPreferences preferences = getEncryptedPreferences(context, easyConnectDPPTAG);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }
        return false;
    }
}
