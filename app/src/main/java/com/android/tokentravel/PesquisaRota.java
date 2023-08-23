package com.android.tokentravel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.util.List;

public class PesquisaRota extends AppCompatActivity {

    private EditText editOrigem; // Adicione esta variável
    private EditText editDestino; // Adicione esta variável

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_rota);

        editOrigem = findViewById(R.id.edit_origem); // Inicialize o EditText
        editDestino = findViewById(R.id.edit_destino);

        Spinner spinnerUserType = findViewById(R.id.spinner_user_type);

        String[] userTypes = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userTypes) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK); // Defina a cor do texto aqui
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLUE); // Defina a cor do texto aqui para o drop-down
                return view;
            }
        };
        spinnerUserType.setAdapter(adapter);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("channel1","notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void notificationButton (View view){
        final String CHANNEL_ID = "channel1";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Ola passageiro")
                .setContentText("Viemos desejar boa viagem!");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());

        //Metodo para pegar as coordenadas de origem e destino
        String endereco = editOrigem.getText().toString();
        String endereco2 = editDestino.getText().toString();

        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getResources().getString(R.string.mapbox_access_token))
                .query(endereco + " " + endereco2)
                .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CarmenFeature> features = response.body().features();
                    if (features.size() >= 2) {
                        CarmenFeature featureOrigem = features.get(0);
                        CarmenFeature featureDestino = features.get(1);

                        Point coordenadasOrigem = featureOrigem.center();
                        Point coordenadasDestino = featureDestino.center();

                        double latitudeOrigem = coordenadasOrigem.latitude();
                        double longitudeOrigem = coordenadasOrigem.longitude();

                        double latitudeDestino = coordenadasDestino.latitude();
                        double longitudeDestino = coordenadasDestino.longitude();

                        // Dessa forma serão passadas para a próxima activity as coordenadas de origem e destino
                        Intent intent = new Intent(PesquisaRota.this, RotaPontoaPonto.class);
                        intent.putExtra("latitudeOrigem", latitudeOrigem);
                        intent.putExtra("longitudeOrigem", longitudeOrigem);
                        intent.putExtra("latitudeDestino", latitudeDestino);
                        intent.putExtra("longitudeDestino", longitudeDestino);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
               // Toast.makeText(PesquisaRota.this, "Não foi possível obter o ponto de origem, forneça manualmente!", Toast.LENGTH_LONG).show();
            }
        });
    }
}