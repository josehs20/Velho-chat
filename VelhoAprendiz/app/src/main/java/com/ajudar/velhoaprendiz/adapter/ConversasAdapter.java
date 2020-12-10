package com.ajudar.velhoaprendiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajudar.velhoaprendiz.R;
import com.ajudar.velhoaprendiz.model.Conversa;
import com.ajudar.velhoaprendiz.model.Usuario;

import java.util.List;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public ConversasAdapter(List<Conversa> lista, Context c) {
         this.conversas = lista;
         this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false );
        return new MyViewHolder( itemLista );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Conversa conversa = conversas.get( position );
        holder.ultimaMensagem.setText( conversa.getUltimaMensagem() );

        Usuario usuario = conversa.getUsuarioExibicao();
        holder.nome.setText( usuario.getNome() );

        holder.foto.setImageResource(R.drawable.ic_pessoa_50dp);
    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView foto;
        TextView nome, ultimaMensagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewUsuario);
            nome = itemView.findViewById(R.id.textViewNomeUsuario);
            ultimaMensagem = itemView.findViewById(R.id.textViewEmailUsuario);

        }
    }

}
