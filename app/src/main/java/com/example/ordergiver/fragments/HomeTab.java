package com.example.ordergiver.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ordergiver.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class HomeTab extends Fragment
{
    // Button
    private Button speechRecognizerButton;
    private TextView speechRecognizerText;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private TextView voiceText;

    public HomeTab()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rotationView = (ViewGroup) inflater.inflate(R.layout.home_tab, container, false);
        setters(rotationView);
        addListeners();
        return rotationView;
    }

    public void setters(ViewGroup rotationView)
    {
        // text voice
        this.voiceText = rotationView.findViewById(R.id.txt_voice);

        // button
        this.speechRecognizerButton = rotationView.findViewById(R.id.btn_mic);
        this.speechRecognizerText = rotationView.findViewById(R.id.txt_state);

        // Create speechRecognizer
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        this.speechRecognizerIntent = new Intent(RecognizerIntent. ACTION_RECOGNIZE_SPEECH );
        this.speechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE_MODEL , RecognizerIntent. LANGUAGE_MODEL_FREE_FORM );
        this.speechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE , Locale.getDefault ());
    }

    public void addListeners()
    {
        this.getSpeechRecognizerButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableRecognizerButton();
                getSpeechRecognizer().startListening(getSpeechRecognizerIntent());
            }
        });

        this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                disableRecognizerButton();
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> textVoice = bundle.getStringArrayList (SpeechRecognizer. RESULTS_RECOGNITION);
                getVoiceText().setText(textVoice.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    public void enableRecognizerButton()
    {
        getSpeechRecognizerButton().setBackground(this.getResources().getDrawable(R.drawable.ic_mic_on));
        getSpeechRecognizerText().setText("Écoute ...");
    }

    public void disableRecognizerButton()
    {
        getSpeechRecognizerButton().setBackground(this.getResources().getDrawable(R.drawable.ic_mic_off));
        getSpeechRecognizerText().setText("Touchez pour donner un ordre");
    }

    public void setTextVoice(String textVoice)
    {
        this.voiceText.setText(textVoice);
    }

    public TextView getVoiceText()
    {
        return this.voiceText;
    }

    public void setSpeechRecognizerButton(Button speechRecognizerOnButton) {
        this.speechRecognizerButton = speechRecognizerOnButton;
    }

    public Button getSpeechRecognizerButton() {
        return this.speechRecognizerButton;
    }

    public void setSpeechRecognizerText(String speechRecognizerText) {
        this.speechRecognizerText.setText(speechRecognizerText);
    }

    public TextView getSpeechRecognizerText() {
        return this.speechRecognizerText;
    }


    public SpeechRecognizer getSpeechRecognizer() {
        return speechRecognizer;
    }

    public Intent getSpeechRecognizerIntent()
    {
        return this.speechRecognizerIntent;
    }

}