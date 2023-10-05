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
import java.util.ArrayList;
import java.util.List;

public class Adapter_motoristas extends RecyclerView.Adapter<Adapter_motoristas.MyViewHolder> {
    private List<Rotas> mylist;
    private Context context;
    private Dao dao;
    private List<List<String>> motoristaNomes;
    private List<List<byte[]>> motoristaImages;

    public Adapter_motoristas(Context context, List<Rotas> mylist) {
        this.context = context;
        this.mylist = mylist;
        dao = new Dao(context);
        motoristaNomes = new ArrayList<>();
        motoristaImages = new ArrayList<>();

        for (Rotas rota : mylist) {
            loadMotoristaData(rota.getId_motorista()); // Carregue os dados do motorista
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

        List<String> nomesDoMotorista = motoristaNomes.get(position);

        if (!nomesDoMotorista.isEmpty()) {
            holder.textName.setText("Motorista: " + nomesDoMotorista.get(0));
        } else {
            holder.textName.setText("Nome do Motorista: N/A");
        }

        List<byte[]> imagensDoMotorista = motoristaImages.get(position);

        if (!imagensDoMotorista.isEmpty()) {
            Bitmap motoristaImage = BitmapFactory.decodeByteArray(imagensDoMotorista.get(0), 0, imagensDoMotorista.get(0).length);

            if (motoristaImage != null) {
                holder.imageView.setImageBitmap(motoristaImage);
                displaySelectedImage(motoristaImage, holder.imageView);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Obtenha a posição do adaptador

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Rotas rota = mylist.get(adapterPosition); // Obtenha o item na posição do adaptador

                    Intent intent = new Intent(context, Perfil_Apresent_RotaProf.class);
                    intent.putExtra("numeroRota", rota.getNumero_rota());
                    intent.putExtra("origem", rota.getOrigem());
                    intent.putExtra("destino", rota.getDestino());

                    // Passe o id_motorista diretamente
                    intent.putExtra("id_motoristaagora", rota.getId_motorista());

                    Log.d("Adapter_motoristas", "ID do motorista: " + rota.getId_motorista());
                    Log.d("Adapter_motoristas", "ID da rota: " + rota.getNumero_rota());

                    // Obtenha a imagem correta do motorista e passe como um ByteArray
                    List<byte[]> imagensDoMotorista = motoristaImages.get(adapterPosition);
                    if (!imagensDoMotorista.isEmpty()) {
                        byte[] byteArray = imagensDoMotorista.get(0);
                        intent.putExtra("motoristaImage", byteArray);
                    }

                    context.startActivity(intent);
                }
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

    private void loadMotoristaData(int idMotorista) {
        List<String> nomesMotoristas = dao.buscaNomesMotoristasPorId(idMotorista);
        motoristaNomes.add(nomesMotoristas);

        List<byte[]> fotosMotorista = dao.obterFotosMotorista(idMotorista);
        motoristaImages.add(fotosMotorista);
    }

    private void displaySelectedImage(Bitmap bitmap, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(bitmap)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }
}
