package com.easysoftware.drill.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.easysoftware.drill.R;

import com.easysoftware.drill.ui.recognition.idiom.IdiomRecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem5.Poem5RecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem7.Poem7RecognitionActivity;
import com.easysoftware.drill.ui.solitaire.idiom.IdiomSolitaireActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btIdiomRecognition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IdiomRecognitionActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btPoem5Recognition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Poem5RecognitionActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btPoem7Recognition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Poem7RecognitionActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btIdiomSolitaire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IdiomSolitaireActivity.class);
                startActivity(intent);
            }
        });

    }
}
