package com.example.asistenciadocente;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextInputEditText nombre,contraseña;
    Button ingresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre= findViewById(R.id.username);
        contraseña=findViewById(R.id.password);
        ingresar =findViewById(R.id.loginButton);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario("http://192.168.31.26/CHECKTECH/Validar_Usuario.php  ");
            }
        });
    }

    private void validarUsuario(String URL){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    Intent inten = new Intent(getApplicationContext(),Docentes.class);
                    startActivity(inten);
                }else{
                    Toast.makeText(MainActivity.this,"incorrecto",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"error de usuario",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> parametros = new HashMap<String, String>();
               parametros.put("nombre",nombre.getText().toString());
                parametros.put("contraseña",contraseña.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
    }
}