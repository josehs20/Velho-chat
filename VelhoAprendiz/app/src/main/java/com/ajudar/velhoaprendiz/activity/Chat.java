package com.ajudar.velhoaprendiz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajudar.velhoaprendiz.R;
import com.ajudar.velhoaprendiz.adapter.MensagensAdapter;
import com.ajudar.velhoaprendiz.config.ConfiguracaoFirebase;
import com.ajudar.velhoaprendiz.helper.Base64custom;
import com.ajudar.velhoaprendiz.helper.UsuarioFirebase;
import com.ajudar.velhoaprendiz.model.Conversa;
import com.ajudar.velhoaprendiz.model.Mensagem;
import com.ajudar.velhoaprendiz.model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private TextView nomeUsuarioNoChat;
    private Usuario usuarioDestinatario;
    private EditText msg;
    private DatabaseReference database;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;


    //identificar usuario remetente e destinatario
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;
    private RecyclerView recyclerMensagens;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //configurações iniciais
        nomeUsuarioNoChat = findViewById(R.id.textViewNomeChat);
        msg = findViewById(R.id.editTextMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);

        //recuperar dados do usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIdentificadorUsuario();


        //recuperar dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();
        if( bundle != null ) {

            usuarioDestinatario = (Usuario) bundle.getSerializable("chatUsuario");
            nomeUsuarioNoChat.setText(usuarioDestinatario.getNome());

            // recuperar dados do usuario destinatario
            idUsuarioDestinatario = Base64custom.codificarBase64( usuarioDestinatario.getEmail() );

        }

        //configurar o adapter
        adapter = new MensagensAdapter( mensagens, getApplicationContext() );

        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager( layoutManager );
        recyclerMensagens.setHasFixedSize( true );
        recyclerMensagens.setAdapter( adapter );

        database = ConfiguracaoFirebase.getFirebaseDatabase();
        mensagensRef = database.child("mensagens")
                .child( idUsuarioRemetente )
                .child( idUsuarioDestinatario );

    }

    public void enviarMensagem(View view){

        String textoMensagem = msg.getText().toString();

        if( !textoMensagem.isEmpty() ){

            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario( idUsuarioRemetente );
            mensagem.setMensagem( textoMensagem );

            //salvar mensagem para o remetente
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

            //salvar mensagem para o destinatario
            salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

            //Salvar conversa
            salvarConversa( mensagem );

        }else{
            Toast.makeText(this, "Digite uma mensagem para enviar!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void salvarConversa( Mensagem msg ){

        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdRemetente( idUsuarioRemetente );
        conversaRemetente.setIdDestinatario( idUsuarioDestinatario );
        conversaRemetente.setUltimaMensagem( msg.getMensagem() );
        conversaRemetente.setUsuarioExibicao( usuarioDestinatario );

        conversaRemetente.salvar();
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);
        //limpar texto que enviar a mensagem
        msg.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener( childEventListenerMensagens );
    }

    private void recuperarMensagens(){

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue( Mensagem.class );
                mensagens.add( mensagem );
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
