package com.ajudar.velhoaprendiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ajudar.velhoaprendiz.R;
import com.ajudar.velhoaprendiz.config.ConfiguracaoFirebase;
import com.ajudar.velhoaprendiz.helper.Base64custom;
import com.ajudar.velhoaprendiz.helper.UsuarioFirebase;
import com.ajudar.velhoaprendiz.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Cadastrar extends AppCompatActivity {

    EditText nomeCadastrar,emailCadastrar, senhaCadastrar;
    TextView entreAqui;
    Button botaoCadastrar;
    FirebaseAuth autenticacao;
    ProgressBar progressBarCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        nomeCadastrar = findViewById(R.id.editTextNome);
        emailCadastrar = findViewById(R.id.emailCadastrar);
        senhaCadastrar = findViewById(R.id.senhaCadastrar);
        botaoCadastrar = findViewById(R.id.botaoCadastrar);
        entreAqui = findViewById(R.id.entreAqui);
        progressBarCadastrar = findViewById(R.id.progressBarCadastro);

        entreAqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Entrar.class));
            }
        });


        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeCadastrar.getText().toString();
                String email = emailCadastrar.getText().toString();
                String senha = senhaCadastrar.getText().toString();

                if (!nome.isEmpty()) {//verifica o nome
                    if (!email.isEmpty()) {//verifica o email
                        if (!senha.isEmpty()) {
                            Usuario usuario = new Usuario();
                            usuario.setNome(nome);
                            usuario.setEmail(email);
                            usuario.setSenha(senha);

                            cadastrarUsuario(usuario);

                        } else {
                            Toast.makeText(Cadastrar.this,
                                    "Preencha a senha!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Cadastrar.this,
                                "Preencha o email!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Cadastrar.this,
                            "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void cadastrarUsuario(final Usuario usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(Cadastrar.this,
                            "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    UsuarioFirebase.atualizarNomeUsuario( usuario.getNome() );

                    finish();
                    progressBarCadastrar.setVisibility(View.VISIBLE);

                    try {
                        String identificadorUsuario = Base64custom.codificarBase64( usuario.getEmail() );
                        usuario.setId( identificadorUsuario );
                        usuario.salvar();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    String excecao="";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um email válido!";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(Cadastrar.this, excecao, Toast.LENGTH_SHORT).show();
                    progressBarCadastrar.setVisibility(View.GONE);
                }

            }
        });


    }

}
