package com.example.asistenciadocente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class RegistroUsuario extends AppCompatActivity {
    EditText txtName, txtEmail, pass;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        txtName = findViewById(R.id.ednombre);
        txtEmail = findViewById(R.id.etemail);
        pass = findViewById(R.id.etcontraseña);
        btn_insert = findViewById(R.id.btn_register);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void insertData() {
        final String nombre = txtName.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String password = pass.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");

        if (nombre.isEmpty()) {
            txtName.setError("Complete los campos");
            return;
        } else if (email.isEmpty()) {
            txtEmail.setError("Complete los campos");
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.56.1/CHECKTECH/Insertar_Usuario.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (!response.equalsIgnoreCase("Usuario registrado correctamente")) {
                                Toast.makeText(RegistroUsuario.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish(); // Finaliza la actividad actual
                            } else {
                                Toast.makeText(RegistroUsuario.this, "no jala tu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistroUsuario.this, "No se puede insertar", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nombre", nombre);
                    params.put("email", email);
                    params.put("contraseña", password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(RegistroUsuario.this);
            requestQueue.add(request);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void login(View v) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}