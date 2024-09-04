package com.example.asistenciadocente;

import android.os.Bundle;

import com.example.asistenciadocente.databinding.ActivityInformeDocentesBinding;

public class InformeDocentes extends menuadministradores {

    ActivityInformeDocentesBinding activityInformeDocentesBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_docentes);
        activityInformeDocentesBinding = ActivityInformeDocentesBinding.inflate(getLayoutInflater());
        setContentView(activityInformeDocentesBinding.getRoot());
        allocateActivityTitle("Informe Docentes");

    }
}