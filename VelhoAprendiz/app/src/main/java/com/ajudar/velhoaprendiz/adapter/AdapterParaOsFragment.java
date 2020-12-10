package com.ajudar.velhoaprendiz.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ajudar.velhoaprendiz.fragment.FragmentAssistencia;
import com.ajudar.velhoaprendiz.fragment.FragmentBoleto;
import com.ajudar.velhoaprendiz.fragment.FragmentTransferencia;
import com.ajudar.velhoaprendiz.fragment.FragmentUsuarios;

public class AdapterParaOsFragment extends FragmentStatePagerAdapter {

    int numeroDeTabelas;


    public AdapterParaOsFragment(FragmentManager fm, int numDeTabelas){
        super(fm);
        this.numeroDeTabelas = numDeTabelas;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
              FragmentBoleto videosDoBoleto = new FragmentBoleto();
              return videosDoBoleto;
            case 1:
                FragmentTransferencia videosDeTransferencia = new FragmentTransferencia();
                return videosDeTransferencia;
            case 2:
                FragmentAssistencia assistencia = new FragmentAssistencia();
                return assistencia;
            case 3:
                FragmentUsuarios usuarios = new FragmentUsuarios();
                return usuarios;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numeroDeTabelas;
    }
}
