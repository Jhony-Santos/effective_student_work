package com.example.newspaper;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MovieDetails extends AppCompatActivity {

    TextView textViewDate;
    EditText editTextDescription;
    ImageView imageViewDetail;
    int indexDetail;
    Foto foto;
    SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setTitle("Details");

        indexDetail = DataModel.getInstance().indexToDetails;
        foto = DataModel.getInstance().getListPhotoTokens().get(indexDetail);

        textViewDate = findViewById(R.id.textViewDate);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageViewDetail = findViewById(R.id.imageViewDetail);
        seekBar = findViewById(R.id.seekBar);

        textViewDate.setText(foto.getData());
        editTextDescription.setText(foto.getNome());
        File file = new File(foto.getFoto());
        imageViewDetail.setImageURI(Uri.fromFile(file));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = ((progress / 100.0f) + 1);
                imageViewDetail.setScaleX(scale);
                imageViewDetail.setScaleY(scale);
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    public void atualizarFotoOnClick(View view) {
        String nome = editTextDescription.getText().toString();
        foto.setNome(nome);
        DataModel.getInstance().atualizarFoto(foto);
        Toast.makeText(this, "Atualizado com sucesso", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }


    public void deletarFotoOnClick(View view) {
        DataModel.getInstance().excluirFoto(foto);
        Toast.makeText(this, "Imagem excluída", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }





}
