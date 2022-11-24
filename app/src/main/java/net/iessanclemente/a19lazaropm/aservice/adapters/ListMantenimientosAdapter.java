package net.iessanclemente.a19lazaropm.aservice.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.net.MailTo;
import androidx.recyclerview.widget.RecyclerView;

import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Contacto;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Empresa;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Fabricante;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Mantenimiento;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Tecnico;
import net.iessanclemente.a19lazaropm.aservice.database.dto.TipoLongFlow;
import net.iessanclemente.a19lazaropm.aservice.database.dto.TipoVitrina;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Vitrina;
import net.iessanclemente.a19lazaropm.aservice.reports.TemplatePdf;
import net.iessanclemente.a19lazaropm.aservice.reports.ViewPdfActivity;
import net.iessanclemente.a19lazaropm.aservice.security.SecurityCipherWithKey;
import net.iessanclemente.a19lazaropm.aservice.ui.forms.FormNewMantenimientoActivity;
import net.iessanclemente.a19lazaropm.aservice.ui.secondary.FichaMantenimientoActivity;
import net.iessanclemente.a19lazaropm.aservice.utils.UtilsMenu;

import java.util.List;
import java.util.Locale;

public class ListMantenimientosAdapter extends RecyclerView.Adapter<ListMantenimientosAdapter.ViewHolder> {

    public static final String SUPERVISOR = "BBR/b9r6ZEsNFF7tdbo+/A==";
    private final String KEY_ATLAS_ROMERO = "AtlasRomero";

    private List<ElementListMantenimientos> listElementsMantenimientos;
    private LayoutInflater listElementsMantenimientosInflater;
    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private ProgressBar createReportProgressBar;
    private TemplatePdf templatePdf;

