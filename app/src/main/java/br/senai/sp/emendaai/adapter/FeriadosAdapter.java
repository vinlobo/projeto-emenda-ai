package br.senai.sp.emendaai.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.senai.sp.emendaai.R;
import br.senai.sp.emendaai.model.Feriado;
import br.senai.sp.emendaai.util.Datas;

public class FeriadosAdapter extends RecyclerView.Adapter<FeriadosAdapter.FeriadosViewHolder> {
    private final List<Feriado> lista;
    public class FeriadosViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtDia, txtMes, txtSemana, txtContagem;
        TextView txtSelo;
        public FeriadosViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNome = itemView.findViewById(R.id.txtNome);
            txtDia = itemView.findViewById(R.id.txtDia);
            txtMes = itemView.findViewById(R.id.txtMes);
            txtSemana = itemView.findViewById(R.id.txtSemana);
            txtContagem = itemView.findViewById(R.id.txtContagem);
            txtSelo = itemView.findViewById(R.id.txtSelo);
        }
    }

    public FeriadosAdapter (List<Feriado> lista) {
        this.lista = lista;
    }
    @NonNull
    @Override
    public FeriadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feriado, parent, false);

        return new FeriadosViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull FeriadosViewHolder holder, int position) {
        Feriado feriado = lista.get(position);

        holder.txtNome.setText(feriado.getNome());
        holder.txtDia.setText(Datas.dia(feriado.getData()));
        holder.txtMes.setText(Datas.mesCurto(feriado.getData()));
        holder.txtSemana.setText(Datas.diaDaSemana(feriado.getData()));
        holder.txtContagem.setText(Datas.contagem(feriado.getData()));

        boolean emenda = Datas.ehEmenda(feriado.getData());
        if (emenda) {
            holder.txtSelo.setVisibility(View.VISIBLE);
        } else {
            holder.txtSelo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }



}
