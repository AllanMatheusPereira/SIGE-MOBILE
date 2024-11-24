package com.example.sige.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sige.R;
import com.example.sige.model.Estacionamento;
import com.example.sige.model.Vaga;
import com.example.sige.Network.APIService;
import com.example.sige.Network.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.LatLngBounds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalizacaoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        apiService = RetrofitClient.getClient().create(APIService.class);

        Estacionamento estacionamento = (Estacionamento) getIntent().getSerializableExtra("estacionamento");

        if (estacionamento != null) {
            TextView textViewRua = findViewById(R.id.tvEndereco);
            textViewRua.setText("Rua: " + estacionamento.getRua());

            TextView textViewNumero = findViewById(R.id.tvNumero);
            textViewNumero.setText("Número: " + estacionamento.getNumero());

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            Button btnAbrirMaps = findViewById(R.id.btnAbrirMaps);
            btnAbrirMaps.setOnClickListener(v -> {
                confirmarVaga(estacionamento);

                abrirGoogleMaps(estacionamento);
            });
        } else {
            Toast.makeText(this, "Estacionamento não encontrado.", Toast.LENGTH_SHORT).show();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button btSair = findViewById(R.id.btSairLocalizacao);
        btSair.setOnClickListener(v -> {

            Intent intent = new Intent(LocalizacaoActivity.this, EstacionamentoActivity.class);
            startActivity(intent);
            finish();
        });
    }


    private void confirmarVaga(Estacionamento estacionamento) {
        Vaga vaga = new Vaga();
        vaga.setId(estacionamento.getId());
        vaga.setStatus(1);

        apiService.atualizarVaga(vaga.getId(), vaga.getStatus()).enqueue(new Callback<Vaga>() {
            @Override
            public void onResponse(Call<Vaga> call, Response<Vaga> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LocalizacaoActivity.this, "Vaga confirmada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LocalizacaoActivity.this, "Erro ao confirmar vaga.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Vaga> call, Throwable t) {
                Toast.makeText(LocalizacaoActivity.this, "Erro de comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissões de localização não concedidas.", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        android.location.Location location = task.getResult();
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        mMap.addMarker(new MarkerOptions().position(userLocation).title("Sua localização"));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                        Estacionamento estacionamento = (Estacionamento) getIntent().getSerializableExtra("estacionamento");
                        if (estacionamento != null) {
                            String urlMaps = estacionamento.getUrlMaps();

                            LatLng estacionamentoLocation = extrairCoordenadasDeUrl(urlMaps);
                            if (estacionamentoLocation != null) {
                                mMap.addMarker(new MarkerOptions().position(estacionamentoLocation).title(estacionamento.getRua()));

                                drawRoute(userLocation, estacionamentoLocation);
                            } else {
                                Toast.makeText(LocalizacaoActivity.this, "Não foi possível extrair as coordenadas do estacionamento.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(LocalizacaoActivity.this, "Não foi possível obter a localização.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void abrirGoogleMaps(Estacionamento estacionamento) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        android.location.Location location = task.getResult();
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        String urlMaps = estacionamento.getUrlMaps();

                        LatLng destinoLocation = extrairCoordenadasDeUrl(urlMaps);
                        if (destinoLocation != null) {
                            String uri = "https://www.google.com/maps/dir/?api=1&origin=" +
                                    userLocation.latitude + "," + userLocation.longitude +
                                    "&destination=" + destinoLocation.latitude + "," + destinoLocation.longitude +
                                    "&travelmode=driving";

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");

                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Google Maps não está instalado.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LocalizacaoActivity.this, "Coordenadas do estacionamento inválidas.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LocalizacaoActivity.this, "Não foi possível obter sua localização.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private LatLng extrairCoordenadasDeUrl(String url) {
        Pattern pattern = Pattern.compile("@(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            double latitude = Double.parseDouble(matcher.group(1));
            double longitude = Double.parseDouble(matcher.group(2));
            return new LatLng(latitude, longitude);
        }
        return null;
    }

    private void drawRoute(LatLng userLocation, LatLng estacionamentoLocation) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(userLocation)
                .add(estacionamentoLocation)
                .color(getResources().getColor(R.color.purple_500));

        Polyline polyline = mMap.addPolyline(polylineOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(userLocation);
        builder.include(estacionamentoLocation);
        LatLngBounds bounds = builder.build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}
