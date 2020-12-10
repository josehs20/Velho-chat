package com.ajudar.velhoaprendiz.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ajudar.velhoaprendiz.R;
import com.ajudar.velhoaprendiz.adapter.AdapterParaOsFragment;
import com.ajudar.velhoaprendiz.fragment.FragmentAssistencia;
import com.ajudar.velhoaprendiz.fragment.FragmentBoleto;
import com.ajudar.velhoaprendiz.fragment.FragmentTransferencia;
import com.ajudar.velhoaprendiz.fragment.FragmentUsuarios;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements FragmentBoleto.OnFragmentInteractionListener,
        FragmentTransferencia.OnFragmentInteractionListener,
        FragmentAssistencia.OnFragmentInteractionListener,
        FragmentUsuarios.OnFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Como pagar Boleto"));
        tabLayout.addTab(tabLayout.newTab().setText("Como transferir"));
        tabLayout.addTab(tabLayout.newTab().setText("Conversa"));
        tabLayout.addTab(tabLayout.newTab().setText("Usuários"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        AdapterParaOsFragment adapter = new AdapterParaOsFragment(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuprincipal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAjuda:
                new AlertDialog.Builder(this)
                        .setTitle("Ajuda")
                        .setMessage("\nAqui você encontra vídeos explicativos\n" +
                                "sobre algumas funcionalidades de aplicativos bancários.\n" +
                                "\nContém vídeos de bancos diversificados.\n" +
                                "\nSelecione o que você quer aprender" +
                                " e escolha o vídeo que corresponde ao seu banco.\n" +
                                "\nVocê também terá a disposição um chat\n" +
                                "para que você possa interagir com outros\n usuários" +
                                "e sanar possíveis dúvidas."
                        )
                        .setNegativeButton("Fechar", null).show();
                return true;

            case R.id.menuSair:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Entrar.class));
                Toast.makeText(MainActivity.this, "Você saiu",Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case R.id.menuConfiguracao:
                startActivity(new Intent(getApplicationContext(), Configuracao.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
