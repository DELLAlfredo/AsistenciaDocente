package com.example.asistenciadocente;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistenciadocente.databinding.ActivityClasesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                .url("http://192.168.56.1:80/Checador/mostrarDatosClasesDocentes.php")
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
                .url("http://192.168.56.1:80/Checador/mostrarDatosClasesAula.php")
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
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(Clases.this, android.R.layout.simple_spinner_item, dataList); //Crea un adaptador de ArrayAdapter para vincular los datos de dataList al Spinner. Utiliza el contexto de la actividad  Clases que es este activity,
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
        });  //En resumen, el código realiza una solicitud HTTP para obtener una lista de nombres de aulas desde un servidor. Luego, procesa la respuesta JSON obtenida,
            // extrae los nombres de las aulas y los muestra en un Spinner en la interfaz de usuario de la actividad Clases.
            // Esto se realiza de forma asíncrona utilizando un Callback para manejar la respuesta de la solicitud HTTP.

        btnGuardarClases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               guardarDatosClases();
            } //ejecuta el metodo guardarDatosClases
        });
    }



   private void guardarDatosClases() {
        String docente = spDocentes.getSelectedItem().toString(); //Obtiene el elemento seleccionado del Spinner spDocentes y lo convierte en una cadena. Esto obtiene el valor seleccionado del docente.
        String aula = spAula.getSelectedItem().toString();        //Obtiene el elemento seleccionado del Spinner spAula y lo convierte en una cadena. Esto obtiene el valor seleccionado del aula.
        String hora = spHora.getSelectedItem().toString();        //Obtiene el elemento seleccionado del Spinner spHora y lo convierte en una cadena. Esto obtiene el valor seleccionado de la hora.
        String opcion = spOpcion.getSelectedItem().toString();    //Obtiene el elemento seleccionado del Spinner spOpcion y lo convierte en una cadena. Esto obtiene el valor seleccionado de la opción.

        String URL= "http://192.168.56.1:80/Checador/insertar_clases.php";

       Calendar calendar = Calendar.getInstance();                      //Obtiene una instancia del calendario actual.
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());  //Crea un formato de fecha en el estilo "yyyy-MM-dd" utilizando el local predeterminado del dispositivo.
       String fechaActual = dateFormat.format(calendar.getTime());                                    //Obtiene la fecha actual formateada según el formato establecido.


       StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, URL,  //Crea una solicitud de cadena (StringRequest) utilizando el método POST y la URL proporcionada. También se definen los manejadores de respuesta exitosa y de error.

                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {                               //repuesta exitosa
                        Toast.makeText(Clases.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {                        // respuesta de error
                       // Toast.makeText(getApplicationContext(), "Fallo", Toast.LENGTH_SHORT).show();
                       Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError { //Sobrescribe el método getParams() para proporcionar los parámetros de la solicitud HTTP. Los datos de la clase (docente, aula, hora, opcion y fechaActual) se agregan a un mapa de parámetros.
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);   //Crea una nueva cola de solicitudes de Volley.
        requestQueue.add(request);                                         //Agrega la solicitud a la cola de solicitudes de Volley para que se ejecute.
    }
    //En resumen, el código recopila los datos de la clase seleccionados por el usuario,
    // formatea la fecha actual y realiza una solicitud HTTP para guardar los datos en un servidor.


}