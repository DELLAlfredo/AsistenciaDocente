package com.example.asistenciadocente;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistenciadocente.databinding.ActivityReporteBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Reporte extends menu {
    ActivityReporteBinding activityReporteBinding;
    Spinner sphora,spopcion;
    EditText txtfecha;

    ImageButton Btncalendario;
    DatePicker dpfecha;
    TableLayout tabla;
    Button btnbusquedafiltrada,btnbusquedafiltradaporfecha;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReporteBinding = ActivityReporteBinding.inflate(getLayoutInflater());
        setContentView(activityReporteBinding.getRoot());
        allocateActivityTitle("Reporte");


        sphora = findViewById(R.id.sphora);
        String[] hora = {"7AM-8AM", "8AM-9AM", "9AM-10AM", "10AM-11AM", "11AM-12AM", "12AM-1PM", "1PM-2PM", "2PM-3PM"};
        ArrayAdapter<String> Adapterhora = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hora);
        sphora.setAdapter(Adapterhora);

        spopcion = findViewById(R.id.spopcion);
        String[] opcion = {"IMPARTIDA", "NO IMPARTIDA", "CLASE INCOMPLETA", "SUSPENCION"};
        ArrayAdapter<String> Adapteropcion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcion);
        spopcion.setAdapter(Adapteropcion);

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

        tabla = findViewById(R.id.tabla);

        // Realizar una solicitud a la API para obtener los datos
        String url = "http://192.168.56.1:80/checador/reporteselect.php"; // Reemplaza con la URL de tu API

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtener el array de resultados de la respuesta JSON
                            JSONArray resultados = response.getJSONArray("results");

                            // Limpiar la tabla antes de agregar los nuevos datos
                            tabla.removeAllViews();

                            // Iterar sobre los resultados y mostrarlos en la tabla
                            for (int i = 0; i < resultados.length(); i++) {
                                // Obtener el objeto de resultado en la posición actual
                                JSONObject resultado = resultados.getJSONObject(i);

                                // Obtener los valores de las columnas
                                String docentes = resultado.getString("docentes");
                                String aula = resultado.getString("aula");
                                String hora = resultado.getString("hora");
                                String opcion = resultado.getString("opcion");
                                String fecha = resultado.getString("fecha");

                                // Crear una nueva fila en la tabla
                                TableRow fila = new TableRow(Reporte.this);

                                // Crear los TextViews para mostrar los datos en la fila
                                TextView txtdocentes = new TextView(Reporte.this);
                                txtdocentes.setText(docentes);

                                TextView txtaula = new TextView(Reporte.this);
                                txtaula.setText(aula);

                                TextView txthora = new TextView(Reporte.this);
                                txthora.setText(hora);

                                TextView txtopcion = new TextView(Reporte.this);
                                txtopcion.setText(opcion);

                                TextView txtfecha = new TextView(Reporte.this);
                                txtfecha.setText(fecha);

                                // Agregar los TextViews a la fila
                                fila.addView(txtdocentes);
                                fila.addView(txtaula);
                                fila.addView(txthora);
                                fila.addView(txtopcion);
                                fila.addView(txtfecha);

                                // Agregar la fila a la tabla
                                tabla.addView(fila);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Agregar la solicitud a la cola
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);

        // Botón de filtro
        btnbusquedafiltrada = findViewById(R.id.BtnBusqueda_filtrada);

        btnbusquedafiltrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha = txtfecha.getText().toString();
                String horas = sphora.getSelectedItem().toString();
                String opciones = spopcion.getSelectedItem().toString();
                llenaTablaConAPIFiltrada(fecha, horas, opciones);
            }
        });

        //
        btnbusquedafiltradaporfecha =findViewById(R.id.btnFiltradofecha);
        btnbusquedafiltradaporfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha = txtfecha.getText().toString();
                llenaTablaConAPIFiltradaporfehca(fecha);
            }
        });

    }
    public String getfecha(){

            String dia = "";
            if (dpfecha != null) {
                dia = String.format("%02d", dpfecha.getDayOfMonth()-1);
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

    public void muestraCalendario(View View){
        dpfecha.setVisibility(View.VISIBLE);
    }

    private void llenaTablaConAPIFiltrada(String fecha, String hora, String opcion) {
        String url = "http://192.168.56.1:80/checador/reportefiltrado.php?fecha=" + fecha + "&hora=" + hora + "&opcion=" + opcion; // Reemplaza con la URL de tu API

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Limpiar la tabla antes de agregar los nuevos datos
                        tabla.removeAllViews();

                        try {
                            // Crear la fila de encabezados
                            TableRow headerRow = new TableRow(Reporte.this);
                            String[] headers = {"docentes", "aula", "hora","opcion","fecha"};
                            for (String header : headers) {
                                TextView textView = new TextView(Reporte.this);
                                textView.setText(header);
                                textView.setPadding(5, 5, 5, 5);
                                textView.setBackgroundResource(R.color.purple_200);
                                textView.setTypeface(null, Typeface.BOLD);
                                headerRow.addView(textView);
                            }
                            tabla.addView(headerRow);

                            // Agregar las filas con los datos filtrados
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject rowData = response.getJSONObject(i);
                                TableRow dataRow = new TableRow(Reporte.this);

                                // Obtener los valores de las columnas de la respuesta JSON
                                String docentes = rowData.getString("docentes");
                                String aula = rowData.getString("aula");
                                String hora = rowData.getString("hora");
                                String opcion = rowData.getString("opcion");
                                String fecha = rowData.getString("fecha");
                                // Crear los TextViews para mostrar los datos en la fila
                                TextView txtdocentes = new TextView(Reporte.this);
                                txtdocentes.setText(docentes);

                                TextView txtaula = new TextView(Reporte.this);
                                txtaula.setText(aula);

                                TextView txthora = new TextView(Reporte.this);
                                txthora.setText(hora);

                                TextView txtopcion = new TextView(Reporte.this);
                                txtopcion.setText(opcion);

                                TextView txtfecha = new TextView(Reporte.this);
                                txtfecha.setText(fecha);

                                // Agregar los TextViews a la fila
                                dataRow.addView(txtdocentes);
                                dataRow.addView(txtaula);
                                dataRow.addView(txthora);
                                dataRow.addView(txtopcion);
                                dataRow.addView(txtfecha);

                                // Agregar la fila a la tabla
                                tabla.addView(dataRow);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Agregar la solicitud a la cola
        queue.add(jsonArrayRequest);
    }

    private void llenaTablaConAPIFiltradaporfehca(String fecha) {
        String url = "http://192.168.56.1/checador/busquedafiltadofecha.php?fecha=" + fecha ; // Reemplaza con la URL de tu API

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Limpiar la tabla antes de agregar los nuevos datos
                        tabla.removeAllViews();

                        try {
                            // Crear la fila de encabezados
                            TableRow headerRow = new TableRow(Reporte.this);
                            String[] headers = {"docentes", "aula", "hora","opcion","fecha"};
                            for (String header : headers) {
                                TextView textView = new TextView(Reporte.this);
                                textView.setText(header);
                                textView.setPadding(5, 5, 5, 5);
                                textView.setBackgroundResource(R.color.purple_200);
                                textView.setTypeface(null, Typeface.BOLD);
                                headerRow.addView(textView);
                            }
                            tabla.addView(headerRow);

                            // Agregar las filas con los datos filtrados
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject rowData = response.getJSONObject(i);
                                TableRow dataRow = new TableRow(Reporte.this);

                                // Obtener los valores de las columnas de la respuesta JSON
                                String docentes = rowData.getString("docentes");
                                String aula = rowData.getString("aula");
                                String hora = rowData.getString("hora");
                                String opcion = rowData.getString("opcion");
                                String fecha = rowData.getString("fecha");
                                // Crear los TextViews para mostrar los datos en la fila
                                TextView txtdocentes = new TextView(Reporte.this);
                                txtdocentes.setText(docentes);

                                TextView txtaula = new TextView(Reporte.this);
                                txtaula.setText(aula);

                                TextView txthora = new TextView(Reporte.this);
                                txthora.setText(hora);

                                TextView txtopcion = new TextView(Reporte.this);
                                txtopcion.setText(opcion);

                                TextView txtfecha = new TextView(Reporte.this);
                                txtfecha.setText(fecha);

                                // Agregar los TextViews a la fila
                                dataRow.addView(txtdocentes);
                                dataRow.addView(txtaula);
                                dataRow.addView(txthora);
                                dataRow.addView(txtopcion);
                                dataRow.addView(txtfecha);

                                // Agregar la fila a la tabla
                                tabla.addView(dataRow);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Agregar la solicitud a la cola
        queue.add(jsonArrayRequest);
    }


}