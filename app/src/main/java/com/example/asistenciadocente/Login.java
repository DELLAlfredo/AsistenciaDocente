package com.example.asistenciadocente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    //Se definen las variables nombre, contraseña y ingresar para
    // representar los elementos de la interfaz de usuario en el archivo de diseño XML (activity_main.xml).
    EditText email,contraseña;
    Button ingresar;
    String Usuario,Paswword;
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
                Usuario=email.getText().toString();
                Paswword=contraseña.getText().toString();
                if (!Usuario.isEmpty() && !Paswword.isEmpty()) {
                     validarUsuario("http://192.168.0.6:80/Checador/Validar_Usuario.php");
                    //validarUsuario("http://192.168.56.1:80/Checador/Validar_Usuario.php");
                }else {
                    Toast.makeText(Login.this, "No deje campos vacios", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
//El método validarUsuario realiza una solicitud POST a la URL especificada utilizando Volley.
// Cuando se recibe una respuesta del servidor,se verifica si la respuesta no está vacía.
// Si no está vacía, se inicia una nueva actividad (Clases.class) utilizando un Intent.
//  De lo contrario, se muestra un mensaje de error utilizando Toast.
    private void validarUsuario(String URL){
        //En el bloque getParams dentro de StringRequest, se definen los parámetros para enviar
        StringRequest StringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    guardarPreferencias();
                    Intent inten = new Intent(getApplicationContext(), Clases.class);
                    startActivity(inten);
                    finish();
                }else{
                    //mensaje en caso de no existir o equivocarse en usuario
                    Toast.makeText(Login.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mensaje de error por internet o conexion
                Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_SHORT).show();
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
    }
    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(), RegistroUsuario.class));
        finish();
    }
    private  void guardarPreferencias(){
        SharedPreferences preferences   = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("email",Usuario);
        editor.putString("contraseña",Paswword);
        editor.putBoolean("sesion",true);
        editor.commit();
    }

    private void recuperarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("PreferenciasLogin",Context.MODE_PRIVATE);
        email.setText(preferences.getString("email",""));
        contraseña.setText(preferences.getString("contraseña",""));


    }
}