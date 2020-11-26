package com.example.ordergiver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button speechRecognizerButton;
    private Button seetingsButton;
    private TextView speechRecognizerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void settersWithId()
    {
        this.speechRecognizerButton = findViewById(R.id.btn_mic);
        this.speechRecognizerText = findViewById(R.id.txt_state);
    }

    public void setSpeechRecognizerButton(Button speechRecognizerButton) {
        this.speechRecognizerButton = speechRecognizerButton;
    }

    public Button getSpeechRecognizerButton() {
        return this.speechRecognizerButton;
    }

    public void setSpeechRecognizerText(TextView speechRecognizerText) {
        this.speechRecognizerText = speechRecognizerText;
    }

    public TextView getSpeechRecognizerText() {
        return this.speechRecognizerText;
    }
}