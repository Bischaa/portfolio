/*
IFT2905 - H2021 - Devoir 1
Équipe 28:
Come M, Olivier Guy, Guillaume P., Maxime Ton et Xuanxuan
*/

package com.example.dev1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button b;
    Handler timer;
    TextView text;
    Chronometer chronometer;
    //PopupWindow popUp;

    boolean repos;
    boolean attente;
    boolean attenteClic;
    boolean start;
    boolean succes;

    int count;
    float [ ] tableauResultats = new float[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //popUp = new PopupWindow(R.layout.endgame_popupwindow);

        b = findViewById(R.id.button);
        b.setText("Appuyer pour débuter le jeu");
        b.setTextColor(getResources().getColor(R.color.white));
        b.setBackgroundColor(getResources().getColor(R.color.light_blue));
        b.setOnClickListener(b_listener);

        text = findViewById(R.id.text);
        text.setText("Veuillez débuter!");
        text.setTextColor(getResources().getColor(R.color.black));
        text.setBackgroundColor(getResources().getColor(R.color.moccasin));

        timer = new Handler();
        count = 1;

        repos = true;
        attente = false;
        attenteClic = false;
        succes = false;
        start = false;

        chronometer = findViewById(R.id.chrono);
        chronometer.setBackgroundColor(getResources().getColor(R.color.moccasin));
    }

    float CalculerTempsReactionMoyen(){

        float moyenneTpsReaction = 0;

        for(int i = 0; i < 5; i++)
            moyenneTpsReaction += tableauResultats[i];

        moyenneTpsReaction = moyenneTpsReaction / (5 * 1000);
        return moyenneTpsReaction;
    }

    View.OnClickListener b_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (start) {
                /* l'application est en mode jeux (l'utilisateur doit cliquer sur le bouton)*/
                count += 1;
                succes = true;

                b.setBackgroundColor(Color.GREEN);
                b.setText("Succès");

                chronometer.stop();
            }
            if (attente) {
                /* l'application est en mode attente */
                attenteClic = true;
            }
            if (repos) {
                /* l'application est en mode repos */
                repos = false;
                attente = true;

                b.setText("Attendez que le bouton devienne jaune...");
                b.setBackgroundColor(getResources().getColor(R.color.grey));
                text.setText("Essai " + count + " de 5");

                chronometer.setVisibility(View.VISIBLE);
            }
            timer_listener.run();
        }
    };

    Runnable timer_listener = new Runnable() {
        @Override
        public void run() {

            //Le paramètre du AlertDialog GP
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Résultat");
            alert.setMessage("Votre temps de réaction moyen est de " + CalculerTempsReactionMoyen() + " secondes");
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            repos = true;
                            count = 1;

                            b.setText("Appuyer pour débuter le jeu");
                            b.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            b.setTextColor(getResources().getColor(R.color.white));
                            text.setText("Veuillez débuter!");

                            chronometer.setVisibility(View.INVISIBLE);
                            alert.dismiss();
                        }
                    });

            /* si on clic pendant la phase d'attente */
            if (attenteClic) {
                b.setBackgroundColor(getResources().getColor(R.color.red));
                Toast.makeText(getApplicationContext(),
                        "Erreur...phase...Attente...", Toast.LENGTH_SHORT).show();
                timer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        b.setBackgroundColor(getResources().getColor(R.color.grey));
                        attenteClic = false;
                    }
                }, 1500);
            }
            //si on est en phase d'attente et que l'on a pas cliqué pendant cette phase (L'utilisateur doit cliquer le plus rapidement possible)
            if ((attente) && (attenteClic != true)){
                timer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        attente = false;
                        start = true;

                        b.setTextColor(getResources().getColor(R.color.black));
                        b.setText("Cliquez le plus vite possible!!");
                        b.setBackgroundColor(getResources().getColor(R.color.yellow));

                        chronometer.setBase(SystemClock.uptimeMillis());
                        chronometer.setFormat("%s");
                        chronometer.start();
                    }
                }, (int)(Math.random()*(10000-3000)+1000));

            }

            if(succes){// decalage de un car on commence le count a 1 GP
                timer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count < 6){
                            chronometer.stop();
                            tableauResultats[count - 1] = SystemClock.uptimeMillis() - chronometer.getBase();
                            chronometer.setBase(SystemClock.uptimeMillis());

                            attente = true;
                            attenteClic = false;
                            start = false;
                            succes=false;

                            text.setText("Essai " + count + " de 5");
                            b.setText("Attendez que le bouton devienne jaune...");
                            b.setBackgroundColor(getResources().getColor(R.color.grey));
                            b.setTextColor(getResources().getColor(R.color.white));

                            timer_listener.run();
                        }
                        else {
                            chronometer.stop();
                            succes = false;
                            start= false;

                            chronometer.setBase(SystemClock.uptimeMillis());
                            alert.show();
                        }
                    }
                },1500);
            }
        }
    };
}

