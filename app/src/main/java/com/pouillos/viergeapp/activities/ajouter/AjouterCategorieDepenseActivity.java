package com.pouillos.viergeapp.activities.ajouter;

import android.os.Bundle;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.viergeapp.R;
import com.pouillos.viergeapp.activities.AccueilActivity;
import com.pouillos.viergeapp.activities.NavDrawerActivity;
import com.pouillos.viergeapp.entities.CategorieDepense;
import com.pouillos.viergeapp.enumeration.TypeDepense;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AjouterCategorieDepenseActivity extends NavDrawerActivity {

    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;

    CategorieDepense categorieDepense;
    @BindView(R.id.textName)
    TextInputEditText textName;
    @BindView(R.id.layoutName)
    TextInputLayout layoutName;

    @BindView(R.id.chipFixe)
    Chip chipFixe;
    @BindView(R.id.chipCourante)
    Chip chipCourante;
    @BindView(R.id.chipOccasionnelle)
    Chip chipOccasionnelle;

    TypeDepense typeDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_ajouter_categorie_depense);

        ButterKnife.bind(this);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
    }

    @OnClick(R.id.chipFixe)
    public void setChipFixeClick() {
        if (chipFixe.isChecked()) {
            chipFixe.setClickable(false);
            chipCourante.setClickable(true);
            chipOccasionnelle.setClickable(true);
            typeDepense = TypeDepense.Fixe;
        }
    }

    @OnClick(R.id.chipCourante)
    public void setChipCouranteClick() {
        if (chipCourante.isChecked()) {
            chipFixe.setClickable(true);
            chipCourante.setClickable(false);
            chipOccasionnelle.setClickable(true);
            typeDepense = TypeDepense.Courante;
        }
    }

    @OnClick(R.id.chipOccasionnelle)
    public void setChipOccasionnelleClick() {
        if (chipOccasionnelle.isChecked()) {
            chipFixe.setClickable(true);
            chipCourante.setClickable(true);
            chipOccasionnelle.setClickable(false);
            typeDepense = TypeDepense.Occasionnelle;
        }
    }

    @OnClick(R.id.fabSave)
    public void setfabSaveClick() {
        if (isFullRempli()) {
            categorieDepense = new CategorieDepense();
            categorieDepense.setTypeDepense(typeDepense);
            String nom = textName.getText().toString();
            String nomCapitale = nom.substring(0,1).toUpperCase()+nom.substring(1);
            categorieDepense.setNom(nomCapitale);
            categorieDepenseDao.insert(categorieDepense);
            ouvrirActiviteSuivante(AjouterCategorieDepenseActivity.this, AccueilActivity.class,false);
        }
    }

    private boolean isFullRempli() {
        boolean bool = true;
        if (!chipFixe.isChecked() && !chipCourante.isChecked() && !chipOccasionnelle.isChecked()) {
            bool = false;
            Snackbar.make(chipFixe, "Veuillez Selectionner un type de dépense", Snackbar.LENGTH_SHORT).setAnchorView(chipFixe).show();
        }
        if (!isFilled(textName)) {
            bool = false;
            layoutName.setError("Obligatoire");
        }
        return bool;
    }
}