    public ListMantenimientosAdapter(
            List<ElementListMantenimientos> listElementsMantenimientos,
            ActivityResultLauncher<Intent> activityResultLauncher,
            ProgressBar progressBar,
            Context context
    ) {
        this.listElementsMantenimientosInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
        this.listElementsMantenimientos = listElementsMantenimientos;
        this.createReportProgressBar = progressBar;
        progressBar.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public ListMantenimientosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = listElementsMantenimientosInflater.inflate(
                R.layout.element_list_mantenimientos, parent, false);
        return new ListMantenimientosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMantenimientosAdapter.ViewHolder holder, int position) {
        holder.bindData(listElementsMantenimientos.get(position));

        //Manejo el evento onClick sobre el CardView del Mantenimiento
        holder.mantenimientoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                //paso a la actividad de la ficha de la vitrina
                Intent intent = new Intent(context, FichaMantenimientoActivity.class);
                intent.putExtra("NOMBRE_EMPRESA", listElementsMantenimientos.get(pos).getEmpresa());
                intent.putExtra("ID_VITRINA", listElementsMantenimientos.get(pos).getIdVitrina());
                intent.putExtra("ID_MANTENIMIENTO", listElementsMantenimientos.get(pos).getId());
                context.startActivity(intent);
            }
        });

        holder.mantenimientoCardView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            UtilsMenu.setForceShowIcon(popupMenu);
            popupMenu.inflate(R.menu.menu_contextual_extended);
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {
                int pos = holder.getAdapterPosition();
                DataBaseOperations datos = DataBaseOperations.getInstance(context);
                Mantenimiento mantenimiento = datos.selectMantenimientoWithId(
                        listElementsMantenimientos.get(pos).getId());

                if (mantenimiento != null) {
                    int idMantenimiento = mantenimiento.getId();

                    switch (item.getItemId()) {
                        case R.id.menuItemDelete:
                            deletMantenimiento(pos, datos, idMantenimiento);
                            break;
                        case R.id.menuItemUpdate:
                            showSupervisorPinDialog(pos);
                            break;
                        case R.id.menuItemShowReport:
                            createReportProgressBar.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    createReportPdf(
                                            pos, DataBaseOperations.getInstance(context), idMantenimiento);

                                    Intent intent = new Intent(context, ViewPdfActivity.class);
                                    intent.putExtra("PATH", templatePdf.getPdfFile().getAbsolutePath());
                                    activityResultLauncher.launch(intent);
                                }
                            }).start();
                            break;
                        case R.id.menuItemSendReport:
                            if (templatePdf != null) {
                                templatePdf.attachPdfAndSendMail();
                                break;
                            } else {
                                new Thread(new Runnable() {
                                    public void run() {
                                        createReportPdf(
                                                pos, DataBaseOperations.getInstance(context), idMantenimiento);
                                        Intent intent = new Intent("android.intent.action.SENDTO");
                                        intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
                                        intent.putExtra(
                                                "android.intent.extra.EMAIL",
                                                new String[]{
                                                        "sat@atlasromero.com",
                                                        "administracion@atlasromero.com",
                                                        "laguipemo@gmail.com"}
                                        );
                                        intent.putExtra(
                                                "android.intent.extra.SUBJECT",
                                                "Informe del mantenimiento"
                                        );
                                        intent.putExtra(
                                                "android.intent.extra.TEXT",
                                                "Le adjuntamos el informe del mantenimiento realizado a su vitrina"
                                        );
                                        intent.putExtra(
                                                "android.intent.extra.STREAM",
                                                Uri.fromFile(ListMantenimientosAdapter.this.templatePdf.getPdfFile()));
                                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                                            activityResultLauncher.launch(intent);
                                        }
                                    }
                                }).start();
                                createReportProgressBar.setVisibility(View.VISIBLE);
                                break;
                            }
                    }
                }
                return false;
            });
            return true;
        });
    }

    private void showSupervisorPinDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        View inflate = LayoutInflater.from(context).inflate(
                R.layout.alert_dialog_admin_permissions, null);
        final EditText editText = inflate.findViewById(R.id.supervisor_pin_editText);
        final SecurityCipherWithKey securityCipherWithKey = new SecurityCipherWithKey();
        securityCipherWithKey.addKey("AtlasRomero");
        builder.setView(inflate)
                .setTitle(R.string.title_pin)
                .setIcon(R.drawable.ic_baseline_error_outline_24)
                .setPositiveButton(R.string.accept, (dialogInterface, i) -> {
                    if (securityCipherWithKey.encrypt(editText.getText().toString().trim()).trim()
                            .equals(ListMantenimientosAdapter.SUPERVISOR)) {
                        launchMaintenanceEdition(pos);
                    } else {
                        Toast.makeText(context, "Pin incorrecto", Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    Toast.makeText(
                            context, "Cancelada la actualización", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                });
        builder.create().show();
    }

    private void launchMaintenanceEdition(int pos) {
        //paso a la actividad de la ficha para un nuevo mantenimiento
        Intent intent = new Intent(
                context, FormNewMantenimientoActivity.class);
        intent.putExtra("TASK", "UPDATE");
        intent.putExtra(
                "NOMBRE_EMPRESA",
                listElementsMantenimientos.get(pos).getEmpresa());
        intent.putExtra(
                "ID_VITRINA",
                listElementsMantenimientos.get(pos).getIdVitrina());
        intent.putExtra(
                "ID_MANTENIMIENTO",
                listElementsMantenimientos.get(pos).getId());
        activityResultLauncher.launch(intent);
    }

    private void deletMantenimiento(int pos, DataBaseOperations datos, int idMantenimiento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        builder.setTitle(R.string.delete)
                .setIcon(R.drawable.ic_baseline_error_outline_24)
                .setMessage(context.getString(R.string.message_alert_delete,
                        context.getString(R.string.the_maintenance))
                )
                .setPositiveButton(R.string.accept, (dialog, which) -> {
                    boolean isMantenimientoDeleted = datos.deleteMantenimiento(idMantenimiento);
                    if (isMantenimientoDeleted) {
                        listElementsMantenimientos.remove(pos);
                        notifyItemRemoved(pos);
                        Toast.makeText(
                                context,"Borrado Mantenimiento", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .create().show();
    }

    public void createReportPdf(int pos, DataBaseOperations db, int manteniId) {
        // Recupero información necesaria sobre el mantenimiento que se se va a reportar
        Empresa empresa = db.selectEmpresaWithName(listElementsMantenimientos.get(pos).getEmpresa());
        Contacto contacto = db.selectContactoWithId(empresa.getIdContacto());
        Mantenimiento mantenimiento = db.selectMantenimientoWithId(manteniId);
        Tecnico tecnico = db.selectTecnicoWithId(mantenimiento.getIdTecnico());
        Vitrina vitrina = db.selectVitrinaWithId(mantenimiento.getIdVitrina());
        Fabricante fabricante = db.selectFabricanteWithId(vitrina.getIdFabricante());
        String vitrinaReferencia = vitrina.getVitrinaReferencia();
        String vitrinaInventario = vitrina.getVitrinaInventario();
        TipoVitrina tipoVitrina = db.selectTipoVitrinaWithId(vitrina.getIdTipo());
        int longitudVitrina = db.getLongitudVitrinaWithIdLongitud(vitrina.getIdLongitud());
        float guillotina = db.getGuillotinaWithIdLongitud(vitrina.getIdLongitud());
        TipoLongFlow tipoLongFlow = db.selectTipoLongFlowWithId(
                vitrina.getIdTipo(), vitrina.getIdLongitud());

        // Instancio la plantilla del informe y abro el documento
        templatePdf = new TemplatePdf(context);
        // Abro el documento. Al hacerlo sin pasarle el fileName entonces utiliza "InformeVitrina.pdf"
        templatePdf.openDocument();
        // Añado los metadatos (Título, objetivo y técnico que realizó el mantenimiento) a la plantilla
        templatePdf.addMetaData(
                "Revisión de vitrina de gases", "Mantenimiento",
                tecnico.getTecnicoNombre());
        // Identificación de la vitrina preferentemente su número de referencia o su número de inventario
        // en caso de que no se cuente con ninguno de ellos, se utiliza "-" para luego añadirla a la
        // plantilla
        String titleVitrina = "-";
        if (vitrinaReferencia != null && !vitrinaReferencia.isEmpty() && !vitrinaReferencia.equals(titleVitrina)) {
            titleVitrina = vitrinaReferencia;
        } else if (vitrinaInventario != null && !vitrinaInventario.isEmpty() && !vitrinaInventario.equals(titleVitrina)) {
            titleVitrina = vitrinaInventario;
        }
        // Añado el título del reporte y el subtítulo con la identificación de la vitrina a la plantilla
        templatePdf.addTitles("REVISION DE VITRINA DE GASES", titleVitrina);
        // Añado los datos de la empresa a la plantilla
        templatePdf.addDatosEmpresa(
                mantenimiento.getFecha(), empresa.getEmpresaNombre(),
                empresa.getEmpresaDirecc(), contacto.getContactoNombre(),
                contacto.getContactoTelef(), contacto.getContactoCorreo()
        );
        templatePdf.addDatosVitrina(
                fabricante.getNombre(), tipoVitrina.getTipoVitrina(),
                vitrinaReferencia, String.valueOf(vitrina.getVitrinaAnho()),
                "-", vitrinaInventario, mantenimiento.isPuestaMarcha(),
                vitrina.getVitrinaContrato(), tecnico.getTecnicoNombre()
        );
        templatePdf.addNewPage();
        templatePdf.addComprobAjusteSistemaExtracion(
                db.getCualitativoWithId(mantenimiento.getFunCtrlDigi()),
                db.getCualitativoWithId(mantenimiento.getVisSistExtr()),
                mantenimiento.isSegunDin(), mantenimiento.isSegunEn(),
                longitudVitrina, guillotina
        );
        float[] allValores = db.selectMedicionWithId(
                mantenimiento.getIdMedicion()).getAllValues();
        templatePdf.createTableMediciones(allValores);
        templatePdf.creatTableCalcVolExtraccion(
                guillotina, allValores[0], tipoLongFlow.getFlowMin(),
                tipoLongFlow.getFlowMax(), tipoLongFlow.getFlowRecom()
        );
        templatePdf.addNewPage();
        templatePdf.addComprobAjusteComponentesFijos(
                db.getCualitativoWithId(mantenimiento.getProtSuperf()),
                db.getCualitativoWithId(mantenimiento.getJuntas()),
                db.getCualitativoWithId(mantenimiento.getFijacion()),
                db.getCualitativoWithId(mantenimiento.getFuncGuillo()),
                db.getCualitativoWithId(mantenimiento.getEstadoGuillo()),
                mantenimiento.getValFuerzaGuillo(),
                db.getCualitativoWithId(mantenimiento.getFuerzaGuillo()),
                db.getCualitativoWithId(mantenimiento.getCtrlPresencia()),
                db.getCualitativoWithId(mantenimiento.getAutoproteccion()),
                db.getCualitativoWithId(mantenimiento.getGrifosMonored()),
                mantenimiento.getValLight(),
                db.getCualitativoWithId(mantenimiento.getLight()),
                mantenimiento.getValSound(),
                db.getCualitativoWithId(mantenimiento.getSound()),
                mantenimiento.getInstrumentosMedida()
        );
        templatePdf.addComentAndFinalEvalu(
                mantenimiento.getComentario(),
                mantenimiento.isAcordeNormasReguSi(),
                mantenimiento.isNecesarioRepaSi()
        );
        templatePdf.addSignatures();
        templatePdf.closeDocument();

        TemplatePdf templatePdf3 = new TemplatePdf(this.context);
        this.templatePdf = templatePdf3;
        templatePdf3.openDocumetStamperPageNumering();
    }

    @Override
    public int getItemCount() {
        return listElementsMantenimientos.size();
    }

    public void addItems(List<ElementListMantenimientos> items) {
        this.listElementsMantenimientos = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mantenimientoCardView;
        private TextView fechaMantenimiento;
        private TextView valorVolumenExtraccionReal;
        private TextView comentario;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mantenimientoCardView = itemView.findViewById(R.id.mantenimientoCardView);
            fechaMantenimiento = itemView.findViewById(R.id.fechaMantenimiTextView);
            valorVolumenExtraccionReal = itemView.findViewById(R.id.valorVolumenExtraccionRealTextView);
            comentario = itemView.findViewById(R.id.comentarioTextView);
        }

        public void bindData(final ElementListMantenimientos item) {
            fechaMantenimiento.setText(item.getFecha());
            valorVolumenExtraccionReal.setText(
                    String.format(Locale.getDefault(), "%.2f", item.getVolumenExtraccion()));
            comentario.setText(item.getComentario());
        }
    }
}
