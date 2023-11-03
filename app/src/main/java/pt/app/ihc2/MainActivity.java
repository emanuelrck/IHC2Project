package pt.app.ihc2;
import android.os.AsyncTask;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final double EARTH_RADIUS = 6371000;
    private static final double DISTANCE_POINT = 5;//metros
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    private Marker markerFonte;
    private Marker markerArv;
    private List<Polyline> polylines = new ArrayList<>();
    private List<LatLng> referencePoints = new ArrayList<>();

    LatLng current;


    private Button cameraBtn;
    private Button clipboardBtn;
    private Button caminhadaBtn;
    private List <Boolean> visitedPlaces = new ArrayList<>();
    private boolean visitedFonte = true;
    private boolean visitedArv = false;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtn();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();



    }
    public void initBtn(){
        cameraBtn = findViewById(R.id.cameraBtn);
        caminhadaBtn = findViewById(R.id.caminhoBtn);
        clipboardBtn = findViewById(R.id.inventarioBtn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        caminhadaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makepath();
            }
        });
        clipboardBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openClipBoar();
            }
        });
    }
    public void openClipBoar(){
        Intent intent = new Intent(this,Clipboard.class);
        intent.putExtra("fonte",visitedFonte);
        intent.putExtra("arvore",visitedArv);
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

        //-------------- Markers ---------

        //fazer marker estatico
        // FONTE
        LatLng botanicoFonte = new LatLng(40.2058, -8.4214);
        referencePoints.add(botanicoFonte);
        visitedPlaces.add(true); //TODO: ONLY TRUE TO DEMO PORPOSE
        MarkerOptions optionsFonte = new MarkerOptions().position(botanicoFonte).title("Botanico");
        if(visitedFonte) optionsFonte.icon(BitmapDescriptorFactory.fromResource(R.drawable.fonte_marker));
        else optionsFonte.icon(BitmapDescriptorFactory.fromResource(R.drawable.unknown1));
        markerFonte = myMap.addMarker(optionsFonte);


        //ARVORE
        LatLng botanicoArv = new LatLng(40.2050, -8.4210);
        referencePoints.add(botanicoArv);
        visitedPlaces.add(false);
        MarkerOptions optionsArv = new MarkerOptions().position(botanicoArv).title("Arvore");
        if(visitedArv)optionsArv.icon(BitmapDescriptorFactory.fromResource(R.drawable.arvoreflor));
        else optionsArv.icon(BitmapDescriptorFactory.fromResource(R.drawable.unknown1));
        markerArv = myMap.addMarker(optionsArv);

        // current location
        current = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17.0f));
        MarkerOptions optionsCurrent = new MarkerOptions().position(current).title("currentLocation");
        optionsCurrent.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        myMap.addMarker(optionsCurrent);

        int cont = 0;
        for (LatLng point : referencePoints ) {
            if (calculateDistance(current, point) < DISTANCE_POINT) {
                visitedPlaces.set(cont, true);
                cont++;
            }
        }

        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearPreviousPolylines();
                drawLineBetweenMarkers(marker.getPosition(), current);

                return true;
            }
        });
        //------------------------------------


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
    private void drawLineBetweenMarkers(LatLng orig, LatLng dest) {
        if (myMap != null) {

            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(orig, dest)
                    .width(5) // Line width
                    .color(Color.GREEN); // Line color
          polylines.add(myMap.addPolyline(polylineOptions));
        }
    }
    private void clearPreviousPolylines() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        polylines.clear(); // Clear the list
    }

    private void makepath(){
        LatLng last = current;
        if(polylines.size() == 0){
            for (LatLng point : referencePoints ) {
               drawLineBetweenMarkers(last,point);
               last = point;
            }

        }else if (polylines.size() == referencePoints.size()){
            clearPreviousPolylines();
        }else {
            clearPreviousPolylines();
            for (LatLng point : referencePoints ) {
                drawLineBetweenMarkers(last,point);
                last = point;
            }
        }
    }
    public static double calculateDistance(LatLng from, LatLng to) {
        double lat1 = Math.toRadians(from.latitude);
        double lon1 = Math.toRadians(from.longitude);
        double lat2 = Math.toRadians(to.latitude);
        double lon2 = Math.toRadians(to.longitude);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

}
