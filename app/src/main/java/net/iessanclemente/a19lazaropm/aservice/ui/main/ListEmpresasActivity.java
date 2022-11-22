package net.iessanclemente.a19lazaropm.aservice.ui.main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Empresa;
import net.iessanclemente.a19lazaropm.aservice.adapters.ElementListEmpresas;
import net.iessanclemente.a19lazaropm.aservice.adapters.ListEmpresasAdapter;
import net.iessanclemente.a19lazaropm.aservice.ui.forms.FormNewEmpresaActivity;

import java.util.ArrayList;
import java.util.List;

public class ListEmpresasActivity extends AppCompatActivity {

    private List<ElementListEmpresas> listElementsEmpresas;

    private ImageView addEmpImageView;
    private final static int ADD_NEW_EMP_REQUEST_CODE = 1;
    private static final int RESULT_ADD_PROBLEM = 666;

    DataBaseOperations datos = DataBaseOperations.getInstance(ListEmpresasActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_empresas);

        init();
        eventHandler();
    }

    private void eventHandler() {
        addEmpImageView = findViewById(R.id.addEmpImageView);
        addEmpImageView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ListEmpresasActivity.this, FormNewEmpresaActivity.class);
            intent.putExtra("TASK", "NEW");
            activityResultLauncher.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null && result.getData().hasExtra("NOMBRE_EMP")) {
                    Toast.makeText(
                            ListEmpresasActivity.this,
                            "Adicionada: " + result.getData().getStringExtra("NOMBRE_EMP"),
                            Toast.LENGTH_SHORT).show();
                }
                init();
            } else if (result != null && result.getResultCode() == RESULT_CANCELED) {
                Toast.makeText(
                        ListEmpresasActivity.this,
                        "Se canceló la adición de una empresa nueva", Toast.LENGTH_SHORT).show();
            } else if (result != null && result.getResultCode() == RESULT_ADD_PROBLEM) {
                if (result.getData() != null && result.getData().hasExtra("NOMBRE_EMP")) {
                    Toast.makeText(
                            ListEmpresasActivity.this,
                            "No se pudo crear: " + result.getData().getStringExtra("NOMBRE_EMP"),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    });


    public void init() {
        listElementsEmpresas = new ArrayList<>();
        List<Empresa> listEmpresas = datos.selectEmpresas();

        for (Empresa empresa : listEmpresas) {
            String nombreEmpresa = empresa.getEmpresaNombre();
            String direccEmpresa = empresa.getEmpresaDirecc();
            String nombreContacto = datos.selectContactoWithId(empresa.getIdContacto()).getContactoNombre();
            listElementsEmpresas.add(new ElementListEmpresas(nombreEmpresa, direccEmpresa, nombreContacto));
        }

        ListEmpresasAdapter listEmpresasAdapter = new ListEmpresasAdapter(
                listElementsEmpresas, activityResultLauncher, ListEmpresasActivity.this);
        RecyclerView listEmpresasRecycleView = findViewById(R.id.listEmpresasRecyclerView);
        listEmpresasRecycleView.setHasFixedSize(true);
        listEmpresasRecycleView.setLayoutManager(new LinearLayoutManager(this));
        listEmpresasRecycleView.setAdapter(listEmpresasAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != R.id.logout) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle(R.string.logout)
                .setIcon(R.drawable.ic_baseline_error_outline_24)
                .setMessage(R.string.message_logut)
                .setPositiveButton(R.string.accept, (dialog, which) -> {
                    finish();
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                }).create().show();
        return true;
    }
}
