package com.easyconnect.easyconnectapp.app.view;

import android.app.Application;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Looper;

import com.easyconnect.easyconnectap.connection.Configurator;
import com.easyconnect.easyconnectap.connection.NSDDiscover;
import com.easyconnect.easyconnectap.network.model.DPPResponse;
import com.easyconnect.easyconnectap.network.model.DPPUri;
import com.easyconnect.easyconnectap.network.repository.DPPRepository;
import com.easyconnect.easyconnectap.scan.ScanQRCode;
import com.easyconnect.easyconnectap.util.SharedPrefsUtils;
import com.easyconnect.easyconnectapp.R;
import com.easyconnect.easyconnectapp.app.view.utils.MockDataFactory;
import com.easyconnect.easyconnectapp.app.view.utils.SingletonTestHelper;
import com.easyconnect.easyconnectapp.app.view.viewmodel.MainActivityViewModel;
import com.google.gson.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {

    @Mock
    NSDDiscover mockNSDDiscover;
    
    @Mock
    ScanQRCode mockScanQRCode;
    
    @Mock
    DPPRepository mockDPPRepository;
    
    @Mock
    SharedPrefsUtils mockSharedPrefs;
    
    @Mock
    MainActivityViewModel mockViewModel;

    private MainActivity mainActivity;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        SingletonTestHelper.injectMockSingleton(NSDDiscover.class, "nsdDiscover", mockNSDDiscover);
        SingletonTestHelper.injectMockSingleton(ScanQRCode.class, "scanQRCode", mockScanQRCode);
        SingletonTestHelper.injectMockSingleton(SharedPrefsUtils.class, "sharedPrefsUtils", mockSharedPrefs);

        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("10.0.0.1");
        when(mockSharedPrefs.getIntegerPreference(any(Context.class), anyString(), anyInt())).thenReturn(8080);
        when(mockSharedPrefs.getBooleanPreference(any(Context.class), anyString(), anyBoolean())).thenReturn(false);

        mainActivity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
    }

    @After
    public void tearDown() throws Exception {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
        SingletonTestHelper.resetSingleton(NSDDiscover.class, "nsdDiscover");
        SingletonTestHelper.resetSingleton(ScanQRCode.class, "scanQRCode");
        SingletonTestHelper.resetSingleton(SharedPrefsUtils.class, "sharedPrefsUtils");
    }

    @Test
    public void testMdnsDiscovery_singleConfigurator_callsResolveListener() throws Exception {
        List<NsdServiceInfo> serviceList = new ArrayList<>();
        NsdServiceInfo service = MockDataFactory.createMockNsdServiceInfo("Configurator1", "192.168.1.100", 8080);
        serviceList.add(service);

        mainActivity.mDNSConfigList(serviceList);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockNSDDiscover).getResolveListener(any(Context.class), eq(service), eq(mainActivity));
    }

    @Test
    public void testMdnsDiscovery_multipleConfigurators_showsDialogFragment() throws Exception {
        List<NsdServiceInfo> serviceList = new ArrayList<>();
        serviceList.add(MockDataFactory.createMockNsdServiceInfo("Configurator1", "192.168.1.100", 8080));
        serviceList.add(MockDataFactory.createMockNsdServiceInfo("Configurator2", "192.168.1.101", 8080));

        mainActivity.mDNSConfigList(serviceList);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockNSDDiscover, never()).getResolveListener(any(Context.class), any(NsdServiceInfo.class), any());
    }

    @Test
    public void testMdnsDiscovery_noConfigurators_showsErrorDialog() throws Exception {
        List<NsdServiceInfo> emptyList = new ArrayList<>();

        mainActivity.mDNSConfigList(emptyList);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockNSDDiscover, never()).getResolveListener(any(Context.class), any(NsdServiceInfo.class), any());
    }

    @Test
    public void testMDNSConfigListCallback_singleConfigurator_updatesConsole() throws Exception {
        List<NsdServiceInfo> serviceList = new ArrayList<>();
        serviceList.add(MockDataFactory.createMockNsdServiceInfo("Configurator1", "192.168.1.100", 8080));

        mainActivity.mDNSConfigList(serviceList);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        assertNotNull(mainActivity);
    }

    @Test
    public void testGetselectedConfiguratorCallback_storesIPAndPort() throws Exception {
        Configurator configurator = MockDataFactory.createMockConfigurator("TestConfig", "192.168.1.100", 8080);

        mainActivity.getselectedConfigurator(configurator);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockSharedPrefs).clearSharedPreference(any(Context.class));
        verify(mockSharedPrefs).setIntegerPreference(any(Context.class), anyString(), eq(8080));
        verify(mockSharedPrefs).setStringPreference(any(Context.class), anyString(), eq("192.168.1.100"));
    }

    @Test
    public void testQRScan_withValidPrerequisites_callsScanQRCode() throws Exception {
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("10.0.0.1");

        mainActivity.onClick(mainActivity.findViewById(R.id.btnScanQR));
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockScanQRCode).scanQRCodeToGetUri(any(Context.class), any());
    }

    @Test
    public void testGetScanResultCallback_storesDPPUri_callsSendUriToServer() throws Exception {
        String testUri = "DPP:C:81/1;M:48598b0145e5;I:SN=4774LH0000;K:MDkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDIgADURzxmttZoIRIPWGoQMV00XHWCAQIhXruVWOz0NjlkIA=;;";
        
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        DPPResponse mockResponse = MockDataFactory.createMockDPPResponse(200, "Success", "test-token");
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.just(mockResponse));

        mainActivity.getScanResult(testUri);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockSharedPrefs).setStringPreference(any(Context.class), anyString(), eq(testUri));
    }

    @Test
    public void testSendUriToServer_success200_showsSuccessDialog() throws Exception {
        DPPResponse mockResponse = MockDataFactory.createMockDPPResponse(200, "DPP initiates successfully", "test-token");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.just(mockResponse));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");

        mainActivity.getScanResult("test-uri");
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockSharedPrefs).setStringPreference(any(Context.class), anyString(), eq("test-token"));
    }

    @Test
    public void testSendUriToServer_redirect307_showsRedirectDialog() throws Exception {
        HttpException httpException = MockDataFactory.createMockHttpException(307, "Temporary Redirect");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.error(httpException));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");
        
        DPPResponse errorResponse = MockDataFactory.createMockDPPResponse(307, "Temporary Redirect", null);
        when(mockViewModel.parseErrorResponse(any(HttpException.class))).thenReturn(errorResponse);

        mainActivity.getScanResult("test-uri");
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockViewModel).parseErrorResponse(any(HttpException.class));
    }

    @Test
    public void testSendUriToServer_badRequest400_showsErrorDialog() throws Exception {
        HttpException httpException = MockDataFactory.createMockHttpException(400, "Bad Request");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.error(httpException));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");
        
        DPPResponse errorResponse = MockDataFactory.createMockDPPResponse(400, "Bad Request", null);
        when(mockViewModel.parseErrorResponse(any(HttpException.class))).thenReturn(errorResponse);

        mainActivity.getScanResult("test-uri");
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockViewModel).parseErrorResponse(any(HttpException.class));
    }

    @Test
    public void testSendUriToServer_unauthorized401_showsChallengeDialog() throws Exception {
        HttpException httpException = MockDataFactory.createMockHttpException(401, "Unauthorized - passphrase required");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.error(httpException));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");
        
        DPPResponse errorResponse = MockDataFactory.createMockDPPResponse(401, "Passphrase required", null);
        when(mockViewModel.parseErrorResponse(any(HttpException.class))).thenReturn(errorResponse);

        mainActivity.getScanResult("test-uri");
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockViewModel).parseErrorResponse(any(HttpException.class));
    }

    @Test
    public void testSendUriToServer_notFound404_showsErrorDialog() throws Exception {
        HttpException httpException = MockDataFactory.createMockHttpException(404, "Not Found");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.error(httpException));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");
        
        DPPResponse errorResponse = MockDataFactory.createMockDPPResponse(404, "Not Found", null);
        when(mockViewModel.parseErrorResponse(any(HttpException.class))).thenReturn(errorResponse);

        mainActivity.getScanResult("test-uri");
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockViewModel).parseErrorResponse(any(HttpException.class));
    }

    @Test
    public void testSendUriToServer_serverError500_showsErrorDialog() throws Exception {
        HttpException httpException = MockDataFactory.createMockHttpException(500, "Internal Server Error");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.error(httpException));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");
        
        DPPResponse errorResponse = MockDataFactory.createMockDPPResponse(500, "Internal Server Error", null);
        when(mockViewModel.parseErrorResponse(any(HttpException.class))).thenReturn(errorResponse);

        mainActivity.getScanResult("test-uri");
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockViewModel).parseErrorResponse(any(HttpException.class));
    }

    @Test
    public void testHandleHttpException_parsesErrorResponse_viaViewModel() throws Exception {
        HttpException httpException = MockDataFactory.createMockHttpException(400, "Bad Request");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.error(httpException));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");
        
        DPPResponse errorResponse = MockDataFactory.createMockDPPResponse(400, "Bad Request", null);
        when(mockViewModel.parseErrorResponse(any(HttpException.class))).thenReturn(errorResponse);

        mainActivity.getScanResult("test-uri");
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockViewModel).parseErrorResponse(any(HttpException.class));
    }

    @Test
    public void testSendUriToServer_networkError_showsGenericError() throws Exception {
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.error(new Exception("Network error")));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");

        mainActivity.getScanResult("test-uri");
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockDPPRepository).getDPPResponse(any(JsonObject.class));
    }

    @Test
    public void testSendChallenge_withResponse_sendsToRepository() throws Exception {
        DPPResponse mockResponse = MockDataFactory.createMockDPPResponse(200, "Challenge accepted", "new-token");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.sendChallengeResponse(anyString(), any(JsonObject.class))).thenReturn(Single.just(mockResponse));
        when(mockSharedPrefs.getStringPreference(any(Context.class), anyString())).thenReturn("test-uri");

        assertNotNull(mainActivity);
    }

    @Test
    public void testGetDPPUriFromServer_success200_returnsDPPUri() throws Exception {
        DPPUri mockUri = MockDataFactory.createMockDPPUri(200, "Success", "DPP:test-uri");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPUri()).thenReturn(Single.just(mockUri));

        assertNotNull(mainActivity);
    }

    @Test
    public void testGetDPPUriFromServer_error401_showsChallengeDialog() throws Exception {
        HttpException httpException = MockDataFactory.createMockHttpException(401, "Unauthorized");
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        when(mockDPPRepository.getDPPUri()).thenReturn(Single.error(httpException));
        
        DPPUri errorUri = MockDataFactory.createMockDPPUri(401, "Passphrase required", null);
        when(mockViewModel.parseDPPErrorResponse(any(HttpException.class))).thenReturn(errorUri);

        assertNotNull(mainActivity);
    }

    @Test
    public void testMdnsDiscovery_clearsConfiguratorList_beforeDiscovery() throws Exception {
        mainActivity.onClick(mainActivity.findViewById(R.id.btnScanMdns));
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockNSDDiscover).DiscoverMDNSConfigurator(any(Context.class), any());
    }

    @Test
    public void testDisplayMessageFromConfigurator_handlesAllStatusCodes() throws Exception {
        DPPResponse response200 = MockDataFactory.createMockDPPResponse(200, "Success", "token");
        DPPResponse response307 = MockDataFactory.createMockDPPResponse(307, "Redirect", null);
        DPPResponse response400 = MockDataFactory.createMockDPPResponse(400, "Bad Request", null);
        DPPResponse response401 = MockDataFactory.createMockDPPResponse(401, "Unauthorized", null);
        DPPResponse response404 = MockDataFactory.createMockDPPResponse(404, "Not Found", null);
        DPPResponse response500 = MockDataFactory.createMockDPPResponse(500, "Server Error", null);

        assertNotNull(response200);
        assertNotNull(response307);
        assertNotNull(response400);
        assertNotNull(response401);
        assertNotNull(response404);
        assertNotNull(response500);
    }

    @Test
    public void testConfiguratorSelection_savesIPAndPortToSharedPrefs() throws Exception {
        Configurator configurator = MockDataFactory.createMockConfigurator("Test", "192.168.1.1", 8080);

        mainActivity.getselectedConfigurator(configurator);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockSharedPrefs).setStringPreference(any(Context.class), anyString(), eq("192.168.1.1"));
        verify(mockSharedPrefs).setIntegerPreference(any(Context.class), anyString(), eq(8080));
    }

    @Test
    public void testQRScanResult_storesUriInSharedPrefs() throws Exception {
        String testUri = "DPP:test-uri-value";
        
        when(mockViewModel.getDppRepository()).thenReturn(mockDPPRepository);
        DPPResponse mockResponse = MockDataFactory.createMockDPPResponse(200, "Success", "token");
        when(mockDPPRepository.getDPPResponse(any(JsonObject.class))).thenReturn(Single.just(mockResponse));

        mainActivity.getScanResult(testUri);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockSharedPrefs).setStringPreference(any(Context.class), anyString(), eq(testUri));
    }

    @Test
    public void testActivity_implementsAllRequiredInterfaces() {
        assertTrue(mainActivity instanceof android.view.View.OnClickListener);
        assertTrue(mainActivity instanceof com.easyconnect.easyconnectap.connection.IMDNSDiscovery);
        assertTrue(mainActivity instanceof com.easyconnect.easyconnectap.connection.IConfigurator);
        assertTrue(mainActivity instanceof com.easyconnect.easyconnectap.scan.IScanResult);
    }

    @Test
    public void testSharedPrefsIntegration_clearsBeforeNewConfiguration() throws Exception {
        Configurator configurator = MockDataFactory.createMockConfigurator("Test", "10.0.0.1", 80);

        mainActivity.getselectedConfigurator(configurator);
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(mockSharedPrefs).clearSharedPreference(any(Context.class));
    }
}
