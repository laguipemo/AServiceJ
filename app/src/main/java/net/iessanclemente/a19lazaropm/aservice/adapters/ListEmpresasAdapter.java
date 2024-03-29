package net.iessanclemente.a19lazaropm.aservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.ui.secondary.FichaEmpresaActivity;
import net.iessanclemente.a19lazaropm.aservice.ui.forms.FormNewEmpresaActivity;
import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Empresa;
import net.iessanclemente.a19lazaropm.aservice.utils.UtilsMenu;

import java.util.List;

public class ListEmpresasAdapter extends RecyclerView.Adapter<ListEmpresasAdapter.ViewHolder> {

    private List<ElementListEmpresas> listElementsEmpresas;
    private final LayoutInflater listElementsEmpresasInflater;
    private final Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public ListEmpresasAdapter(
            List<ElementListEmpresas> listElementsEmpresas,
            ActivityResultLauncher<Intent> activityResultLauncher,
            Context context) {
        this.listElementsEmpresasInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
        this.listElementsEmpresas = listElementsEmpresas;
    }


    @NonNull
    @Override
    public ListEmpresasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = listElementsEmpresasInflater.inflate(
                R.layout.element_list_empresas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(listElementsEmpresas.get(position));

        //Manejo del evento onClick sobre el CardView de la empresa
        holder.empresaCardView.setOnClickListener(v -> {
            //paso a la actividad de la ficha de la empresa
            Intent intent = new Intent(context, FichaEmpresaActivity.class);
            intent.putExtra(
                    "NOMBRE_EMPRESA", holder.nombreEmpresaTextView.getText().toString());
            context.startActivity(intent);
        });

        holder.empresaCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                UtilsMenu.setForceShowIcon(popupMenu);
                popupMenu.inflate(R.menu.menu_contextual);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int pos = holder.getAdapterPosition();
                        DataBaseOperations datos = DataBaseOperations.getInstance(context);
                        Empresa empresa = datos.selectEmpresaWithName(
                                listElementsEmpresas.get(pos).getNombreEmpresa());
                        if (empresa != null) {
                            int idEmpresa = empresa.getId();
                            int idContactoEmp = empresa.getIdContacto();

                            switch (item.getItemId()) {
                                case R.id.menuItemDelete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            context, R.style.CustomAlertDialog);
                                    builder.setTitle(R.string.delete)
                                            .setIcon(R.drawable.ic_baseline_error_outline_24)
                                            .setMessage(
                                                    context.getString(R.string.message_alert_delete,
                                                                    context.getString(R.string.the_company))
                                            )
                                            .setPositiveButton(R.string.accept, (dialog, which) -> {
                                                boolean isEmpDeleted = datos.deleteEmpresa(idEmpresa);
                                                if (isEmpDeleted) {
                                                    boolean isContactDeleted = datos.deleteContacto(idContactoEmp);
                                                    if (isContactDeleted) {
                                                        listElementsEmpresas.remove(pos);
                                                        notifyItemRemoved(pos);
                                                        Toast.makeText(
                                                                context,
                                                                "Borrada Empresa: " +
                                                                        holder.direccEmpresaTextView.getText().toString(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, (dialog, which) -> {
                                                dialog.dismiss();
                                            }).create().show();
                                    break;
                                case R.id.menuItemUpdate:
                                    //paso a la actividad de la ficha para nueva empresa
                                    Intent intent = new Intent(
                                            context, FormNewEmpresaActivity.class);
                                    intent.putExtra("TASK", "UPDATE");
                                    intent.putExtra(
                                            "NOMBRE_EMPRESA", 
                                            holder.nombreEmpresaTextView.getText().toString());
                                    activityResultLauncher.launch(intent);
                                    break;
                            }
                        }
                        return false;
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listElementsEmpresas.size();
    }

    public void setItems(List<ElementListEmpresas> items) {
        this.listElementsEmpresas = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView empresaCardView;
        private final ImageView iconEmpresaImageView;
        private final ImageView iconContactoImageView;
        private final TextView nombreEmpresaTextView;
        private final TextView direccEmpresaTextView;
        private final TextView nombreContactoTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empresaCardView = itemView.findViewById(R.id.empresaCardView);
            iconEmpresaImageView = itemView.findViewById(R.id.iconEmpresaImageView);
            iconContactoImageView = itemView.findViewById(R.id.iconContactoEmpresaImageView);
            nombreEmpresaTextView = itemView.findViewById(R.id.nombreEmpresaTextView);
            direccEmpresaTextView = itemView.findViewById(R.id.direccEmpresaTextView);
            nombreContactoTextView = itemView.findViewById(R.id.nombreContactoEmpresaTextView);
        }

        public void bindData(final ElementListEmpresas item) {
            nombreEmpresaTextView.setText(item.getNombreEmpresa());
            direccEmpresaTextView.setText(item.getDireccEmpresa());
            nombreContactoTextView.setText(item.getNombreContacto());
        }
    }

    public interface IListaEmpresas {
        public void intentFormProvider(String task);
    }
}
