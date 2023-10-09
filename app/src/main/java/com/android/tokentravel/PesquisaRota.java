package com.android.tokentravel;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.text.Normalizer;

public class PesquisaRota extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteOrigem;
    private AutoCompleteTextView autoCompleteDestino;
    private static final int REQUEST_CODE_ORIGEM = 1;
    private static final int REQUEST_CODE_DESTINO = 2;
    private ImageView imagemautocompleteorigem;
    private ImageView imagemautocompletedestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_rota);

        imagemautocompleteorigem = findViewById(R.id.autcompleteorigemcampo);
        imagemautocompletedestino = findViewById(R.id.autcompletedestinocampo);

        autoCompleteOrigem = findViewById(R.id.autoCompleteOrigem);
        autoCompleteDestino = findViewById(R.id.autoCompleteDestino);

        // Mapbox access token is configured here.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // Inicializar os elementos de UI

        // Configurar o clique do botão
        imagemautocompleteorigem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chame o método para selecionar a origem
                openPlaceAutocompleteOrigem(REQUEST_CODE_ORIGEM);
            }
        });

        imagemautocompletedestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chame o método para selecionar a origem
                openPlaceAutocompleteDestino(REQUEST_CODE_ORIGEM);
            }
        });

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

                        String locationInfo = cityName + ", " + stateName + ", " + countryName;
                        autoCompleteOrigem.setText(locationInfo);
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Toast.makeText(PesquisaRota.this, "Erro ao obter descrição de origem, forneça manualmente!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(PesquisaRota.this, "Não foi possível obter automaticamente a origem, forneça manualmente!", Toast.LENGTH_LONG).show();
        }

    }

    public void openPlaceAutocompleteOrigem(int requestCodeOrigem) {
        openPlaceAutocomplete(REQUEST_CODE_ORIGEM, "Forneça a origem");
    }

    public void openPlaceAutocompleteDestino(int requestCodeOrigem) {
        openPlaceAutocomplete(REQUEST_CODE_DESTINO, "Forneça o destino");
    }

    private void openPlaceAutocomplete(int requestCode, String title) {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#23238E"))
                        .hint(title) // Define o texto aqui
                        .limit(10)
                        .country("BR")
                        .language("pt-BR")
                        .build(PlaceOptions.MODE_CARDS))
                .build(PesquisaRota.this);

        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            String placeName = selectedCarmenFeature.placeName();
            String coordinates = selectedCarmenFeature.center().toString();

            // Verificar qual solicitação foi retornada
            if (requestCode == REQUEST_CODE_ORIGEM) {
                autoCompleteOrigem.setText(placeName);
                // Após definir a origem, chame o método para selecionar o destino
            } else if (requestCode == REQUEST_CODE_DESTINO) {
                autoCompleteDestino.setText(placeName);
            }
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

        if (TextUtils.isEmpty(enderecoOrigem)) {
            Toast.makeText(this, "A origem é obrigatória.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(enderecoDestino)) {
            Toast.makeText(this, "O destino é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        MapboxGeocoding geocodingServiceOrigem = MapboxGeocoding.builder()
                .accessToken(getResources().getString(R.string.mapbox_access_token))
                .query(enderecoOrigem)
                .geocodingTypes(GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_POI)
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
                            .geocodingTypes(GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_POI)
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
                                Spinner spinnerUserType = findViewById(R.id.spinner_user_type);
                                String dia = spinnerUserType.getSelectedItem().toString().toLowerCase(); // Converter para minúsculas

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("nomeOrigem", enderecoOrigem);
                                editor.putString("nomeDestino", enderecoDestino);
                                editor.putString("diaSemana", removeAcentosEMinusculas(dia)); // Aplicar a função
                                editor.apply();
                                Log.d("SharedPreferences", "nomeOrigem: " + enderecoOrigem);
                                Log.d("SharedPreferences", "nomeDestino: " + enderecoDestino);
                                Log.d("SharedPreferences", "diaSemana: " + removeAcentosEMinusculas(dia)); // Aplicar a função
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

    private String removeAcentosEMinusculas(String input) {
        String semAcentos = Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return semAcentos.toLowerCase();
    }
}
