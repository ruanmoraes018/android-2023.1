package com.android.tokentravel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.tokentravel.dao.Dao;
import com.android.tokentravel.objetos.Rotas;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Adapter_motoristas extends RecyclerView.Adapter<Adapter_motoristas.MyViewHolder> {
    private List<Rotas> mylist;
    private Context context;
    private Dao dao;
    private List<Bitmap> motoristaImages;
    private Bitmap selectedImage; // Variável para armazenar a imagem decodificada

    public Adapter_motoristas(Context context, List<Rotas> mylist) {
        this.context = context;
        this.mylist = mylist;
        dao = new Dao(context);
        motoristaImages = new ArrayList<>();

        for (Rotas rota : mylist) {
            loadMotoristaImage(rota.getId_motorista());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_motoristas, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Rotas rota = mylist.get(position);

        String nomeDoMotorista = dao.buscaNomeMotoristaPorId(rota.getId_motorista());

        if (nomeDoMotorista != null) {
            holder.textName.setText("Motorista: " + nomeDoMotorista);
        } else {
            holder.textName.setText("Nome do Motorista: N/A");
        }

        if (!motoristaImages.isEmpty()) {
            Bitmap motoristaImage = motoristaImages.get(position);
            holder.imageView.setImageBitmap(motoristaImage);
            displaySelectedImage(motoristaImage, holder.imageView);

            // Armazene a imagem atual na variável selectedImage
            selectedImage = motoristaImage;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Perfil_Apresent_RotaProf.class);
                intent.putExtra("numeroRota", rota.getNumero_rota());
                intent.putExtra("origem", rota.getOrigem());
                intent.putExtra("destino", rota.getDestino());

                // Passa a imagem decodificada como ByteArray
                if (selectedImage != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("motoristaImage", byteArray);
                }

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            imageView = itemView.findViewById(R.id.miniFoto);
        }
    }

    private void loadMotoristaImage(int idMotorista) {
        byte[] imageBytes = dao.obterFotoMotorista(idMotorista);

        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            if (bitmap != null) {
                motoristaImages.add(bitmap);
            } else {
                Log.d("LoadMotoristaImage", "Falha na decodificação da imagem");
            }
        } else {
            Log.d("LoadMotoristaImage", "Bytes da imagem não obtidos");
        }
    }

    private void displaySelectedImage(Bitmap bitmap, ImageView imageView) {
        Glide.with(context)
                .load(bitmap)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }
}
