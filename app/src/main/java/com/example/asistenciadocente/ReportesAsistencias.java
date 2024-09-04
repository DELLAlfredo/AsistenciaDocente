package com.example.asistenciadocente;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.asistenciadocente.databinding.ActivityReportesAsistenciasBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ReportesAsistencias extends menuadministradores {
    ActivityReportesAsistenciasBinding reportesasistencias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_asistencias);
        reportesasistencias = ActivityReportesAsistenciasBinding.inflate(getLayoutInflater());
        setContentView(reportesasistencias.getRoot());
        allocateActivityTitle("Reporte Asistencias");

        BarChart barChart = findViewById(R.id.barDataSet);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 10f));
        entries.add(new BarEntry(2f, 20f));
        entries.add(new BarEntry(3f, 30f));

        BarDataSet dataSet = new BarDataSet(entries, "Label");
        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        barChart.invalidate(); // refrescar el gráfico

        // Gráfico de Pastel
        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(10f, "Jan"));
        pieEntries.add(new PieEntry(20f, "Feb"));
        pieEntries.add(new PieEntry(30f, "Mar"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Datos de Pastel");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.invalidate(); // Refrescar el gráfico
    }

}