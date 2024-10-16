package com.example.asistenciadocente;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.asistenciadocente.databinding.ActivityEditarInformeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditarInforme extends menuadministradores {
    Spinner spDocentes,spAula,spHora, spOpcion;
    Button btnGuardarClases;
    DatePicker dpfecha;

    ImageButton Btncalendario;
    EditText txtfecha;
    ActivityEditarInformeBinding activityEditarInformeBinding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_informe);
        activityEditarInformeBinding = ActivityEditarInformeBinding.inflate(getLayoutInflater());
        setContentView(activityEditarInformeBinding.getRoot());
        allocateActivityTitle("Editar Clases");

        spDocentes = findViewById(R.id.spDocentes);
        spAula = findViewById(R.id.spAula);
        spHora = findViewById(R.id.spHora);
        spOpcion = findViewById(R.id.spOpcion);
        btnGuardarClases = findViewById(R.id.btnGuardarClases);
        txtfecha = findViewById(R.id.etfecha);
        dpfecha = findViewById(R.id.dtfecha);

        txtfecha.setText(getfecha());

        dpfecha.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String fechaSeleccionada = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                txtfecha.setText(fechaSeleccionada);
                dpfecha.setVisibility(View.GONE);
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        btnGuardarClases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtinen los balores de los campos
                String docente = spDocentes.getSelectedItem().toString();
                String aula = spAula.getSelectedItem().toString();
                String hora = spHora.getSelectedItem().toString();
                String opcion = spOpcion.getSelectedItem().toString();
                String fecha = txtfecha.getText().toString();

                // Crear el JSON con los datos para almacenar los en la db del servidor
                JSONObject json = new JSONObject();
                try {
                    json.put("docentes", docente);
                    json.put("aula", aula);
                    json.put("hora", hora);
                    json.put("opcion", opcion);
                    json.put("fecha", fecha);
                } catch (Exception e) {
                    // Imprime la traza de la excepción en caso de error
                    e.printStackTrace();
                }

                // Crear un cliente HTTP usando OkHttp
                OkHttpClient client = new OkHttpClient();
                // Define el tipo de contenido como JSON
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                // Crea el cuerpo de la solicitud con el objeto JSON
                RequestBody body = RequestBody.create(JSON, json.toString());

                // Construye la solicitud HTTP POST
                Request request = new Request.Builder()
                        .url("http://201.164.155.166/api_checador/Modificar_Registro")//URL de la api o endpoint
                        .post(body)// Establece el método POST y el cuerpo de la solicitud
                        .build();

                // Ejecuta la solicitud HTTP de manera asíncrona
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // En caso de fallo, ejecuta en el hilo principal de la UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Muestra un mensaje Toast indicando el error
                                Toast.makeText(EditarInforme.this, "Error en la solicitud HTTP", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // Obtiene la respuesta del servidor en forma de cadena
                        final String responseData = response.body().string();
                        // Ejecuta en el hilo principal de la UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Convierte la respuesta en un objeto JSON
                                    JSONObject jsonResponse = new JSONObject(responseData);
                                    // Obtiene el mensaje de la respuesta JSON
                                    String message = jsonResponse.getString("message");
                                    // Muestra el mensaje en un Toast
                                    Toast.makeText(EditarInforme.this, message, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    // Imprime la traza de la excepción en caso de error
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //spiners horas y eventos
        String[] horas = {"7AM-8AM","8AM-9AM","9AM-10AM","10AM-11AM","11AM-12AM","12AM-1PM","1PM-2PM","2PM-3PM"};
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, horas);
        spHora.setAdapter(Adapter);
        String[] crud = {"DIA NO LABORAL","CLASE IMPARTIDA","CLASE NO IMPARTIDA","GRUPO SIN ALUMNOS","JUSTIFICACION ACADEMICA","PERMISO","PROBLEMAS DE SALUD","COMISION","FALLA TECNICA DE AULA","CLASE INCOMPLETA"};
        ArrayAdapter<String> AdapterCrud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, crud);
        spOpcion.setAdapter(AdapterCrud);

        ////////////obtener docentes
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder ()
                .url("http://201.164.155.166/api_checador/obtener-docentes")
                //.url("http://192.168.11.152:80/api_checador/obtener-docentes")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { ////Este método se ejecuta si la llamada HTTP falla. Imprime el rastro de errores en la consola.
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
                            String nombre = jsonObject.getString("nombre"); // // Extrae el valor de la clave "nombre" del objeto JSON y lo guarda en la variable nombre.
                            String apellidos = jsonObject.getString("apellidos"); // Extrae el valor de la clave "apellidos" del objeto JSON y lo guarda en la variable apellidos.
                            String nombreCompleto = nombre + " " + apellidos; //Une ambos campos para que se guarden
                            dataList.add(nombreCompleto);      //Agrega el nombre del docente a la lista dataList.
                        }

                        // Poblar el Spinner con los datos
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Spinner spinner = findViewById(R.id.spDocentes);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarInforme.this, android.R.layout.simple_spinner_item, dataList);
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
//obtener aulas
        OkHttpClient clientAula = new OkHttpClient();
        Request aula = new Request.Builder ()
                .url("http://201.164.155.166/api_checador/aulas")
                //.url("http://192.168.11.152:80/api_checador/aulas")
                .build();

        clientAula.newCall(aula).enqueue(new Callback() { //: hace una  llamada a una URL específica utilizando el cliente OkHttp y asocia un Callback para manejar la respuesta de la llamada de manera asíncrona.
            @Override
            public void onFailure(Call call, IOException e) {//Este método se ejecuta si la llamada HTTP falla. Imprime el rastro de errores en la consola.
                Toast.makeText(getApplicationContext(), "Fallo la solicitud HTTP", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { // Este método se ejecuta cuando se recibe una respuesta HTTP. Verifica si la respuesta es exitosa y luego procesa la respuesta.
                if (response.isSuccessful()) {                                        //Verifica si la respuesta HTTP fue exitosa (código de respuesta en el rango 200-299 satisfactorias).
                    String jsonData = response.body().string();                       //Obtiene el cuerpo de la respuesta HTTP como una cadena de texto.

                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);                  //Convierte la cadena de texto JSON en un objeto JSONArray.
                        List<String> dataList = new ArrayList<>();                      //Crea una lista vacía para almacenar los nombres de las aulas.

                        for (int i = 0; i < jsonArray.length(); i++) {                  //Itera (repetir) sobre cada objeto JSON dentro del JSONArray.
                            JSONObject jsonObject = jsonArray.getJSONObject(i);         //Obtiene el objeto JSON en la posición i dentro del JSONArray.
                            String nombre_aula = jsonObject.getString("nombre_aula"); // Extrae el valor de la clave "nombre_aula" del objeto JSON y lo guarda en la variable nombre_aula.
                            dataList.add(nombre_aula);                                         //Agrega el nombre del aula a la lista dataList.
                        }

                        // Poblar el Spinner con los datos
                        runOnUiThread(new Runnable() {                              //Ejecuta el bloque de código en el subproceso de la interfaz de usuario (UI) principal.
                            @Override
                            public void run() {
                                Spinner spinner = findViewById(R.id.spAula);        //Obtiene una referencia al Spinner en la interfaz de usuario utilizando su ID = spAula.
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarInforme.this, android.R.layout.simple_spinner_item, dataList); //Crea un adaptador de ArrayAdapter para vincular los datos de dataList al Spinner. Utiliza el contexto de la actividad  Clases que es este activity,
                                //para mostrar cada elemento en el Spinner y la lista de datos dataList.
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Especifica el diseño de vista desplegable para el adaptador del Spinner.
                                spinner.setAdapter(adapter); //Asigna el adaptador al Spinner, lo que muestra los datos en el Spinner y permite al usuario seleccionar un elemento de la lista.
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //salir de configuracion de editar informes
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir");
        builder.setMessage("¿Desea salir ?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity(); // Cerrar todas las actividades y salir de la aplicación
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //FECHA
    public String getfecha(){

        String dia = "";
        if (dpfecha != null) {
            dia = String.format("%02d", dpfecha.getDayOfMonth());
        }

        String mes = "";
        if (dpfecha != null) {
            mes = String.format("%02d", dpfecha.getMonth() + 1);
        }

        String año = "";
        if (dpfecha != null) {
            año = String.format("%04d", dpfecha.getYear());
        }

        return año + "-" + mes + "-" + dia;


    }
    //mostrar fecha cuando selecciones
    public void muestraCalendarioeditarinforme(View View){
        dpfecha.setVisibility(View.VISIBLE);
    }
    // Dentro de la actividad EditarInforme


}