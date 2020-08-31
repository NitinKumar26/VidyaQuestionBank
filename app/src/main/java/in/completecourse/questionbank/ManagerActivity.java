package in.completecourse.questionbank;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.google.ads.mediation.adcolony.AdColonyAdapterUtils;
import com.google.ads.mediation.adcolony.AdColonyMediationAdapter;
import com.google.android.gms.ads.AdFormat;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManagerActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        //RequestConfiguration.Builder.setTestDeviceIds(Collections.singletonList("6DEE9D20290B46C296130C33458BC333"));

    }
    /*
    @Override
    public void initialize(Context context,
                           InitializationCompleteCallback initializationCompleteCallback,
                           List<MediationConfiguration> mediationConfigurations) {

        if (!(context instanceof Activity)) {
            initializationCompleteCallback.onInitializationFailed("AdColony SDK requires an " +
                    "Activity context to initialize");
            return;
        }

        HashSet<String> appIDs = new HashSet<>();
        ArrayList<String> zoneList = new ArrayList<>();
        for (MediationConfiguration configuration : mediationConfigurations) {
            Bundle serverParameters = configuration.getServerParameters();
            String appIDFromServer = serverParameters.getString(AdColonyAdapterUtils.KEY_APP_ID);

            if (!TextUtils.isEmpty(appIDFromServer)) {
                appIDs.add(appIDFromServer);
            }

            // We need to include zone IDs from non-rewarded ads to configure the
            // AdColony SDK and avoid issues with Interstitial Ads.
            ArrayList<String> zoneIDs = AdColonyManager.getInstance()
                    .parseZoneList(serverParameters);
            if (zoneIDs != null && zoneIDs.size() > 0) {
                zoneList.addAll(zoneIDs);
            }
        }

        String appID;
        int count = appIDs.size();
        if (count > 0) {
            appID = appIDs.iterator().next();

            if (count > 1) {
                String logMessage = String.format("Multiple '%s' entries found: %s. " +
                                "Using '%s' to initialize the AdColony SDK.",
                        AdColonyAdapterUtils.KEY_APP_ID, appIDs.toString(), appID);
                Log.w(TAG, logMessage);
            }
        } else {
            initializationCompleteCallback.onInitializationFailed("Initialization Failed: " +
                    "Missing or Invalid App ID.");
            return;
        }

        if (zoneList.isEmpty()) {
            initializationCompleteCallback.onInitializationFailed("Initialization Failed: " +
                    "No zones provided to initialize the AdColony SDK.");
            return;
        }

        // Always set mediation network info.
        appOptions.setMediationNetwork(AdColonyAppOptions.ADMOB, BuildConfig.VERSION_NAME);
        boolean success = AdColony.configure((Activity) context, appOptions, appID,
                zoneList.toArray(new String[0]));

        if (success) {
            initializationCompleteCallback.onInitializationSucceeded();
        } else {
            initializationCompleteCallback.onInitializationFailed("Initialization Failed: " +
                    "Internal Error on Configuration");
        }
    }

     */
}
