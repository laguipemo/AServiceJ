package net.iessanclemente.a19lazaropm.aservice.ui.forms;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Cualitativo;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Longitud;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Mantenimiento;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Tecnico;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Vitrina;
import net.iessanclemente.a19lazaropm.aservice.ui.extra.MedicionesVolumenExtraccionActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FormNewMantenimientoActivity extends AppCompatActivity {

    private static final int RESULT_ADD_PROBLEM = 666;

    private EditText fechaNewMantenimientoEditText;
    private AutoCompleteTextView tecnicoNombreNewManteniAutoCompleteTextView;
    private CheckBox puestaMarchaNewManteniCheckBox;
    private CheckBox segunDinNewManteniCheckBox;
    private CheckBox segunEnNewManteniCheckBox;
    private Spinner evaluControlDigitalSpinner;
    private Spinner evaluSistemaExtraccionSpinner;
    private Spinner evaluProteccionSuperficieSpinner;
    private Spinner evaluJuntasSpinner;
    private Spinner evaluFijacionPiezasRigidasSpnner;
    private Spinner evaluFuncionamientoGuilloSpinner;
    private Spinner evaluEstadoGeneralGuilloSpinner;
    private EditText valorFuerzaDesplazaGuilloNewManteniEditText;
    private Spinner evaluFuerzaDesplazaGuilloSpinner;
    private Spinner evaluControlPresenciaSpinner;
    private Spinner evaluAutoproteccionSpinner;
    private Spinner evaluFuncGrifosMonoredSpinner;
    private EditText valorVolumenExtraccionRealNewManteniEditText;
    private Spinner evaluVolumenExtraccionRealSpinner;
    private RadioButton acuerdoNormasReguSiNewManteniRadioButton;
    private RadioButton acuerdoNormasReguNoNewManteniRadioButton;
    private RadioButton necesarioRepaSiNewManteniRadioButton;
    private RadioButton necesarioRepaNoNewManteniRadioButton;
    private EditText comentarioNewManteniEditText;

    private Button newMantenimientoAcceptButton;
    private Button newMantenimientoCancelButton;

    private boolean isUpdateTask;
    private Mantenimiento mantenimientoOld;

    private DataBaseOperations datos = DataBaseOperations.getInstance(this);

    private List<String> listTecnicos;
    private List<String> listCualitativos;

    private String nombreEmpresa;
    private int idVitrina;
    private Vitrina vitrina;
    private int idMedicion;
    private int idMantenimiento;

    private InputMethodManager virtualKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_mantenimiento);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        init();
        enventHandler();
    }

    private void enventHandler() {
        // instancia del teclado virtual
        virtualKeyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        fechaNewMantenimientoEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        newMantenimientoCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult("", RESULT_CANCELED);
            }
        });

        newMantenimientoAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = addMantenimiento();
                if (isAdded) {
                    sendResult(fechaNewMantenimientoEditText.getText().toString(), RESULT_OK);
                } else {
                    sendResult("", RESULT_ADD_PROBLEM);
                }
            }
        });

        tecnicoNombreNewManteniAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                virtualKeyboard.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        valorFuerzaDesplazaGuilloNewManteniEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    virtualKeyboard.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return false;
            }
        });

        valorVolumenExtraccionRealNewManteniEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        FormNewMantenimientoActivity.this,
                        MedicionesVolumenExtraccionActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private boolean addMantenimiento() {
        boolean isOk = false;
        String fecha = fechaNewMantenimientoEditText.getText().toString();

        //El usuario podría haber introducido un técnico que no está en la base de datos, por lo
        //pronto esto no será permitido y hay que controlarlo
        int idTecnico = datos.getIdOfTecnicoWhithName(
                tecnicoNombreNewManteniAutoCompleteTextView.getText().toString());
        if (idTecnico < 0) {
            Toast.makeText(
                    FormNewMantenimientoActivity.this,
                    "Técnico desconocido",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean puestaMarcha = puestaMarchaNewManteniCheckBox.isChecked();
        boolean segunDin = segunDinNewManteniCheckBox.isChecked();
        boolean segunEn = segunEnNewManteniCheckBox.isChecked();
        if (!segunDin && !segunEn) {
            Toast.makeText(
                    FormNewMantenimientoActivity.this,
                    "Falta seleccionar norma seguida.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int ctrlDigital = datos.getIdOfCualitativoWithEvalu(
                evaluControlDigitalSpinner.getSelectedItem().toString());
        int sistExtraccion = datos.getIdOfCualitativoWithEvalu(
                evaluSistemaExtraccionSpinner.getSelectedItem().toString());
        int protSuperficie = datos.getIdOfCualitativoWithEvalu(
                evaluProteccionSuperficieSpinner.getSelectedItem().toString());
        int juntas = datos.getIdOfCualitativoWithEvalu(evaluJuntasSpinner.getSelectedItem().toString());
        int fijacion = datos.getIdOfCualitativoWithEvalu(
                evaluFijacionPiezasRigidasSpnner.getSelectedItem().toString());
        int funcGuillotina = datos.getIdOfCualitativoWithEvalu(
                evaluFuncionamientoGuilloSpinner.getSelectedItem().toString());
        int estadoGuillotina = datos.getIdOfCualitativoWithEvalu(
                evaluEstadoGeneralGuilloSpinner.getSelectedItem().toString());
        float valFuerzaGuillo = Float.parseFloat(
                valorFuerzaDesplazaGuilloNewManteniEditText.getText().toString());
        int fuerzaGuillotina = datos.getIdOfCualitativoWithEvalu(
                evaluFuerzaDesplazaGuilloSpinner.getSelectedItem().toString());
        int ctrlPresencia = datos.getIdOfCualitativoWithEvalu(
                evaluControlPresenciaSpinner.getSelectedItem().toString());
        int autoproteccion = datos.getIdOfCualitativoWithEvalu(
                evaluAutoproteccionSpinner.getSelectedItem().toString());
        int grifosMonored = datos.getIdOfCualitativoWithEvalu(
                evaluFuncGrifosMonoredSpinner.getSelectedItem().toString());
        if (idMedicion < 0) {
            Toast.makeText(
                    FormNewMantenimientoActivity.this,
                    "Faltan mediciones para calcular volumen de extracción",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        int evaluVolExtrac = datos.getIdOfCualitativoWithEvalu(
                evaluVolumenExtraccionRealSpinner.getSelectedItem().toString());
        boolean acordeNormasSi = acuerdoNormasReguSiNewManteniRadioButton.isChecked();
        boolean acordeNormasNo = acuerdoNormasReguNoNewManteniRadioButton.isChecked();
        if (!acordeNormasSi && !acordeNormasNo) {
            Toast.makeText(
                    FormNewMantenimientoActivity.this,
                    "Evaluación final incompleta",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean necesarioRepaSi = necesarioRepaSiNewManteniRadioButton.isChecked();
        boolean necesarioRepaNo = necesarioRepaNoNewManteniRadioButton.isChecked();
        if (!necesarioRepaSi && !necesarioRepaNo) {
            Toast.makeText(
                    FormNewMantenimientoActivity.this,
                    "Evaluación final incompleta",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        String comentario = comentarioNewManteniEditText.getText().toString();
        //Creo mantenimiento con los nuevos datos o los corregidos
        Mantenimiento mantenimiento = new Mantenimiento(
                idMantenimiento, fecha, idVitrina, puestaMarcha, idTecnico, segunDin, segunEn, ctrlDigital,
                sistExtraccion, protSuperficie, juntas, fijacion, funcGuillotina, estadoGuillotina,
                valFuerzaGuillo, fuerzaGuillotina, ctrlPresencia, autoproteccion, grifosMonored,
                idMedicion, evaluVolExtrac, acordeNormasSi, acordeNormasNo, necesarioRepaSi, necesarioRepaNo,
                comentario);
        //compruebo si este mantenimiento ya existe para saber si añadir o actualizar
        boolean existIdMantenimiento = datos.getIdsOfMantenimientosForVitrina(idVitrina).contains(idMantenimiento);
        if (!existIdMantenimiento) {
            if ((idMantenimiento = (int) datos.insertMantenimiento(mantenimiento)) > 0) {
                isOk = true;
            } else {
               Toast.makeText(
                       FormNewMantenimientoActivity.this,
                       "No se pudo insertar el mantenimiento",
                       Toast.LENGTH_SHORT).show();
               return false;
            }
        } else {
            boolean isUpdated = datos.updateMantenimiento(mantenimiento);
            if (isUpdated) {
                isOk = true;
            } else {
                Toast.makeText(
                        FormNewMantenimientoActivity.this,
                        "No se pudo actualizar el mantenimiento",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return isOk;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null && result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().hasExtra("VALOR_MEDIO")) {
                            idMedicion = result.getData().getIntExtra("ID_MEDICION", -1);
                            float valorMedio = result.getData().getFloatExtra("VALOR_MEDIO", 0F);
                            float longitudGuillotina = datos.getGuillotinaWithIdLongitud(vitrina.getIdLongitud());
                            float volumenExtraccion = valorMedio * 0.50F * 3600F * longitudGuillotina;
                            valorVolumenExtraccionRealNewManteniEditText.setText(
                                    String.format(Locale.getDefault(),
                                            "%.2f",
                                            volumenExtraccion));
                        }
                    } else if (result != null && result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(
                                FormNewMantenimientoActivity.this,
                                "Se cancelaron las mediciones", Toast.LENGTH_SHORT).show();
                    } else if (result != null && result.getResultCode() == RESULT_ADD_PROBLEM) {
                        if (result.getData() != null && result.getData().hasExtra("VALOR_MEDIO")) {
                            Toast.makeText(
                                    FormNewMantenimientoActivity.this,
                                    "No se pudo calcular Volumen de Extraccion: " + result.getData().getStringExtra("VALOR_MEDIO"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private void showDatePickerDialog() {
        Calendar nuevoCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                FormNewMantenimientoActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechaNewMantenimientoEditText.setText(
                                String.format(Locale.getDefault(),
                                        "%d-%d-%d",
                                        year, month + 1,
                                        dayOfMonth));
                    }
                },
                nuevoCalendar.get(Calendar.YEAR),
                nuevoCalendar.get(Calendar.MONTH),
                nuevoCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void sendResult(String fechaMantenimiento, int result) {
        Intent intent = new Intent();
        intent.putExtra("MANTENIMIENTO", fechaMantenimiento);
        setResult(result, intent);
        finish();
    }


    private void init() {
        //Recupero nombre empresa e id de la vitrina
        nombreEmpresa = getIntent().getStringExtra("NOMBRE_EMPRESA");
        idVitrina = getIntent().getIntExtra("ID_VITRINA", -1);
        vitrina = datos.selectVitrinaWithId(idVitrina);
        if (getIntent().hasExtra("ID_MANTENIMIENTO")) {
            idMantenimiento = getIntent().getIntExtra("ID_MANTENIMIENTO", -1);
        }
        //fecha
        fechaNewMantenimientoEditText = findViewById(R.id.fechaNewMantenimientoEditText);
       //Técnico
        tecnicoNombreNewManteniAutoCompleteTextView = findViewById(R.id.tecnicoNombreNewManteniAutoCompleteTextView);
        listTecnicos = getListTecnicos();
        ArrayAdapter<String> tecnicoAdapter = new ArrayAdapter<>(
                FormNewMantenimientoActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                listTecnicos);
        tecnicoNombreNewManteniAutoCompleteTextView.setAdapter(tecnicoAdapter);

        puestaMarchaNewManteniCheckBox = findViewById(R.id.puestaMarchaNewManteniCheckBox);
        segunDinNewManteniCheckBox = findViewById(R.id.segunDinNewManteniCheckBox);
        segunEnNewManteniCheckBox = findViewById(R.id.segunEnNewManteniCheckBox);

        listCualitativos = getCualitativos();
        ArrayAdapter<String> cualitativoAdapter = new ArrayAdapter<>(
                FormNewMantenimientoActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                listCualitativos);
        evaluControlDigitalSpinner = findViewById(R.id.evaluControlDigitalSpinner);
        evaluControlDigitalSpinner.setAdapter(cualitativoAdapter);
        evaluSistemaExtraccionSpinner = findViewById(R.id.evaluSistemaExtraccionSpinner);
        evaluSistemaExtraccionSpinner.setAdapter(cualitativoAdapter);
        evaluProteccionSuperficieSpinner = findViewById(R.id.evaluProteccionSuperficieSpinner);
        evaluProteccionSuperficieSpinner.setAdapter(cualitativoAdapter);
        evaluJuntasSpinner = findViewById(R.id.evaluJuntasSpinner);
        evaluJuntasSpinner.setAdapter(cualitativoAdapter);
        evaluFijacionPiezasRigidasSpnner = findViewById(R.id.evaluFijacionPiezasRigidasSpinner);
        evaluFijacionPiezasRigidasSpnner.setAdapter(cualitativoAdapter);
        evaluFuncionamientoGuilloSpinner = findViewById(R.id.evaluFuncionamientoGuilloSpinner);
        evaluFuncionamientoGuilloSpinner.setAdapter(cualitativoAdapter);
        evaluEstadoGeneralGuilloSpinner = findViewById(R.id.evaluEstadoGeneralGuilloSpinner);
        evaluEstadoGeneralGuilloSpinner.setAdapter(cualitativoAdapter);
        valorFuerzaDesplazaGuilloNewManteniEditText = findViewById(R.id.valorFuerzaDesplazaGuilloNewManteniEditText);
        evaluFuerzaDesplazaGuilloSpinner = findViewById(R.id.evaluFuerzaDesplazaGuilloSpinner);
        evaluFuerzaDesplazaGuilloSpinner.setAdapter(cualitativoAdapter);
        evaluControlPresenciaSpinner = findViewById(R.id.evaluControlPresenciaSpinner);
        evaluControlPresenciaSpinner.setAdapter(cualitativoAdapter);
        evaluAutoproteccionSpinner = findViewById(R.id.evaluAutoproteccionSpinner);
        evaluAutoproteccionSpinner.setAdapter(cualitativoAdapter);
        evaluFuncGrifosMonoredSpinner = findViewById(R.id.evaluFuncGrifosMonoredSpinner);

        valorVolumenExtraccionRealNewManteniEditText = findViewById(R.id.valorVolumenExtraccionRealNewManteniEditText);
        evaluVolumenExtraccionRealSpinner = findViewById(R.id.evaluVolumenExtraccionRealSpinner);
        evaluVolumenExtraccionRealSpinner.setAdapter(cualitativoAdapter);

        evaluFuncGrifosMonoredSpinner.setAdapter(cualitativoAdapter);
        acuerdoNormasReguSiNewManteniRadioButton = findViewById(R.id.acuerdoNormasReguSiNewManteniRadioButton);
        acuerdoNormasReguNoNewManteniRadioButton = findViewById(R.id.acuerdoNormasReguNoNewManteniRadioButton);
        necesarioRepaSiNewManteniRadioButton = findViewById(R.id.necesarioRepaSiNewManteniRadioButton);
        necesarioRepaNoNewManteniRadioButton = findViewById(R.id.necesarioRepaNoNewManteniRadioButton);
        comentarioNewManteniEditText = findViewById(R.id.comentarioNewManteniEditText);

        newMantenimientoAcceptButton = findViewById(R.id.newMantenimientoAcceptButton);
        newMantenimientoCancelButton = findViewById(R.id.newMantenimientoCancelButton);

        //comporobar si la tarea es de actualizar
        if (getIntent().hasExtra("TASK")) {
            isUpdateTask = getIntent().getStringExtra("TASK").equalsIgnoreCase("UPDATE");
        } else {
            isUpdateTask = false;
        }
        //Poblar con los datos del mantenimiento a actualizar
        if (isUpdateTask) {
           if (idMantenimiento > 0) {
                mantenimientoOld = datos.selectMantenimientoWithId(idMantenimiento);
            }
            if (mantenimientoOld != null) {
                fechaNewMantenimientoEditText.setText(mantenimientoOld.getFecha());
                tecnicoNombreNewManteniAutoCompleteTextView.setText(
                        datos.getNombreTecnicoWithId(mantenimientoOld.getIdTecnico()));
                puestaMarchaNewManteniCheckBox.setChecked(mantenimientoOld.isPuestaMarcha());
                segunDinNewManteniCheckBox.setChecked(mantenimientoOld.isSegunDin());
                segunEnNewManteniCheckBox.setChecked(mantenimientoOld.isSegunEn());

                evaluControlDigitalSpinner.setSelection(mantenimientoOld.getFunCtrlDigi()-1);
                evaluSistemaExtraccionSpinner.setSelection(mantenimientoOld.getVisSistExtr()-1);
                evaluProteccionSuperficieSpinner.setSelection(mantenimientoOld.getProtSuperf()-1);
                evaluJuntasSpinner.setSelection(mantenimientoOld.getJuntas()-1);
                evaluFijacionPiezasRigidasSpnner.setSelection(mantenimientoOld.getFijacion()-1);
                evaluFuncionamientoGuilloSpinner.setSelection(mantenimientoOld.getFuncGuillo()-1);
                evaluEstadoGeneralGuilloSpinner.setSelection(mantenimientoOld.getEstadoGuillo()-1);
                valorFuerzaDesplazaGuilloNewManteniEditText.setText(
                        String.format(
                                Locale.getDefault(),"%.2f", mantenimientoOld.getValFuerzaGuillo()));

                evaluFuerzaDesplazaGuilloSpinner.setSelection(mantenimientoOld.getFuerzaGuillo()-1);
                evaluControlPresenciaSpinner.setSelection(mantenimientoOld.getCtrlPresencia()-1);
                evaluAutoproteccionSpinner.setSelection(mantenimientoOld.getAutoproteccion()-1);
                evaluFuncGrifosMonoredSpinner.setSelection(mantenimientoOld.getGrifosMonored()-1);

                Vitrina vitrinaOld = datos.selectVitrinaWithId(idVitrina);
                Longitud longitudOld = datos.selectLongitudWithId(vitrinaOld.getIdLongitud());
                idMedicion = mantenimientoOld.getIdMedicion();
                valorVolumenExtraccionRealNewManteniEditText.setText(
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                datos.getVolumenExtraccionReal(
                                        idMedicion,
                                        longitudOld.getLogintudGillotina())));
                evaluVolumenExtraccionRealSpinner.setSelection(mantenimientoOld.getEvaluVolExtrac()-1);
                acuerdoNormasReguSiNewManteniRadioButton.setChecked(mantenimientoOld.isAcordeNormasReguSi());
                acuerdoNormasReguNoNewManteniRadioButton.setChecked(mantenimientoOld.isAcordeNormasReguNo());
                necesarioRepaSiNewManteniRadioButton.setChecked(mantenimientoOld.isNecesarioRepaSi());
                necesarioRepaNoNewManteniRadioButton.setChecked(mantenimientoOld.isNecesarioRepaNo());
                comentarioNewManteniEditText.setText(mantenimientoOld.getComentario());
            }
        }

    }

    private List<String> getCualitativos() {
        List<String> listCualitativos = new ArrayList<>();
        for (Cualitativo cualitativo: datos.selectCualitativos()) {
            listCualitativos.add(cualitativo.getCualitativo());
        }
        return listCualitativos;
    }

    private List<String> getListTecnicos() {
        List<String> listTecnicos = new ArrayList<>();
        for (Tecnico tecnico: datos.selectTecnicos()) {
            listTecnicos.add(tecnico.getTecnicoNombre());
        }
        return listTecnicos;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}