package net.iessanclemente.a19lazaropm.aservice.ui.extra;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Medicion;

public class MedicionesVolumenExtraccionActivity extends AppCompatActivity {

    private static final int RESULT_ADD_PROBLEM = 666;

    private EditText valor1MedicionesVolumenEditText;
    private EditText valor2MedicionesVolumenEditText;
    private EditText valor3MedicionesVolumenEditText;
    private EditText valor4MedicionesVolumenEditText;
    private EditText valor5MedicionesVolumenEditText;
    private EditText valor6MedicionesVolumenEditText;
    private EditText valor7MedicionesVolumenEditText;
    private EditText valor8MedicionesVolumenEditText;
    private EditText valor9MedicionesVolumenEditText;
    private Medicion medicion = new Medicion();
    private float valorMedioAdded;
    private Button medicionesVolumentCancelButton;
    private Button medicionesVolumentAcceptButton;

    EditText arrayEditTextValores[];

    DataBaseOperations datos = DataBaseOperations.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediciones_volumen_extraccion);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        eventHandler();
    }

    private void eventHandler() {
        medicionesVolumentCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(-1, 0F, RESULT_CANCELED);
            }
        });

        medicionesVolumentAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idMedicionAdded = addNewMedicion();
                if (idMedicionAdded > 0) {
                    sendResult(idMedicionAdded, valorMedioAdded, RESULT_OK);
                } else {
                    //sendResult(-1, 0F, RESULT_ADD_PROBLEM);
                }
            }
        });
    }

    private int addNewMedicion() {
        boolean isAllMediciones = true;
        int idMedicionAdded = -1;
        float media = 0F;
        float sum = 0;
        for (int i = 0; i< arrayEditTextValores.length; i++){
            EditText editTextValor = arrayEditTextValores[i];
            try {
                float valor = Float.parseFloat(editTextValor.getText().toString());
                switch (i+1) {
                    case 1:
                        medicion.setValor1(valor);
                        break;
                    case 2:
                        medicion.setValor2(valor);
                        break;
                    case 3:
                        medicion.setValor3(valor);
                        break;
                    case 4:
                        medicion.setValor4(valor);
                        break;
                    case 5:
                        medicion.setValor5(valor);
                        break;
                    case 6:
                        medicion.setValor6(valor);
                        break;
                    case 7:
                        medicion.setValor7(valor);
                        break;
                    case 8:
                        medicion.setValor8(valor);
                        break;
                    case 9:
                        medicion.setValor9(valor);
                        break;
                }
                sum += valor;
            } catch (NumberFormatException e) {
                Toast.makeText(
                        MedicionesVolumenExtraccionActivity.this,
                        getString(R.string.error_requerido_numero_decimal, i+1),
                        Toast.LENGTH_SHORT).show();
                isAllMediciones = false;
                break;
            }
        }
        if (!isAllMediciones) {
            return idMedicionAdded;
        }
        media = sum/ arrayEditTextValores.length;
        medicion.setValorMedio(media);
        //Compruebo si existe este objeto medición en la bbdd y si no existe lo añado
        if (!datos.existMedicion(medicion)) { //no exite este objeto medicion e intento añadirlo
            if ((idMedicionAdded = (int) datos.insertMediciones(medicion)) > 0) {
                valorMedioAdded = media;
            }
        }
        return idMedicionAdded;
    }

    private void sendResult(int idMedicion, Float valorMedio, int result) {
        Intent intent = new Intent();
        intent.putExtra("ID_MEDICION", idMedicion);
        intent.putExtra("VALOR_MEDIO", valorMedio);
        setResult(result, intent);
        finish();
    }

    private void init() {

        valor1MedicionesVolumenEditText = findViewById(R.id.valor1MedicionesVolumenEditTextNumberDecimal);
        valor2MedicionesVolumenEditText = findViewById(R.id.valor2MedicionesVolumenEditTextNumberDecimal);
        valor3MedicionesVolumenEditText = findViewById(R.id.valor3MedicionesVolumenEditTextNumberDecimal);
        valor4MedicionesVolumenEditText = findViewById(R.id.valor4MedicionesVolumenEditTextNumberDecimal);
        valor5MedicionesVolumenEditText = findViewById(R.id.valor5MedicionesVolumenEditTextNumberDecimal);
        valor6MedicionesVolumenEditText = findViewById(R.id.valor6MedicionesVolumenEditTextNumberDecimal);
        valor7MedicionesVolumenEditText = findViewById(R.id.valor7MedicionesVolumenEditTextNumberDecimal);
        valor8MedicionesVolumenEditText = findViewById(R.id.valor8MedicionesVolumenEditTextNumberDecimal);
        valor9MedicionesVolumenEditText = findViewById(R.id.valor9MedicionesVolumenEditTextNumberDecimal);

        arrayEditTextValores = new EditText[]{
                valor1MedicionesVolumenEditText,
                valor2MedicionesVolumenEditText,
                valor3MedicionesVolumenEditText,
                valor4MedicionesVolumenEditText,
                valor5MedicionesVolumenEditText,
                valor6MedicionesVolumenEditText,
                valor7MedicionesVolumenEditText,
                valor8MedicionesVolumenEditText,
                valor9MedicionesVolumenEditText
        };
        medicionesVolumentCancelButton = findViewById(R.id.medicionesVolumenCancelButton);
        medicionesVolumentAcceptButton = findViewById(R.id.medicionesVolumenAcceptButton);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}