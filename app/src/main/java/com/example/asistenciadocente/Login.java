package com.example.asistenciadocente;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText email, contraseña;
    Button ingresar;
    String Usuario = "Admin", Paswword = "@admin", Usuario2 = "Admin2", Paswword2 = "@admin2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        contraseña = findViewById(R.id.contraseña);
        ingresar = findViewById(R.id.loginButton);

        recuperarPreferencias();  // Recuperamos preferencias al iniciar

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String frase = email.getText().toString();
                String pass = contraseña.getText().toString();

                if (frase.equalsIgnoreCase(Usuario) && pass.equalsIgnoreCase(Paswword)) {
                    Toast.makeText(getApplicationContext(), "Ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Clases.class);
                    startActivity(intent);
                    guardarPreferencias(Usuario);  // Guardar preferencias del primer usuario
                } else if (frase.equalsIgnoreCase(Usuario2) && pass.equalsIgnoreCase(Paswword2)) {
                    Toast.makeText(getApplicationContext(), "Ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, EditarInforme.class);
                    startActivity(intent);
                    guardarPreferencias(Usuario2);  // Guardar preferencias del segundo usuario
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos, por favor verifiquelos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Guardar las preferencias del usuario seleccionado
    private void guardarPreferencias(String usuario) {
        SharedPreferences preferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario", usuario);  // Guardamos el identificador del usuario
        editor.putBoolean("sesion", true);
        editor.commit();
    }

    // Recuperar las preferencias al iniciar la aplicación y mostrar los datos del usuario correcto
    private void recuperarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usuario", "");

        if (usuario.equalsIgnoreCase(Usuario)) {
            email.setText(Usuario);
            contraseña.setText(Paswword);
        } else if (usuario.equalsIgnoreCase(Usuario2)) {
            email.setText(Usuario2);
            contraseña.setText(Paswword2);
        }
    }

    // Método para mover a la actividad de registro
    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(), RegistroUsuario.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir");
        builder.setMessage("¿Desea salir de la aplicación?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();  // Cerrar todas las actividades y salir de la aplicación
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
