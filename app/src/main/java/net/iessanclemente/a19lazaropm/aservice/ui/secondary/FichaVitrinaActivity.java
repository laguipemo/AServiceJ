package net.iessanclemente.a19lazaropm.aservice.ui.secondary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseContract;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Mantenimiento;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Vitrina;
import net.iessanclemente.a19lazaropm.aservice.adapters.ElementListMantenimientos;
import net.iessanclemente.a19lazaropm.aservice.adapters.ListMantenimientosAdapter;
import net.iessanclemente.a19lazaropm.aservice.ui.forms.FormNewMantenimientoActivity;

import java.util.ArrayList;
import java.util.List;

public class FichaVitrinaActivity extends AppCompatActivity {

    private static final int ADD_NEW_MANTENIMIENTO_REQUEST_CODE = 3;
    private static final int RESULT_ADD_PROBLEM = 666;

    private TextView fichaVitrinaEmpNombreTextView;
    private TextView fichaVitrinaFabricanteTextView;
    private TextView fichaVitrinasReferenciaTextView;
    private TextView fichaVitrinaInventarioTextView;
    private TextView fichaVitrinaLongitudTextView;
    private TextView fichaVitrinaGuillotinaTextView;
    private TextView fichaVitrinaAnhoTextView;
    private TextView fichaVitrinaContratoTextView;
    private ImageView addMantenimientoImageView;

    private List<ElementListMantenimientos> listElementsMantenimientos;

    ProgressBar progressBar;

