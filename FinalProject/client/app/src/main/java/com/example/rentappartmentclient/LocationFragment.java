package com.example.rentappartmentclient;

import static org.osmdroid.tileprovider.util.StorageUtils.getStorage;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.rentappartmentclient.model.database.Offer;
import com.example.rentappartmentclient.retrofit.DataManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.config.IConfigurationProvider;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationFragment extends Fragment {
        MapView mapView = null;
        SeekBar seekBar = null;
        EditText streetText = null;
        EditText houseText = null;
        List<Offer> offers = null;
        Geocoder geocoder = null;
        double initialLatitude = 0;
        double initialLongitude = 0;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                View locationView = inflater.inflate(R.layout.fragment_location, container, false);

                seekBar = locationView.findViewById(R.id.seekBar);
                seekBar.setOnSeekBarChangeListener(new seekBarListener());

                streetText = locationView.findViewById(R.id.etStreet);
                houseText = locationView.findViewById(R.id.etHouse);

                geocoder = new Geocoder(locationView.getContext(), Locale.getDefault());

                IConfigurationProvider provider = Configuration.getInstance();
                provider.setUserAgentValue(BuildConfig.APPLICATION_ID);
                provider.setOsmdroidBasePath(getStorage());
                provider.setOsmdroidTileCache(getStorage());

                mapView = (MapView) locationView.findViewById(R.id.mapview);
                mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

                newStartLocation();

                streetText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}

                        @Override
                        public void afterTextChanged(Editable s) {
                                newStartLocation();
                        }
                });

                offers = DataManager.getInstance().getOfferList();
                return locationView;
        }

        public void newStartLocation() {
                mapView.getOverlays().removeAll(mapView.getOverlays());
                mapView.invalidate();

                String street = streetText.getText().toString();
                String house = houseText.getText().toString();
                String initialLocationName = "Пермь, " + street + ", " + house;
                try {
                        Address address  = geocoder.getFromLocationName(initialLocationName, 1).get(0);
                        initialLatitude = address.getLatitude();
                        initialLongitude = address.getLongitude();

                        Marker startMarker = new Marker(mapView);
                        GeoPoint startPoint = new GeoPoint(initialLatitude, initialLongitude);
                        startMarker.setPosition(startPoint);
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        startMarker.setIcon(getResources().getDrawable(R.drawable.ic_initial_location_marker));
                        mapView.getOverlays().add(startMarker);
                        mapView.getController().setZoom(13);
                        mapView.getController().setCenter(startPoint);
                } catch (Exception e) {
                }
        }

        private class seekBarListener implements SeekBar.OnSeekBarChangeListener {
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {}

                public void onStartTrackingTouch(SeekBar seekBar) {
                        mapView.getOverlays().removeAll(mapView.getOverlays());
                        mapView.invalidate();

                        Marker startMarker = new Marker(mapView);
                        GeoPoint startPoint = new GeoPoint(initialLatitude, initialLongitude);
                        startMarker.setPosition(startPoint);
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        startMarker.setIcon(getResources().getDrawable(R.drawable.ic_initial_location_marker));
                        mapView.getOverlays().add(startMarker);
                        mapView.getController().setZoom(13);
                        mapView.getController().setCenter(startPoint);
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                        for (Offer offer : offers) {
                                try {
                                        String locationName = "Пермь, " + offer.getAddress().getStreet() +
                                                ", " + offer.getAddress().getHouse();
                                        List<Address> addresses  = geocoder.getFromLocationName(locationName, 1);

                                        GeoPoint startPoint = new GeoPoint(addresses.get(0).getLatitude(),
                                                addresses.get(0).getLongitude());

                                        double latitude = startPoint.getLatitude();
                                        double longitude = startPoint.getLongitude();
                                        double distance = Math.sqrt(Math.pow(initialLatitude - latitude, 2) +
                                                Math.pow(initialLongitude - longitude, 2));
                                        if (distance*100 <= seekBar.getProgress()) {
                                                Marker startMarker = new Marker(mapView);
                                                startMarker.setPosition(startPoint);
                                                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                                startMarker.setIcon(getResources().getDrawable(R.drawable.ic_location_marker));
                                                mapView.getOverlays().add(startMarker);
                                        }
                                } catch (IOException e) {
                                        throw new RuntimeException(e);
                                }
                        }
                }

        }
}