package com.alekseyvasilev.morseflashlight;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import android.hardware.camera2.*;

public class MainActivity extends AppCompatActivity {
    HashMap<Character, String> symbolsAndMeanings = new HashMap<>();
    int dotDuration = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fulFill();
        SeekBar seekBar = findViewById(R.id.seekBar);
        // Duration of dot.
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView seekBarValue = findViewById(R.id.seekBarValue);
                dotDuration = progress;
                StringBuilder sb = new StringBuilder();
                sb.append("Dot duration: ");
                sb.append(progress);
                sb.append(" ms");
                seekBarValue.setText(sb.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    // It's not a hardcoding. There is no other way to fulfill a HashMap in Java.
    public void fulFill(){
        symbolsAndMeanings.put('a', ". -");
        symbolsAndMeanings.put('b', "- . . . ");
        symbolsAndMeanings.put('c', "- . - .");
        symbolsAndMeanings.put('d', "- . .");
        symbolsAndMeanings.put('e', ".");
        symbolsAndMeanings.put('f', ". . - .");
        symbolsAndMeanings.put('g', "- - .");
        symbolsAndMeanings.put('h', ". . . .");
        symbolsAndMeanings.put('i', ". .");
        symbolsAndMeanings.put('j', ". - - -");
        symbolsAndMeanings.put('k', "- . -");
        symbolsAndMeanings.put('l', ". - . .");
        symbolsAndMeanings.put('m', "- -");
        symbolsAndMeanings.put('n', "- .");
        symbolsAndMeanings.put('o', "- - -");
        symbolsAndMeanings.put('p', ". - - .");
        symbolsAndMeanings.put('q', "- - . -");
        symbolsAndMeanings.put('r', ". - .");
        symbolsAndMeanings.put('s', ". . .");
        symbolsAndMeanings.put('t', "-");
        symbolsAndMeanings.put('u', ". . -");
        symbolsAndMeanings.put('v', ". . . -");
        symbolsAndMeanings.put('w', ". - -");
        symbolsAndMeanings.put('x', "- . . -");
        symbolsAndMeanings.put('y', "- . - -");
        symbolsAndMeanings.put('z', "- - . .");

        symbolsAndMeanings.put('0', "- - - - -");
        symbolsAndMeanings.put('1', ". - - - -");
        symbolsAndMeanings.put('2', ". . - - -");
        symbolsAndMeanings.put('3', ". . . - -");
        symbolsAndMeanings.put('4', ". . . . -");
        symbolsAndMeanings.put('5', ". . . . .");
        symbolsAndMeanings.put('6', "- . . . .");
        symbolsAndMeanings.put('7', "- - . . .");
        symbolsAndMeanings.put('8', "- - - . .");
        symbolsAndMeanings.put('9', "- - - - .");

        symbolsAndMeanings.put('_', ". . - - . -");
        symbolsAndMeanings.put('.', ". - . - . -");
        symbolsAndMeanings.put(',', "- - . . - -");
        symbolsAndMeanings.put('?', ". . - - . .");
        symbolsAndMeanings.put('\'', ". - - - - .");
        symbolsAndMeanings.put('!', "- . - . - -");
        symbolsAndMeanings.put('/', "- . . - .");
        symbolsAndMeanings.put('(', "- . - - .");
        symbolsAndMeanings.put(')', "- . - - . -");
        symbolsAndMeanings.put('&', ". - . . .");
        symbolsAndMeanings.put(':', "- - - . . .");
        symbolsAndMeanings.put(';', "- . - . - .");
        symbolsAndMeanings.put('=', "- . . . -");
        symbolsAndMeanings.put('+', ". - . - .");
        symbolsAndMeanings.put('-', "- . . . . -");
        symbolsAndMeanings.put('\"', ". - . . - .");
        symbolsAndMeanings.put('$', ". . . - . . -");
        symbolsAndMeanings.put('@', ". - - . - .");
    }

    // Closing the app.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Transforming String to Morse Code.
    private String simplified(String inp) {
        String[] temp = inp.toLowerCase().split(" +");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length; i++) {
            char[] lettersOfTheWord = temp[i].toCharArray();
            for (int j = 0; j < temp[i].length(); j++) {
                sb.append(symbolsAndMeanings.get(lettersOfTheWord[j]));
                sb.append("   ");
            }
            sb.append("       ");
        }
        return sb.toString();
    }

    // Turning the light on or switching it off.
    public void turnOnOrSwitchOffTheLight(Boolean onOrOff){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, onOrOff);
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Camera is unreachable. Maybe, your device has no " +
                    "flashlight, maybe it's currently unavailable.", Toast.LENGTH_SHORT).show();
        }
    }

    // Sending information via flashlight.
    public void onMainButtonClcik(View view) {
        EditText input = findViewById(R.id.InputText);
        String inputText = input.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Please, print anything.", Toast.LENGTH_SHORT).show();
        }
        String temp = simplified(inputText.replaceAll("[^A-Za-z\\d_ .,?'!/()&:;=+\"$@-]+", ""));
        TextView output = findViewById(R.id.OutputText);
        output.setText(temp);
        final char [] toConvert = temp.toCharArray();

        // Converting to light-flashes.
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < toConvert.length; i++) {
                    if (toConvert[i] == ' ') {
                        turnOnOrSwitchOffTheLight(false);
                        try {
                            TimeUnit.MILLISECONDS.sleep(dotDuration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (toConvert[i] == '.') {
                        turnOnOrSwitchOffTheLight(true);
                        try {
                            TimeUnit.MILLISECONDS.sleep(dotDuration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (toConvert[i] == '-') {
                        turnOnOrSwitchOffTheLight(true);
                        try {
                            TimeUnit.MILLISECONDS.sleep(dotDuration*3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t1.start();
        turnOnOrSwitchOffTheLight(false);
    }
}
