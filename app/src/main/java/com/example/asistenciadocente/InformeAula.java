package com.example.asistenciadocente;

import android.os.Bundle;

import com.example.asistenciadocente.databinding.ActivityInformeAulaBinding;

public class InformeAula extends menuadministradores {

    ActivityInformeAulaBinding activityInformeGeneralBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_aula);
        activityInformeGeneralBinding =ActivityInformeAulaBinding.inflate(getLayoutInflater());
        setContentView(activityInformeGeneralBinding.getRoot());
        allocateActivityTitle("Informe Aula");
    }
}