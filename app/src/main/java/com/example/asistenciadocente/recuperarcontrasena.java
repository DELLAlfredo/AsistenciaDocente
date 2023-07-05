package com.example.asistenciadocente;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class recuperarcontrasena extends AppCompatActivity {

    private EditText editTextEmail,edtContraseña,edtConfirmpass;
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
                String email = editTextEmail.getText().toString().trim();
                String newPassword = edtContraseña.getText().toString().trim();
                String confirmNewPassword = edtConfirmpass.getText().toString().trim();

                if (email.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    Toast.makeText(recuperarcontrasena.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(recuperarcontrasena.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                cambiarContrasena(email, newPassword);
            }
        });
    }

    private void cambiarContrasena(String email, String newPassword) {
        // URL de la API para cambiar la contraseña
        String url = "http://192.168.56.1:80/checador/obtenerCorreo.php";

        // Construir el objeto JSON con los datos necesarios para la API
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("email", email);
            requestData.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud POST a la API de cambio de contraseña
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // La contraseña se cambió exitosamente
                                Toast.makeText(getApplicationContext(), "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                            } else {
                                // Hubo un error al cambiar la contraseña
                                Toast.makeText(getApplicationContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error de respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error de conexión
                        Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }


}
