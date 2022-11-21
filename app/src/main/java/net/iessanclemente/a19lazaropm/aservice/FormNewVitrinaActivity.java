package net.iessanclemente.a19lazaropm.aservice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Fabricante;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Longitud;
import net.iessanclemente.a19lazaropm.aservice.database.dto.TipoVitrina;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Vitrina;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FormNewVitrinaActivity extends AppCompatActivity {

    private static final int RESULT_ADD_PROBLEM = 666;

    private ActionBar actionBar;

    private AutoCompleteTextView newVitrinaFabriNameAutoCompleteTextView;
    private Spinner newVitrinaTipoSpinner;
    private EditText newVitrinaReferenceEditText;
    private EditText newVitrinaInventaryEditText;
    private Spinner newVitrinaLongitudSpinner;
    private TextView getNewVitrinaGuillotinaTextView;
    private Spinner newVitrinaAnhoSpinner;
    private EditText newVitrinaContratoEditTex;
    private Button newVitrinaAcceptButton;
    private Button newVitrinaCancelButton;

    private boolean isUpdateTask;
    private Vitrina vitrinaOld;

    private final DataBaseOperations datos = DataBaseOperations.getInstance(FormNewVitrinaActivity.this);

    private List<String> listFabricantes;
    private List<String> listTipos;
    private List<String> listLongitudes;
    private List<String> listAnhos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_vitrina);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        init();
        enventHandler();
    }

    private void enventHandler() {
        newVitrinaLongitudSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getNewVitrinaGuillotinaTextView.setText(String.valueOf(getGuillotina()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        newVitrinaCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult("", RESULT_CANCELED);
            }
        });

        newVitrinaAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = addNewVitrina();
                if (isAdded) {
                    sendResult(newVitrinaFabriNameAutoCompleteTextView.getText().toString(), RESULT_OK);
                } else {
                    sendResult("", RESULT_ADD_PROBLEM);
                }
            }
        });
    }

    private boolean addNewVitrina() {
        boolean isOk = false;
        int idEmpresa = datos.getIdOfEmpresaWhithName(getIntent().getStringExtra("NOMBRE_EMPRESA"));
        int idTipo = datos.getIdOfVitrinaTipo(newVitrinaTipoSpinner.getSelectedItem().toString());
        int idLongitud = datos.getIdLongitudForLongVitrina(
                Integer.parseInt(newVitrinaLongitudSpinner.getSelectedItem().toString()));
        String nombreFabricante = newVitrinaFabriNameAutoCompleteTextView.getText().toString();
        String referencia = newVitrinaReferenceEditText.getText().toString();
        String inventario = newVitrinaInventaryEditText.getText().toString();
        int longitudVitrina = Integer.parseInt(newVitrinaLongitudSpinner.getSelectedItem().toString());
        int anho = Integer.parseInt(newVitrinaAnhoSpinner.getSelectedItem().toString());
        String contrato = newVitrinaContratoEditTex.getText().toString();

        //El usuario puede introducir un nuevo fabricante por lo que hay que comprobar si existe o no
        //y de no existrir crearlo.
        int idFabricante = -1;
        if (nombreFabricante.isEmpty()) {
            Toast.makeText(
                    FormNewVitrinaActivity.this,
                    "Se requiere un Fabricante",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        Fabricante fabriNewVitrina = new Fabricante(idFabricante, nombreFabricante);

        if (isUpdateTask) {
            fabriNewVitrina.setId(vitrinaOld.getIdFabricante());
            if (datos.updateFabricante(fabriNewVitrina)) {
                Vitrina newVitrina = new Vitrina(
                        vitrinaOld.getId(), idEmpresa, idTipo, idLongitud, fabriNewVitrina.getId(),
                        referencia, inventario, anho, contrato);
                if (datos.updateVitrina(newVitrina)) {
                    isOk = true;
                } else {
                    Toast.makeText(
                            FormNewVitrinaActivity.this,
                            "No se pudo Añadir el nuevo Fabricante",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (!datos.existFabricante(fabriNewVitrina)) {
                idFabricante = (int) datos.insertFabricante(fabriNewVitrina);
            } else {
                idFabricante = datos.getIdOfFabricanteWithName(fabriNewVitrina.getNombre());
            }
            if (idFabricante > 0) {
                Vitrina newVitrina = new Vitrina(
                        -1, idEmpresa, idTipo, idLongitud, idFabricante, referencia, inventario,
                        anho, contrato);
                if (!datos.existVitrina(newVitrina)) {
                    long idNewVitrina = datos.insertVitrina(newVitrina);
                    if (idNewVitrina > 0) {
                        isOk = true;
                    }
                } else {
                    Toast.makeText(
                            FormNewVitrinaActivity.this,
                            "Vitrina ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return isOk;
    }

    private void init() {
        //Fabricante
        newVitrinaFabriNameAutoCompleteTextView = findViewById(R.id.newVitrinaFabriNameAutoCompleteTextView);
        listFabricantes = getListFabricantes();
        ArrayAdapter<String> fabricanteAdapter = new ArrayAdapter<>(
                FormNewVitrinaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                listFabricantes);
        newVitrinaFabriNameAutoCompleteTextView.setAdapter(fabricanteAdapter);


        //Tipo
        newVitrinaTipoSpinner = findViewById(R.id.newVitrinaTipoSpinner);
        listTipos = getListTiposVitrinas();
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(
                FormNewVitrinaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                listTipos);
        newVitrinaTipoSpinner.setAdapter(tipoAdapter);
        //Referencia
        newVitrinaReferenceEditText = findViewById(R.id.newVritrinaReferenceEditText);
        //Inventario
        newVitrinaInventaryEditText = findViewById(R.id.newVitrinaInventaryEditText);
        //Longitud
        newVitrinaLongitudSpinner = findViewById(R.id.newVitrinaLongitudSpinner);
        listLongitudes = getListLongitudes();
        ArrayAdapter<String> longitudAdapter = new ArrayAdapter<>(
                FormNewVitrinaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                listLongitudes);
        newVitrinaLongitudSpinner.setAdapter(longitudAdapter);
        //Guillotina
        getNewVitrinaGuillotinaTextView = findViewById(R.id.newVitrinaGuillotinaTextView);
        getNewVitrinaGuillotinaTextView.setText(String.valueOf(getGuillotina()));

        //Año
        newVitrinaAnhoSpinner = findViewById(R.id.newVitrinaAnhoSpinner);
        listAnhos = getListAnhos();
        ArrayAdapter<String> anhoAdapter = new ArrayAdapter<>(
                FormNewVitrinaActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                listAnhos);
        newVitrinaAnhoSpinner.setAdapter(anhoAdapter);
        //Contrato
        newVitrinaContratoEditTex = findViewById(R.id.newVitrinaContratoEditText);

        newVitrinaCancelButton = findViewById(R.id.newVitrinaCancelButton);
        newVitrinaAcceptButton = findViewById(R.id.newVitrinaAcceptButton);

        //Comprobar si la tarea es actualizar
        if (getIntent().hasExtra("TASK")) {
            isUpdateTask = getIntent().getStringExtra("TASK").equalsIgnoreCase("UPDATE");
        } else {
            isUpdateTask = false;
        }
        //Poblar con los datos de la vitrina a actualizar
        if (isUpdateTask) {
            if (getIntent().hasExtra("ID_VITRINA")) {
                int idVitrinaOld = getIntent().getIntExtra("ID_VITRINA", -1);
                if (idVitrinaOld > 0) {
                    vitrinaOld = datos.selectVitrinaWithId(idVitrinaOld);
                }
            }
            if (vitrinaOld != null) {
                newVitrinaFabriNameAutoCompleteTextView.setText(
                        datos.getNameFabricanteWithIdFabricante(vitrinaOld.getIdFabricante()));

                //Obtengo tipo de vitrina a partir de su id
                TipoVitrina tipoVitrinaOld = datos.selectTipoVitrinaWithId(vitrinaOld.getIdTipo());
                newVitrinaTipoSpinner.setSelection(
                        datos.getIdOfVitrinaTipo(tipoVitrinaOld.getTipoVitrina()));

                newVitrinaReferenceEditText.setText(vitrinaOld.getVitrinaReferencia());
                newVitrinaInventaryEditText.setText(vitrinaOld.getVitrinaInventario());

                //Obtengo el largo de vitrina a partir de su id
                Longitud longitudOld = datos.selectLongitudWithId(vitrinaOld.getIdLongitud());
                newVitrinaLongitudSpinner.setSelection(
                        datos.getIdOfLongitudWithName(longitudOld.getLongitudVitrina()));
                getNewVitrinaGuillotinaTextView.setText(
                        String.valueOf(longitudOld.getLogintudGillotina()));

                newVitrinaAnhoSpinner.setSelection(getListAnhos().indexOf(
                        String.valueOf(vitrinaOld.getVitrinaAnho())));
                newVitrinaContratoEditTex.setText(vitrinaOld.getVitrinaContrato());
            }
        }

    }

    private void sendResult(String fabricante, int result) {
        Intent intent = new Intent();
        intent.putExtra("FABRICANTE", fabricante);
        setResult(result, intent);
        finish();
    }

    private float getGuillotina() {
        int longitudVitrina = Integer.parseInt(newVitrinaLongitudSpinner.getSelectedItem().toString());
        return datos.getGuillotinaForLongVitrina(longitudVitrina);
    }

    private List<String> getListAnhos() {
        List<String> listAnhos = new ArrayList<>();
        int anhoActual = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1980; i <= anhoActual; i++) {
            listAnhos.add(String.valueOf(i));
        }
        return listAnhos;
    }

    private List<String> getListLongitudes() {
        List<String> listLongitudes = new ArrayList<>();
        for (Longitud longitud: datos.selectLongitudes()) {
            listLongitudes.add(String.valueOf(longitud.getLongitudVitrina()));
        }
        return listLongitudes;
    }

    private List<String> getListTiposVitrinas() {
        List<String> listTipos = new ArrayList<>();
        for (TipoVitrina tipoVitrina: datos.selectTiposVitrinas()) {
            listTipos.add(tipoVitrina.getTipoVitrina());
        }
        return listTipos;
    }

    private List<String> getListFabricantes() {
        List<String> listFabricantes = new ArrayList<>();
        for (Fabricante fabricante: datos.selectFabricantes()) {
            listFabricantes.add(fabricante.getNombre());
        }
        return listFabricantes;
     }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}