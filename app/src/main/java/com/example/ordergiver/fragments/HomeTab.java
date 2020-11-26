package com.example.ordergiver.fragments;

import android.content.Intent;
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

import java.util.Locale;

public class HomeTab extends Fragment
{
    // Button off
    private Button speechRecognizerOffButton;
    private TextView speechRecognizerOffText;

    // Button on
    private Button speechRecognizerOnButton;
    private TextView speechRecognizerOnText;

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

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
        // button off
        this.speechRecognizerOffButton = rotationView.findViewById(R.id.btn_mic_off);
        this.speechRecognizerOffText = rotationView.findViewById(R.id.txt_state_off);

        // button on
        this.speechRecognizerOnButton = rotationView.findViewById(R.id.btn_mic_on);
        this.speechRecognizerOnText = rotationView.findViewById(R.id.txt_state_on);

        // Create speechRecognizer
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        this.speechRecognizerIntent = new Intent(RecognizerIntent. ACTION_RECOGNIZE_SPEECH );
        this.speechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE_MODEL , RecognizerIntent. LANGUAGE_MODEL_FREE_FORM );
        this.speechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE , Locale.getDefault ());
    }

    public void addListeners()
    {
        this.getSpeechRecognizerOffButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechRecognizer().startListening(getSpeechRecognizerIntent());
            }
        });

        this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                enableRecognizerButton();
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
        getSpeechRecognizerOffButton().setVisibility(View.INVISIBLE);
        getSpeechRecognizerOffText().setVisibility(View.INVISIBLE);
        getSpeechRecognizerOnButton().setVisibility(View.VISIBLE);
        getSpeechRecognizerOnText().setVisibility(View.VISIBLE);
    }

    public void disableRecognizerButton()
    {
        getSpeechRecognizerOffButton().setVisibility(View.VISIBLE);
        getSpeechRecognizerOffText().setVisibility(View.VISIBLE);
        getSpeechRecognizerOnButton().setVisibility(View.INVISIBLE);
        getSpeechRecognizerOnText().setVisibility(View.INVISIBLE);
    }

    public void setSpeechRecognizerOnButton(Button speechRecognizerOnButton) {
        this.speechRecognizerOnButton = speechRecognizerOnButton;
    }

    public Button getSpeechRecognizerOnButton() {
        return this.speechRecognizerOnButton;
    }

    public void setSpeechRecognizerOffButton(Button speechRecognizerOffButton) {
        this.speechRecognizerOffButton = speechRecognizerOffButton;
    }

    public Button getSpeechRecognizerOffButton() {
        return this.speechRecognizerOffButton;
    }

    public void setSpeechRecognizerOffText(String speechRecognizerText) {
        this.speechRecognizerOffText.setText(speechRecognizerText);
    }

    public TextView getSpeechRecognizerOffText() {
        return this.speechRecognizerOffText;
    }

    public void setSpeechRecognizerOnText(String speechRecognizerOnText) {
        this.speechRecognizerOnText.setText(speechRecognizerOnText);
    }

    public TextView getSpeechRecognizerOnText() {
        return this.speechRecognizerOnText;
    }

    public SpeechRecognizer getSpeechRecognizer() {
        return speechRecognizer;
    }

    public Intent getSpeechRecognizerIntent()
    {
        return this.speechRecognizerIntent;
    }

}