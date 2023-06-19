package com.example.asistenciadocente;

import android.os.Bundle;

import com.example.asistenciadocente.databinding.ActivityAbcAulaBinding;

public class abcAula extends menu {
    ActivityAbcAulaBinding activityAbcAulaBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAbcAulaBinding = ActivityAbcAulaBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Aulas");
        setContentView(activityAbcAulaBinding.getRoot());

        /*--------------------------------------------------------*/


    }

}