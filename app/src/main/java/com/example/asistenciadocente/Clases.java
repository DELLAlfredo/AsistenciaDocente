package com.example.asistenciadocente;

import android.os.Bundle;

import com.example.asistenciadocente.databinding.ActivityClasesBinding;

public class Clases extends menu {
    ActivityClasesBinding activityClasesBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityClasesBinding= ActivityClasesBinding.inflate(getLayoutInflater());
        setContentView(activityClasesBinding.getRoot());
    }
}