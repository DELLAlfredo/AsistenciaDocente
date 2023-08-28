package com.example.asistenciadocente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class recuperarcontrasena extends AppCompatActivity {

    private EditText editTextEmail, edtContraseña, edtConfirmpass;
    private Button buttonRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperarcontrasena);
        editTextEmail = findViewById(R.id.editTextEmail);
        edtContraseña = findViewById(R.id.editTextNewPassword);
        edtConfirmpass = findViewById(R.id.editTextConfirmPassword);

        buttonRecover = findViewById(R.id.buttonRecover);

        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = edtContraseña.getText().toString();
                String confirmPassword = edtConfirmpass.getText().toString();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    guardar("https://checador.tech/api_checador/cambiar-contrasena", email, password);
                }
            }
        });
    }

    private void guardar(String URL, String email, String password) {
        StringRequest guardarRequest = new StringRequest(com.android.volley.Request.Method.POST, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String trimmedResponse = response.trim();
                        Log.d("RESPONSE", trimmedResponse);

                        if (trimmedResponse.equals("CORREO_NO_REGISTRADO")) {
                            Toast.makeText(getApplicationContext(), "Correo no registrado", Toast.LENGTH_SHORT).show();
                        } else if (trimmedResponse.equals("CONTRASEÑA_CAMBIADA")) {
                            Toast.makeText(getApplicationContext(), "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("email", email);
                parametros.put("contraseña", password);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(guardarRequest);
    }

    public void login(View v) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }


}