package com.example.asistenciadocente;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistenciadocente.databinding.ActivityAbcAulaBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class abcAula extends menu {
    Button btnBuscarAula,btnEditarAula,btnBorrarAula,btnGuardarAula;
    EditText etNombreAula,etIdAula;
    RequestQueue requestQueue;
    ActivityAbcAulaBinding activityAbcAulaBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAbcAulaBinding = ActivityAbcAulaBinding.inflate(getLayoutInflater());

        setContentView(activityAbcAulaBinding.getRoot());
        allocateActivityTitle("Aulas");

        btnBorrarAula=findViewById(R.id.btnBorrarAula);
        btnEditarAula=findViewById(R.id.btnEditarAula);
        btnBuscarAula=findViewById(R.id.btnBuscarAula);
        btnGuardarAula=findViewById(R.id.btnGuardarAula);
        etNombreAula=findViewById(R.id.etNombreAula);
        etIdAula=findViewById(R.id.etIdAula);


        /*--------------------------------------------------------*/
        btnGuardarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Insetar("http://201.164.155.166/api_checador/aulas");

            }
        });
        btnBuscarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idAula = etIdAula.getText().toString();
                Log.d("DEBUG", "ID de aula a buscar: " + idAula);  // Agregar este log

                if (idAula.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese un ID de aula válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "http://201.164.155.166/api_checador/aulas/" + idAula;
                buscarAula(url);
            }
        });
        btnEditarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Insetar("http://201.164.155.166/api_checador/aulas/update");
            }
        });
        btnBorrarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Borrar("http://201.164.155.166/api_checador/aulas/delete");
            }
        });


    }
    private void Insetar(String URL) {
        // Validar campos vacíos
        if (etNombreAula.getText().toString().isEmpty() || etIdAula.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest guardarRequest = new StringRequest(Request.Method.POST, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Verificar la respuesta del servidor
                        String trimmedResponse = response.trim(); // Eliminar espacios en blanco antes y después
                        Log.d("RESPONSE", trimmedResponse); // Imprimir la respuesta para verificar
                        if (response.equals("{\"message\":\"EXISTE\"}")){
                            Toast.makeText(abcAula.this, "ID o Aula Existente", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("{\"idAula\":\"" + etIdAula + "\",\"nombre_aula\":\"" + etNombreAula + "\"}")) {
                            // Respuesta inesperada del servidor
                            Toast.makeText(abcAula.this, "Error servidor ", Toast.LENGTH_SHORT).show();

                            Log.e("Unexpected Response", response);
                        } else {
                            Toast.makeText(getApplicationContext(), "Se Realizo La Operacion Correctamente", Toast.LENGTH_SHORT).show();
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
                parametros.put("idAula", etIdAula.getText().toString());
                parametros.put("nombre_aula", etNombreAula.getText().toString());
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(guardarRequest);

    }
    private void buscarAula(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        final String nombreAula = jsonObject.getString("nombre_aula");

                        // Ejecuta el código en el hilo de la IU usando runOnUiThread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                etNombreAula.setText(nombreAula);
                            }
                        });
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Numero de ID Inexistente", Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("DEBUG", "Respuesta de la API: " + response.toString());  // Agregar este log

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void Borrar(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Aula Borrada",Toast.LENGTH_SHORT).show();
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
                parametros.put("idAula",etIdAula.getText().toString());
                return  parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void limpiarFormulario(){
        etIdAula.setText("");
        etNombreAula.setText("");
    }

}