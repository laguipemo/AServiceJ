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

import net.iessanclemente.a19lazaropm.aservice.ui.secondary.FichaVitrinaActivity;
import net.iessanclemente.a19lazaropm.aservice.ui.forms.FormNewVitrinaActivity;
import net.iessanclemente.a19lazaropm.aservice.R;
import net.iessanclemente.a19lazaropm.aservice.database.dao.DataBaseOperations;
import net.iessanclemente.a19lazaropm.aservice.database.dto.Vitrina;
import net.iessanclemente.a19lazaropm.aservice.utils.UtilsMenu;

import java.util.List;

public class ListVitrinasAdapter extends RecyclerView.Adapter<ListVitrinasAdapter.ViewHolder> {

    private List<ElementListVitrinas> listElementsVitrinas;
    private LayoutInflater listElementsVitrinasInflater;
    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public ListVitrinasAdapter(
            List<ElementListVitrinas> listElementsVitrinas,
            ActivityResultLauncher<Intent> activityResultLauncher,
            Context context) {
        this.listElementsVitrinasInflater = LayoutInflater.from(context);
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
        this.listElementsVitrinas = listElementsVitrinas;
    }

    @NonNull
    @Override
    public ListVitrinasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = listElementsVitrinasInflater.inflate(
                R.layout.element_list_vitrinas, parent, false);
        return new ListVitrinasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListVitrinasAdapter.ViewHolder holder, int position) {
        holder.bindData(listElementsVitrinas.get(position));

        //Manejo del evento onClick sobre el CardView de la Vitrina
        holder.vitrinaCardView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            //paso a la actividad de la ficha de la vitrina
            Intent intent = new Intent(context, FichaVitrinaActivity.class);
            intent.putExtra("NOMBRE_EMPRESA", listElementsVitrinas.get(pos).getEmpresa());
            intent.putExtra("ID_VITRINA", listElementsVitrinas.get(pos).getId());
            context.startActivity(intent);
        });

        holder.vitrinaCardView.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            UtilsMenu.setForceShowIcon(popupMenu);
            popupMenu.inflate(R.menu.menu_contextual);
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {
                int pos = holder.getAdapterPosition();
                DataBaseOperations datos = DataBaseOperations.getInstance(context);
                Vitrina vitrina = datos.selectVitrinaWithId(
                        listElementsVitrinas.get(pos).getId());
                if (vitrina != null) {
                    int idVitrina = vitrina.getId();

                    switch (item.getItemId()) {
                        case R.id.menuItemDelete:
                            boolean isVitrinaDeleted = datos.deleteVitrina(idVitrina);
                            if (isVitrinaDeleted) {
                                listElementsVitrinas.remove(pos);
                                notifyDataSetChanged();
                                Toast.makeText(
                                        context,
                                        "Borrada Vitrina",
                                        Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.id.menuItemUpdate:
                            //paso a la actividad de la ficha para nueva vitrina
                            Intent intent = new Intent(
                                    context, FormNewVitrinaActivity.class);
                            intent.putExtra("TASK", "UPDATE");
                            intent.putExtra(
                                    "NOMBRE_EMPRESA",
                                    listElementsVitrinas.get(pos).getEmpresa());
                            intent.putExtra(
                                    "ID_VITRINA",
                                    listElementsVitrinas.get(pos).getId());
                            activityResultLauncher.launch(intent);
                            break;
                    }
                }
                return false;
            });
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listElementsVitrinas.size();
    }

    public void addItems(List<ElementListVitrinas> items) { this.listElementsVitrinas = items; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView vitrinaCardView;
        private TextView fabricanteVitrinaTextView;
        private TextView tipoVitrinaTextView;
        private TextView longitudVitrinaTextView;
        private TextView longitudGuillotinaTextView;
        private TextView referenciaVitrinaTextView;
        private TextView inventarioVitrinaTextView;
        private TextView anhoVitrinaTextView;
        private TextView contratoVitrinaTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vitrinaCardView = itemView.findViewById(R.id.vitrinaCardView);
            fabricanteVitrinaTextView = itemView.findViewById(R.id.fabricanteVitrinaTextView);
            tipoVitrinaTextView = itemView.findViewById(R.id.tipoVitrinaTextView);
            longitudVitrinaTextView = itemView.findViewById(R.id.longitudVitrinaTextView);
            longitudGuillotinaTextView = itemView.findViewById(R.id.longitudGuillotinaTextView);
            referenciaVitrinaTextView = itemView.findViewById(R.id.referenciaVitrinaTextView);
            inventarioVitrinaTextView = itemView.findViewById(R.id.inventarioVitrinaTextView);
            anhoVitrinaTextView = itemView.findViewById(R.id.anhoVitrinaTextView);
            contratoVitrinaTextView = itemView.findViewById(R.id.contratoVitrinaTextView);
        }

        public void bindData(final ElementListVitrinas item) {
            fabricanteVitrinaTextView.setText(item.getFabricante());
            tipoVitrinaTextView.setText(item.getTipoVitrina());
            longitudVitrinaTextView.setText(String.valueOf(item.getLongitudVitrina()));
            longitudGuillotinaTextView.setText(String.valueOf(item.getLongitudGuillotina()));
            referenciaVitrinaTextView.setText(item.getReferenciaVitrina());
            inventarioVitrinaTextView.setText(item.getInventarioVitrina());
            anhoVitrinaTextView.setText(String.valueOf(item.getAnhoVitrina()));
            contratoVitrinaTextView.setText(item.getContrato());
        }
    }
}
