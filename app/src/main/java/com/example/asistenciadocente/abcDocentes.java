package com.example.asistenciadocente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class abcDocentes extends AppCompatActivity {
    EditText etAcademia,etNombre, etApellido;
    Button btnGuardar,btnEditar,btnBorrar,btnBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abc_docentes);

        etAcademia = findViewById(R.id.etAcademia);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEditar = findViewById(R.id.btnEditar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnBuscar = findViewById(R.id.btnBuscar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Guardar("http://192.168.0.6:80/Checador/docentes.php");

            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Guardar("http://192.168.0.6:80/Checador/editar_docentes.php");
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Borrar("http://192.168.0.6:80/Checador/eliminar_docentes.php");
            }
        });


    }
    private void Guardar(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"OPERACION EXITOSA",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError{
                Map<String,String> parametros= new HashMap<String,String>();
                parametros.put("nombre",etNombre.getText().toString());
                parametros.put("apellido",etApellido.getText().toString());
                parametros.put("academia",etAcademia.getText().toString());
                return  parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void Borrar(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Producto eliminado",Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError{
                Map<String,String> parametros= new HashMap<String,String>();
                parametros.put("nombre",etNombre.getText().toString());

                return  parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void limpiarFormulario(){
        etNombre.setText("");
        etApellido.setText("");
        etAcademia.setText("");
    }
}