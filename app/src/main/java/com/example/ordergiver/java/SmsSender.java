package com.example.ordergiver.java;

import android.telephony.SmsManager;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;


public class SmsSender extends AppCompatActivity
{
    /**
     * Send sms
     */
    public boolean sendSms(@NotNull String message, @NotNull String num)
    {
        if (message.trim().equals("") || num.trim().equals("")) {
            return false;
        }

        try {
            SmsManager smsMgrVar = SmsManager.getDefault();
            smsMgrVar.sendTextMessage(num, null, message, null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
