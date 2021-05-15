package com.pouillcorp.oukelegare.activities;


import android.os.Bundle;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillcorp.oukelegare.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavDrawerLieuActivity extends NavDrawerActivity {
    @Nullable
    @BindView(R.id.textView)
    protected TextView textView;

    @Nullable
    @BindView(R.id.fabSaveLieu)
    protected FloatingActionButton fabSaveLieu;
    @Nullable
    @BindView(R.id.fabDeleteLieu)
    protected FloatingActionButton fabDeleteLieu;
    @Nullable
    @BindView(R.id.selectLieuEnregistre)
    protected AutoCompleteTextView selectLieuEnregistre;
    @Nullable
    @BindView(R.id.listLieuEnregistre)
    protected TextInputLayout listLieuEnregistre;
    @Nullable
    @BindView(R.id.textAddresseGeo)
    protected TextInputEditText textAddresseGeo;
    @Nullable
    @BindView(R.id.layoutAddresseGeo)
    protected TextInputLayout layoutAddresseGeo;
    @Nullable
    @BindView(R.id.textDetail)
    protected TextInputEditText textDetail;
    @Nullable
    @BindView(R.id.layoutDetail)
    protected TextInputLayout layoutDetail;
    @Nullable
    @BindView(R.id.adView)
    protected AdView mAdView;

    @Nullable
    @BindView(R.id.textName)
    protected TextInputEditText textName;
    @Nullable
    @BindView(R.id.layoutName)
    protected TextInputLayout layoutName;

    protected Menu bottomNavigationViewMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lieu_);
        ButterKnife.bind(this);
        this.configureToolBar();
        this.configureBottomView();
        bottomNavigationViewMenu = bottomNavigationView.getMenu();

        Bundle networkExtrasBundle = new Bundle();
        networkExtrasBundle.putInt("rdp", 1);
        AdRequest request = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, networkExtrasBundle)
                .build();
        mAdView.loadAd(request);

        //test a suppr pour prod
       /* List<String> testDeviceIds = Arrays.asList("2480E8D5022BDF1B2B25922CEDC2921C");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);*/
    }
}
