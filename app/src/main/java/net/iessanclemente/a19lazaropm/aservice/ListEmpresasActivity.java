package net.iessanclemente.a19lazaropm.aservice;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.iessanclemente.a19lazaropm.aservice.models.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.models.dto.Empresa;
import net.iessanclemente.a19lazaropm.aservice.adapters.ElementListEmpresas;
import net.iessanclemente.a19lazaropm.aservice.adapters.ListEmpresasAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListEmpresasActivity extends AppCompatActivity {

    private List<ElementListEmpresas> listElementsEmpresas;

    private ImageView addEmpImageView;
    private final static int ADD_NEW_EMP_REQUEST_CODE = 1;
    private static final int RESULT_ADD_PROBLEM = 666;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_empresas);

        init();
        eventHandler();
    }

    private void eventHandler() {
        addEmpImageView = findViewById(R.id.addEmpImageView);
        addEmpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        ListEmpresasActivity.this, FormNewEmpresaActivity.class);
                intent.putExtra("TASK", "NEW");
                activityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData().hasExtra("NOMBRE_EMP")) {
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
                if (result.getData().hasExtra("NOMBRE_EMP")) {
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
        DataBaseOperations datos = DataBaseOperations.getInstance(ListEmpresasActivity.this);
        List<Empresa> listEmpresas = datos.selectEmpresas();

        for (Empresa empresa : datos.selectEmpresas()) {
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

}