    private String nombreEmpresa;
    private int idVitrina;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null && result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().hasExtra("MANTENIMIENTO")) {
                            Toast.makeText(
                                    FichaVitrinaActivity.this,
                                    "Adicionado mantenimiento fecha: " + result.getData().getStringExtra("MANTENIMIENTO"),
                                    Toast.LENGTH_SHORT).show();
                        }
                        init();
                    } else if (result != null && result.getResultCode() == RESULT_CANCELED) {
                        if (result.getData() != null && result.getData().hasExtra("MANTENIMIENTO")) {
                            String message;
                            if (result.getData().hasExtra("UPDATE")) {
                                message = "Se canceló la actualización del mantenimiento";
                            } else {
                                message = "Se canceló la adición del nuevo mantenimiento";
                            }
                           Toast.makeText(
                                FichaVitrinaActivity.this, message, Toast.LENGTH_SHORT
                           ).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    } else if (result != null && result.getResultCode() == RESULT_ADD_PROBLEM) {
                        if (result.getData() != null && result.getData().hasExtra("MANTENIMIENTO")) {
                            String message;
                            if (result.getData().hasExtra("UPDATE")) {
                                message = "No se pudo actualizar el mantenimiento";
                            } else {
                                message = "No se pudo crear el nuevo mantenimiento";
                            }
                            Toast.makeText(
                                    FichaVitrinaActivity.this, message, Toast.LENGTH_SHORT
                            ).show();
                            Toast.makeText(
                                    FichaVitrinaActivity.this, message, Toast.LENGTH_SHORT
                            ).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_vitrina);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        eventHandler();
    }

    private void eventHandler() {
        addMantenimientoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        FichaVitrinaActivity.this, FormNewMantenimientoActivity.class);
                intent.putExtra("NOMBRE_EMPRESA", nombreEmpresa);
                intent.putExtra("ID_VITRINA", idVitrina);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void init() {
        progressBar = findViewById(R.id.progress_circular);
        //recupero las views del layout
        fichaVitrinaEmpNombreTextView = findViewById(R.id.fichaVitrinaEmpNombreTextView);
        fichaVitrinaFabricanteTextView = findViewById(R.id.fichaVitrinaFabricanteTextView);
        fichaVitrinasReferenciaTextView = findViewById(R.id.fichaVitrinaReferenceTextView);
        fichaVitrinaInventarioTextView = findViewById(R.id.fichaVitrinaInventarioTextView);
        fichaVitrinaLongitudTextView = findViewById(R.id.fichaVitrinaLongitudTextView);
        fichaVitrinaGuillotinaTextView = findViewById(R.id.fichaVitrinaGuillotinaTextView);
        fichaVitrinaAnhoTextView = findViewById(R.id.fichaVitrinaAnhoTextView);
        fichaVitrinaContratoTextView = findViewById(R.id.fichaVitrinaContratoTextView);
        addMantenimientoImageView = findViewById(R.id.addMantenimientoImageView);

        //recupero nombre de la empresa e id de la vitrina de los extras del intent
        nombreEmpresa = getIntent().getStringExtra("NOMBRE_EMPRESA");
        idVitrina = getIntent().getIntExtra("ID_VITRINA", -1);
        fichaVitrinaEmpNombreTextView.setText(nombreEmpresa);

        //creo instancia de la base de datos pasándo como contexto esta actividad
        DataBaseOperations datos = DataBaseOperations.getInstance(FichaVitrinaActivity.this);
        //Recupero de la base de datos la vitrina en cuestión
        if (idVitrina > 0) {
            Vitrina vitrina = datos.selectVitrinaWithId(idVitrina);
            //Cubro los datos de la vitrina que se muestra en la ficha
            fichaVitrinaFabricanteTextView.setText(
                    datos.getNameFabricanteWithIdFabricante(vitrina.getIdFabricante()));
            fichaVitrinasReferenciaTextView.setText(vitrina.getVitrinaReferencia());
            fichaVitrinaInventarioTextView.setText(vitrina.getVitrinaInventario());
            fichaVitrinaLongitudTextView.setText(
                    String.valueOf(datos.getLongitudVitrinaWithIdLongitud(vitrina.getIdLongitud())));
            fichaVitrinaGuillotinaTextView.setText(
                    String.valueOf(datos.getGuillotinaWithIdLongitud(vitrina.getIdLongitud())));
            fichaVitrinaAnhoTextView.setText(String.valueOf(vitrina.getVitrinaAnho()));
            fichaVitrinaContratoTextView.setText(vitrina.getVitrinaContrato());

            //Instancio lista vacía donde almacenaré los ElementosListMantenimientos
            listElementsMantenimientos = new ArrayList<>();
            //seleccionando solo las mantenimientos de la vitrina
            String sqlSelectMantenimientos = String.format("SELECT * FROM %s WHERE %s=%d",
                    DataBaseContract.MantenimientosTable.TABLE_NAME,
                    DataBaseContract.MantenimientosTable.COL_ID_VITRINA,
                    vitrina.getId());

            for (Mantenimiento mantenimiento: datos.selectMantenimientos(sqlSelectMantenimientos)) {
                //recupero cada uno de los parámetros necesarios para construir el ElementListMantenimientos
                int id = mantenimiento.getId();
                String fecha = mantenimiento.getFecha();
                float volumenExtraccion = datos.getVolumenExtraccionReal(
                        mantenimiento.getIdMedicion(),
                        datos.getGuillotinaWithIdLongitud(vitrina.getIdLongitud()));
                String comentario = mantenimiento.getComentario();

                //creo el ElementListMantenimientos y lo añado a la lista de mantenimientos
                listElementsMantenimientos.add(
                        new ElementListMantenimientos(
                                id, nombreEmpresa, idVitrina, fecha, volumenExtraccion, comentario));
            }
            //Instancio el adaptador con la lista de elementos Mantenimientos a mostrar
            ListMantenimientosAdapter listMantenimientosAdapter = new ListMantenimientosAdapter(
                    listElementsMantenimientos, activityResultLauncher, progressBar,FichaVitrinaActivity.this);
            //Instancio el RecyclerView, lo configuro y finalmente le asigno su adaptador.
            RecyclerView listMantenimintosRecyclerView = findViewById(R.id.listMantenimientosRecyclerView);
            listMantenimintosRecyclerView.setHasFixedSize(true);
            listMantenimintosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            listMantenimintosRecyclerView.setAdapter(listMantenimientosAdapter);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}