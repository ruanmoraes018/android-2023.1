package com.android.tokentravel;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.Stack;

public class Navigation_View extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ImageView btnUpdateLocation; // ImageView for updating location
    private Button botaoPesquisarRota;
    private Button btpesquisaRota;
    private Stack<Fragment> fragmentStack;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private SymbolLayer iconLayer;
    private GeoJsonSource iconGeoJsonSource;
    private static final int DOUBLE_BACK_EXIT_DELAY = 2000; // Tempo limite para pressionar o botão voltar novamente (2 segundos)
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_navigation_view);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);
        botaoPesquisarRota = findViewById(R.id.botao_pesquisar_rota);
        fragmentStack = new Stack<>();
        IniciarBotPesquisaRota();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                Navigation_View.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        style.addImage("red-pin-icon-id", BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_localizacao)));

                        iconLayer = new SymbolLayer("icon-layer-id", "icon-source-id").withProperties(
                                iconImage("red-pin-icon-id"),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                                iconOffset(new Float[]{0f, -9f})
                        );
                        style.addLayer(iconLayer);

                        iconGeoJsonSource = new GeoJsonSource("icon-source-id", Feature.fromGeometry(Point.fromLngLat(0, 0)));
                        style.addSource(iconGeoJsonSource);

                        enableLocation();
                    }
                });
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(Navigation_View.this, "Meus Dados", Toast.LENGTH_SHORT);
                    fragmentR(new MenuFragment());
                    botaoPesquisarRota.setVisibility(View.GONE);
                } else if (itemId == R.id.favoritos) {
                    fragmentR(new FavoritosFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(Navigation_View.this, "Favoritos", Toast.LENGTH_SHORT);
                    botaoPesquisarRota.setVisibility(View.GONE);
                } else if (itemId == R.id.suporte) {
                    fragmentR(new SuporteFragment());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(Navigation_View.this, "Fale Conosco", Toast.LENGTH_SHORT);
                    botaoPesquisarRota.setVisibility(View.GONE);
                } else {
                    // Item de deslogar da aplicação.
                    // Limpe o token de autenticação das preferências compartilhadas
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("authToken");
                    editor.apply();

                    // Redirecione para a tela de login
                    Intent intent = new Intent(Navigation_View.this, Form_Login.class);
                    startActivity(intent);
                    finish(); // Encerre a tela de navegação
                    Toast.makeText(getApplicationContext(), "Obrigado por usar o TokenTravel, até logo!", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        btpesquisaRota = findViewById(R.id.botao_pesquisar_rota);
        btpesquisaRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation_View.this, PesquisaRota.class);
                startActivity(intent);
            }
        });

        // Find and set click listener for the "Update Location" ImageView
        btnUpdateLocation = findViewById(R.id.btn_update_location);
        btnUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocation();
            }
        });
    }

    private void fragmentR(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentStack.push(fragment);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!fragmentStack.isEmpty()) {
            fragmentStack.pop();

            if (!fragmentStack.isEmpty()) {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentStack.clear();
                botaoPesquisarRota.setVisibility(View.VISIBLE);
            } else {
                super.onBackPressed();
                botaoPesquisarRota.setVisibility(View.VISIBLE);
            }
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finishAffinity(); // Fecha todas as atividades no aplicativo
            } else {
                doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Pressione voltar novamente para sair", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, DOUBLE_BACK_EXIT_DELAY);
            }
        }
    }

    private void IniciarBotPesquisaRota() {
        btpesquisaRota = findViewById(R.id.botao_pesquisar_rota);
    }

    private void enableLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permissões de localização se ainda não foram concedidas
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else {
            // Permissões de localização já concedidas, inicializar o cliente de localização
            initLocationClient();
        }
    }

    private void initLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        // Update the user's location on the map
                        addMarker(location.getLatitude(), location.getLongitude());
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()), 15.0));

                        // Stop receiving location updates
                        fusedLocationClient.removeLocationUpdates(locationCallback);

                        // Show the "Update Location" ImageView after getting the initial location
                        btnUpdateLocation.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        // Start receiving location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            initLocationClient();

            btnUpdateLocation.setVisibility(View.GONE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    private void addMarker(double latitude, double longitude) {
        if (mapboxMap != null && mapboxMap.getStyle() != null) {
            if (iconGeoJsonSource != null) {
                mapboxMap.getStyle().removeLayer(iconLayer);
                mapboxMap.getStyle().removeSource(iconGeoJsonSource.getId());
            }

            iconGeoJsonSource = new GeoJsonSource("icon-source-id",
                    Feature.fromGeometry(Point.fromLngLat(longitude, latitude)));
            mapboxMap.getStyle().addSource(iconGeoJsonSource);

            iconLayer = new SymbolLayer("icon-layer-id", "icon-source-id").withProperties(
                    iconImage("red-pin-icon-id"),
                    iconIgnorePlacement(true),
                    iconAllowOverlap(true),
                    iconOffset(new Float[]{0f, -9f})
            );
            mapboxMap.getStyle().addLayer(iconLayer);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissões de localização concedidas, inicializar o cliente de localização
                initLocationClient();
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
