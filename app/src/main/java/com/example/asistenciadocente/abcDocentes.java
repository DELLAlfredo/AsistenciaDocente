package com.example.asistenciadocente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class abcDocentes extends AppCompatActivity {
    EditText etID,etNombre, etApellido;
    Spinner spCarrera,spOpcion;
    Button btnGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abc_docentes);

        etID = findViewById(R.id.etID);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        spCarrera = findViewById(R.id.spCarrera);
        spOpcion = findViewById(R.id.spOpcion);
        btnGuardar = findViewById(R.id.btnGuardar);

        String[] abc = {"ISIC","IIND","IGEM","IINA","IIA"};
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, abc);
        spCarrera.setAdapter(Adapter);

        String[] crud = {"Añadir","Actualizar","Buscar","Eliminar"};
        ArrayAdapter<String> AdapterCrud = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, crud);
        spOpcion.setAdapter(AdapterCrud);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (spOpcion.getSelectedItem().toString()) {
                    case "Añadir":
                        if (!etID.getText().toString().isEmpty() &&
                                !etNombre.getText().toString().isEmpty() &&
                                !etApellido.getText().toString().isEmpty() &&
                                !spCarrera.getSelectedItem().toString().isEmpty()) {
                            int code = Integer.parseInt(etID.getText().toString());
                            String nombre = etNombre.getText().toString();
                            String apellidos = etApellido.getText().toString();
                            String academia = spCarrera.getSelectedItem().toString();

                            add(code, nombre, apellidos, academia, spCarrera.getSelectedItem().toString());

                        } else {
                            Toast.makeText(abcDocentes.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Actualizar":
                        // Realizar la lógica para actualizar el registro usando la API
                        if (!etID.getText().toString().isEmpty() &&
                                !etNombre.getText().toString().isEmpty() &&
                                !etApellido.getText().toString().isEmpty() &&
                                !spCarrera.getSelectedItem().toString().isEmpty()) {
                            int code = Integer.parseInt(etID.getText().toString());
                            String nombre = etNombre.getText().toString();
                            String apellidos = etApellido.getText().toString();
                            String academia = spCarrera.getSelectedItem().toString();

                            // Llamar a la API para añadir los datos a la base de datos externa
                           // boolean success = api.addMaestro(code, nombre, apellidos, academia);
                            edit(code, nombre, apellidos, academia, spCarrera.getSelectedItem().toString());

                        } else {
                            Toast.makeText(abcDocentes.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Eliminar":
                        // Realizar la lógica para eliminar el registro usando la API
                        break;

                    case "Buscar":
                        // Realizar la lógica para buscar el registro usando la API
                        break;

                    default:
                        // Caso predeterminado, si no coincide con ninguna opción seleccionada
                        break;
                }

            }
        });

    }
    public void add(int code, String nombre, String apellidos, String academia, String spOpcion) {
        if (spOpcion.equals("Añadir")) {
            if (code != 0) {
                try {
                    URL url = new URL("URL_DE_LA_API"); // Reemplaza con la URL de tu API
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    // Crea los parámetros para enviar al servidor
                    String params = "code=" + code + "&nombre=" + nombre + "&apellidos=" + apellidos + "&academia=" + academia;

                    // Escribe los parámetros en el cuerpo de la solicitud
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params);
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // La solicitud se realizó con éxito
                        // Puedes realizar alguna acción adicional si es necesario
                        Toast.makeText(abcDocentes.this, "Se guardó el registro", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hubo un error en la solicitud
                        // Puedes mostrar un mensaje de error si lo deseas
                        Toast.makeText(abcDocentes.this, "Error al enviar los datos", Toast.LENGTH_SHORT).show();
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(abcDocentes.this, "No se pudo añadir, llena los campos", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void edit(int code, String nombre, String apellidos, String academia, String spOpcion) {
        if (spOpcion.equals("Actualizar")) {
            if (code != 0) {
                try {
                    URL url = new URL("URL_DE_LA_API"); // Reemplaza con la URL de tu API
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setDoOutput(true);

                    // Crea los parámetros para enviar al servidor
                    String params = "code=" + code + "&nombre=" + nombre + "&apellidos=" + apellidos + "&academia=" + academia;

                    // Escribe los parámetros en el cuerpo de la solicitud
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params);
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // La solicitud se realizó con éxito
                        // Puedes realizar alguna acción adicional si es necesario
                        Toast.makeText(abcDocentes.this, "Registro actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hubo un error en la solicitud
                        // Puedes mostrar un mensaje de error si lo deseas
                        Toast.makeText(abcDocentes.this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(abcDocentes.this, "No se pudo actualizar, llena los campos", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void delete(int code, String spOpcion) {
        if (spOpcion.equals("Eliminar")) {
            if (code != 0) {
                try {
                    URL url = new URL("URL_DE_LA_API"); // Reemplaza con la URL de tu API
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("DELETE");

                    // Crea los parámetros para enviar al servidor
                    String params = "code=" + code;

                    // Escribe los parámetros en el cuerpo de la solicitud
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params);
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // La solicitud se realizó con éxito
                        // Puedes realizar alguna acción adicional si es necesario
                        Toast.makeText(abcDocentes.this, "Registro eliminado", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hubo un error en la solicitud
                        // Puedes mostrar un mensaje de error si lo deseas
                        Toast.makeText(abcDocentes.this, "Error al eliminar el registro", Toast.LENGTH_SHORT).show();
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(abcDocentes.this, "No se pudo eliminar, llena los campos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}