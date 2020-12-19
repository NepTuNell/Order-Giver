package com.example.ordergiver.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ordergiver.R;
import com.example.ordergiver.java.SmsSender;
import com.example.ordergiver.manager.OrderManager;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

public class HomeTab extends Fragment
{
    // Permissions
    private static final int PERMISSION_RECORD_AUDIO = 1;
    private static final int PERMISSION_SEND_SMS = 1;

    private Button speechRecognizerButton;
    private TextView speechRecognizerText, voiceText;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private Dialog dialog;
    private OrderManager orderManager;
    private SmsSender smsSender;
    private boolean buttonAlreadyTouched;
    private boolean isSpeaking;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rotationView = (ViewGroup) inflater.inflate(R.layout.home_tab, container, false);
        setters(rotationView);
        addEventListeners();
        return rotationView;
    }

    /* Setters */

    public void setters(ViewGroup rotationView)
    {
        orderManager = new OrderManager(getActivity());
        smsSender = new SmsSender();
        // text voice
        voiceText = rotationView.findViewById(R.id.txt_voice);
        // button
        speechRecognizerButton = rotationView.findViewById(R.id.btn_mic);
        speechRecognizerText = rotationView.findViewById(R.id.txt_state);
        // Create speechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent. ACTION_RECOGNIZE_SPEECH );
        speechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE_MODEL , RecognizerIntent. LANGUAGE_MODEL_FREE_FORM );
        speechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE , Locale.getDefault ());
        // Create Dialog Box
        dialog = new Dialog(getActivity());
    }

    private void setButtonAlreadyTouched(boolean buttonTouched)
    {
        buttonAlreadyTouched = buttonTouched;
    }

    private void setIsSpeaking(boolean speaking)
    {
        isSpeaking = speaking;
    }

    /* Getters */

    public OrderManager getOrderManager() { return orderManager; }
    public SmsSender getSmsSender() { return smsSender; }
    public TextView getVoiceText() { return voiceText; }
    public boolean getButtonAlreadyTouched() { return buttonAlreadyTouched; }
    public Button getSpeechRecognizerButton() { return speechRecognizerButton; }
    public TextView getSpeechRecognizerText() { return speechRecognizerText; }
    public SpeechRecognizer getSpeechRecognizer() { return speechRecognizer; }
    public Intent getSpeechRecognizerIntent() { return speechRecognizerIntent; }
    public boolean getIsSpeaking() { return isSpeaking; }
    public Dialog getDialog() { return dialog; }

    /* Listeners */

    public void addEventListeners()
    {
        getSpeechRecognizerButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Détection permissions
                if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                    requestRecordAudio();
                } else {
                    if (false == getIsSpeaking()) {
                        enableRecognizerButton();
                        getSpeechRecognizer().startListening(getSpeechRecognizerIntent());
                    } else {
                        disableRecognizerButton();
                        getSpeechRecognizer().stopListening();
                    }
                }
            }
        });

        getSpeechRecognizer().setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) { }

            @Override
            public void onBeginningOfSpeech() { }

            @Override
            public void onRmsChanged(float v) { }

            @Override
            public void onBufferReceived(byte[] bytes) { }

            @Override
            public void onEndOfSpeech() {
                disableRecognizerButton();
            }

            @Override
            public void onError(int i) { }

            @Override
            public void onResults(Bundle bundle)
            {
                ArrayList<String> textVoice = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                getVoiceText().setText(textVoice.get(0));

                if (orderExist(textVoice.get(0).trim())) {
                    showPopup(textVoice.get(0).trim());
                } else {
                    printSmsMessage("L'ordre dicté n'existe pas .", false);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) { }

            @Override
            public void onEvent(int i, Bundle bundle) { }
        });
    }

    /* Methods */

    public boolean orderExist(String sentence)
    {
        if (sentence.equals("")) {
            return false;
        }

        String orderName = "";
        int length = sentence.length();

        if (sentence.contains(" ")) {
            length = sentence.indexOf(" ");
        }

        orderName = sentence.substring(0, length);
        orderName = normalize(orderName);

        if (getOrderManager().checkOrderExist(orderName, -1)) {
            return true;
        }

        return false;
    }

    public void enableRecognizerButton()
    {
        setIsSpeaking(true);
        getSpeechRecognizerButton().setBackground(getResources().getDrawable(R.drawable.ic_mic_on));
        getSpeechRecognizerText().setText("Écoute ...");
    }

    public void disableRecognizerButton()
    {
        setIsSpeaking(false);
        getSpeechRecognizerButton().setBackground(getResources().getDrawable(R.drawable.ic_mic_off));
        getSpeechRecognizerText().setText("Touchez pour donner un ordre");
    }

    // - Permissions - - - - -
    private void requestRecordAudio()
    {
        ActivityCompat.requestPermissions(getActivity(),
                new String[] {Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
    }

    private void requestSmsAuthorization()
    {
        ActivityCompat.requestPermissions(getActivity(),
                new String[] {Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
    }

    // - Popup message sms - - - - -
    private void showPopup(final String order)
    {
        Button btnClose, btnAccept;
        final EditText phoneNumber;

        getDialog().setContentView(R.layout.send_sms_popup);
        btnClose = getDialog().findViewById(R.id.sms_popup_cancel);
        btnAccept = getDialog().findViewById(R.id.sms_popup_valid);
        phoneNumber = getDialog().findViewById(R.id.sms_popup_number);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getDialog().dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (getButtonAlreadyTouched()) {
                    return;
                }

                setButtonAlreadyTouched(true);

                // Détection permissions
                if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)) {
                    requestSmsAuthorization();
                } else {
                    // Contenu du code pour l'envoi du sms avant la fermeture du dialogue
                    if (getSmsSender().sendSms(order, phoneNumber.getText().toString())) {
                        printSmsMessage("Ordre envoyé.", true);
                    } else {
                        printSmsMessage("Ordre non envoyé.", false);
                    }
                }

                setButtonAlreadyTouched(false);
            }
        });

        getDialog().show();
    }

    public static String normalize(String str)
    {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return str.toLowerCase();
    }

    public void printSmsMessage(final String str, final boolean close)
    {
        Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (close) {
                    dialog.dismiss();
                }
                setButtonAlreadyTouched(false);
            }
        }, 3000);
    }
}