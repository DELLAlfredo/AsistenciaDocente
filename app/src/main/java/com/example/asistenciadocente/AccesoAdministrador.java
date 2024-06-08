package com.example.asistenciadocente;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AccesoAdministrador extends AppCompatActivity {
    EditText email,contraseña;
    Button ingresar;
    String Usuario="Admin2",Paswword="@admin2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso_administrador);
        email= findViewById(R.id.email);
        contraseña=findViewById(R.id.contraseña);
        ingresar =findViewById(R.id.loginButton);

        //Se configura un OnClickListener para el botón ingresar.
        // Cuando el botón se hace clic, se llama al método validarUsuario con la URL del servidor para validar el usuario.
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String frase = email.getText().toString();
                String pass = contraseña.getText().toString();
                if(email.getText().toString().equalsIgnoreCase(Usuario)&& contraseña.getText().toString().equalsIgnoreCase(Paswword)){
                    Toast.makeText(getApplicationContext(),"Ha iniciado sesión correctamente",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AccesoAdministrador.this, EditarInforme.class);  //Creamos el intent y le indicamos desde donde vamos (this) y a donde vamos (OtraActividad.class)
                    startActivity(intent);  //Abrimos la otra actividad

                }else{
                    email.setError("Ingresa tu nombre de usuario correcto");
                    contraseña.setError("Ingresa la contraseña correcta");
                    Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                    camposVacios();

                }

            }
        });
    }
    private void camposVacios(){
        Toast.makeText(AccesoAdministrador.this, "Campos vacios", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir");
        builder.setMessage("¿Desea regresar?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AccesoAdministrador.this, Clases.class);  //Creamos el intent y le indicamos desde donde vamos (this) y a donde vamos (OtraActividad.class)
                startActivity(intent);
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