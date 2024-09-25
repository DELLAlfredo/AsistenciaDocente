package com.example.asistenciadocente;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReportesAsistencias extends menuadministradores {
    private BarChart barChart;
    private PieChart pieChart;
    private String startDate = "";
    private String endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_asistencias);

        barChart = findViewById(R.id.barDataSet);
        pieChart = findViewById(R.id.pieChart);

        // Configurar cliente HTTP personalizado que ignore la verificación del hostname
        try {
            setupHttpsTrustManager();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Llamar al método para obtener los datos y llenar las gráficas
        fetchChartData();
        // Configurar los selectores de fecha
        findViewById(R.id.selectStartDateButton).setOnClickListener(v -> showDatePicker(true));
        findViewById(R.id.selectEndDateButton).setOnClickListener(v -> showDatePicker(false));

        // Llamar al método para obtener datos cuando se seleccione el rango de fechas
        findViewById(R.id.fetchDataButton).setOnClickListener(v -> fetchChartData());
    }

    // Configurar un cliente HTTPS que ignore la verificación del hostname
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
                        // Actualizar UI para mostrar la fecha seleccionada
                        ((TextView) findViewById(R.id.startDateTextView)).setText(dateString);
                    } else {
                        endDate = dateString;
                        // Actualizar UI para mostrar la fecha seleccionada
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

        String url = "https://201.164.155.166/api_checador/opciones?fecha_inicio=" + startDate + "&fecha_fin=" + endDate;

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

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);

                    String opcion = jsonObject.getString("opcion");
                    int cantidad = jsonObject.getInt("cantidad");

                    barEntries.add(new BarEntry(i, cantidad));
                    barLabels.add(opcion);

                    pieEntries.add(new PieEntry(cantidad, opcion));
                } catch (JSONException e) {
                    Log.e("API", "JSON Parsing error: " + e.getMessage());
                }
            }

            // Configurar gráfico de barras
            if (!barEntries.isEmpty()) {
                BarDataSet barDataSet = new BarDataSet(barEntries, "Cantidad por Opción");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
               // barDataSet.setBarSpacePercent(30f); // Ajusta el espacio entre las barras
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);

                barChart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        // Muestra solo las etiquetas que caben en el gráfico
                        int index = (int) value;
                        return index < barLabels.size() ? barLabels.get(index) : "";
                    }
                });

                barChart.setTouchEnabled(true);
                barChart.setDrawBorders(true);
                barChart.setDrawValueAboveBar(true);
                barChart.setHighlightFullBarEnabled(true);
                barChart.getXAxis().setSpaceMax(0.1f);
                barChart.getXAxis().setGranularity(1f);
                barChart.getXAxis().setGranularityEnabled(true);
               // barChart.getXAxis().setLabelCount(4, true); // Mostrar solo 5 etiquetas en el eje X
                barChart.setExtraLeftOffset(10f); // Agrega espacio en la parte izquierda
                barChart.setExtraRightOffset(10f); // Agrega espacio en la parte derecha

                barChart.getXAxis().setLabelRotationAngle(-45f); // Ángulo de rotación
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                barChart.invalidate(); // Refrescar el gráfico
            }

            // Configurar gráfico de pastel
            if (!pieEntries.isEmpty()) {
                PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);

                // Ocultar las etiquetas sobre las porciones del pastel
                pieChart.setDrawEntryLabels(false);

                pieChart.setDrawEntryLabels(true);
                pieChart.setEntryLabelTextSize(8f); //tamaño de letra de eventos dentro de grafica
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.getLegend().setEnabled(false); // con sus colores
                pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                pieChart.getLegend().setTextSize(8f); //tamaño de letra de eventos bajo grafica
                pieChart.getLegend().setTextColor(Color.BLACK);

                pieChart.invalidate(); //refresca la grafica
            }

        } catch (Exception e) {
            Log.e("API", "Error procesando la respuesta: " + e.getMessage());
        }
    }
}
