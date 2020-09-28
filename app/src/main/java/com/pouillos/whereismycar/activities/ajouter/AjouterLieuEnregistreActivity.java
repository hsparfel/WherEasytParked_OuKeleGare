package com.pouillos.whereismycar.activities.ajouter;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.whereismycar.R;
import com.pouillos.whereismycar.activities.AccueilActivity;
import com.pouillos.whereismycar.activities.NavDrawerActivity;
import com.pouillos.whereismycar.entities.LieuEnregistre;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AjouterLieuEnregistreActivity extends NavDrawerActivity {

    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;

    LieuEnregistre lieuEnregistre;
    @BindView(R.id.textName)
    TextInputEditText textName;
    @BindView(R.id.layoutName)
    TextInputLayout layoutName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_ajouter_lieu_enregistre);

        ButterKnife.bind(this);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
    }


    @OnClick(R.id.fabSave)
    public void setfabSaveClick() {
        if (isFullRempli()) {
            lieuEnregistre = new LieuEnregistre();
            String nom = textName.getText().toString();
            String nomCapitale = nom.substring(0,1).toUpperCase()+nom.substring(1);
            lieuEnregistre.setNom(nomCapitale);
            lieuEnregistreDao.insert(lieuEnregistre);
            ouvrirActiviteSuivante(AjouterLieuEnregistreActivity.this, AccueilActivity.class,false);
        }
    }

    private boolean isFullRempli() {
        boolean bool = true;
        if (!isFilled(textName)) {
            bool = false;
            layoutName.setError("Obligatoire");
        }
        return bool;
    }
}
