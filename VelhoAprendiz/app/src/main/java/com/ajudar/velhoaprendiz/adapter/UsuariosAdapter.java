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
import com.ajudar.velhoaprendiz.model.Usuario;

import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.MyViewHolder> {

    private List<Usuario> usuarios;
    private Context context;

    public UsuariosAdapter(List<Usuario> listaUsuarios, Context c) {
        this.usuarios = listaUsuarios;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from( parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder( itemLista );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = usuarios.get( position );

        holder.nome.setText( usuario.getNome() );
        holder.email.setText( usuario.getEmail() );
        holder.foto.setImageResource( R.drawable.ic_pessoa_50dp );

    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        ImageView foto;
        TextView nome, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewUsuario);
            nome = itemView.findViewById(R.id.textViewNomeUsuario);
            email = itemView.findViewById(R.id.textViewEmailUsuario);
        }
    }

}
