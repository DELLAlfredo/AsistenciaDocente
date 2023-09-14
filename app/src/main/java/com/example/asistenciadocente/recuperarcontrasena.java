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
import com.android.volley.Request;
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
    String Usuario,Paswword;

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
                } else if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    guardar("http://201.164.155.166/api_checador/cambiar-contraseña", email, password);
                }
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void guardar(String URL, String email, String password) {
        StringRequest guardarRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String trimmedResponse = response.trim();
                        Log.d("RESPONSE", trimmedResponse);

                        if (response.equals("{\"message\":\"CORREO_NO_REGISTRADO\"}")){
                            Toast.makeText(recuperarcontrasena.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("{\"email\":\"" + Usuario + "\",\"contraseña\":\"" + Paswword + "\"}")) {
                            // Respuesta inesperada del servidor
                            Log.e("Unexpected Response", response);
                        } else {
                            // Iniciar actividad después de la autenticación exitosa
                            Toast.makeText(recuperarcontrasena.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
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
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("contraseña", password);  // Asegúrate de usar "contraseña" aquí si es la clave correcta en el servidor
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(guardarRequest);
    }


    public void login(View v) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
