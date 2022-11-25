package net.iessanclemente.a19lazaropm.aservice.ui.secondary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseContract;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Contacto;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Empresa;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Longitud;
import net.iessanclemente.a19lazaropm.aservice.database.dto.TipoVitrina;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Vitrina;
import net.iessanclemente.a19lazaropm.aservice.adapters.ElementListVitrinas;
import net.iessanclemente.a19lazaropm.aservice.adapters.ListVitrinasAdapter;
import net.iessanclemente.a19lazaropm.aservice.ui.forms.FormNewVitrinaActivity;

import java.util.ArrayList;
import java.util.List;

public class FichaEmpresaActivity extends AppCompatActivity {

    private TextView fichaEmpNombreTextView;
    private TextView fichaEmpDireccTextView;
    private TextView fichaEmpNomContactTextView;
    private TextView fichaEmpTelefContactTextView;
    private TextView fichaEmpCorreoContactTextView;
    private ImageView addVitrinaImageView;

    private static final int ADD_NEW_VITRINA_REQUEST_CODE = 2;
    private static final int RESULT_ADD_PROBLEM = 666;

    private List<ElementListVitrinas> listElementsVitrinas;

    DataBaseOperations datos = DataBaseOperations.getInstance(FichaEmpresaActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_empresa);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        eventHandler();
    }

    private void eventHandler() {
        addVitrinaImageView.setOnClickListener(v -> {
            Intent intent = new Intent(
                FichaEmpresaActivity.this, FormNewVitrinaActivity.class);
            intent.putExtra("NOMBRE_EMPRESA", fichaEmpNombreTextView.getText().toString());
            activityResultLauncher.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null && result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().hasExtra("FABRICANTE")) {
                            Toast.makeText(
                                    FichaEmpresaActivity.this,
                                    "Adicionada: " + result.getData().getStringExtra("FABRICANTE"),
                                    Toast.LENGTH_SHORT).show();
                        }
                        init();
                    } else if (result != null && result.getResultCode() == RESULT_CANCELED) {
                        if (result.getData() != null && result.getData().hasExtra("FABRICANTE")) {
                            String message;
                            if (result.getData().hasExtra("UPDATE")) {
                                message = "Se canceló al actualización de la vitrina " + result.getData().getStringExtra("FABRICANTE");
                            } else {
                                message = "Se canceló la adición de la vitrina " + result.getData().getStringExtra("FABRICANTE");
                            }
                            Toast.makeText(
                                    FichaEmpresaActivity.this, message, Toast.LENGTH_SHORT
                            ).show();
                        }

                    } else if (result != null && result.getResultCode() == RESULT_ADD_PROBLEM) {
                        if (result.getData() != null && result.getData().hasExtra("FABRICANTE")) {
                            if (result.getData() != null && result.getData().hasExtra("FABRICANTE")) {
                                String message;
                                if (result.getData().hasExtra("UPDATE")) {
                                    message = "No se pudo actualizar la vitrina " + result.getData().getStringExtra("FABRICANTE");
                                } else {
                                    message = "No se pudo adicionar la vitrina " + result.getData().getStringExtra("FABRICANTE");
                                }
                            }
                            Toast.makeText(
                                    FichaEmpresaActivity.this,
                                    "No se pudo crear: " + result.getData().getStringExtra("FABRICANTE"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    public void init() {
        //recupero las views del layout
        fichaEmpNombreTextView = findViewById(R.id.fichaEmpNombreTextView);
        fichaEmpDireccTextView = findViewById(R.id.fichaEmpDireccTextView);
        fichaEmpNomContactTextView = findViewById(R.id.fichaEmpNomContactTextView);
        fichaEmpTelefContactTextView = findViewById(R.id.fichaEmpTelefContactTextView);
        fichaEmpCorreoContactTextView = findViewById(R.id.fichaEmpCorreoContactTextView);
        addVitrinaImageView = findViewById(R.id.addVitrinaImageView);

        //recupero nombre de la empresa de los extras del intent
        String nombreEmpresa = getIntent().getStringExtra("NOMBRE_EMPRESA");

        //Recupero de la base de datos la empresa en cuestión y de su contacto
        Empresa empresa = datos.selectEmpresaWithName(nombreEmpresa);
        Contacto contacto = datos.selectContactoWithId(empresa.getIdContacto());

        //Cubro los datos de la empresa y de su contacto
        fichaEmpNombreTextView.setText(empresa.getEmpresaNombre());
        fichaEmpDireccTextView.setText(empresa.getEmpresaDirecc());
        fichaEmpNomContactTextView.setText(contacto.getContactoNombre());
        fichaEmpTelefContactTextView.setText(contacto.getContactoTelef());
        fichaEmpCorreoContactTextView.setText(contacto.getContactoCorreo());

        //Instancio lista vacía donde almacenaré los ElementListVitrinas
        listElementsVitrinas = new ArrayList<>();
        //pueblo la listElementsVitrinas para el adpatador del listVitrinasRecyclerView
        //seleccionando solo las vitrinas de la empresa
        String sqlSelectVitrinas = String.format("SELECT * FROM %s WHERE %s=%d",
                DataBaseContract.VitrinasTable.TABLE_NAME,
                DataBaseContract.VitrinasTable.COL_ID_EMPRESA,
                empresa.getId());

        for (Vitrina vitrina: datos.selectVitrinas(sqlSelectVitrinas)) {
            int idVitrina = vitrina.getId();

            //Recupero fabricante a partir de su idFabricante recuperado de vitrina
            String fabricante = datos.selectFabricanteWithId(vitrina.getIdFabricante()).getNombre();

            //Recupero la info sobre el tipo de vitrina a partir de su idTipo recuperado de vitrina
            TipoVitrina infoTipoVitrina = datos.selectTipoVitrinaWithId(vitrina.getIdTipo());
            String tipoVitrina = infoTipoVitrina.getTipoVitrina();

            //recupero la info sobre las logintudes a partir del idLongitud recuperado de vitrina
            Longitud longitud = datos.selectLongitudWithId(vitrina.getIdLongitud());
            int logitudVitrina = longitud.getLongitudVitrina();
            float longitudGuillotina = longitud.getLogintudGillotina();

            //obtengo la info restante directamente del objeto vitrina.
            String referenciaVitrina = vitrina.getVitrinaReferencia();
            String inventarioVitrina = vitrina.getVitrinaInventario();
            int anhoVitrina = vitrina.getVitrinaAnho();
            String contrato = vitrina.getVitrinaContrato();

            //creo el ElementListVitrinas y lo añado a la lista de vitrinas
            listElementsVitrinas.add(
                    new ElementListVitrinas(
                            nombreEmpresa, idVitrina, fabricante, tipoVitrina, logitudVitrina,
                            longitudGuillotina, referenciaVitrina, inventarioVitrina, anhoVitrina,
                            contrato));
        }
        //Instancio el adaptador con la lista de elementos vitrinas a mostrar
        ListVitrinasAdapter listVitrinasAdapter = new ListVitrinasAdapter(
                listElementsVitrinas, activityResultLauncher, FichaEmpresaActivity.this);
        //Instancio el RecyclerView, lo configuro y finalmente le asigno su adaptador.
        RecyclerView listVitrinasRecyclerView = findViewById(R.id.listVitrinasRecyclerView);
        listVitrinasRecyclerView.setHasFixedSize(true);
        listVitrinasRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listVitrinasRecyclerView.setAdapter(listVitrinasAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}