package com.easyconnect.easyconnectapp.app.view.utils;

import android.net.nsd.NsdServiceInfo;

import com.easyconnect.easyconnectap.connection.Configurator;
import com.easyconnect.easyconnectap.network.model.DPPResponse;
import com.easyconnect.easyconnectap.network.model.DPPUri;

import java.net.InetAddress;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class MockDataFactory {

    public static DPPResponse createMockDPPResponse(int status, String message, String token) {
        DPPResponse response = new DPPResponse();
        response.setStatus(String.valueOf(status));
        response.setMessage(message);
        response.setToken(token);
        return response;
    }

    public static DPPUri createMockDPPUri(int status, String message, String dppUri) {
        DPPUri uri = new DPPUri();
        uri.setStatus(String.valueOf(status));
        uri.setMessage(message);
        uri.setDpp_uri(dppUri);
        return uri;
    }

    public static Configurator createMockConfigurator(String name, String ip, int port) {
        return new Configurator(name, ip, port);
    }

    public static NsdServiceInfo createMockNsdServiceInfo(String serviceName, String host, int port) {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setPort(port);
        try {
            serviceInfo.setHost(InetAddress.getByName(host));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceInfo;
    }

    public static HttpException createMockHttpException(int statusCode, String message) {
        String jsonBody = String.format("{\"message\":\"%s\"}", message);
        ResponseBody responseBody = ResponseBody.create(
            MediaType.parse("application/json"), 
            jsonBody
        );
        Response<?> response = Response.error(statusCode, responseBody);
        return new HttpException(response);
    }
}
