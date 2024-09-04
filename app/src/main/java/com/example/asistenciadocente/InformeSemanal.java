package com.example.asistenciadocente;

import android.os.Bundle;

import com.example.asistenciadocente.databinding.ActivityInformeSemanalBinding;

public class InformeSemanal extends menuadministradores {
ActivityInformeSemanalBinding   activityInformeSemanalBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_semanal);
        activityInformeSemanalBinding = ActivityInformeSemanalBinding.inflate(getLayoutInflater());
        setContentView(activityInformeSemanalBinding.getRoot());
        allocateActivityTitle("Informe Semanal");
    }
}