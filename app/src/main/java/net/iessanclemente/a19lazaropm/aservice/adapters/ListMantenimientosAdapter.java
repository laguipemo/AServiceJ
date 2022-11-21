package net.iessanclemente.a19lazaropm.aservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.iessanclemente.a19lazaropm.aservice.FichaMantenimientoActivity;
import net.iessanclemente.a19lazaropm.aservice.FormNewMantenimientoActivity;
import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Mantenimiento;
import net.iessanclemente.a19lazaropm.aservice.utils.UtilsMenu;

import java.util.List;
import java.util.Locale;

public class ListMantenimientosAdapter extends RecyclerView.Adapter<ListMantenimientosAdapter.ViewHolder> {

    private List<ElementListMantenimientos> listElementsMantenimientos;
    private LayoutInflater listElementsMantenimientosInflater;
    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public ListMantenimientosAdapter(
            List<ElementListMantenimientos> listElementsMantenimientos,
            ActivityResultLauncher<Intent> activityResultLauncher,
            Context context) {
        this.listElementsMantenimientosInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
        this.listElementsMantenimientos = listElementsMantenimientos;
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

        holder.mantenimientoCardView.setOnLongClickListener(new View.OnLongClickListener() {
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
                        Mantenimiento mantenimiento = datos.selectMantenimientoWithId(
                                listElementsMantenimientos.get(pos).getId());

                        if (mantenimiento != null) {
                            int idMantenimiento = mantenimiento.getId();

                            switch (item.getItemId()) {
                                case R.id.menuItemDelete:
                                    boolean isMantenimientoDeleted = datos.deleteMantenimiento(idMantenimiento);
                                    if (isMantenimientoDeleted) {
                                        listElementsMantenimientos.remove(pos);
                                        notifyDataSetChanged();
                                        Toast.makeText(
                                                context,
                                                "Borrado Mantenimiento",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case R.id.menuItemUpdate:
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
