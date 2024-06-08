package com.example.asistenciadocente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class abcDocentes extends menu {
    ActivityAbcDocentesBinding activityAbcDocentesBinding;
    EditText etNombre, etApellido, etMatricula;
    Button btnGuardar,btnEditar,btnBorrar,btnBuscar,ListaDocente;
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
        ListaDocente=findViewById(R.id.ListaDocente);
        etMatricula = findViewById(R.id.etMatricula);

        String[] crud = {"ISC","IIAS","II","IGE","IIAL"};
        ArrayAdapter<String> AdapterCrud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, crud);
        spAcademia.setAdapter(AdapterCrud);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Guardar("http://192.168.56.1:80/api_checador/docentes");
               //Guardar("http://201.164.155.166/api_checador/docentes");

            }
        });
        ListaDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(abcDocentes.this, ListaDocentes.class);  //Creamos el intent y le indicamos desde donde vamos (this) y a donde vamos (OtraActividad.class)
                startActivity(intent);  //Abrimos la otra actividad
            }
            });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idMatricula = etMatricula.getText().toString();
                Log.d("DEBUG", "Matricula a buscar: " + idMatricula);  // Agregar este log

                if (idMatricula.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese un ID de aula válido", Toast.LENGTH_SHORT).show();
                    return;
                }

               //String url = "http://201.164.155.166/api_checador/docentes/" + idMatricula;
                String url = "http://192.168.56.1:80/api_checador/docentes/" + idMatricula;

                buscarDocente(url);


               // buscarDocente("https://checador.tech/api_checador/docentes?Matricula="+etMatricula.getText()+"");
                //buscarDocente("http://192.168.56.1:80/CHECKTECH/buscar_docente.php?Matricula="+etMatricula.getText()+"");
                //Log.d("DEBUG", "ID de aula a buscar: " + etMatricula);  // Agregar este log
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar("http://201.164.155.166/api_checador/actualizar-docente");
                Guardar("http://192.168.56.1:80/api_checador/actualizar-docente");

            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //Borrar("http://201.164.155.166/api_checador/eliminar-docente");

             Borrar("http://192.168.56.1:80/api_checador/eliminar-docente");
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


                        if (trimmedResponse.equals("{\"message\":\"EXISTE\"}")) {
                            Toast.makeText(abcDocentes.this, "La matrícula ya existe, ingresa otro valor", Toast.LENGTH_SHORT).show();


                        } else if (trimmedResponse.equals("{\"Matricula\":\"" + etMatricula + "\",\"nombre\":\"" + etNombre + "\",\"apellidos\":\"" + etApellido + "\",\"academia\":\"" + opcion + "\"}")) {
                            // Respuesta inesperada del servidor
                            Toast.makeText(abcDocentes.this, "Error servidor ", Toast.LENGTH_SHORT).show();

                            Log.e("Unexpected Response", trimmedResponse);
                        } else {
                            Toast.makeText(getApplicationContext(), "Se Realizo La Operacion Correctamente", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getApplicationContext(),"Numero de Matricula Inexistente",Toast.LENGTH_SHORT).show();

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getApplicationContext(),"Docente Borrado",Toast.LENGTH_SHORT).show();
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