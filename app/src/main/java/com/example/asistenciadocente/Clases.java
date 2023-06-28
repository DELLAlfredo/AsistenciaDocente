package com.example.asistenciadocente;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.privacysandbox.tools.core.model.Method;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistenciadocente.databinding.ActivityClasesBinding;
import com.google.androidgamesdk.gametextinput.Listener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Clases extends menu {
    ActivityClasesBinding activityClasesBinding;
    Spinner spDocentes,spAula,spHora, spOpcion;
    Button btnGuardarClases;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityClasesBinding = ActivityClasesBinding.inflate(getLayoutInflater());
        setContentView(activityClasesBinding.getRoot());
        spDocentes = findViewById(R.id.spDocentes);
        spAula = findViewById(R.id.spAula);
        spHora = findViewById(R.id.spHora);
        spOpcion = findViewById(R.id.spOpcion);
        btnGuardarClases = findViewById(R.id.btnGuardarClases);

        String[] horas = {"7AM-8AM","8AM-9AM","9AM-10AM","10AM-11AM","11AM-12AM","12AM-1PM","1PM-2PM","2PM-3PM"};
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, horas);
        spHora.setAdapter(Adapter);
        String[] crud = {"IMPARTIDA","NO IMPARTIDA","CLASE INCOMPLETA","SUSPENCION"};
        ArrayAdapter<String> AdapterCrud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, crud);
        spOpcion.setAdapter(AdapterCrud);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder ()
                .url("http://192.168.0.7:80/Checador/mostrarDatosClasesDocentes.php")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        List<String> dataList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i); //para obtener un objeto JSON específico de un JSONArray.
                            String nombre = jsonObject.getString("nombre"); // manda a llamar y muestra lo que contiene el campo nombre
                            String apellidos = jsonObject.getString("apellidos"); // manda a llamar y muestra lo que contiene el campo apellido
                            String nombreCompleto = nombre + " " + apellidos; //Une ambos campos para quese muestren
                            dataList.add(nombreCompleto);
                        }

                        // Poblar el Spinner con los datos
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Spinner spinner = findViewById(R.id.spDocentes);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(Clases.this, android.R.layout.simple_spinner_item, dataList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        OkHttpClient clientAula = new OkHttpClient();
        Request aula = new Request.Builder ()
                .url("http://192.168.0.7:80/Checador/mostrarDatosClasesAula.php")
                .build();

        clientAula.newCall(aula).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        List<String> dataList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String nombre_aula = jsonObject.getString("nombre_aula"); // muestra el nombre del aula
                            dataList.add(nombre_aula);
                        }

                        // Poblar el Spinner con los datos
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Spinner spinner = findViewById(R.id.spAula); //
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(Clases.this, android.R.layout.simple_spinner_item, dataList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnGuardarClases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               guardarDatosClases();
            }
        });
    }



   private void guardarDatosClases() {
        String docente = spDocentes.getSelectedItem().toString();
        String aula = spAula.getSelectedItem().toString();
        String hora = spHora.getSelectedItem().toString();
        String opcion = spOpcion.getSelectedItem().toString();

        String URL= "http://192.168.0.7:80/Checador/insertar_clases.php";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());// fecha actual en la que se guarden
        String fechaActual = dateFormat.format(new Date());


       StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, URL,

                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Clases.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("docentes", docente);
                parametros.put("aula", aula);
                parametros.put("hora", hora);
                parametros.put("opcion", opcion);
                parametros.put("fecha", fechaActual);
                return parametros;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


}