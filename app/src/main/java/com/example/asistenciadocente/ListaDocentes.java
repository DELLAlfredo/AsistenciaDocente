package com.example.asistenciadocente;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

public class ListaDocentes extends AppCompatActivity {
    TableLayout tabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_docentes);

        tabla = findViewById(R.id.tabla);

        llenaTablaDocentes();
    }


    private void llenaTablaDocentes() {
        //String url = "http://201.164.155.166/api_checador/filtrar-clases-fecha?fecha=" + fecha; // Reemplaza con la URL de tu API
        String url = "http://192.168.56.1:80/api_checador/obtener-Docentes";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Limpiar la tabla antes de agregar los nuevos datos
                        tabla.removeAllViews();

                        try {
                            // Crear la fila de encabezados
                            TableRow headerRow = new TableRow(ListaDocentes.this);
                            String[] headers = {"nombre", "apellidos", "academia", "Matricula"};
                            for (String header : headers) {
                                TextView textView = new TextView(ListaDocentes.this);
                                textView.setText(header);
                                textView.setPadding(5, 5, 5, 5);
                                textView.setBackgroundResource(R.color.tabla);
                                textView.setTypeface(null, Typeface.BOLD);
                                headerRow.addView(textView);
                            }
                            tabla.addView(headerRow);

                            // Verificar si el array de respuesta está vacío
                            if (response.length() == 0) {
                                // Mostrar un mensaje indicando que no hay registros disponibles
                                Toast.makeText(ListaDocentes.this, "No se encontraron registros para la fecha especificada", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Agregar las filas con los datos filtrados
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject rowData = response.getJSONObject(i);
                                TableRow dataRow = new TableRow(ListaDocentes.this);

                                // Obtener los valores de las columnas de la respuesta JSON
                                String nombre = rowData.getString("nombre");
                                String apellidos = rowData.getString("apellidos");
                                String academia = rowData.getString("academia");
                                String Matricula = rowData.getString("Matricula");

                                // Crear los TextViews para mostrar los datos en la fila
                                TextView txtnombre = new TextView(ListaDocentes.this);
                                txtnombre.setText(nombre);

                                TextView txtapellidos = new TextView(ListaDocentes.this);
                                txtapellidos.setText(apellidos);

                                TextView txtacademia = new TextView(ListaDocentes.this);
                                txtacademia.setText(academia);

                                TextView txtMatricula = new TextView(ListaDocentes.this);
                                txtMatricula.setText(Matricula);
                                //color tabla
                                int colortabla = 0;

                                colortabla =getResources().getColor(R.color.white);
                                dataRow.setBackgroundColor(colortabla);

                                // Agregar los TextViews a la fila
                                dataRow.addView(txtnombre);
                                dataRow.addView(txtapellidos);
                                dataRow.addView(txtacademia);
                                dataRow.addView(txtMatricula);

                                // Establecer color de texto y estilo
                                txtnombre.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtapellidos.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtacademia.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtMatricula.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));

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