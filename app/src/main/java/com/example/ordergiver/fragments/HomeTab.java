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

    private Button speechRecognizerButton;
    private TextView speechRecognizerText;
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
        this.speechRecognizerButton = rotationView.findViewById(R.id.btn_mic);
        this.speechRecognizerText = rotationView.findViewById(R.id.txt_state);
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
                getSpeechRecognizer().startListening(getSpeechRecognizerIntent());
            }
        });

        this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                setSpeechRecognizerText("Écoute ...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                setSpeechRecognizerText("Touchez pour donner un ordre");
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

    public void setSpeechRecognizerButton(Button speechRecognizerButton) {
        this.speechRecognizerButton = speechRecognizerButton;
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