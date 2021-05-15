package com.pouillcorp.oukelegare.activities.ajouter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.stetho.Stetho;
import com.pouillcorp.oukelegare.R;
import com.pouillcorp.oukelegare.activities.AccueilActivity;
import com.pouillcorp.oukelegare.activities.NavDrawerLieuActivity;
import com.pouillcorp.oukelegare.entities.Lieu;
import com.pouillcorp.oukelegare.entities.LieuEnregistre;
import com.pouillcorp.oukelegare.utils.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AjouterLieuActivity extends NavDrawerLieuActivity implements AdapterView.OnItemClickListener {

    private LocationManager locationManager;

    double actualLatitude;
    double actualLongitude;

    List<LieuEnregistre> listeLieuEnregistre;
    LieuEnregistre groupeLieuEnregistre;

    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);

        bottomNavigationViewMenu.findItem(R.id.bottom_navigation_home).setChecked(false);
        bottomNavigationViewMenu.findItem(R.id.bottom_navigation_add_lieu).setChecked(true);

        textView.setText(R.string.emplacement_actuel);

        masquer_inutile();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                actualLatitude = location.getLatitude();
                actualLongitude = location.getLongitude();
                convertirCoordonneesToAdresse();
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 10, new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

                @Override
                public void onLocationChanged(Location location) {
                    actualLatitude = location.getLatitude();
                    actualLongitude = location.getLongitude();
                    convertirCoordonneesToAdresse();
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,new String[]
            {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        listeLieuEnregistre = lieuEnregistreDao.loadAll();
        buildDropdownMenu(listeLieuEnregistre,AjouterLieuActivity.this,selectLieuEnregistre);

        selectLieuEnregistre.setOnItemClickListener(this);
        selectLieuEnregistre.setText(getString(R.string.a_preciser),false);
    }

    public void masquer_inutile() {
        layoutName.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ouvrirActiviteSuivante(this, AjouterLieuActivity.class, false);
                }
                return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        groupeLieuEnregistre = listeLieuEnregistre.get(position);
        if (position == 0) {
            fabDeleteLieu.hide();
            layoutAddresseGeo.setVisibility(View.VISIBLE);
            layoutDetail.setVisibility(View.VISIBLE);
            textDetail.setText(null);
        } else {
            fabDeleteLieu.show();
            layoutAddresseGeo.setVisibility(View.GONE);
            textDetail.setText(groupeLieuEnregistre.getDetail());
        }
    }

    @OnClick(R.id.fabSaveLieu)
    public void setFabSaveLieuClick() {
        Lieu lieu = new Lieu();
        Date date = new Date();
        lieu.setDate(date);
        lieu.setDateString(DateUtils.ecrireDateHeure(date));
        lieu.setAdresse(textAddresseGeo.getText().toString());
        lieu.setLatitude(actualLatitude);
        lieu.setLongitude(actualLongitude);
        lieu.setDetail(textDetail.getText().toString());
        if (groupeLieuEnregistre != null) {
            lieu.setLieuEnregistre(groupeLieuEnregistre);
        }
        lieuDao.insert(lieu);
        ouvrirActiviteSuivante(this, AccueilActivity.class,false);
    }

    @OnClick(R.id.fabDeleteLieu)
    public void setFabDeleteLieuClick() {
        LieuEnregistre lieu = new LieuEnregistre();
        if (groupeLieuEnregistre != null) {
            lieu = lieuEnregistreDao.load(groupeLieuEnregistre.getId());
        }
        lieuEnregistreDao.delete(lieu);
        ouvrirActiviteSuivante(this, AjouterLieuActivity.class,false);
    }

    public void convertirCoordonneesToAdresse() {
        Geocoder coder = new Geocoder(AjouterLieuActivity.this);
        try {
            List<Address> listAddress = coder.getFromLocation(actualLatitude,actualLongitude,1);
            if (listAddress.size()>0) {
                textAddresseGeo.setText(listAddress.get(0).getAddressLine(0));
            }
        } catch (IOException e) {
            textAddresseGeo.setText(R.string.ErrorGeo);
            e.printStackTrace();
        }

    }
}
