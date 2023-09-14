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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class enviacorreo extends AppCompatActivity {

    private EditText Correo,Clave;
    private ImageButton enviacodigo;
    private OkHttpClient client;
    private Button btnCorreo;
    String Usuario,Paswword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviacorreo);

        Clave=findViewById(R.id.Clave);
        Correo = findViewById(R.id.edtCorreo);
        enviacodigo = findViewById(R.id.enviacorreo);
        btnCorreo=findViewById(R.id.btnCorreo);
        client = new OkHttpClient();

        enviacodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario = Correo.getText().toString();
                if (isValidEmail(Usuario)) { // Verificar si el correo es válido
                    enviarCorreo();
                } else {
                    Toast.makeText(enviacorreo.this, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario = Correo.getText().toString();
                Paswword = Clave.getText().toString();

                if (!Usuario.isEmpty() && !Paswword.isEmpty()) {
                    if (isValidEmail(Usuario)) { // Verificar si el correo es válido
                        validarrecupeacion("http://201.164.155.166/api_checador/validar_recuperacion");
                    } else {
                        Toast.makeText(enviacorreo.this, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(enviacorreo.this, "No deje campos vacíos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
        int randomNumber = generateRandomNumber(1, 5); // Generate a random number between 1 and 5
        String serverUrl = "http://201.164.155.166/api_checador/Validacion"; // Replace with your server URL

        FormBody requestBody = new FormBody.Builder()
                .add("email", emailAddress)
                .add("recuperacion", String.valueOf(randomNumber)) // Add the random number to the request
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

    private int generateRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private void validarrecupeacion(String URL){
        //En el bloque getParams dentro de StringRequest, se definen los parámetros para enviar
        StringRequest StringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("{\"message\":\"Login failed\"}")){
                    Toast.makeText(enviacorreo.this, "Usuario o Codigo incorrectos", Toast.LENGTH_SHORT).show();
                } else if (response.equals("{\"email\":\"" + Usuario + "\",\"recuperacion\":\"" + Paswword + "\"}")) {
                    // Respuesta inesperada del servidor
                    Log.e("Unexpected Response", response);
                } else {
                    // Iniciar actividad después de la autenticación exitosa
                    Intent intent = new Intent(getApplicationContext(), recuperarcontrasena.class);
                    startActivity(intent);
                    finish();


                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mensaje de error por internet o conexion
                Toast.makeText(enviacorreo.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //insersion de datos para validar usuario y comparar
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("email",Usuario);
                parametros.put("recuperacion",Paswword);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
    }
}