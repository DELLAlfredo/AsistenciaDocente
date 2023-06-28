package com.example.asistenciadocente;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asistenciadocente.databinding.ActivityAbcDocentesBinding;

import java.io.BufferedReader;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class abcDocentes extends menu {
    ActivityAbcDocentesBinding activityAbcDocentesBinding;
    EditText etNombre, etApellido, etMatricula;
    Button btnGuardar,btnEditar,btnBorrar,btnBuscar;
    Spinner spAcademia;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAbcDocentesBinding=ActivityAbcDocentesBinding.inflate(getLayoutInflater());
        setContentView(activityAbcDocentesBinding.getRoot());
        allocateActivityTitle("Docentes");

        //etAcademia = findViewById(R.id.etAcademia);
        spAcademia = findViewById(R.id.spAcademia);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEditar = findViewById(R.id.btnEditar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnBuscar = findViewById(R.id.btnBuscar);
        etMatricula = findViewById(R.id.etMatricula);

        String[] crud = {"ISIC","INVAG","GES","SUSTENTABLE"};
        ArrayAdapter<String> AdapterCrud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, crud);
        spAcademia.setAdapter(AdapterCrud);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Guardar("http://192.168.0.10:80/Checador/docentes.php");
                //Guardar("http://192.168.56.1:80/CHECKTECH//docentes.php");

            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarDocente("http://192.168.0.10/Checador/buscar_docente.php?Matricula="+etMatricula.getText()+"");
                //buscarDocente("http://192.168.56.1:80/CHECKTECH/buscar_docente.php?Matricula="+etMatricula.getText()+"");

            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Guardar("http://192.168.0.10:80/Checador/editar_docente.php");
               //Guardar("http://192.168.56.1:80/CHECKTECH/editar_docente.php");

            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Borrar("http://192.168.0.10:80/Checador/eliminar_docente.php");

                //Borrar("http://192.168.56.1:80/CHECKTECH/eliminar_docente.php");
            }
        });


    }


    private void Guardar(String URL) {
        String opcion = spAcademia.getSelectedItem().toString();
        // Validar campos vacíos
        if (etNombre.getText().toString().isEmpty() || etApellido.getText().toString().isEmpty() ||
                etMatricula.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest guardarRequest = new StringRequest(com.android.volley.Request.Method.POST, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Verificar la respuesta del servidor
                        String trimmedResponse = response.trim(); // Eliminar espacios en blanco antes y después
                        Log.d("RESPONSE", trimmedResponse); // Imprimir la respuesta para verificar

                        if (trimmedResponse.equals("EXISTE")) {
                            // La matrícula ya existe
                            Toast.makeText(getApplicationContext(), "La matrícula ya existe, ingresa otro valor", Toast.LENGTH_SHORT).show();
                        } else {
                            // La matrícula no existe, proceder a guardar
                            Toast.makeText(getApplicationContext(), "Operacion Exitosa", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", etNombre.getText().toString());
                parametros.put("apellidos", etApellido.getText().toString());
                parametros.put("academia", opcion);
                parametros.put("Matricula", etMatricula.getText().toString());
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(guardarRequest);

    }

    private void buscarDocente(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String academia = jsonObject.getString("academia");//llena el campo del spinner con el dato que este en la db
                        etNombre.setText(jsonObject.getString("nombre"));//llena el campo del editText con el dato que este en la db
                        etApellido.setText(jsonObject.getString("apellidos"));//llena el campo del editText con el dato que este en la db

                        // Establecer los valores en los elementos correspondientes
                        spAcademia.setSelection(obtenerIndiceSpinner(spAcademia, academia));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Numero de Matricula Inexistente",Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    // Método auxiliar para obtener el índice de un elemento en un Spinner según su valor
    private int obtenerIndiceSpinner(Spinner spinner, String valor) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        return adapter.getPosition(valor);
    }





    private void Borrar(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //arroga un mensaje cuando se borra un dato
                Toast.makeText(getApplicationContext(),"Producto eliminado",Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            }
        }, new Response.ErrorListener(){
            @Override
            // se ejecuta cuando ocurre un error en la solicitud de la red.
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError{
                Map<String,String> parametros= new HashMap<String,String>();
                parametros.put("Matricula",etMatricula.getText().toString());
                return  parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //limpia los campos de los formularios
    private void limpiarFormulario(){
        etMatricula.setText("");
        etNombre.setText("");
        etApellido.setText("");
        //etAcademia.setText("");
    }
}