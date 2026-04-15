package com.easyconnect.easyconnectapp.app.view.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.easyconnect.easyconnectap.instance.ProviderInstance;
import com.easyconnect.easyconnectap.network.model.DPPResponse;
import com.easyconnect.easyconnectap.network.model.DPPUri;
import com.easyconnect.easyconnectap.network.repository.DPPRepository;
import com.easyconnect.easyconnectap.util.RuntimePermissionHelper;
import com.easyconnect.easyconnectap.util.SharedPrefsUtils;
import com.easyconnect.easyconnectapp.app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import retrofit2.HttpException;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * ViewModel for {@link MainActivity} that manages network repository access
 * and HTTP error parsing for the DPP provisioning flow.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Constructs a {@link DPPRepository} configured with the configurator's
 *       IP and port discovered via mDNS (stored in {@link SharedPrefsUtils})</li>
 *   <li>Parses {@link HttpException} error bodies into {@link DPPResponse}
 *       or {@link DPPUri} models for display by the Activity</li>
 *   <li>Creates error-state response objects and clears stale auth tokens
 *       when requests fail</li>
 * </ul>
 *
 * <p>Survives configuration changes (e.g., screen rotation) as an {@link AndroidViewModel}.
 *
 * @see DPPRepository
 * @see ProviderInstance
 */
public class MainActivityViewModel extends AndroidViewModel {

    private DPPRepository dppRepository = null;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Returns a {@link DPPRepository} configured with the base URL of the
     * currently selected mDNS configurator.
     *
     * <p>Reads the configurator's IP and port from {@link SharedPrefsUtils},
     * constructs an HTTP base URL ({@code http://<ip>:<port>/}), and obtains
     * a repository instance via {@link ProviderInstance}. If the IP is not
     * available (no configurator selected), shows a toast and returns {@code null}.
     *
     * @return the DPP repository, or {@code null} if no configurator is configured
     */
    public DPPRepository getDppRepository() {

        try {
            String dnsIp = SharedPrefsUtils.getInstance().getStringPreference(getApplication(), getApplication().getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_ip));
            Integer dnsPort = SharedPrefsUtils.getInstance().getIntegerPreference(getApplication(), getApplication().getResources().getString(com.easyconnect.easyconnectapp.R.string.mdns_port), 00);

            String URL = getApplication().getResources().getString(R.string.http) + dnsIp + ":" + dnsPort + "/";

            Log.d("URL", URL);

            if (dnsIp != null) {

                dppRepository = ProviderInstance.getProvider().getDPPRepository(URL);

                return dppRepository;

            } else {

                Toast.makeText(getApplication(), "Unable to find MDNS Configurator, Select MDNS Configurator.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

            Log.d(TAG, "DPPResponse body " + e.getMessage());

            Toast.makeText(getApplication(), "Unable to send to server, Please try again", Toast.LENGTH_SHORT).show();
        }

        return dppRepository;
    }

    /**
     * Parses an {@link HttpException} from the DPP initiation endpoint into
     * a {@link DPPResponse} containing the status code and error message.
     *
     * @param httpException the HTTP exception to parse
     * @return the parsed response, or {@code null} if parsing fails
     */
    public DPPResponse parseErrorResponse(HttpException httpException) {

        try {
            //To get status code
            httpException.code();

            Gson gson = new Gson();
            Type type = new TypeToken<HttpException>() {
            }.getType();
            HttpException errorResponse = gson.fromJson(httpException.response().errorBody().charStream(), type);
            String message = errorResponse.message();

            return getDppResponse(httpException.code(),message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Parses an {@link HttpException} from the DPP URI endpoint into
     * a {@link DPPUri} containing the status code and error message.
     *
     * @param httpException the HTTP exception to parse
     * @return the parsed URI response, or {@code null} if parsing fails
     */
    public DPPUri parseDPPErrorResponse(HttpException httpException) {

        try {
            //To get the status code
            httpException.code();
            Gson gson = new Gson();
            Type type = new TypeToken<HttpException>() {
            }.getType();
            HttpException errorResponse = gson.fromJson(httpException.response().errorBody().charStream(), type);
            String message = errorResponse.message();

           return getDppUri(httpException.code(),message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a {@link DPPResponse} representing an error state and clears
     * the stored auth token.
     *
     * @param status  the HTTP status code
     * @param message the error message to display
     * @return a new {@link DPPResponse} with the given status and message
     */
    public DPPResponse getDppResponse(int status,String message){

        //Creating DPPResponse object to set response details
        DPPResponse dppResponse = new DPPResponse();
        dppResponse.setStatus(String.valueOf(status));
        dppResponse.setMessage(message);
        dppResponse.setToken(null);

        SharedPrefsUtils.getInstance().setTokenPreference(getApplication(), getApplication().getResources().getString(R.string.token), null);

        return dppResponse;
    }

    /**
     * Creates a {@link DPPUri} representing an error state and clears
     * the stored QR code URI.
     *
     * @param status  the HTTP status code
     * @param message the error message to display
     * @return a new {@link DPPUri} with the given status and message
     */
    public DPPUri getDppUri(int status,String message){

        DPPUri dppUri = new DPPUri();
        dppUri.setStatus(String.valueOf(status));
        dppUri.setMessage(message);
        dppUri.setDpp_uri(null);

        SharedPrefsUtils.getInstance().setStringPreference(getApplication(), getApplication().getResources().getString(R.string.dpp_for_qrcode), null);

        return dppUri;
    }
}
