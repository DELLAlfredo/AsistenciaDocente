package com.example.asistenciadocente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class menuadministradores extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;



    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_menuadministradores,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activitycontainer);
        container.addView(view);

        super.setContentView(drawerLayout);
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView =  drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId()== R.id.nav_EditarClases){
            Intent intent = new Intent(getApplicationContext(), EditarInforme.class);
            startActivity(intent);
        }else if (item.getItemId()== R.id.nav_ReportesAsistencias){
            Intent intent = new Intent(getApplicationContext(), ReportesAsistencias.class);
            startActivity(intent);
        }else if (item.getItemId()== R.id.nav_ReporteAula){
            Intent intent = new Intent(getApplicationContext(), InformeAula.class);
            startActivity(intent);
        }else if (item.getItemId()== R.id.nav_ReporteDocente){
            Intent intent = new Intent(getApplicationContext(), InformeDocentes.class);
            startActivity(intent);
        }else if (item.getItemId()== R.id.nav_ReporteFechasemana){
            Intent intent = new Intent(getApplicationContext(), InformeSemanal.class);
            startActivity(intent);
        }else if (item.getItemId()== R.id.nav_ReporteSemestral){
            Intent intent = new Intent(getApplicationContext(), InformeSemestral.class);
            startActivity(intent);
        }else if (item.getItemId()== R.id.logout) {
            SharedPreferences preferences =getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
    protected  void  allocateActivityTitle(String titleString){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }

}