package com.example.sargentqa_f_38;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class MainQA_F_38 extends AppCompatActivity {

    private Button btnCreatePart;
    private Button btnUpdate;       // Botón para agregar fila nueva desde SharedPreferences
    private Button btnGenerateQR;   // Botón para generar código QR
    private TableLayout dataTable;  // Tabla de datos
    private ImageView qrImage;      // Imagen donde se mostrará el código QR

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_qa_f38);

        // Enlazar vistas
        btnCreatePart = findViewById(R.id.btnCreatePart);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnGenerateQR = findViewById(R.id.btnGenerateQR);
        dataTable = findViewById(R.id.dataTable);
        qrImage = findViewById(R.id.qrImage);

        // Accion crear pieza
        btnCreatePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCP = new Intent(MainQA_F_38.this, MainCreatePart.class);
                MainQA_F_38.this.startActivity(intentCP);
            }
        });

        // Acción botón actualizar: añade fila con datos guardados
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRowFromSharedPreferences();
            }
        });

        // Acción botón generar QR: crea QR con toda la tabla
        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRFromTable();
            }
        });
    }

    //Agregar fila nueva
    private void addNewRowFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("PART", MODE_PRIVATE);

        String id = sharedPreferences.getString("ID_PART", "");
        String quantity = sharedPreferences.getString("QUANTITY_PART", "");
        String description = sharedPreferences.getString("DESCRIPTION_PART", "");

        if (id.isEmpty() || quantity.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "No hay datos guardados para mostrar", Toast.LENGTH_SHORT).show();
            return;
        }

        TableRow newRow = new TableRow(this);
        newRow.setBackgroundColor(0xFFE3F2FD); // Color azul claro

        TextView tvId = new TextView(this);
        tvId.setText(id);
        tvId.setPadding(8, 8, 8, 8);

        TextView tvQty = new TextView(this);
        tvQty.setText(quantity);
        tvQty.setPadding(8, 8, 8, 8);

        TextView tvDesc = new TextView(this);
        tvDesc.setText(description);
        tvDesc.setPadding(10, 10, 10, 10);

        newRow.addView(tvId);
        newRow.addView(tvQty);
        newRow.addView(tvDesc);

        dataTable.addView(newRow);
    }

    //generar QR
    private void generateQRFromTable() {
        StringBuilder data = new StringBuilder();

        // Empezamos desde la fila 1 para omitir el encabezado
        for (int i = 1; i < dataTable.getChildCount(); i++) {
            TableRow row = (TableRow) dataTable.getChildAt(i);
            StringBuilder rowData = new StringBuilder();

            for (int j = 0; j < row.getChildCount(); j++) {
                TextView cell = (TextView) row.getChildAt(j);
                rowData.append(cell.getText().toString().trim());
                if (j < row.getChildCount() - 1) {
                    rowData.append(" | ");
                }
            }
            data.append(rowData.toString()).append("\n");
        }

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter()
                    .encode(data.toString(), BarcodeFormat.QR_CODE, 500, 500);
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "Error al generar QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
