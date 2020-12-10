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
import com.ajudar.velhoaprendiz.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Entrar extends AppCompatActivity {

    EditText emailEntrar, senhaEntrar;
    Button botaoEntrar;
    TextView cadastreAqui;
    ProgressBar progressBarEntrar;
    FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        emailEntrar = findViewById(R.id.emailEntrar);
        senhaEntrar = findViewById(R.id.senhaEntrar);
        cadastreAqui = findViewById(R.id.cadastreAqui);
        botaoEntrar = findViewById(R.id.botaoEntrar);

        progressBarEntrar = findViewById(R.id.progressBarEntrar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEntrar.getText().toString().trim();
                String senha = senhaEntrar.getText().toString().trim();

                if (!email.isEmpty()) {//verifica o email
                    if (!senha.isEmpty()) {//verifica a senha

                        Usuario usuario = new Usuario();
                        usuario.setEmail(email);
                        usuario.setSenha(senha);

                        logarUsuario( usuario );

                        progressBarEntrar.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(Entrar.this,
                                "Preencha a senha   !", Toast.LENGTH_SHORT).show();
                        progressBarEntrar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(Entrar.this,
                            "Preencha o email!", Toast.LENGTH_SHORT).show();
                    progressBarEntrar.setVisibility(View.GONE);
                }


            }
        });

        cadastreAqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Cadastrar.class));
            }
        });
    }

    public void logarUsuario(Usuario usuario){
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                }else{
                    String excecao="";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao = "Senha não corresponde!";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Email e senha não correspondem a um usuário cadastrado!";
                    }catch (Exception e){
                        excecao = "Este usuário não existe!";
                        e.printStackTrace();
                    }
                    Toast.makeText(Entrar.this, excecao, Toast.LENGTH_SHORT).show();
                    progressBarEntrar.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuario = autenticacao.getCurrentUser();
        if ( usuario != null){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
