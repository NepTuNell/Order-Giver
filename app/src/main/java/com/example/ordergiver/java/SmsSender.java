package com.example.ordergiver.java;

import android.telephony.SmsManager;
import androidx.appcompat.app.AppCompatActivity;


public class SmsSender extends AppCompatActivity {

    public boolean sendSms(String message, String num)
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
