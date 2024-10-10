package com.example.asistenciadocente;

import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistenciadocente.databinding.ActivityInformeAulaBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class InformeAula extends menuadministradores {

    ActivityInformeAulaBinding activityInformeAulaBinding;
    private BarChart barChart;
    private PieChart pieChart;
    private String startDate = "";
    private String endDate = "";
    private Spinner spAcademia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_aula);
        activityInformeAulaBinding = ActivityInformeAulaBinding.inflate(getLayoutInflater());
        setContentView(activityInformeAulaBinding.getRoot());
        allocateActivityTitle("Informe Carrera");

        barChart = findViewById(R.id.barDataSet);
        pieChart = findViewById(R.id.pieChart);
        try {
            setupHttpsTrustManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Configurar el Spinner de carreras
        spAcademia = findViewById(R.id.spAcademia);
        String[] crud = {"ISC", "IIAS", "II", "IGE", "IIAL"};
        ArrayAdapter<String> adapterCrud = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, crud);
        adapterCrud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAcademia.setAdapter(adapterCrud);

        // Configurar los selectores de fecha
        findViewById(R.id.selectStartDateButton).setOnClickListener(v -> showDatePicker(true));
        findViewById(R.id.selectEndDateButton).setOnClickListener(v -> showDatePicker(false));

        // Botón para obtener datos basados en las fechas y la carrera seleccionada
        findViewById(R.id.fetchDataButton).setOnClickListener(v -> fetchChartData());

        // Botón para generar PDF
        findViewById(R.id.generatePdfButton).setOnClickListener(v -> createPdf());
    }
    private void setupHttpsTrustManager() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }
                }
        };

        // Ignorar la verificación del hostname
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;

        // Crear un contexto SSL que acepte todos los certificados
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }
    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, month1, dayOfMonth);
                    String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());

                    if (isStartDate) {
                        startDate = dateString;
                        ((TextView) findViewById(R.id.startDateTextView)).setText(dateString);
                    } else {
                        endDate = dateString;
                        ((TextView) findViewById(R.id.endDateTextView)).setText(dateString);
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void fetchChartData() {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            Log.e("API", "Fecha de inicio o fin no seleccionadas.");
            return;
        }

        // Obtener la carrera seleccionada en el Spinner
        String academia = spAcademia.getSelectedItem().toString();
        String url = "https://201.164.155.166/api_checador/opcionescarrera?fecha_inicio=" + startDate + "&fecha_fin=" + endDate + "&academia=" + academia;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        processApiResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API", "Error en la solicitud API: " + error.getMessage());
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }

    private void processApiResponse(JSONArray response) {
        try {
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            final ArrayList<String> barLabels = new ArrayList<>();
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();

            // Crear un Map con los colores específicos para cada opción
            Map<String, Integer> colorMap = new HashMap<>();
            colorMap.put("DIA NO LABORAL", ContextCompat.getColor(this, R.color.colordianolaboral));
            colorMap.put("CLASE IMPARTIDA", ContextCompat.getColor(this, R.color.colorImpartida));
            colorMap.put("CLASE NO IMPARTIDA", ContextCompat.getColor(this, R.color.colorNoImpartida));
            colorMap.put("GRUPO SIN ALUMNOS", ContextCompat.getColor(this, R.color.colorSinalumnos));
            colorMap.put("JUSTIFICACION ACADEMICA", ContextCompat.getColor(this, R.color.colorJustificacionAcademica));
            colorMap.put("PERMISO", ContextCompat.getColor(this, R.color.colorpermiso));
            colorMap.put("PROBLEMAS DE SALUD", ContextCompat.getColor(this, R.color.colorProblemasSalud));
            colorMap.put("COMISION", ContextCompat.getColor(this, R.color.colorComision));
            colorMap.put("FALLA TECNICA DE AULA", ContextCompat.getColor(this, R.color.colorFalllaTecnica));
            colorMap.put("CLASE INCOMPLETA", ContextCompat.getColor(this, R.color.colorClaseIncompleta));

            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                String opcion = jsonObject.getString("opcion");
                int cantidad = jsonObject.getInt("cantidad");

                // Añadir entradas a las gráficas
                barEntries.add(new BarEntry(i, cantidad));
                barLabels.add(opcion);
                pieEntries.add(new PieEntry(cantidad, opcion));

                if (colorMap.containsKey(opcion)) {
                    colors.add(colorMap.get(opcion));
                } else {
                    colors.add(ContextCompat.getColor(this, R.color.white));
                }
            }

            // Configurar gráfico de barras
            BarDataSet barDataSet = new BarDataSet(barEntries, "Cantidad por Opción");
            barDataSet.setColors(colors);
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.setTouchEnabled(true);
            barChart.setDrawBorders(true);
            barChart.setDrawValueAboveBar(true);
            barChart.setHighlightFullBarEnabled(true);
            barChart.getXAxis().setSpaceMax(0.1f);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setGranularityEnabled(true);
            barChart.setExtraLeftOffset(10f);
            barChart.setExtraRightOffset(10f);
            barChart.getXAxis().setLabelRotationAngle(-45f);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
            barDataSet.setValueTextSize(15f); //tamaño de los numeros
            barChart.invalidate(); // Refresca la gráfica
            barChart.getXAxis().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int index = (int) value;
                    return index < barLabels.size() ? barLabels.get(index) : "";
                }
            });

            barChart.invalidate();

            // Configurar gráfico de pastel
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setColors(colors);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(15f); //tamaño de los numeros
        } catch (JSONException e) {
            Log.e("API", "Error procesando la respuesta: " + e.getMessage());
        }
    }

    private void createPdf() {
        int pageWidth = 1000;
        int pageHeight = 1900;

        PdfDocument pdfDocument = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        barChart.draw(canvas);
        canvas.translate(0, barChart.getHeight() + 50);

        pieChart.draw(canvas);

        pdfDocument.finishPage(page);

        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Reportes/";
        File file = new File(directoryPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        File pdfFile = new File(directoryPath, "informe_aula.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(this, "PDF generado exitosamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("PDF", "Error al escribir el PDF: " + e.getMessage());
            Toast.makeText(this, "Error al generar el PDF", Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }
}
