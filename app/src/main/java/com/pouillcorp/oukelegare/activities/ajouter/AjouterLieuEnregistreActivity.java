package com.pouillcorp.oukelegare.activities.ajouter;

import android.os.Bundle;
import android.view.View;

import com.pouillcorp.oukelegare.R;
import com.pouillcorp.oukelegare.activities.AccueilActivity;
import com.pouillcorp.oukelegare.activities.NavDrawerLieuActivity;
import com.pouillcorp.oukelegare.entities.LieuEnregistre;

import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AjouterLieuEnregistreActivity extends NavDrawerLieuActivity {

    LieuEnregistre lieuEnregistre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        ButterKnife.bind(this);

        bottomNavigationViewMenu.findItem(R.id.bottom_navigation_home).setChecked(false);
        bottomNavigationViewMenu.findItem(R.id.bottom_navigation_add_lieu_enregistre).setChecked(true);

        textView.setText(R.string.new_emplacement);

        masquer_inutile();

    }

    public void masquer_inutile() {
        listLieuEnregistre.setVisibility(View.GONE);
        layoutAddresseGeo.setVisibility(View.GONE);
    }

    @OnClick(R.id.fabSaveLieu)
    public void setfabSaveLieuClick() {
        if (isFullRempli()) {
            lieuEnregistre = new LieuEnregistre();
            String nom = textName.getText().toString();
            String nomCapitale = nom.substring(0,1).toUpperCase()+nom.substring(1);
            lieuEnregistre.setNom(nomCapitale);
            lieuEnregistre.setDetail(textDetail.getText().toString());
            lieuEnregistreDao.insert(lieuEnregistre);
            ouvrirActiviteSuivante(AjouterLieuEnregistreActivity.this, AccueilActivity.class,false);
        }
    }

    private boolean isFullRempli() {
        boolean bool = true;
        if (!isFilled(textName)) {
            bool = false;
            layoutName.setError(getString(R.string.obligatoire));
        }
        return bool;
    }
}
