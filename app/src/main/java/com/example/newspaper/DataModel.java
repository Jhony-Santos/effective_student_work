package com.example.newspaper;

import android.content.Context;
import android.widget.Toast;

import com.example.newspaper.dao.salvarFotoBanco;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class DataModel {

    private static DataModel instance = new DataModel();
    private ArrayList<Foto> listaFotos;
    private salvarFotoBanco bancoDeDados;
    private Context context;

    public int indexToDetails;


    public void setContext(Context context) {
        this.context = context;
        bancoDeDados = new salvarFotoBanco(context);
        listaFotos = bancoDeDados.recuperarFotos();
    }

    private DataModel() {
    }


    public static DataModel getInstance() {
        return instance;
    }


    public ArrayList<Foto> getListPhotoTokens() {
        return listaFotos;
    }


    public long addPhotoToken(Foto foto) {
        long id = bancoDeDados.criandoFotoNoBanco(foto);
        if(id > 0) {
            foto.setId(id);
            listaFotos.add(foto);
        }
        else {
            Toast.makeText(context, "Não conseguimos salvar a foto", Toast.LENGTH_LONG).show();
        }

        return id;
    }


    public long atualizarFoto(Foto foto) {
        foto.setData(new Date().toString());
        return bancoDeDados.AtualizarFoto(foto);
    }


    public void excluirFoto(Foto foto) {
        bancoDeDados.deletarFoto(foto);
        listaFotos.remove(foto);
        new File(foto.getFoto()).delete();
    }



}
