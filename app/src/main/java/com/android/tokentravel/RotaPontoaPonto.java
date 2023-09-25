package com.android.tokentravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class RotaPontoaPonto extends AppCompatActivity {

    private MapView mapView;
    private MapboxDirections client;
    private Point origin;
    private Point destination;
    private double latitudeOrigem;
    private double longitudeOrigem;
    private double latitudeDestino;
    private double longitudeDestino;

    private Button btListadeMoto;
    private ImageView ajustarCameraImageView; // Adicione o ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rota_pontoa_ponto);

        btListadeMotoristas();

        // Recupere as coordenadas de origem e destino da Intent
        latitudeOrigem = getIntent().getDoubleExtra("latitudeOrigem", 0.0);
        longitudeOrigem = getIntent().getDoubleExtra("longitudeOrigem", 0.0);
        latitudeDestino = getIntent().getDoubleExtra("latitudeDestino", 0.0);
        longitudeDestino = getIntent().getDoubleExtra("longitudeDestino", 0.0);

        // Inicializando o MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        origin = Point.fromLngLat(longitudeOrigem, latitudeOrigem);
                        destination = Point.fromLngLat(longitudeDestino, latitudeDestino);

                        initSource(style);
                        getRoute(mapboxMap, origin, destination);

                        // Ajustando a camera para concentrar na rota
                        LatLngBounds routeBounds = new LatLngBounds.Builder()
                                .include(new LatLng(latitudeOrigem, longitudeOrigem))
                                .include(new LatLng(latitudeDestino, longitudeDestino))
                                .build();

                        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 200), 2000);

                        LineLayer routeLayer = new LineLayer("route-layer-id", "route-source-id");
                        routeLayer.setProperties(
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineColor(Color.parseColor("#009688"))
                        );
                        style.addLayer(routeLayer);

                        style.addImage("red-pin-icon-id", BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_inicio_map)));

                        SymbolLayer originSymbolLayer = new SymbolLayer("origin-symbol-layer-id", "icon-source-id1")
                                .withProperties(
                                        iconImage("red-pin-icon-id"),
                                        iconIgnorePlacement(true),
                                        iconAllowOverlap(true),
                                        iconOffset(new Float[]{0f, -9f})
                                );
                        style.addLayer(originSymbolLayer);

                        style.addImage("blue-pin-icon-id", BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_destino_map)));

                        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "icon-source-id2")
                                .withProperties(
                                        iconImage("blue-pin-icon-id"),
                                        iconIgnorePlacement(true),
                                        iconAllowOverlap(true),
                                        iconOffset(new Float[]{0f, -9f})
                                );
                        style.addLayer(destinationSymbolLayer);

                        GeoJsonSource iconGeoJsonSource1 = new GeoJsonSource("icon-source-id1", FeatureCollection.fromFeatures(new Feature[]{
                                Feature.fromGeometry(Point.fromLngLat(longitudeOrigem, latitudeOrigem))
                        }));
                        style.addSource(iconGeoJsonSource1);

                        GeoJsonSource iconGeoJsonSource2 = new GeoJsonSource("icon-source-id2", FeatureCollection.fromFeatures(new Feature[]{
                                Feature.fromGeometry(Point.fromLngLat(longitudeDestino, latitudeDestino))
                        }));
                        style.addSource(iconGeoJsonSource2);
                    }
                });
            }
        });

        // Encontre o ImageView no layout e atribua um clique a ele
        ajustarCameraImageView = findViewById(R.id.ajustarCameraImageView);
        ajustarCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajustarCameraParaRota();
            }
        });

        btListadeMoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RotaPontoaPonto.this, Lista_motoras_dispon.class);
                startActivity(intent);
            }
        });

    }

    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource("route-source-id"));
    }

    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null || response.body().routes().size() < 1) {
                    Toast.makeText(RotaPontoaPonto.this, "Nenhuma rota encontrada", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Obtenha o percurso de direções
                DirectionsRoute currentRoute = response.body().routes().get(0);

                if (mapboxMap != null) {
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            GeoJsonSource source = style.getSourceAs("route-source-id");

                            if (source != null) {
                                source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Toast.makeText(RotaPontoaPonto.this, "Erro: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancelar a solicitação da API de direções
        if (client != null) {
            client.cancelCall();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void btListadeMotoristas() {
        btListadeMoto = findViewById(R.id.verificarMotoristasButton);
    }

    public void ajustarCameraParaRota() {
        LatLngBounds routeBounds = new LatLngBounds.Builder()
                .include(new LatLng(latitudeOrigem, longitudeOrigem))
                .include(new LatLng(latitudeDestino, longitudeDestino))
                .build();
        ajustarCameraImageView.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ajustarCameraImageView.setVisibility(View.VISIBLE);
            }
        }, 2000);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(routeBounds, 200), 2000);
            }
        });

    }
}
