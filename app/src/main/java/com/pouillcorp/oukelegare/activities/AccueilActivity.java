package com.pouillcorp.oukelegare.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.facebook.stetho.Stetho;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillcorp.oukelegare.R;
import com.pouillcorp.oukelegare.entities.Lieu;
import com.pouillcorp.oukelegare.entities.LieuEnregistre;
import com.pouillcorp.oukelegare.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AccueilActivity extends NavDrawerActivity {

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

    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Stetho.initializeWithDefaults(this);

        this.configureToolBar();
        this.configureBottomView();

        ButterKnife.bind(this);

        AccueilActivity.AsyncTaskRunnerBD runnerBD = new AccueilActivity.AsyncTaskRunnerBD();
        runnerBD.execute();

        listeLieuEnregistre = lieuEnregistreDao.loadAll();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("gad_rdp", 1);
        editor.commit();
        Bundle networkExtrasBundle = new Bundle();
        networkExtrasBundle.putInt("rdp", 1);
        AdRequest request = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, networkExtrasBundle)
                .build();
        mAdView.loadAd(request);

        //test a suppr pour prod
        /*List<String> testDeviceIds = Arrays.asList("2480E8D5022BDF1B2B25922CEDC2921C");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);*/

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
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

            if (actualLieu != null) {
                afficherChampLieu();
                remplirChampLieu();
                masquerChampLieu(actualLieu.getLieuEnregistreId() <= 1);
            } else {
                masquerChampLieu();
            }

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
        }
    }

    private void afficherChampLieu() {
        listLieuEnregistre.setVisibility(View.VISIBLE);
        layoutDate.setVisibility(View.VISIBLE);
        layoutAddresseGeo.setVisibility(View.VISIBLE);
        layoutDetail.setVisibility(View.VISIBLE);
    }

    private void masquerChampLieu() {
        layoutDate.setVisibility(View.GONE);
        layoutAddresseGeo.setVisibility(View.GONE);
        layoutDetail.setVisibility(View.GONE);
        fabGoogleMap.setVisibility(View.GONE);
        fabWaze.setVisibility(View.GONE);
        selectLieuEnregistre.setText(R.string.aucun_emplacement);
    }

    private void masquerChampLieu(boolean bool) {
        if (bool) {
            listLieuEnregistre.setVisibility(View.GONE);
            if (textDetail.getText().toString().equalsIgnoreCase("/")) {
                layoutDetail.setVisibility(View.GONE);
            }
        } else {
            layoutAddresseGeo.setVisibility(View.GONE);
            if (textDetail.getText().toString().equalsIgnoreCase("/")) {
                layoutDetail.setVisibility(View.GONE);
            }
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
            lieuEnregistre.setNom(getString(R.string.a_preciser));
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
