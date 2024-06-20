package com.example.asistenciadocente;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListaAula extends AppCompatActivity {
    TableLayout tabla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aula);
        tabla = findViewById(R.id.tabla);
        llenaTablaDocentes();
    }

    private void llenaTablaDocentes() {
        //String url = "http://201.164.155.166/api_checador/filtrar-clases-fecha?fecha=" + fecha; // Reemplaza con la URL de tu API
        String url = "http://201.164.155.166/api_checador/obtener-AULAS";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Limpiar la tabla antes de agregar los nuevos datos
                        tabla.removeAllViews();

                        try {
                            // Crear la fila de encabezados
                            TableRow headerRow = new TableRow(ListaAula.this);
                            String[] headers = {"ID", "Nombre del Aula"};
                            for (String header : headers) {
                                TextView textView = new TextView(ListaAula.this);
                                textView.setText(header);
                                textView.setPadding(5, 5, 5, 5);
                                textView.setBackgroundResource(R.color.tabla);
                                textView.setTypeface(null, Typeface.BOLD);
                                headerRow.addView(textView);
                            }
                            tabla.addView(headerRow);



                            // Agregar las filas con los datos filtrados
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject rowData = response.getJSONObject(i);
                                TableRow dataRow = new TableRow(ListaAula.this);

                                // Obtener los valores de las columnas de la respuesta JSON
                                String idAula = rowData.getString("idAula");
                                String nombre_aula = rowData.getString("nombre_aula");

                                // Crear los TextViews para mostrar los datos en la fila
                                TextView txtid = new TextView(ListaAula.this);
                                txtid.setText(idAula);

                                TextView txtnombreaula = new TextView(ListaAula.this);
                                txtnombreaula.setText(nombre_aula);


                                //color tabla
                                int colortabla = 0;

                                colortabla =getResources().getColor(R.color.white);
                                dataRow.setBackgroundColor(colortabla);

                                // Agregar los TextViews a la fila
                                dataRow.addView(txtid);
                                dataRow.addView(txtnombreaula);

                                // Establecer color de texto y estilo
                                txtid.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtnombreaula.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));


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