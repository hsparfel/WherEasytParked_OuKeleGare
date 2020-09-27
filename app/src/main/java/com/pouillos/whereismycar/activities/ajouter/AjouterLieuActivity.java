package com.pouillos.whereismycar.activities.ajouter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.whereismycar.R;
import com.pouillos.whereismycar.activities.NavDrawerActivity;
import com.pouillos.whereismycar.entities.LieuEnregistre;
import com.pouillos.whereismycar.utils.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AjouterLieuActivity extends NavDrawerActivity {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.fabAddLieu)
    FloatingActionButton fabAddLieu;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;


    private LocationManager locationManager;

    double actualLatitude;
    double actualLongitude;
   /* @BindView(R.id.textLatitude)
    TextInputEditText textLatitude;
    @BindView(R.id.layoutLatitude)
    TextInputLayout layoutLatitude;
    @BindView(R.id.textLongitude)
    TextInputEditText textLongitude;
    @BindView(R.id.layoutLongitude)
    TextInputLayout layoutLongitude;*/

    @BindView(R.id.textAddresseGeo)
    TextInputEditText textAddresseGeo;
    @BindView(R.id.layoutAddresseGeo)
    TextInputLayout layoutAddresseGeo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_ajouter_lieu);
        Stetho.initializeWithDefaults(this);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        textView.setText(DateUtils.ecrireDateLettre(new Date()));

        progressBar.setVisibility(View.VISIBLE);
        AjouterLieuActivity.AsyncTaskRunnerBD runnerBD = new AjouterLieuActivity.AsyncTaskRunnerBD();
        runnerBD.execute();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                actualLatitude = location.getLatitude();
                actualLongitude = location.getLongitude();
            //    textLatitude.setText("" + actualLatitude);
              //  textLongitude.setText("" + actualLongitude);
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
                    Log.d("Location Changes", location.toString());
                    actualLatitude = location.getLatitude();
                    actualLongitude = location.getLongitude();
                  //  textLatitude.setText("" + actualLatitude);
                 //   textLongitude.setText("" + actualLongitude);
                    convertirCoordonneesToAdresse();
                    Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
                }
            });

        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


    }

    @OnClick(R.id.fabAddLieu)
    public void setFabAddLieuClick() {
        ouvrirActiviteSuivante(this, AjouterLieuEnregistreActivity.class,false);
    }

    public void convertirCoordonneesToAdresse() {
        Geocoder coder = new Geocoder(AjouterLieuActivity.this);
        try {
            List<Address> listAddress = coder.getFromLocation(actualLatitude,actualLongitude,1);
            if (listAddress.size()>0) {
                //textAddresseGeo.setText(listAddress.get(0).toString());
                textAddresseGeo.setText(listAddress.get(0).getAddressLine(0));
            }
        } catch (IOException e) {
            textAddresseGeo.setText("Erreur - non trouvé");
            e.printStackTrace();
        }

    }

    private class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            publishProgress(10);
            remplirBDLieuEnregistre();
            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(AjouterLieuActivity.this, R.string.text_DB_created, Toast.LENGTH_LONG).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    private void remplirBDLieuEnregistre() {
        if (lieuEnregistreDao.count() == 0) {
            LieuEnregistre lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Non Defini");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Parking Aerien");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Rue Droite");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Rue Gauche");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Devant Donadey");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Devant Parc Enfants");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Box");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Box Locatif");
            lieuEnregistreDao.insert(lieuEnregistre);
        }
    }

}
