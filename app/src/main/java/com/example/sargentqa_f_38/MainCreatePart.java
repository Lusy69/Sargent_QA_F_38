package com.example.sargentqa_f_38;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainCreatePart extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText etPartId, etQuantity, etPartDescription;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_create_part);

        // Inicializar SharedPreferences una sola vez y correctamente
        sharedPreferences = getSharedPreferences("PART", MODE_PRIVATE);

        // Enlazar vistas
        etPartId = findViewById(R.id.etPartId);
        etQuantity = findViewById(R.id.etQuantity);
        etPartDescription = findViewById(R.id.etPartDescription);
        btnCreate = findViewById(R.id.btnCreate);

        // Asignar evento al botón
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePart();
            }
        });
    }

    private void savePart() {
        String id = etPartId.getText().toString().trim();
        String quantity = etQuantity.getText().toString().trim();
        String description = etPartDescription.getText().toString().trim();

        // Validar campos vacíos
        if (id.isEmpty() || quantity.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar datos en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID_PART", id);
        editor.putString("QUANTITY_PART", quantity);
        editor.putString("DESCRIPTION_PART", description);
        editor.apply(); // Guarda de forma asíncrona

        Toast.makeText(this, "Parte guardada correctamente", Toast.LENGTH_SHORT).show();
    }
}
