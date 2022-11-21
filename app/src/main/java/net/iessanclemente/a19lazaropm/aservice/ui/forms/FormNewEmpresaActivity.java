package net.iessanclemente.a19lazaropm.aservice.ui.forms;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Contacto;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Empresa;

public class FormNewEmpresaActivity extends AppCompatActivity {

    private static final int RESULT_ADD_PROBLEM = 666;

    private TextView newEmpNameEditText;
    private TextView newEmpDireccEditText;
    private EditText newEmpContactNameEditText;
    private EditText newEmpContactTelefEditText;
    private EditText newEmpContactCorreoEditText;
    private Button newEmpCancelButton;
    private Button newEmpAcceptButton;

    private boolean isUpdateTask;
    private Empresa empresaOld;

    private final DataBaseOperations datos = DataBaseOperations.getInstance(FormNewEmpresaActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_new_empresa);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        eventHandler();
    }

    private void init() {
        newEmpNameEditText = findViewById(R.id.newEmpNameEditText);
        newEmpDireccEditText = findViewById(R.id.newEmpDireccEditText);
        newEmpContactNameEditText = findViewById(R.id.newEmpContactNameEditText);
        newEmpContactTelefEditText = findViewById(R.id.newEmpContactTelefEditText);
        newEmpContactCorreoEditText = findViewById(R.id.newEmpContactCorreoEditText);
        newEmpCancelButton = findViewById(R.id.newEmpCancelButton);
        newEmpAcceptButton = findViewById(R.id.newEmpAcceptButton);
        if (getIntent().hasExtra("TASK")) {
            isUpdateTask = getIntent().getStringExtra("TASK").equalsIgnoreCase("UPDATE");
        } else {
            isUpdateTask = false;
        }
        //Poblar con los datos de la vitrina a actualizar
        if (isUpdateTask) {
            if (getIntent().hasExtra("NOMBRE_EMPRESA")){
                String nombreEmpOld = getIntent().getStringExtra("NOMBRE_EMPRESA");
                empresaOld = datos.selectEmpresaWithName(nombreEmpOld);
                if (empresaOld != null) {
                    newEmpNameEditText.setText(empresaOld.getEmpresaNombre());
                    newEmpDireccEditText.setText(empresaOld.getEmpresaDirecc());
                    Contacto contactoOld = datos.selectContactoWithId(empresaOld.getIdContacto());
                    if (contactoOld != null) {
                        newEmpContactNameEditText.setText(contactoOld.getContactoNombre());
                        newEmpContactTelefEditText.setText(contactoOld.getContactoTelef());
                        newEmpContactCorreoEditText.setText(contactoOld.getContactoCorreo());
                    }
                }
            }
        }
    }

    private void eventHandler() {
        newEmpCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult("", RESULT_CANCELED);
            }
        });

        newEmpAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = addNewEmp();
                if (isAdded) {
                    sendResult(newEmpNameEditText.getText().toString(), RESULT_OK);
                } else {
                    sendResult("", RESULT_ADD_PROBLEM);
                }

            }
        });
    }

    private void sendResult(String nameEmp, int result) {
        Intent intent = new Intent();
        intent.putExtra("NOMBRE_EMP", nameEmp);
        setResult(result, intent);
        finish();
    }

    private boolean addNewEmp() {
        boolean isOk = false;
        String contactoNombre = newEmpContactNameEditText.getText().toString();
        String contactoTelef = newEmpContactTelefEditText.getText().toString();
        String contactoCorreo = newEmpContactCorreoEditText.getText().toString();
        int idContacto = -1;
        Contacto contactoNewEmp = new Contacto(
                idContacto, contactoNombre, contactoTelef, contactoCorreo);

        if (isUpdateTask) {
            contactoNewEmp.setId(empresaOld.getIdContacto());
            if (datos.updateContacto(contactoNewEmp)) {
                Empresa newEmp = new Empresa(
                        empresaOld.getId(),
                        newEmpNameEditText.getText().toString(),
                        newEmpDireccEditText.getText().toString(),
                        contactoNewEmp.getId());
                if (datos.updateEmpresa(newEmp)) {
                    isOk = true;
                } else {
                    Toast.makeText(
                            FormNewEmpresaActivity.this,
                            "No se pudo actualizar la empresa",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (!datos.existContacto(contactoNewEmp)) {
                idContacto = (int) datos.insertContacto(contactoNewEmp);
            } else {
                idContacto = datos.getIdOfContactoWhithName(contactoNewEmp.getContactoNombre());
            }
            if (idContacto > 0) {
                Empresa newEmp = new Empresa(
                        -1,
                        newEmpNameEditText.getText().toString(),
                        newEmpDireccEditText.getText().toString(),
                        idContacto);
                if (!datos.existEmpresa(newEmp)) {
                    long idNewEmp = datos.insertEmpresa(newEmp);
                    if (idNewEmp > 0) {
                        isOk = true;
                    }
                } else {
                    Toast.makeText(
                            FormNewEmpresaActivity.this,
                            "Empresa ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return isOk;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}