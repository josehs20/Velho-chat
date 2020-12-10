package com.ajudar.velhoaprendiz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ajudar.velhoaprendiz.R;
import com.ajudar.velhoaprendiz.helper.UsuarioFirebase;
import com.ajudar.velhoaprendiz.model.Usuario;
import com.google.firebase.auth.FirebaseUser;

public class Configuracao extends AppCompatActivity {

    private EditText nomeUsuarioConfig;
    private ImageView editarNome;
    private Usuario usuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        nomeUsuarioConfig = findViewById(R.id.editTextNomeTelaConfig);
        editarNome = findViewById(R.id.imageViewEditarNomeTelaConfig);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        getSupportActionBar().setTitle("Configuração");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Recuperar dados do usuario
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        nomeUsuarioConfig.setText( usuario.getDisplayName() );


        editarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = nomeUsuarioConfig.getText().toString();
                boolean retorno = UsuarioFirebase.atualizarNomeUsuario( nome );
                if( retorno ){
                    usuarioLogado.setNome( nome );
                    usuarioLogado.atualizar();

                    Toast.makeText(Configuracao.this,
                            "Nome alterado com sucesso", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

