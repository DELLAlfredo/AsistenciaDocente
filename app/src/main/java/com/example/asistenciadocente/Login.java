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
    //Se definen las variables nombre, contraseña y ingresar para
    // representar los elementos de la interfaz de usuario en el archivo de diseño XML (activity_main.xml).
    EditText email,contraseña;
    Button ingresar;
    String Usuario="Admin",Paswword="@admin", Usuario2="Admin2",Paswword2="@admin2";
    //En el método onCreate, se establece el contenido de la actividad utilizando el archivo de diseño activity_main.xml. Luego,
    // se asignan los elementos de la interfaz de usuario a las variables correspondientes utilizando findViewById.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= findViewById(R.id.email);
        contraseña=findViewById(R.id.contraseña);

        ingresar =findViewById(R.id.loginButton);

    recuperarPreferencias();


        //Se configura un OnClickListener para el botón ingresar.
        // Cuando el botón se hace clic, se llama al método validarUsuario con la URL del servidor para validar el usuario.
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String frase = email.getText().toString();
                String pass = contraseña.getText().toString();

                if (frase.equalsIgnoreCase(Usuario) && pass.equalsIgnoreCase(Paswword)) {
                    Toast.makeText(getApplicationContext(), "Ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Clases.class);
                    startActivity(intent);
                   guardarPreferencias(Usuario, Paswword);  // Guardar preferencias del primer usuario
                } else if (frase.equalsIgnoreCase(Usuario2) && pass.equalsIgnoreCase(Paswword2)) {
                    Toast.makeText(getApplicationContext(), "Ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, EditarInforme.class);
                    startActivity(intent);
                    guardarPreferencias(Usuario2, Paswword2);  // Guardar preferencias del segundo usuario
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos, por favor verifiquelos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    /*private void camposVacios(){
        Toast.makeText(Login.this, "Campos vacios", Toast.LENGTH_SHORT).show();
    }*/
//El método validarUsuario realiza una solicitud POST a la URL especificada utilizando Volley.
// Cuando se recibe una respuesta del servidor,se verifica si la respuesta no está vacía.
// Si no está vacía, se inicia una nueva actividad (Clases.class) utilizando un Intent.
//  De lo contrario, se muestra un mensaje de error utilizando Toast.
    private boolean isValidEmail(CharSequence target) {
         return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
   /* private void validarUsuario(String URL){
        //En el bloque getParams dentro de StringRequest, se definen los parámetros para enviar
        StringRequest StringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response);

                if (response.equals("{\"message\":\"Login failed\"}")){
                    Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                } else if (response.equals("{\"email\":\"" + Usuario + "\",\"contraseña\":\"" + Paswword + "\"}")) {
                    // Respuesta inesperada del servidor
                    Toast.makeText(Login.this, "Error servidor ", Toast.LENGTH_SHORT).show();

                    Log.e("Unexpected Response", response);
                } else {
                    // Iniciar actividad después de la autenticación exitosa
                    guardarPreferencias();
                    Intent intent = new Intent(getApplicationContext(), Clases.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mensaje de error por internet o conexion
                Toast.makeText(Login.this, "Conexion Inestable Verfique Su Conexion", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //insersion de datos para validar usuario y comparar
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("email",Usuario);
                parametros.put("contraseña",Paswword);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(StringRequest);
    }*/
    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(), RegistroUsuario.class));
        finish();
    }
    private void guardarPreferencias(String usuario, String password) {
        SharedPreferences preferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Verificamos cuál usuario ha iniciado sesión y guardamos sus preferencias
        if (usuario.equalsIgnoreCase(Usuario) && password.equalsIgnoreCase(Paswword)) {
            editor.putString("email", Usuario);
            editor.putString("contraseña", Paswword);
        } else if (usuario.equalsIgnoreCase(Usuario2) && password.equalsIgnoreCase(Paswword2)) {
            editor.putString("email", Usuario2);
            editor.putString("contraseña", Paswword2);
        }

        // Guardamos el estado de la sesión como activa
        editor.putBoolean("sesion", true);
        editor.apply();  // Usamos apply() para guardar de manera asíncrona
    }


    private void recuperarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("PreferencesLogin",Context.MODE_PRIVATE);
        email.setText(preferences.getString("email",""));
        contraseña.setText(preferences.getString("contraseña",""));
    }

   /* public void Recuperarcontraseña(View view) {
        startActivity(new Intent(getApplicationContext(), enviacorreo.class));
        finish();
    }*/

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir");
        builder.setMessage("¿Desea salir de la aplicación?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity(); // Cerrar todas las actividades y salir de la aplicación
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