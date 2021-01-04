package com.example.ordergiver.service;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordergiver.entity.Verb;
import com.example.ordergiver.manager.VerbManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class VerbsCreate extends AppCompatActivity
{

    //****************************
    // Attributes
    //****************************

    private AssetManager mAssetManager;
    private OrderNormalizer mNormalizer;
    private VerbManager mVerbManager;
    private Context mContext;
    private JSONArray mJsonArray;

    public VerbsCreate(Context context)
    {
        mContext = context;
        mNormalizer = new OrderNormalizer();
        mVerbManager = new VerbManager(this);
        mAssetManager = mContext.getApplicationContext().getAssets();
    }

    //****************************
    // Getters
    //****************************

    private AssetManager getAssetManager() { return mAssetManager; }
    private OrderNormalizer getNormalizer() { return mNormalizer; }
    private VerbManager getVerbManager() { return mVerbManager; }
    private Context getContext() { return mContext; }
    private JSONArray getJsonArray() { return mJsonArray; }

    //****************************
    // Methods
    //****************************

    /**
     * Read the json file and create a new json array
     */
    public void readJsonFile()
    {
        String json = null;

        try {
            String fileName = "verbs.json";
            InputStream inputStream = getAssetManager().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != json) {
            try {
                mJsonArray = new JSONArray(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create verbs by reading the json array
     */
    public boolean createVerbs()
    {
        boolean isSync = false;
        String conjugateVerb, infinitiveVerb = "";

        // Reading json
        readJsonFile();

        if (null != getJsonArray()) {
            try {
                Verb verb = new Verb();
                JSONObject jObject;
                JSONArray verbArray;

                for (int i = 0; i < getJsonArray().length(); i++) {

                    // Check if key exists in json array
                    if (getJsonArray().getJSONObject(i).has("Infinitif")) {
                        jObject = getJsonArray().getJSONObject(i).getJSONObject("Infinitif");

                        if (jObject.has("Présent")) {

                            verbArray = jObject.getJSONArray("Présent");
                            infinitiveVerb = getNormalizer().normalize(verbArray.get(0).toString());

                            // Set infinitive
                            if (!getVerbManager().checkInfinitiveExist(infinitiveVerb, -1)) {
                                verb.setInfinitiveVerb(infinitiveVerb);
                            } else {
                                verb.setInfinitiveVerb("");
                            }
                        }
                    }

                    // Check if key exists in json array
                    if (!verb.getInfinitiveVerb().equals("") && getJsonArray().getJSONObject(i).has("Impératif")) {
                        jObject = getJsonArray().getJSONObject(i).getJSONObject("Impératif");

                        if (jObject.has("Présent")) {

                            verbArray = jObject.getJSONArray("Présent");
                            verb.setImperativeVerbFirst("");
                            verb.setImperativeVerbSecond("");
                            verb.setImperativeVerbThird("");

                            // Set imperative first, second and third form
                            if (!verbArray.optString(0).equals("")) {
                                conjugateVerb = getNormalizer().normalize(verbArray.get(0).toString());
                                verb.setImperativeVerbFirst(conjugateVerb);
                            }

                            if(!verbArray.optString(1).equals("")) {
                                conjugateVerb = getNormalizer().normalize(verbArray.get(1).toString());
                                verb.setImperativeVerbSecond(conjugateVerb);
                            }

                            if(!verbArray.optString(2).equals("")) {
                                conjugateVerb = getNormalizer().normalize(verbArray.get(2).toString());
                                verb.setImperativeVerbThird(conjugateVerb);
                            }

                            // Create a new verb
                            getVerbManager().create(verb);
                        }
                    }
                }

                isSync = true;
            } catch (JSONException e) {
                e.printStackTrace();
                isSync = false;
            }
        }

        return isSync;
    }
}
