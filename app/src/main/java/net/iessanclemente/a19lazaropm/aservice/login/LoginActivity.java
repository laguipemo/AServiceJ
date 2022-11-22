package net.iessanclemente.a19lazaropm.aservice.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.ui.main.ListEmpresasActivity;
import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.security.SecurityCipherWithKey;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Tecnico;

public class LoginActivity extends AppCompatActivity {

    private final String KEY_ATLAS_ROMERO = "AtlasRomero";
    private DataBaseOperations datos;
    private TextInputLayout userLoginTextInputLayout, passwordLoginTextInputLayout;
    private TextInputEditText userLoginTextInputEditText, passwordLoginTextInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handlerEvent();
    }

    private void handlerEvent() {

        userLoginTextInputEditText = findViewById(R.id.userLoginTextInputEditText);
        passwordLoginTextInputEditText = findViewById(R.id.passwordLoginTextInputEditText);
        TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        Button accessButton = findViewById(R.id.accessButton);

        forgotPasswordTextView.setOnClickListener(v -> showForgotPassDialog());

        accessButton.setOnClickListener(v -> {
            // Recupero los datos introducidos por el usuario
            String usuario = userLoginTextInputEditText.getText().toString().trim();
            String password = passwordLoginTextInputEditText.getText().toString().trim();
            // Encript la clave introducido por el usuario para compararla con la base de datos
            SecurityCipherWithKey security = new SecurityCipherWithKey();
            security.addKey(KEY_ATLAS_ROMERO);
            String passwordEncrypted = security.encrypt(password);

            //Recupero, desde la base de datos la clave encriptada para el usuario introducido
            DataBaseOperations datos = DataBaseOperations.getInstance(LoginActivity.this);

            //Comprobar si el usuario introducido es un ténico y de ser así comprueba la clave
            //son correctos los datos, se sigue con la app
            Tecnico tecnico = datos.selectTecnicoWithUsuario(usuario);
            if (tecnico == null){
                if (usuario.isEmpty()) {
                    showToast(getString(R.string.user_does_not_exist));
                } else {
                    showToast(getString(R.string.user_does_not_exist));
                }
                clearAll();
            } else {
                String passwordEncryptedValido = tecnico.getTecnicoClave();
                if (passwordEncryptedValido == null || passwordEncryptedValido.isEmpty()) {
                    showToast(getString(R.string.incorrect_password));
                } else if (passwordEncrypted.trim().equals(passwordEncryptedValido.trim())) {
                    Intent intent = new Intent(
                            LoginActivity.this, ListEmpresasActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(getString(R.string.incorrect_user_or_password));
                    clearAll();
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearAll() {
        passwordLoginTextInputEditText.setText("");
        userLoginTextInputEditText.setText("");
    }

    private void showForgotPassDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this, R.style.CustomAlertDialog);
        //Creo vista parsonalizada inflando el diálogo xml creado
        View customDialog = getLayoutInflater().inflate(
                R.layout.alert_dialog_forgot_password, null);
        //Recupero las vistas con los datos
        EditText usuario = customDialog.findViewById(R.id.forgotPassUserEditText);
        EditText nombreCompleto = customDialog.findViewById(R.id.forgotPassFullNameEditText);
        EditText correo = customDialog.findViewById(R.id.forgotPassEmailEditText);

        alertDialogBuilder.setView(customDialog);
        alertDialogBuilder.setPositiveButton(
                R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String[] addresses = {"laguipemo@me.com"};
                        final String subject = getString(R.string.email_subject_forgot_password);
                        if (usuario.getText().toString().isEmpty()) {
                            showToast(getString(R.string.null_user));
                        } else if (correo.getText().toString().isEmpty()) {
                            showToast(getString(R.string.email_required));
                        } else {
                            String textoEmail = getString(R.string.email_text_forgot_password,
                                    usuario.getText().toString(),
                                    nombreCompleto.getText().toString(),
                                    correo.getText().toString());
                            sendEmail(addresses, subject, textoEmail);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast(getString(R.string.email_canceled));
                        dialog.cancel();
                    }
                });
        AlertDialog forgotPassDialog = alertDialogBuilder.create();
        forgotPassDialog.show();
    }

    private void sendEmail(String[] addresses, String subject, String textoEmail) {
        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
        intentEmail.setData(Uri.parse("mailto:"));
        intentEmail.putExtra(Intent.EXTRA_EMAIL, addresses);
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentEmail.putExtra(Intent.EXTRA_TEXT, textoEmail);
        if (intentEmail.resolveActivity(LoginActivity.this.getPackageManager()) != null) {
            LoginActivity.this.startActivity(intentEmail);
        }
    }


}