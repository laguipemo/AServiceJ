package net.iessanclemente.a19lazaropm.aservice.ui.secondary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Mantenimiento;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Vitrina;

import java.util.Locale;

public class FichaMantenimientoActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView fichaManteniFechaMantenimiTextView;
    private TextView fichaManteniTecnicoNombreTextView;
    private CheckBox fichaManteniPuestaMarchaCheckBox;
    private CheckBox fichaManteniSegunDinCheckBox;
    private CheckBox fichaManteniSegunEnCheckBox;
    private TextView fichaManteniEvaluControlDigitalTextView;
    private TextView fichaManteniEvaluSistemaExtraccionTextView;
    private TextView fichaManteniEvaluProteccionSuperficieTextView;
    private TextView fichaManteniEvaluJuntasTextView;
    private TextView fichaManteniEvaluFijacionPiezasRigidasTextView;
    private TextView fichaManteniEvaluFuncionamientoGuilloTextView;
    private TextView fichaManteniEvaluEstadoGeneralGuilloTextView;
    private TextView fichaManteniValorFuerzaDesplazaGuilloTextView;
    private TextView fichaManteniEvaluFuerzaDesplazaGuilloTextView;
    private TextView fichaManteniEvaluControlPresenciaTextView;
    private TextView fichaManteniEvaluAutoproteccionTextView;
    private TextView fichaManteniEvaluFuncGrifosMonoredTextView;
    private TextView fichaManteniValorVolumenExtraccionRealTextView;
    private TextView fichaManteniEvaluVolumExtraccionRealTextView;
    private RadioGroup fichaManteniAcuerdoNormasReguRadioGroup;
    private RadioButton fichaManteniAcuerdoNormasReguSiRadioButton;
    private RadioButton fichaManteniAcuerdoNormasReguNoRadioButton;
    private RadioGroup fichaManteniNecesarioReparcionRadioGroup;
    private RadioButton fichaManteniNecesarioRepaSiRadioButton;
    private RadioButton fichaManteniNecesarioRepaNoRadioButton;
    private TextView fichaManteniComentarioTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_mantenimiento);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        eventHandler();
    }

    private void eventHandler() {


    }

    private void init() {
        fichaManteniFechaMantenimiTextView = findViewById(R.id.fichaManteniFechaMantenimiTextView);
        fichaManteniTecnicoNombreTextView = findViewById(R.id.fichaManteniTecnicoNombreTextView);
        fichaManteniPuestaMarchaCheckBox = findViewById(R.id.fichaManteniPuestaMarchaCheckBox);
        fichaManteniSegunDinCheckBox = findViewById(R.id.fichaManteniSegunDinCheckBox);
        fichaManteniSegunEnCheckBox = findViewById(R.id.fichaManteniSegunEnCheckBox);
        fichaManteniEvaluControlDigitalTextView = findViewById(R.id.fichaManteniEvaluControlDigitalTextView);
        fichaManteniEvaluSistemaExtraccionTextView = findViewById(R.id.fichaManteniEvaluSistemaExtraccionTextView);
        fichaManteniEvaluProteccionSuperficieTextView = findViewById(R.id.fichaManteniEvaluProteccionSuperficieTextView);
        fichaManteniEvaluJuntasTextView = findViewById(R.id.fichaManteniEvaluJuntasTextView);
        fichaManteniEvaluFijacionPiezasRigidasTextView = findViewById(R.id.fichaManteniEvaluFijacionPiezasRigidasTextView);
        fichaManteniEvaluFuncionamientoGuilloTextView = findViewById(R.id.fichaManteniEvaluFuncionamientoGuilloTextView);
        fichaManteniEvaluEstadoGeneralGuilloTextView = findViewById(R.id.fichaManteniEvaluEstadoGeneralGuilloTextView);
        fichaManteniValorFuerzaDesplazaGuilloTextView = findViewById(R.id.fichaManteniValorFuerzaDesplazaGuilloTextView);
        fichaManteniEvaluFuerzaDesplazaGuilloTextView = findViewById(R.id.fichaManteniEvaluFuerzaDesplazaGuilloTextView);
        fichaManteniEvaluControlPresenciaTextView = findViewById(R.id.fichaManteniEvaluControlPresenciaTextView);
        fichaManteniEvaluAutoproteccionTextView = findViewById(R.id.fichaManteniEvaluAutoproteccionTextView);
        fichaManteniEvaluFuncGrifosMonoredTextView = findViewById(R.id.fichaManteniEvaluFuncGrifosMonoredTextView);
        fichaManteniValorVolumenExtraccionRealTextView = findViewById(R.id.fichaManteniValorVolumenExtraccionRealTextView);
        fichaManteniEvaluVolumExtraccionRealTextView = findViewById(R.id.fichaManteniEvaluVolExtraccionTextView);
        fichaManteniAcuerdoNormasReguRadioGroup = findViewById(R.id.fichaManteniAcuerdoNormasReguRadioGroup);
        fichaManteniAcuerdoNormasReguSiRadioButton = findViewById(R.id.fichaManteniAcuerdoNormasReguSiRadioButton);
        fichaManteniAcuerdoNormasReguNoRadioButton = findViewById(R.id.fichaManteniAcuerdoNormasReguNoRadioButton);
        fichaManteniNecesarioReparcionRadioGroup = findViewById(R.id.fichaManteniNecesarioReparcionRadioGroup);
        fichaManteniNecesarioRepaSiRadioButton = findViewById(R.id.fichaManteniNecesarioRepaSiRadioButton);
        fichaManteniNecesarioRepaNoRadioButton = findViewById(R.id.fichaManteniNecesarioRepaNoRadioButton);
        fichaManteniComentarioTextView = findViewById(R.id.fichaManteniComentarioTextView);

        //recupero datos del intent que condujo hasta esta activity
        String nombreEmpresa = getIntent().getStringExtra("NOMBRE_EMPRESA");
        int idVitrina = getIntent().getIntExtra("ID_VITRINA", -1);
        int idMantenimiento = getIntent().getIntExtra("ID_MANTENIMIENTO", -1);

        //Creo instancia de la base de dato pasando como contexto esta actividad
        DataBaseOperations datos = DataBaseOperations.getInstance(FichaMantenimientoActivity.this);
        //recupero vitrina para tener algunso datos necesarios
        Vitrina vitrina = datos.selectVitrinaWithId(idVitrina);
        //recupero el mantenimiento en cuestión desde la base de datos
        if (idMantenimiento > 0) {
            Mantenimiento mantenimiento = datos.selectMantenimientoWithId(idMantenimiento);
            //comienzo a mostrar los datos en sus respectivas views
            fichaManteniFechaMantenimiTextView.setText(mantenimiento.getFecha());
            fichaManteniTecnicoNombreTextView.setText(
                    datos.getNombreTecnicoWithId(mantenimiento.getIdTecnico()));
            fichaManteniPuestaMarchaCheckBox.setChecked(mantenimiento.isPuestaMarcha());
            fichaManteniSegunDinCheckBox.setChecked(mantenimiento.isSegunDin());
            fichaManteniSegunEnCheckBox.setChecked(mantenimiento.isSegunEn());
            fichaManteniEvaluControlDigitalTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getFunCtrlDigi()));
            fichaManteniEvaluSistemaExtraccionTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getVisSistExtr()));
            fichaManteniEvaluProteccionSuperficieTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getProtSuperf()));
            fichaManteniEvaluJuntasTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getJuntas()));
            fichaManteniEvaluFijacionPiezasRigidasTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getFijacion()));
            fichaManteniEvaluFuncionamientoGuilloTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getFuncGuillo()));
            fichaManteniEvaluEstadoGeneralGuilloTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getEstadoGuillo()));
            fichaManteniValorFuerzaDesplazaGuilloTextView.setText(
                    String.valueOf(mantenimiento.getValFuerzaGuillo()));
            fichaManteniEvaluFuerzaDesplazaGuilloTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getFuerzaGuillo()));
            fichaManteniEvaluControlPresenciaTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getCtrlPresencia()));
            fichaManteniEvaluAutoproteccionTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getAutoproteccion()));
            fichaManteniEvaluFuncGrifosMonoredTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getGrifosMonored()));
            fichaManteniValorVolumenExtraccionRealTextView.setText(
                    String.format(Locale.getDefault(),
                            "%.2f",
                            datos.getVolumenExtraccionReal(
                                    mantenimiento.getIdMedicion(),
                                    datos.getGuillotinaWithIdLongitud(vitrina.getIdLongitud()))));
            fichaManteniEvaluVolumExtraccionRealTextView.setText(
                    datos.getCualitativoWithId(mantenimiento.getEvaluVolExtrac()));
            fichaManteniAcuerdoNormasReguSiRadioButton.setChecked(mantenimiento.isAcordeNormasReguSi());
            fichaManteniAcuerdoNormasReguNoRadioButton.setChecked(mantenimiento.isAcordeNormasReguNo());
            fichaManteniNecesarioRepaSiRadioButton.setChecked(mantenimiento.isNecesarioRepaSi());
            fichaManteniNecesarioRepaNoRadioButton.setChecked(mantenimiento.isNecesarioRepaNo());
            fichaManteniComentarioTextView.setText(mantenimiento.getComentario());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}