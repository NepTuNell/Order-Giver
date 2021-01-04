package com.example.ordergiver.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.example.ordergiver.manager.VerbManager;
import com.example.ordergiver.service.OrderNormalizer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import static java.util.Objects.*;

public class HomeTab extends Fragment
{
    //****************************
    // Attributes
    //****************************

    // Permissions
    private static final int PERMISSION_RECORD_AUDIO = 1;
    private static final int PERMISSION_SEND_SMS = 1;

    private Button mSpeechRecognizerButton;
    private TextView mSpeechRecognizerText, mVoiceText;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private Dialog mDialog;
    private VerbManager mVerbManager;
    private OrderManager mOrderManager;
    private OrderNormalizer mOrderNormalizer;
    private SmsSender mSmsSender;
    private boolean mButtonAlreadyTouched;
    private boolean mIsSpeaking;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rotationView = (ViewGroup) inflater.inflate(R.layout.home_tab, container, false);
        InitAllAttributes(rotationView);
        addEventListeners();
        return rotationView;
    }

    //****************************
    // Initialization methods
    //****************************

    public void InitAllAttributes(ViewGroup rotationView)
    {
        // Services
        mVerbManager = new VerbManager(getContext());
        mOrderManager = new OrderManager(getContext());
        mOrderNormalizer = new OrderNormalizer();
        mSmsSender = new SmsSender();

        // Create speechRecognizer
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent. ACTION_RECOGNIZE_SPEECH );
        mSpeechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE_MODEL , RecognizerIntent. LANGUAGE_MODEL_FREE_FORM );
        mSpeechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE , Locale.getDefault ());

        // button
        mSpeechRecognizerButton = rotationView.findViewById(R.id.btn_mic);
        mSpeechRecognizerText = rotationView.findViewById(R.id.txt_state);

        // Others
        mVoiceText = rotationView.findViewById(R.id.txt_voice);

        // Create Dialog Box
        mDialog = new Dialog(requireNonNull(getActivity()));
    }

    //****************************
    // Accessors
    //****************************

    // Setters
    private void setButtonAlreadyTouched(boolean buttonTouched)
    {
        mButtonAlreadyTouched = buttonTouched;
    }

    private void setIsSpeaking(boolean speaking)
    {
        mIsSpeaking = speaking;
    }

    // Getters
    public SpeechRecognizer getSpeechRecognizer() { return mSpeechRecognizer; }
    public VerbManager getVerbManager() { return mVerbManager; }
    public OrderManager getOrderManager() { return mOrderManager; }
    public OrderNormalizer getOrderNormalizer() { return mOrderNormalizer; }
    public SmsSender getSmsSender() { return mSmsSender; }
    public Button getSpeechRecognizerButton() { return mSpeechRecognizerButton; }
    public TextView getVoiceText() { return mVoiceText; }
    public TextView getSpeechRecognizerText() { return mSpeechRecognizerText; }
    public Intent getSpeechRecognizerIntent() { return mSpeechRecognizerIntent; }
    public boolean getIsSpeaking() { return mIsSpeaking; }
    public boolean getButtonAlreadyTouched() { return mButtonAlreadyTouched; }
    public Dialog getDialog() { return mDialog; }

    // Listeners
    public void addEventListeners()
    {
        getSpeechRecognizerButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (0 == getVerbManager().countEntries()) {
                    printMessage("Veuillez installer les données.", false);
                    return;
                }

                // Détection permissions
                if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                    requestRecordAudio();
                } else {
                    if (!getIsSpeaking()) {
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
                String verbToFind = getVerbManager().getInfinitiveVerbByString(getOrderNormalizer().subStringOrder(textVoice.get(0)));

                if ( verbToFind.equals("") ) {
                    printMessage("L'ordre dicté n'existe pas .", false);
                    return;
                }

                if ( orderExist(verbToFind) ) {
                    showPopup(textVoice.get(0).trim());
                } else {
                    printMessage("L'ordre dicté n'existe pas dans la liste des ordres .", false);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) { }

            @Override
            public void onEvent(int i, Bundle bundle) { }
        });
    }

    //****************************
    // Methods
    //****************************

    /**
     * Check if an order exist by calling order manager
     */
    public boolean orderExist(@NotNull String sentence)
    {
        if (sentence.equals("")) {
            return false;
        }

        String orderName = getOrderNormalizer().subStringOrder(sentence);
        return getOrderManager().checkOrderExist(orderName, -1);
    }

    /**
     * Enable the recognition
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void enableRecognizerButton()
    {
        setIsSpeaking(true);
        getSpeechRecognizerButton().setBackground(getResources().getDrawable(R.drawable.ic_mic_on));
        getSpeechRecognizerText().setText("Écoute ...");
    }

    /**
     * Disable the recognition
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void disableRecognizerButton()
    {
        setIsSpeaking(false);
        getSpeechRecognizerButton().setBackground(getResources().getDrawable(R.drawable.ic_mic_off));
        getSpeechRecognizerText().setText("Touchez pour donner un ordre");
    }

    /**
     * Show sms popup
     */
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

                // Detect permissions
                if (!(ContextCompat.checkSelfPermission(requireNonNull(getActivity()), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)) {
                    requestSmsAuthorization();
                } else {
                    // Contenu du code pour l'envoi du sms avant la fermeture du dialogue
                    if (getSmsSender().sendSms(order, phoneNumber.getText().toString())) {
                        printMessage("Ordre envoyé.", true);
                    } else {
                        printMessage("Ordre non envoyé.", false);
                    }
                }

                setButtonAlreadyTouched(false);
            }
        });

        getDialog().show();
    }

    /**
     * Display toast messages with the possibility of dismiss the dialog popup
     */
    public void printMessage(final String str, final boolean close)
    {
        Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (close) {
                    mDialog.dismiss();
                }
                setButtonAlreadyTouched(false);
            }
        }, 3000);
    }

    //****************************
    // Permissions methods
    //****************************

    private void requestRecordAudio()
    {
        ActivityCompat.requestPermissions(requireNonNull(getActivity()),
                new String[] {Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
    }

    private void requestSmsAuthorization()
    {
        ActivityCompat.requestPermissions(requireNonNull(getActivity()),
                new String[] {Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
    }
}