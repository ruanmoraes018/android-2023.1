package com.android.tokentravel;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenContext;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PesquisaRota extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteOrigem;
    private AutoCompleteTextView autoCompleteDestino;
    private ArrayAdapter<String> autoCompleteAdapterOrigem;
    private ArrayAdapter<String> autoCompleteAdapterDestino;
    private Set<String> sugestoesUnicas = new HashSet<>();
    private static final long AUTOCOMPLETE_DELAY = 100;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autocompleteRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_rota);

        autoCompleteOrigem = findViewById(R.id.autoCompleteOrigem);
        autoCompleteDestino = findViewById(R.id.autoCompleteDestino);

        autoCompleteAdapterOrigem = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteAdapterDestino = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);

        autoCompleteOrigem.setAdapter(autoCompleteAdapterOrigem);
        autoCompleteDestino.setAdapter(autoCompleteAdapterDestino);

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

        // Configurar canais de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel1", "notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Obter a localização atual
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        // Obter informações de localização da Mapbox
        if (location != null) {
            MapboxGeocoding geocodingService = MapboxGeocoding.builder()
                    .accessToken(getResources().getString(R.string.mapbox_access_token))
                    .query(Point.fromLngLat(location.getLongitude(), location.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .languages("pt-BR")
                    .build();

            geocodingService.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                    if (response.body() != null && !response.body().features().isEmpty()) {
                        CarmenFeature feature = response.body().features().get(0);
                        String cityName = getCityNameFromFeature(feature);
                        String stateName = getStateNameFromFeature(feature);
                        String countryName = getCountryNameFromFeature(feature);

                        String locationInfo = cityName + " " + stateName + ", " + countryName;
                        autoCompleteOrigem.setText(locationInfo);
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Toast.makeText(PesquisaRota.this, "Erro ao obter descrição de origem, forneça manualmente!", Toast.LENGTH_LONG).show();
                }
            });
        }

        else{
            Toast.makeText(PesquisaRota.this, "Não foi possível obter automaticamente a origem, forneça manualmente!", Toast.LENGTH_LONG).show();
        }

        // Configurar o TextWatcher para as caixas de texto de origem e destino
        autoCompleteOrigem.addTextChangedListener(new TextWatcher() {
            private CharSequence beforeText;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeText = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                handler.removeCallbacks(autocompleteRunnable);
                handler.postDelayed(() -> consultarSugestoes(autoCompleteOrigem, autoCompleteAdapterOrigem), AUTOCOMPLETE_DELAY);
            }

        });

        autoCompleteDestino.addTextChangedListener(new TextWatcher() {
            private CharSequence beforeText;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeText = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Não é necessário implementar isso
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                handler.removeCallbacks(autocompleteRunnable);
                handler.postDelayed(() -> consultarSugestoes(autoCompleteDestino, autoCompleteAdapterDestino), AUTOCOMPLETE_DELAY);
            }
        });
    }

    private void consultarSugestoes(AutoCompleteTextView autoCompleteTextView, ArrayAdapter<String> adapter) {
        String textoDigitado = autoCompleteTextView.getText().toString().trim();

        if (!textoDigitado.isEmpty()) {
            MapboxGeocoding geocodingService = MapboxGeocoding.builder()
                    .accessToken(getResources().getString(R.string.mapbox_access_token))
                    .query(textoDigitado)
                    .geocodingTypes(GeocodingCriteria.TYPE_POI)
                    .languages("pt-BR")
                    .country("BR")
                    .build();

            geocodingService.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<CarmenFeature> features = response.body().features();
                        if (features.size() >= 2) {
                            sugestoesUnicas.clear();

                            for (CarmenFeature feature : features) {
                                String cidade = "";
                                String estado = "";
                                String pais = "";

                                for (CarmenContext context : feature.context()) {
                                    String type = context.id();
                                    String text = context.text();

                                    if (type.startsWith("place")) {
                                        cidade = text;
                                    } else if (type.startsWith("region")) {
                                        estado = text;
                                    } else if (type.startsWith("country")) {
                                        pais = text;
                                    }
                                }

                                String sugestao = cidade + " " + estado + ", " + pais;
                                sugestoesUnicas.add(sugestao);
                            }

                            adapter.clear();
                            adapter.addAll(new ArrayList<>(sugestoesUnicas));
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                    Toast.makeText(PesquisaRota.this, "Algo deu errado, verifique sua conexão!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void notificationButton(View view) {
        final String CHANNEL_ID = "channel1";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Ola passageiro")
                .setContentText("Viemos desejar boa viagem!");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());

        String enderecoOrigem = autoCompleteOrigem.getText().toString();
        String enderecoDestino = autoCompleteDestino.getText().toString();

        if(TextUtils.isEmpty(enderecoOrigem)){
            Toast.makeText(this, "A origem é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(enderecoDestino)){
            Toast.makeText(this, "O destino é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        MapboxGeocoding geocodingServiceOrigem = MapboxGeocoding.builder()
                .accessToken(getResources().getString(R.string.mapbox_access_token))
                .query(enderecoOrigem)
                .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                .build();

        geocodingServiceOrigem.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CarmenFeature featureOrigem = response.body().features().get(0);
                    Point coordenadasOrigem = featureOrigem.center();
                    double latitudeOrigem = coordenadasOrigem.latitude();
                    double longitudeOrigem = coordenadasOrigem.longitude();

                    MapboxGeocoding geocodingServiceDestino = MapboxGeocoding.builder()
                            .accessToken(getResources().getString(R.string.mapbox_access_token))
                            .query(enderecoDestino)
                            .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                            .build();

                    geocodingServiceDestino.enqueueCall(new Callback<GeocodingResponse>() {
                        @Override
                        public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                CarmenFeature featureDestino = response.body().features().get(0);
                                Point coordenadasDestino = featureDestino.center();
                                double latitudeDestino = coordenadasDestino.latitude();
                                double longitudeDestino = coordenadasDestino.longitude();

                                Intent intent = new Intent(PesquisaRota.this, RotaPontoaPonto.class);
                                intent.putExtra("latitudeOrigem", latitudeOrigem);
                                intent.putExtra("longitudeOrigem", longitudeOrigem);
                                intent.putExtra("latitudeDestino", latitudeDestino);
                                intent.putExtra("longitudeDestino", longitudeDestino);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                            Toast.makeText(PesquisaRota.this, "Algo deu errado, verifique sua conexão!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(PesquisaRota.this, "Algo deu errado, verifique sua conexão!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getCityNameFromFeature(CarmenFeature feature) {
        for (CarmenContext context : feature.context()) {
            if (context.id().startsWith("place")) {
                return context.text();
            }
        }
        return "";
    }

    private String getStateNameFromFeature(CarmenFeature feature) {
        for (CarmenContext context : feature.context()) {
            if (context.id().startsWith("region")) {
                return context.text();
            }
        }
        return "";
    }

    private String getCountryNameFromFeature(CarmenFeature feature) {
        for (CarmenContext context : feature.context()) {
            if (context.id().startsWith("country")) {
                return context.text();
            }
        }
        return "";
    }
}
