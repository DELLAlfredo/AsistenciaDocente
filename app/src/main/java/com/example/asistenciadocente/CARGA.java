package com.example.asistenciadocente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class CARGA extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                boolean sesion = preferences.getBoolean("sesion", false);
                String usuario = preferences.getString("email", "");

                if (sesion) {
                    Intent intent;
                    if (usuario.equals("Admin")) {
                        intent = new Intent(getApplicationContext(), Clases.class);
                    } else if (usuario.equals("Admin2")) {
                        intent = new Intent(getApplicationContext(), EditarInforme.class);
                    } else {
                        // Si por alguna razón el usuario guardado no coincide, se dirige al Login
                        intent = new Intent(getApplicationContext(), Login.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
            }
        }, 2000);  // Tiempo de espera de 2 segundos antes de redirigir
    }
}
