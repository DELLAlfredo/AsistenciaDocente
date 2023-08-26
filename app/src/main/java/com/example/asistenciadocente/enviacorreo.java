package com.example.asistenciadocente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asistenciadocente.Login;
import com.example.asistenciadocente.R;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class enviacorreo extends AppCompatActivity {

    private EditText Correo;
    private ImageButton enviacodigo;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviacorreo);

        Correo = findViewById(R.id.edtCorreo);
        enviacodigo = findViewById(R.id.enviacorreo);
        client = new OkHttpClient();

        enviacodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarCorreo();
            }
        });
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

    public void enviarCorreo() {
        String emailAddress = Correo.getText().toString();
        String serverUrl = "http://192.168.0.11:80/Checador/verificacion.php"; // Replace with your server URL

        FormBody requestBody = new FormBody.Builder()
                .add("email", emailAddress)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure (e.g., network error)
                Log.e("EnviarCorreo", "Error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(enviacorreo.this, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle successful response (e.g., display a message)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(enviacorreo.this, "Solicitud enviada con éxito", Toast.LENGTH_SHORT).show();
                            // Aquí puedes abrir la pantalla de cambio de contraseña
                            // Ejemplo: startActivity(new Intent(enviacorreo.this, CambioContrasenaActivity.class));
                        }
                    });
                } else {
                    // Handle unsuccessful response (e.g., display an error message)
                    Log.e("EnviarCorreo", "Error en la respuesta del servidor: " + response.code());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(enviacorreo.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                response.close();
            }
        });
    }
}