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
    EditText txtName, txtEmail, pass,passconfirm;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        txtName = findViewById(R.id.ednombre);
        txtEmail = findViewById(R.id.etemail);
        pass = findViewById(R.id.etcontraseña);
        passconfirm=findViewById(R.id.etcontraseñavalidar);
        btn_insert = findViewById(R.id.btn_register);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidRegistrationInput()) { // Verificar si los datos de registro son válidos
                    insertData();
                }
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private boolean isValidRegistrationInput() {
        final String nombre = txtName.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String passwordConfirm = passconfirm.getText().toString().trim();

        if (nombre.isEmpty()) {
            txtName.setError("Complete los campos");
            return false;
        } else if (email.isEmpty()) {
            txtEmail.setError("Complete los campos");
            return false;
        } else if (!isValidEmail(email)) {
            txtEmail.setError("Ingrese un correo electrónico válido");
            return false;
        } else if (password.isEmpty()) {
            pass.setError("Complete los campos");
            return false;
        } else if (password.length() < 6) {
            pass.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        } else if (!password.equals(passwordConfirm)) {
            passconfirm.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }
    private void insertData() {
        final String nombre = txtName.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String passwordConfirm = passconfirm.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");

        if (nombre.isEmpty()) {
            txtName.setError("Complete los campos");
            return;
        } else if (email.isEmpty()) {
            txtEmail.setError("Complete los campos");
            return;
        } else if (password.isEmpty()) {
            pass.setError("Complete los campos");
            return;
        } else if (!password.equals(passwordConfirm)) {
            passconfirm.setError("Las contraseñas no coinciden");
            return;
        }

        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "http://201.164.155.166/api_checador/usuarios/registro",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (!response.equalsIgnoreCase("Usuario registrado correctamente")) {
                            Toast.makeText(RegistroUsuario.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegistroUsuario.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void login(View v) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }


}
