package com.example.asistenciadocente;

import android.os.Bundle;

import com.example.asistenciadocente.databinding.ActivityInformeSemestralBinding;

public class InformeSemestral extends menuadministradores {
    ActivityInformeSemestralBinding activityInformeSemestralBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_semestral);
        activityInformeSemestralBinding = ActivityInformeSemestralBinding.inflate(getLayoutInflater());
        setContentView(activityInformeSemestralBinding.getRoot());
        allocateActivityTitle("Informe Semestral");
    }
}