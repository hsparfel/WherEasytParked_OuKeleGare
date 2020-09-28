package com.pouillos.whereismycar.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
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

import com.pouillos.whereismycar.activities.ajouter.AjouterLieuActivity;
import com.pouillos.whereismycar.activities.ajouter.AjouterLieuEnregistreActivity;
import com.pouillos.whereismycar.entities.Lieu;
import com.pouillos.whereismycar.entities.LieuEnregistre;
import com.pouillos.whereismycar.utils.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AccueilActivity extends NavDrawerActivity {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.selectLieuEnregistre)
    TextInputEditText selectLieuEnregistre;
    @BindView(R.id.listLieuEnregistre)
    TextInputLayout listLieuEnregistre;

    @BindView(R.id.textAddresseGeo)
    TextInputEditText textAddresseGeo;
    @BindView(R.id.layoutAddresseGeo)
    TextInputLayout layoutAddresseGeo;
    @BindView(R.id.textDate)
    TextInputEditText textDate;
    @BindView(R.id.layoutDate)
    TextInputLayout layoutDate;
    @BindView(R.id.textDetail)
    TextInputEditText textDetail;
    @BindView(R.id.layoutDetail)
    TextInputLayout layoutDetail;

    Lieu actualLieu;
    List<LieuEnregistre> listeLieuEnregistre;

    @BindView(R.id.fabGoogleMap)
    FloatingActionButton fabGoogleMap;
    @BindView(R.id.fabWaze)
    FloatingActionButton fabWaze;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Stetho.initializeWithDefaults(this);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        textView.setText(DateUtils.ecrireDateLettre(new Date()));

        progressBar.setVisibility(View.VISIBLE);
        AccueilActivity.AsyncTaskRunnerBD runnerBD = new AccueilActivity.AsyncTaskRunnerBD();
        runnerBD.execute();

        listeLieuEnregistre = lieuEnregistreDao.loadAll();
    }

    private class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            publishProgress(10);
            remplirBDLieuEnregistre();
            publishProgress(20);
            recupLastLieu();
            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            if (actualLieu != null) {
                afficherChampLieu();
                remplirChampLieu();
            } else {
                masquerChampLieu();
            }
            masquerChampLieu(actualLieu.getLieuEnregistreId() <= 1);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    private void afficherChampLieu() {
        listLieuEnregistre.setVisibility(View.VISIBLE);
        layoutDate.setVisibility(View.VISIBLE);
        layoutAddresseGeo.setVisibility(View.VISIBLE);
        layoutDetail.setVisibility(View.VISIBLE);
    }

    private void masquerChampLieu() {
        listLieuEnregistre.setVisibility(View.GONE);
        layoutDate.setVisibility(View.GONE);
        layoutAddresseGeo.setVisibility(View.GONE);
        layoutDetail.setVisibility(View.GONE);
        fabGoogleMap.setVisibility(View.GONE);
        fabWaze.setVisibility(View.GONE);
    }

    private void masquerChampLieu(boolean bool) {
        if (bool) {
            listLieuEnregistre.setVisibility(View.GONE);
            if (textDetail.getText().toString().equalsIgnoreCase("/")) {
                layoutDetail.setVisibility(View.GONE);
            }
        } else {
            layoutAddresseGeo.setVisibility(View.GONE);
            layoutDetail.setVisibility(View.GONE);
            fabGoogleMap.setVisibility(View.GONE);
            fabWaze.setVisibility(View.GONE);
        }
    }

    private void recupLastLieu() {
        List<Lieu> listLieu = lieuDao.loadAll();
        if (listLieu.size()>0) {
            actualLieu = listLieu.get(listLieu.size()-1);
        }
    }

    private void remplirChampLieu() {
        textAddresseGeo.setText(""+actualLieu.getAdresse());
        textDate.setText(actualLieu.getDateString());
        textDate.setText(DateUtils.ecrireDateHeureLettre(actualLieu.getDate()));
        textDetail.setText((actualLieu.getDetail().equals("")) ? "/" : actualLieu.getDetail());
        if (actualLieu.getLieuEnregistre() != null) {
            selectLieuEnregistre.setText(actualLieu.getLieuEnregistre().getNom());
        } else {
            selectLieuEnregistre.setText(listeLieuEnregistre.get(0).getNom());
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
            lieuEnregistre.setNom("Box");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Box Locatif");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Devant Donadey");
            lieuEnregistreDao.insert(lieuEnregistre);

            lieuEnregistre = new LieuEnregistre();
            lieuEnregistre.setNom("Devant Parc Enfants");
            lieuEnregistreDao.insert(lieuEnregistre);
        }
    }

    @OnClick(R.id.fabGoogleMap)
    public void fabGoogleMapClick() {
        String url = "geo:";
        String addr = "";
        if (actualLieu.getLatitude() != 0 && actualLieu.getLongitude() != 0) {
            url += actualLieu.getLatitude()+","+actualLieu.getLongitude();
            url += "?q="+actualLieu.getLatitude()+","+actualLieu.getLongitude();
        } else if (actualLieu.getAdresse() != null) {
            url += "0,0?q=";
            addr += Uri.parse(actualLieu.getAdresse());
            url += addr;
        }
        Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.fabWaze)
    public void fabWazeClick() {
        try
        {
            String url = "https://waze.com/ul?q=";
            url += actualLieu.getAdresse();
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            startActivity( intent );
        }
        catch ( ActivityNotFoundException ex  )
        {
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            startActivity(intent);
        }
    }
}
