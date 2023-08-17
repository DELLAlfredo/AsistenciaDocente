package com.example.asistenciadocente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class enviacorreo extends AppCompatActivity {

    EditText Correo;
    Button btnCorreo;
    ImageButton enviacodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviacorreo);

        Correo = findViewById(R.id.edtCorreo);
        btnCorreo = findViewById(R.id.btnCorreo);
        enviacodigo = findViewById(R.id.enviacorreo);

        enviacodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarCorreo();
            }
        });
    }

    public void enviarCorreo() {


    }

    public void login(View v) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
