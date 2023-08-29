package com.example.asistenciadocente;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistenciadocente.databinding.ActivityReporteBinding;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Reporte extends menu {
    ActivityReporteBinding activityReporteBinding;
    Spinner sphora,spopcion;
    EditText txtfecha;

    ImageButton Btncalendario;
    DatePicker dpfecha;
    TableLayout tabla;
    Button btnbusquedafiltrada,btnbusquedafiltradaporfecha,btnFechaRegistro,btnPDF;


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
       // String url = "http://192.168.56.1:80/checador/reporteselect.php"; // Reemplaza con la URL de tu API
        String url = "https://checador.tech/api_checador/obtener-clases";
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

        btnFechaRegistro=findViewById(R.id.btnfecharegistro);

        btnFechaRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha = txtfecha.getText().toString();
                String opciones = spopcion.getSelectedItem().toString();
                llenaTablaConAPIFechaRegistro(fecha, opciones);
            }
        });

        btnPDF=findViewById(R.id.btnPDF);
        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPDF();
            }
        });


    }
    private void generarPDF() {
        String nombreBase = "Asistencia Docente "; // para dar el nombre al documento
        String fechaActual = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());// para sacar la fecha actual
        String nombreArchivo = nombreBase + fechaActual + ".pdf"; // une ambos balores del nombre y fecha y jenera el documento en PDF
        File directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); // se utiliza para obtener el directorio de descargas del dispositivo.
        String filePath = directorioDescargas.getAbsolutePath() + File.separator + nombreArchivo;         // se encarga de construir la ruta completa del archivo PDF que se generará.

        Document document = new Document();


        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Agregar un título al documento
            String titulo = "Tabla de Asistencia "; // Texto del título del documento
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.UNDERLINE); // Fuente y estilo del título
            Paragraph paragraphTitulo = new Paragraph(titulo, fontTitulo); // Crea un párrafo con el título y la fuente
            paragraphTitulo.setAlignment(Element.ALIGN_CENTER); // Alinea el párrafo al centro
            paragraphTitulo.setSpacingAfter(20); // Establece el espacio después del párrafo
            document.add(paragraphTitulo); // Agrega el párrafo al documento


            // Obtén los datos de la tabla
            List<String> datosTabla = obtenerDatosTabla();
            // Crear una tabla y definir el número de columnas
            PdfPTable table = new PdfPTable(5);

            // Crear celdas de encabezado de tabla
            PdfPCell columna1 = new PdfPCell(new Paragraph("Nombre de Docente", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell columna2 = new PdfPCell(new Paragraph("Aula", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell columna3 = new PdfPCell(new Paragraph("Hora", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell columna4 = new PdfPCell(new Paragraph("Acción", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            PdfPCell columna5 = new PdfPCell(new Paragraph("Fecha", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            // Agregar celdas de encabezado a la tabla
            table.addCell(columna1);
            table.addCell(columna2);
            table.addCell(columna3);
            table.addCell(columna4);
            table.addCell(columna5);

            // Agregar los datos a la tabla
            for (String dato : datosTabla) {
                String[] campos = dato.split("\\|"); // Separar los campos por el carácter '|'

                // Agregar una celda para cada campo en la fila
                for (String campo : campos) {
                    PdfPCell celda = new PdfPCell(new Paragraph(campo.trim())); // Trim para eliminar espacios en blanco
                    table.addCell(celda);
                }
            }

            // Agregar la tabla al documento
            document.add(table);

            document.close();

            // Mostrar mensaje de éxito
            Toast.makeText(this, "PDF generado y guardado en: " + filePath, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            // Mostrar mensaje de error
            mostrarMensaje("Error al generar el PDF");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private List<String> obtenerDatosTabla() {
        List<String> datosTabla = new ArrayList<>();

        // Obtén los datos de la tabla de la interfaz de usuario o de la base de datos
        // Aquí deberías reemplazar esto con tu propia lógica para obtener los datos

        // Iterar sobre las filas de la tabla
        for (int i = 1; i < tabla.getChildCount(); i++) {
            TableRow fila = (TableRow) tabla.getChildAt(i);

            // Obtener los TextViews de cada columna de la fila
            TextView txtDocente = (TextView) fila.getChildAt(0);
            TextView txtAula = (TextView) fila.getChildAt(1);
            TextView txtHora = (TextView) fila.getChildAt(2);
            TextView txtAccion = (TextView) fila.getChildAt(3);
            TextView txtFecha = (TextView) fila.getChildAt(4);

            String docente = txtDocente.getText().toString();
            String aula = txtAula.getText().toString();
            String hora = txtHora.getText().toString();
            String accion = txtAccion.getText().toString();
            String fecha = txtFecha.getText().toString();

            // Concatenar los datos y agregarlos a la lista
            String datoTabla = docente + " | " + aula + " | " + hora + " | " + accion + " | " + fecha;
            datosTabla.add(datoTabla);
        }

        return datosTabla;
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
        String url = "https://checador.tech/api_checador/filtrar-reporte?fecha=" + fecha + "&hora=" + hora + "&opcion=" + opcion; // Reemplaza con la URL de tu API
        // String url = "http://192.168.56.1:80/checador/reportefiltrado.php?fecha=" + fecha + "&hora=" + hora + "&opcion=" + opcion;

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
                            String[] headers = {"Nombre de Docente", "Aula", "Hora","Accion","Fecha"};
                            for (String header : headers) {
                                TextView textView = new TextView(Reporte.this);
                                textView.setText(header);
                                textView.setPadding(5, 5, 5, 5);
                                textView.setBackgroundResource(R.color.tabla);
                                textView.setTypeface(null, Typeface.BOLD);
                                headerRow.addView(textView);
                            }
                            tabla.addView(headerRow);
                            if (response.length()!=0){
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
                                    int color = 0;
                                    if (opcion.equals("IMPARTIDA")) {
                                        color = getResources().getColor(R.color.colorImpartida);
                                    } else if (opcion.equals("NO IMPARTIDA")){
                                        color = getResources().getColor(R.color.colorNoImpartida);
                                    } else if (opcion.equals("CLASE INCOMPLETA")) {
                                        color = getResources().getColor(R.color.colorRetardo);
                                    } else if (opcion.equals("SUSPENCION")) {
                                        color = getResources().getColor(R.color.colorsuspencion);
                                    }
                                    int colortabla = 0;

                                    colortabla =getResources().getColor(R.color.white);
                                    dataRow.setBackgroundColor(colortabla);
                                    txtopcion.setBackgroundColor(color);

                                    // Agregar los TextViews a la fila
                                    dataRow.addView(txtdocentes);
                                    dataRow.addView(txtaula);
                                    dataRow.addView(txthora);
                                    dataRow.addView(txtopcion);
                                    dataRow.addView(txtfecha);

                                    txtdocentes.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                    txtaula.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                    txthora.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                    txtopcion.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                    txtfecha.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                    // Agregar la fila a la tabla
                                    tabla.addView(dataRow);

                                }
                            }else {
                                Toast.makeText(Reporte.this, "SIN DATOS EN ESTA BUSQUEDA", Toast.LENGTH_SHORT).show();
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
        String url = "https://checador.tech/api_checador/filtrar-clases-fecha?fecha=" + fecha; // Reemplaza con la URL de tu API
        //String url = "http://192.168.56.1:80/checador/busquedafiltadofecha.php?fecha=" + fecha;

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
                            String[] headers = {"Nombre de Docente", "Aula", "Hora", "Acción", "Fecha"};
                            for (String header : headers) {
                                TextView textView = new TextView(Reporte.this);
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
                                Toast.makeText(Reporte.this, "No se encontraron registros para la fecha especificada", Toast.LENGTH_SHORT).show();
                                return;
                            }

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

                                // Obtener el color de fondo según la opción
                                int color = 0;
                                if (opcion.equals("IMPARTIDA")) {
                                    color = getResources().getColor(R.color.colorImpartida);
                                } else if (opcion.equals("NO IMPARTIDA")) {
                                    color = getResources().getColor(R.color.colorNoImpartida);
                                } else if (opcion.equals("CLASE INCOMPLETA")) {
                                    color = getResources().getColor(R.color.colorRetardo);
                                } else if (opcion.equals("SUSPENCION")) {
                                    color = getResources().getColor(R.color.colorsuspencion);
                                }
                                // Establecer el color de fondo de la fila

                                int colortabla = 0;

                                colortabla =getResources().getColor(R.color.white);
                                dataRow.setBackgroundColor(colortabla);
                                txtopcion.setBackgroundColor(color);

                                // Agregar los TextViews a la fila
                                dataRow.addView(txtdocentes);
                                dataRow.addView(txtaula);
                                dataRow.addView(txthora);
                                dataRow.addView(txtopcion);
                                dataRow.addView(txtfecha);



                                // Establecer color de texto y estilo
                                txtdocentes.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtaula.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txthora.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtopcion.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtfecha.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));

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

    private void llenaTablaConAPIFechaRegistro(String fecha, String opcion) {
        String url = "https://checador.tech/api_checador/filtrar-fecha?fecha=" + fecha  + "&opcion=" + opcion; // Reemplaza con la URL de tu API

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
                            String[] headers = {"Nombre de Docente", "Aula", "Hora","Accion","Fecha"};
                            for (String header : headers) {
                                TextView textView = new TextView(Reporte.this);
                                textView.setText(header);
                                textView.setPadding(5, 5, 5, 5);
                                textView.setBackgroundResource(R.color.tabla);
                                textView.setTypeface(null, Typeface.BOLD);
                                headerRow.addView(textView);
                            }
                            tabla.addView(headerRow);
                            if (response.length() == 0) {
                                // Mostrar un mensaje indicando que no hay registros disponibles
                                Toast.makeText(Reporte.this, "No se encontraron registros para la fecha y registro", Toast.LENGTH_SHORT).show();
                                return;
                            }
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
                                int color = 0;
                                if (opcion.equals("IMPARTIDA")) {
                                    color = getResources().getColor(R.color.colorImpartida);
                                } else if (opcion.equals("NO IMPARTIDA")){
                                    color = getResources().getColor(R.color.colorNoImpartida);
                                } else if (opcion.equals("CLASE INCOMPLETA")) {
                                    color = getResources().getColor(R.color.colorRetardo);
                                } else if (opcion.equals("SUSPENCION")) {
                                    color = getResources().getColor(R.color.colorsuspencion);
                                }
                                // Establecer el color de fondo de la fila

                                int colortabla = 0;

                                colortabla =getResources().getColor(R.color.white);
                                dataRow.setBackgroundColor(colortabla);
                                txtopcion.setBackgroundColor(color);


                                // Agregar los TextViews a la fila
                                dataRow.addView(txtdocentes);
                                dataRow.addView(txtaula);
                                dataRow.addView(txthora);
                                dataRow.addView(txtopcion);
                                dataRow.addView(txtfecha);
                                
                                txtdocentes.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtaula.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txthora.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtopcion.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
                                txtfecha.setTextColor(getResources().getColor(com.kusu.loadingbutton.R.color.Black));
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