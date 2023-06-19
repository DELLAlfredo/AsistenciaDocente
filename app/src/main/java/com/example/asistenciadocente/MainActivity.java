package com.example.asistenciadocente;

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

public class MainActivity extends AppCompatActivity {
    EditText nombre,contraseña;
    Button ingresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre= findViewById(R.id.nombre);
        contraseña=findViewById(R.id.contraseña);
        ingresar =findViewById(R.id.loginButton);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent inten = new Intent(getApplicationContext(), abcDocentes.class);
                //startActivity(inten);
                validarUsuario("http://192.168.0.6:80/Checador/Validar_Usuario.php");
            }
        });
    }

    private void validarUsuario(String URL){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    Intent inten = new Intent(getApplicationContext(), Clases.class);
                    startActivity(inten);
                }else{
                    //mensaje en caso de no existir o equivocarse en usuario
                    Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mensaje de error por internet o conexion
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //insersion de datos para validar usuario y comparar
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