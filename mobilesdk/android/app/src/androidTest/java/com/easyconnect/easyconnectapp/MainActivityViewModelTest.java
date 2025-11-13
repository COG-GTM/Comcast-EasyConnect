package com.easyconnect.easyconnectapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.easyconnect.easyconnectap.network.model.DPPResponse;
import com.easyconnect.easyconnectap.network.model.DPPUri;
import com.easyconnect.easyconnectap.network.repository.DPPRepository;
import com.easyconnect.easyconnectap.util.SharedPrefsUtils;
import com.easyconnect.easyconnectapp.app.view.MainActivity;
import com.easyconnect.easyconnectapp.app.view.viewmodel.MainActivityViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import kotlin.jvm.Throws;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.http.HTTP;

import static org.junit.Assert.*;


public class MainActivityViewModelTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityViewModelActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private Context appContext;
    private MainActivityViewModel mainActivityViewModel;

    @Before
    public void setup() throws Exception {

        appContext= InstrumentationRegistry.getTargetContext();
        mainActivityViewModel =new MainActivityViewModel((Application) appContext.getApplicationContext());
    }

    @Test
    public void parseErrorResponse(){

        DPPResponse dppResponse = mainActivityViewModel.getDppResponse(401,"what is the wifi passphrase");
        assertNotNull(dppResponse);
    }

    @Test
    public void parseDPPErrorResponse(){

        DPPUri dppUri = mainActivityViewModel.getDppUri(400,"Bad Request");
        assertNotNull(dppUri);
    }

    @Test
    public void testGetDppResponse_CreatesCorrectResponse() {
        int statusCode = 401;
        String message = "what is the wifi passphrase";

        DPPResponse dppResponse = mainActivityViewModel.getDppResponse(statusCode, message);

        assertNotNull("DPPResponse should not be null", dppResponse);
        assertEquals("Status should match input", String.valueOf(statusCode), dppResponse.getStatus());
        assertEquals("Message should match input", message, dppResponse.getMessage());
        assertNull("Token should be null", dppResponse.getToken());
    }

    @Test
    public void testGetDppResponse_ClearsStoredToken() {
        String tokenKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.token);
        
        SharedPrefsUtils.getInstance().setTokenPreference(appContext, tokenKey, "test-token-123");
        
        mainActivityViewModel.getDppResponse(401, "what is the wifi passphrase");
        
        String storedToken = SharedPrefsUtils.getInstance().getTokenPreference(appContext, tokenKey);
        assertNull("Token should be cleared from SharedPreferences", storedToken);
    }

    @Test
    public void testGetDppResponse_WithDifferentStatusCodes() {
        DPPResponse response400 = mainActivityViewModel.getDppResponse(400, "Bad Request");
        assertEquals("400", response400.getStatus());
        assertEquals("Bad Request", response400.getMessage());

        DPPResponse response404 = mainActivityViewModel.getDppResponse(404, "Not Found");
        assertEquals("404", response404.getStatus());
        assertEquals("Not Found", response404.getMessage());

        DPPResponse response500 = mainActivityViewModel.getDppResponse(500, "Internal Server Error");
        assertEquals("500", response500.getStatus());
        assertEquals("Internal Server Error", response500.getMessage());
    }

    @Test
    public void testGetDppUri_CreatesCorrectUri() {
        int statusCode = 400;
        String message = "Bad Request";

        DPPUri dppUri = mainActivityViewModel.getDppUri(statusCode, message);

        assertNotNull("DPPUri should not be null", dppUri);
        assertEquals("Status should match input", String.valueOf(statusCode), dppUri.getStatus());
        assertEquals("Message should match input", message, dppUri.getMessage());
        assertNull("DPP URI should be null", dppUri.getDpp_uri());
    }

    @Test
    public void testGetDppUri_ClearsStoredDppUri() {
        String dppUriKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.dpp_for_qrcode);
        
        SharedPrefsUtils.getInstance().setStringPreference(appContext, dppUriKey, "DPP:C:81/1;M:00:c0:ca:97:64:ca;K:MDkw...");
        
        mainActivityViewModel.getDppUri(400, "Bad Request");
        
        String storedDppUri = SharedPrefsUtils.getInstance().getStringPreference(appContext, dppUriKey);
        assertNull("DPP URI should be cleared from SharedPreferences", storedDppUri);
    }

    @Test
    public void testGetDppUri_WithDifferentStatusCodes() {
        DPPUri uri307 = mainActivityViewModel.getDppUri(307, "Temporary Redirect");
        assertEquals("307", uri307.getStatus());
        assertEquals("Temporary Redirect", uri307.getMessage());

        DPPUri uri404 = mainActivityViewModel.getDppUri(404, "Not Found");
        assertEquals("404", uri404.getStatus());
        assertEquals("Not Found", uri404.getMessage());

        DPPUri uri500 = mainActivityViewModel.getDppUri(500, "Internal Server Error");
        assertEquals("500", uri500.getStatus());
        assertEquals("Internal Server Error", uri500.getMessage());
    }

    @Test
    public void testGetDppRepository_WithValidConfiguratorCoordinates() {
        String ipKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_ip);
        String portKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_port);
        
        SharedPrefsUtils.getInstance().setStringPreference(appContext, ipKey, "192.168.1.100");
        SharedPrefsUtils.getInstance().setIntegerPreference(appContext, portKey, 8080);

        DPPRepository repository = mainActivityViewModel.getDppRepository();

        assertNotNull("Repository should not be null when valid coordinates are set", repository);
    }

    @Test
    public void testGetDppRepository_WithNullIp() {
        String ipKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_ip);
        String portKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_port);
        
        SharedPrefsUtils.getInstance().setStringPreference(appContext, ipKey, null);
        SharedPrefsUtils.getInstance().setIntegerPreference(appContext, portKey, 8080);

        DPPRepository repository = mainActivityViewModel.getDppRepository();

        assertNull("Repository should be null when IP is not set", repository);
    }

    @Test
    public void testGetDppRepository_BuildsCorrectUrl() {
        String ipKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_ip);
        String portKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_port);
        
        SharedPrefsUtils.getInstance().setStringPreference(appContext, ipKey, "10.0.0.1");
        SharedPrefsUtils.getInstance().setIntegerPreference(appContext, portKey, 80);

        DPPRepository repository = mainActivityViewModel.getDppRepository();

        assertNotNull("Repository should be created with valid IP and port", repository);
    }

    @Test
    public void testGetDppRepository_WithDifferentPorts() {
        String ipKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_ip);
        String portKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_port);
        
        SharedPrefsUtils.getInstance().setStringPreference(appContext, ipKey, "192.168.1.1");
        SharedPrefsUtils.getInstance().setIntegerPreference(appContext, portKey, 443);

        DPPRepository repository1 = mainActivityViewModel.getDppRepository();
        assertNotNull("Repository should be created with port 443", repository1);

        SharedPrefsUtils.getInstance().setIntegerPreference(appContext, portKey, 8000);
        DPPRepository repository2 = mainActivityViewModel.getDppRepository();
        assertNotNull("Repository should be created with port 8000", repository2);
    }

    @Test
    public void testCredentialClearing_SecurityPattern() {
        String tokenKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.token);
        String dppUriKey = appContext.getResources().getString(com.easyconnect.easyconnectapp.R.string.dpp_for_qrcode);
        
        SharedPrefsUtils.getInstance().setTokenPreference(appContext, tokenKey, "sensitive-token");
        SharedPrefsUtils.getInstance().setStringPreference(appContext, dppUriKey, "DPP:sensitive-data");
        
        mainActivityViewModel.getDppResponse(401, "Unauthorized");
        String clearedToken = SharedPrefsUtils.getInstance().getTokenPreference(appContext, tokenKey);
        assertNull("Token must be cleared on error for security", clearedToken);
        
        SharedPrefsUtils.getInstance().setStringPreference(appContext, dppUriKey, "DPP:sensitive-data");
        mainActivityViewModel.getDppUri(404, "Not Found");
        String clearedUri = SharedPrefsUtils.getInstance().getStringPreference(appContext, dppUriKey);
        assertNull("DPP URI must be cleared on error for security", clearedUri);
    }

    @Test
    public void testGetDppResponse_WithEmptyMessage() {
        DPPResponse response = mainActivityViewModel.getDppResponse(500, "");
        
        assertNotNull("Response should not be null", response);
        assertEquals("500", response.getStatus());
        assertEquals("", response.getMessage());
        assertNull("Token should be null", response.getToken());
    }

    @Test
    public void testGetDppUri_WithEmptyMessage() {
        DPPUri uri = mainActivityViewModel.getDppUri(500, "");
        
        assertNotNull("URI should not be null", uri);
        assertEquals("500", uri.getStatus());
        assertEquals("", uri.getMessage());
        assertNull("DPP URI should be null", uri.getDpp_uri());
    }

    @After
    public void terDown() throws Exception {

        mainActivityViewModel = null;
    }
}
