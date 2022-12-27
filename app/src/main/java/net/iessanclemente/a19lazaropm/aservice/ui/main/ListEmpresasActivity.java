package net.iessanclemente.a19lazaropm.aservice.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.adapters.ElementListEmpresas;
import net.iessanclemente.a19lazaropm.aservice.adapters.ListEmpresasAdapter;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseContract;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Empresa;
import net.iessanclemente.a19lazaropm.aservice.ui.forms.FormNewEmpresaActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListEmpresasActivity extends AppCompatActivity {

    private List<ElementListEmpresas> listElementsEmpresas;

    private ImageView addEmpImageView;
    private final static int ADD_NEW_EMP_REQUEST_CODE = 1;
    private static final int RESULT_ADD_PROBLEM = 666;

    DataBaseOperations datos = DataBaseOperations.getInstance(ListEmpresasActivity.this);
    String databaseName = DataBaseContract.DATABASE_NAME.split("\\.")[0];

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
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
                        if (result.getData() != null && result.getData().hasExtra("NOMBRE_EMP")) {
                            String message;
                            if (result.getData().hasExtra("UPDATE")) {
                                message = "Se canceló la actualización de la nueva empresa";
                            } else {
                                message = "Se canceló la adición de la nueva empresa";
                            }
                            Toast.makeText(
                                    ListEmpresasActivity.this, message, Toast.LENGTH_SHORT
                            ).show();
                        }

                    } else if (result != null && result.getResultCode() == RESULT_ADD_PROBLEM) {
                        if (result.getData() != null && result.getData().hasExtra("NOMBRE_EMP")) {
                            String message;
                            if (result.getData().hasExtra("UPDATE")) {
                                message = "No se pudo actualizar la empresa " + result.getData().getStringExtra("NOMBRE_EMP");
                            } else {
                                message = "No se pudo crear la empresa " + result.getData().getStringExtra("NOMBRE_EMP");
                            }
                            Toast.makeText(
                                    ListEmpresasActivity.this, message, Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }
            });


    private final ActivityResultLauncher<String> exportRequestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted) {
                        // se cuenta con el permiso y por tanto se realiza la acción
                        createDocLauncher.launch(String.format("%s_backup.db", databaseName));
                    } else {
                        Toast.makeText(
                                ListEmpresasActivity.this,
                                "Es necesario tener permisos de escritura para exportar la base de datos",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> importRequestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted) {
                        openDocLauncher.launch(new String[]{"*/*"});
                    } else {
                        Toast.makeText(
                                ListEmpresasActivity.this,
                                "Es necesario tener permisos de lectura para importar la base de datos",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> createDocLauncher =
            registerForActivityResult(new ActivityResultContracts.CreateDocument("application/vnd.sqlite3"),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            if (uri != null) {
                                performExportation(uri);
                            }


                        }
                    });

    private final ActivityResultLauncher<String[]> openDocLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            if (uri != null) {
                                performImportation(uri);
                            }

                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_empresas);

        init();
        eventHandler();
    }

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

    private void eventHandler() {
        addEmpImageView = findViewById(R.id.addEmpImageView);
        addEmpImageView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ListEmpresasActivity.this, FormNewEmpresaActivity.class);
            intent.putExtra("TASK", "NEW");
            activityResultLauncher.launch(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    private void performExportation(Uri uri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            datos.backup();
        } else {
            // database path, source (input)=
            final String inFilePath = getDatabasePath(DataBaseContract.DATABASE_NAME).toString();
            File infile = new File(inFilePath);
            FileInputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(infile);
                outputStream = getContentResolver().openOutputStream(uri, "rwt");
                byte[] buffer = new byte[1024];
                int readed;
                while ((readed = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readed);
                    outputStream.flush();
                }
                Toast.makeText(
                        ListEmpresasActivity.this,
                        "Export Completed",
                        Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(
                        ListEmpresasActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("ListEmpresasActivity", Arrays.toString(e.getStackTrace()));
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void performImportation(Uri uri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            datos.importDB();
        } else {
            InputStream inputStream = null;
            final String outFilePath = getDatabasePath(DataBaseContract.DATABASE_NAME).toString();
            File outFile = new File(outFilePath);
            try {
                inputStream = getContentResolver().openInputStream(uri);
                OutputStream outputStream = new FileOutputStream(outFile, false);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                    outputStream.flush();
                }
                Toast.makeText(
                        ListEmpresasActivity.this,
                        "Import Completed",
                        Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(
                        ListEmpresasActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("ListEmpresasActivity", Arrays.toString(e.getStackTrace()));
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logout) {
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
        } else if (itemId == R.id.export_db) {
            if (ContextCompat.checkSelfPermission(
                    ListEmpresasActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // se cuenta con el permiso y por tanto se realiza la acción
                createDocLauncher.launch(String.format("%s_backup.db", databaseName));
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(
                        ListEmpresasActivity.this, R.style.CustomAlertDialog);
                builder.setTitle("Permiso rechazado")
                        .setIcon(R.drawable.ic_baseline_error_outline_24)
                        .setMessage("El permiso de escritura fue previamente rechazado por ud.\n" +
                                "Dicho permiso es necesario para realizar la exportación. \n" +
                                "Desea ir a los ajustes para concederlo")
                        .setPositiveButton(R.string.accept, (dialogInterface, wich) -> {
                            Toast.makeText(
                                    ListEmpresasActivity.this,
                                    "Enviar a ajustes",
                                    Toast.LENGTH_SHORT
                            ).show();
                        })
                        .setNegativeButton(R.string.cancel, (dialogInterface, wich) -> {
                            dialogInterface.dismiss();
                        }).create().show();
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                exportRequestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

        } else if (itemId == R.id.import_db) {
            if (ContextCompat.checkSelfPermission(
                    ListEmpresasActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // se cuenta con el permiso y por tanto se realiza la acción
                openDocLauncher.launch(new String[]{"*/*"});
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(
                        ListEmpresasActivity.this, R.style.CustomAlertDialog);
                builder.setTitle("Permiso rechazado")
                        .setIcon(R.drawable.ic_baseline_error_outline_24)
                        .setMessage("El permiso de lectura fue previamente rechazado por ud.\n" +
                                "Dicho permiso es necesario para realizar la importación. \n" +
                                "Desea ir a los ajustes para concederlo")
                        .setPositiveButton(R.string.accept, (dialogInterface, wich) -> {
                            Toast.makeText(
                                    ListEmpresasActivity.this,
                                    "Enviar a ajustes",
                                    Toast.LENGTH_SHORT
                            ).show();
                        })
                        .setNegativeButton(R.string.cancel, (dialogInterface, wich) -> {
                            dialogInterface.dismiss();
                        }).create().show();
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                importRequestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }
        return true;
    }

}
