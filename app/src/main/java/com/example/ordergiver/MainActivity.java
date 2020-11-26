package com.example.ordergiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import com.example.ordergiver.fragments.HomeTab;
import com.example.ordergiver.fragments.OrderTab;
import com.example.ordergiver.java.Adapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewVisible;
    private PagerAdapter pageAdapter;
    private Button speechRecognizerButton;
    private TextView speechRecognizerText;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setters();
        addListeners();
    }

    public void setters()
    {
        this.speechRecognizerButton = findViewById(R.id.btn_mic);
        this.speechRecognizerText = findViewById(R.id.txt_state);
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        this.speechRecognizerIntent = new Intent(RecognizerIntent. ACTION_RECOGNIZE_SPEECH );
        this.speechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE_MODEL , RecognizerIntent. LANGUAGE_MODEL_FREE_FORM );
        this.speechRecognizerIntent.putExtra(RecognizerIntent. EXTRA_LANGUAGE , Locale.getDefault ());
    }

    // View configuration ----------------------
    private void initView()
    {
        // Add Tabs
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeTab());
        fragmentList.add(new OrderTab());

        // Config
        this.viewVisible = findViewById(R.id.view_visible);
        this.pageAdapter = new Adapter(getSupportFragmentManager(), fragmentList);
        this.viewVisible.setAdapter(this.pageAdapter);
    }

    public void addListeners()
    {
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

        this.getSpeechRecognizerButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechRecognizer().startListening(getSpeechRecognizerIntent());
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