package com.codyvbs.malwhere;

import android.app.Activity;

import androidx.lifecycle.Lifecycle;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;

public class CheckConnectionClass {
    public void checkConnection(Activity activity, Lifecycle lifecycle){
        // No Internet Dialog: Pendulum
        NoInternetDialogPendulum.Builder builder = new NoInternetDialogPendulum.Builder(
                activity,
                lifecycle
        );

        DialogPropertiesPendulum properties = builder.getDialogProperties();

        properties.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });

        properties.setCancelable(false);
        properties.setNoInternetConnectionTitle("No Internet");
        properties.setNoInternetConnectionMessage("Check your Internet connection and try again");
        properties.setShowInternetOnButtons(true);
        properties.setPleaseTurnOnText("Please turn on");
        properties.setWifiOnButtonText("Wifi");
        properties.setMobileDataOnButtonText("Mobile data");

        properties.setOnAirplaneModeTitle("No Internet");
        properties.setOnAirplaneModeMessage("You have turned on the airplane mode.");
        properties.setPleaseTurnOffText("Please turn off");
        properties.setAirplaneModeOffButtonText("Airplane mode");
        properties.setShowAirplaneModeOffButtons(true);

        builder.build();
    }
}
