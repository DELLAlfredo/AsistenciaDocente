package com.example.asistenciadocente;

import android.os.Bundle;

import com.example.asistenciadocente.databinding.ActivityReportesAsistenciasBinding;

public class ReportesAsistencias extends menuadministradores {
    ActivityReportesAsistenciasBinding reportesasistencias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_asistencias);
        reportesasistencias = ActivityReportesAsistenciasBinding.inflate(getLayoutInflater());
        setContentView(reportesasistencias.getRoot());
        allocateActivityTitle("Reporte Asistencias");
    }
}