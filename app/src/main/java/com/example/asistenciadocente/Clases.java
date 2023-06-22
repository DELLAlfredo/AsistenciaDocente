package com.example.asistenciadocente;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.asistenciadocente.databinding.ActivityClasesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String nombre = jsonObject.getString("nombre"); // Reemplaza "nombre" por el nombre del campo en tu respuesta JSON
                            String apellidos = jsonObject.getString("apellidos"); // Reemplaza "apellido" por el nombre del campo en tu respuesta JSON
                            String nombreCompleto = nombre + " " + apellidos;
                            dataList.add(nombreCompleto);
                        }

                        // Poblar el Spinner con los datos
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Spinner spinner = findViewById(R.id.spDocentes); // Reemplaza "spinner" con el ID de tu Spinner en el layout
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
    }
}