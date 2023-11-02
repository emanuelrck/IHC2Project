package pt.app.ihc2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;

    private Button cameraBtn;
    private Button clipboardBtn;
    private Button caminhadaBtn;
    private boolean visitedFonte = true;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        caminhadaBtn = findViewById(R.id.caminhoBtn);
        clipboardBtn = findViewById(R.id.inventarioBtn);
        clipboardBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openClipBoar();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();



    }
    public void openClipBoar(){
        Intent intent = new Intent(this,Clipboard.class);
        intent.putExtra("fonte",visitedFonte);
        startActivity(intent);
    }
    public void openCamera(){
        Intent intent = new Intent(this,Camerak3.class);
        startActivity(intent);
    }
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE); // AQUI DIFERENTE
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;

        myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //fazer marker estatico
        LatLng botanicoFonte = new LatLng(40.2058, -8.4214);
        //myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(botanico, 17.0f));
        MarkerOptions optionsFonte = new MarkerOptions().position(botanicoFonte).title("Botanico");
        optionsFonte.icon(BitmapDescriptorFactory.fromResource(R.drawable.fonte_marker));
        myMap.addMarker(optionsFonte);

        // current location
        LatLng current = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17.0f));
        MarkerOptions optionsCurrent = new MarkerOptions().position(current).title("Botanico");


        optionsCurrent.icon(BitmapDescriptorFactory.fromResource(R.drawable.current));
        myMap.addMarker(optionsCurrent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            } else{
                Toast.makeText(this,"Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //este esta relacionado com a pasta menu em res

        int id = item.getItemId();

        if (id == R.id.arvore){
            //mostrar arvore info
        }
        else if (id == R.id.fontanario){
            //mostrar fonte info
        }

        return super.onOptionsItemSelected(item);
    }

}
