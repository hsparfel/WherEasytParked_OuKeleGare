package com.pouillos.viergeapp.activities.afficher;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.viergeapp.R;
import com.pouillos.viergeapp.activities.AccueilActivity;
import com.pouillos.viergeapp.activities.NavDrawerActivity;
import com.pouillos.viergeapp.dao.BudgetDao;
import com.pouillos.viergeapp.entities.Budget;
import com.pouillos.viergeapp.entities.CategorieDepense;
import com.pouillos.viergeapp.entities.Depense;
import com.pouillos.viergeapp.enumeration.FrequenceDepense;
import com.pouillos.viergeapp.enumeration.TypeDepense;
import com.pouillos.viergeapp.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AfficherDepenseActivity extends NavDrawerActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.textMontant)
    TextInputEditText textMontant;
    @BindView(R.id.layoutMontant)
    TextInputLayout layoutMontant;

    @BindView(R.id.textDetail)
    TextInputEditText textDetail;
    @BindView(R.id.layoutDetail)
    TextInputLayout layoutDetail;

    @BindView(R.id.textDate)
    TextInputEditText textDate;
    @BindView(R.id.layoutDate)
    TextInputLayout layoutDate;
    Date date;

    @BindView(R.id.switchRecurrent)
    SwitchMaterial switchRecurrent;

    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;
    @BindView(R.id.fabDelete)
    FloatingActionButton fabDelete;

    List<CategorieDepense> listeCategorieDepense = new ArrayList<>();
    CategorieDepense categorieDepenseSelected;
    @BindView(R.id.selectCategorieDepense)
    AutoCompleteTextView selectCategorieDepense;
    @BindView(R.id.listCategorieDepense)
    TextInputLayout listCategorieDepense;

    FrequenceDepense frequenceDepenseSelected;

    @BindView(R.id.chipFixe)
    Chip chipFixe;
    @BindView(R.id.chipCourante)
    Chip chipCourante;
    @BindView(R.id.chipOccasionnelle)
    Chip chipOccasionnelle;

    @BindView(R.id.chipPonctuel)
    Chip chipPonctuel;
    @BindView(R.id.chipMensuel)
    Chip chipMensuel;
    @BindView(R.id.chipBimestriel)
    Chip chipBimestriel;
    @BindView(R.id.chipTrimestriel)
    Chip chipTrimestriel;
    @BindView(R.id.chipQuadrimestriel)
    Chip chipQuadrimestriel;
    @BindView(R.id.chipSemestriel)
    Chip chipSemestriel;
    @BindView(R.id.chipAnnuel)
    Chip chipAnnuel;

    Depense currentDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_depense);

        ButterKnife.bind(this);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        //limiter digits du montant
        textMontant.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        textDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(view, textDate,false,false,null,null);
                    textDate.clearFocus();
                }
            }
        });

        hideAllChipsFrequenceExceptOne(null);

        date = new Date();
        textDate.setText(DateUtils.ecrireDate(date));

        fabDelete.hide();
        traiterIntent();
    }

    private void traiterIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("depenseId")) {
            fabDelete.show();
            Long depenseId = intent.getLongExtra("depenseId", 0);
            currentDepense = depenseDao.load(depenseId);

            textMontant.setText(currentDepense.getMontant().toString());
            textDate.setText(DateUtils.ecrireDate(currentDepense.getDate()));
            date = currentDepense.getDate();
            textDetail.setText(currentDepense.getDetail());
            //switchRecurrent.isChecked(currentDepense.getIsRecurrent());



            List<CategorieDepense> liste;
            switch(currentDepense.getCategorieDepense().getTypeDepense()) {
                case Fixe:
                    chipFixe.setChecked(true);
                    listeCategorieDepense = new ArrayList<>();
                    liste = categorieDepenseDao.loadAll();
                    if (liste.size() > 0) {
                        for (CategorieDepense currentCategorieDepense : liste) {
                            if (currentCategorieDepense.getTypeDepense() == TypeDepense.Fixe) {
                                listeCategorieDepense.add(currentCategorieDepense);
                            }
                        }
                        Collections.sort(listeCategorieDepense);
                        buildDropdownMenu(listeCategorieDepense, AfficherDepenseActivity.this, selectCategorieDepense);
                        selectCategorieDepense.setOnItemClickListener(this);
                    }
                    uncheckedAllChipsFrequence();
                    showAllChipsFrequence();
                    chipFixe.setClickable(false);
                    chipCourante.setClickable(true);
                    chipOccasionnelle.setClickable(true);
                    break;
                case Courante:
                    chipCourante.setChecked(true);
                    listeCategorieDepense = new ArrayList<>();
                    liste = categorieDepenseDao.loadAll();
                    if (liste.size() > 0) {
                        for (CategorieDepense currentCategorieDepense : liste) {
                            if (currentCategorieDepense.getTypeDepense() == TypeDepense.Courante) {
                                listeCategorieDepense.add(currentCategorieDepense);
                            }
                        }
                        Collections.sort(listeCategorieDepense);
                        buildDropdownMenu(listeCategorieDepense, AfficherDepenseActivity.this, selectCategorieDepense);
                        selectCategorieDepense.setOnItemClickListener(this);
                    }
                    uncheckedAllChipsFrequence();
                    hideAllChipsFrequenceExceptOne(null);
                    chipFixe.setClickable(true);
                    chipCourante.setClickable(false);
                    chipOccasionnelle.setClickable(true);
                    break;
                case Occasionnelle:
                    chipOccasionnelle.setChecked(true);
                    listeCategorieDepense = new ArrayList<>();
                    liste = categorieDepenseDao.loadAll();
                    if (liste.size() > 0) {
                        for (CategorieDepense currentCategorieDepense : liste) {
                            if (currentCategorieDepense.getTypeDepense() == TypeDepense.Occasionnelle) {
                                listeCategorieDepense.add(currentCategorieDepense);
                            }
                        }
                        Collections.sort(listeCategorieDepense);
                        buildDropdownMenu(listeCategorieDepense, AfficherDepenseActivity.this, selectCategorieDepense);
                        selectCategorieDepense.setOnItemClickListener(this);
                    }
                    uncheckedAllChipsFrequence();
                    hideAllChipsFrequenceExceptOne(null);
                    chipFixe.setClickable(true);
                    chipCourante.setClickable(true);
                    chipOccasionnelle.setClickable(false);
                    break;
                case Default:
                    break;
            }
            //selectCategorieDepense.setText(currentDepense.getCategorieDepense().getNom());
            categorieDepenseSelected = currentDepense.getCategorieDepense();
            selectCategorieDepense.setText(currentDepense.getCategorieDepense().getNom(),false);
            //selectCategorieDepense.
            //todo à poursuivre
            if (currentDepense.getFrequenceDepense() != null) {
                switch(currentDepense.getFrequenceDepense()) {
                    case Ponctuel:
                        chipPonctuel.setChecked(true);
                        hideAllChipsFrequenceExceptOne(chipPonctuel);
                        frequenceDepenseSelected = FrequenceDepense.Ponctuel;
                        break;
                    case Mensuel:
                        chipMensuel.setChecked(true);
                        hideAllChipsFrequenceExceptOne(chipMensuel);
                        frequenceDepenseSelected = FrequenceDepense.Mensuel;
                        break;
                    case Bimestriel:
                        chipBimestriel.setChecked(true);
                        hideAllChipsFrequenceExceptOne(chipBimestriel);
                        frequenceDepenseSelected = FrequenceDepense.Bimestriel;
                        break;
                    case Trimestriel:
                        chipTrimestriel.setChecked(true);
                        hideAllChipsFrequenceExceptOne(chipTrimestriel);
                        frequenceDepenseSelected = FrequenceDepense.Trimestriel;
                        break;
                    case Quadrimestriel:
                        chipQuadrimestriel.setChecked(true);
                        hideAllChipsFrequenceExceptOne(chipQuadrimestriel);
                        frequenceDepenseSelected = FrequenceDepense.Quadrimestriel;
                        break;
                    case Semestriel:
                        chipSemestriel.setChecked(true);
                        hideAllChipsFrequenceExceptOne(chipSemestriel);
                        frequenceDepenseSelected = FrequenceDepense.Semestriel;
                        break;
                    case Annuel:
                        chipAnnuel.setChecked(true);
                        hideAllChipsFrequenceExceptOne(chipAnnuel);
                        frequenceDepenseSelected = FrequenceDepense.Annuel;
                        break;
                    case Default:
                        break;
                }
            }
            switchRecurrent.setChecked(currentDepense.getIsRecurrent());

        }
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        private final int digitsBeforeZero;
        private final int digitsAfterZero;
        private Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            this.digitsBeforeZero = digitsBeforeZero;
            this.digitsAfterZero = digitsAfterZero;
            applyPattern(digitsBeforeZero, digitsAfterZero);
        }

        private void applyPattern(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)|(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (dest.toString().contains(".") || source.toString().contains("."))
                applyPattern(digitsBeforeZero + 2, digitsAfterZero);
            else
                applyPattern(digitsBeforeZero, digitsAfterZero);

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }

    private void uncheckedAllChipsFrequence() {
        chipPonctuel.setChecked(false);
        chipMensuel.setChecked(false);
        chipBimestriel.setChecked(false);
        chipTrimestriel.setChecked(false);
        chipQuadrimestriel.setChecked(false);
        chipSemestriel.setChecked(false);
        chipAnnuel.setChecked(false);

        switchRecurrent.setChecked(false);

        selectCategorieDepense.setText(null);
    }

    private void showAllChipsFrequence() {
        chipPonctuel.setVisibility(View.VISIBLE);
        chipMensuel.setVisibility(View.VISIBLE);
        chipBimestriel.setVisibility(View.VISIBLE);
        chipTrimestriel.setVisibility(View.VISIBLE);
        chipQuadrimestriel.setVisibility(View.VISIBLE);
        chipSemestriel.setVisibility(View.VISIBLE);
        chipAnnuel.setVisibility(View.VISIBLE);

        switchRecurrent.setVisibility(View.VISIBLE);
    }

    private void hideAllChipsFrequenceExceptOne(Chip chip) {

        if (chip == null) {
            switchRecurrent.setVisibility(View.GONE);
        }

        if (chip != chipPonctuel) {
            chipPonctuel.setVisibility(View.GONE);
        }
        if (chip != chipMensuel) {
            chipMensuel.setVisibility(View.GONE);
        }
        if (chip != chipBimestriel) {
            chipBimestriel.setVisibility(View.GONE);
        }
        if (chip != chipTrimestriel) {
            chipTrimestriel.setVisibility(View.GONE);
        }
        if (chip != chipQuadrimestriel) {
            chipQuadrimestriel.setVisibility(View.GONE);
        }
        if (chip != chipSemestriel) {
            chipSemestriel.setVisibility(View.GONE);
        }
        if (chip != chipAnnuel) {
            chipAnnuel.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.chipPonctuel)
    public void setchipPonctuelClick() {
        if (chipPonctuel.isChecked()) {
            Snackbar.make(chipPonctuel, FrequenceDepense.Ponctuel.detail, Snackbar.LENGTH_SHORT).setAnchorView(chipPonctuel).show();
            hideAllChipsFrequenceExceptOne(chipPonctuel);
            frequenceDepenseSelected = FrequenceDepense.Ponctuel;
        } else {
            showAllChipsFrequence();
        }
    }

    @OnClick(R.id.chipMensuel)
    public void setchipMensuelClick() {
        if (chipMensuel.isChecked()) {
            Snackbar.make(chipMensuel, FrequenceDepense.Mensuel.detail, Snackbar.LENGTH_SHORT).setAnchorView(chipMensuel).show();
            hideAllChipsFrequenceExceptOne(chipMensuel);
            frequenceDepenseSelected = FrequenceDepense.Mensuel;
        } else {
            showAllChipsFrequence();
        }
    }
    @OnClick(R.id.chipBimestriel)
    public void setchipBimestrielClick() {
        if (chipBimestriel.isChecked()) {
            Snackbar.make(chipBimestriel, FrequenceDepense.Bimestriel.detail, Snackbar.LENGTH_SHORT).setAnchorView(chipBimestriel).show();
            hideAllChipsFrequenceExceptOne(chipBimestriel);
            frequenceDepenseSelected = FrequenceDepense.Bimestriel;
        } else {
            showAllChipsFrequence();
        }
    }
    @OnClick(R.id.chipTrimestriel)
    public void setchipTrimestrielClick() {
        if (chipTrimestriel.isChecked()) {
            Snackbar.make(chipTrimestriel, FrequenceDepense.Trimestriel.detail, Snackbar.LENGTH_SHORT).setAnchorView(chipTrimestriel).show();
            hideAllChipsFrequenceExceptOne(chipTrimestriel);
            frequenceDepenseSelected = FrequenceDepense.Trimestriel;
        } else {
            showAllChipsFrequence();
        }
    }
    @OnClick(R.id.chipQuadrimestriel)
    public void setchipQuadrimestrielClick() {
        if (chipQuadrimestriel.isChecked()) {
            Snackbar.make(chipQuadrimestriel, FrequenceDepense.Quadrimestriel.detail, Snackbar.LENGTH_SHORT).setAnchorView(chipQuadrimestriel).show();
            hideAllChipsFrequenceExceptOne(chipQuadrimestriel);
            frequenceDepenseSelected = FrequenceDepense.Quadrimestriel;
        } else {
            showAllChipsFrequence();
        }
    }
    @OnClick(R.id.chipSemestriel)
    public void setchipSemestrielClick() {
        if (chipSemestriel.isChecked()) {
            Snackbar.make(chipSemestriel, FrequenceDepense.Semestriel.detail, Snackbar.LENGTH_SHORT).setAnchorView(chipSemestriel).show();
            hideAllChipsFrequenceExceptOne(chipSemestriel);
            frequenceDepenseSelected = FrequenceDepense.Semestriel;
        } else {
            showAllChipsFrequence();
        }
    }
    @OnClick(R.id.chipAnnuel)
    public void setchipAnnuelClick() {
        if (chipAnnuel.isChecked()) {
            Snackbar.make(chipAnnuel, FrequenceDepense.Annuel.detail, Snackbar.LENGTH_SHORT).setAnchorView(chipAnnuel).show();
            hideAllChipsFrequenceExceptOne(chipAnnuel);
            frequenceDepenseSelected = FrequenceDepense.Annuel;
        } else {
            showAllChipsFrequence();
        }
    }

    @OnClick(R.id.chipFixe)
    public void setChipFixeClick() {
        if (chipFixe.isChecked()) {
            listeCategorieDepense = new ArrayList<>();
            List<CategorieDepense> liste = categorieDepenseDao.loadAll();
            if (liste.size() > 0) {
                for (CategorieDepense currentCategorieDepense : liste) {
                    if (currentCategorieDepense.getTypeDepense() == TypeDepense.Fixe) {
                        listeCategorieDepense.add(currentCategorieDepense);
                    }
                }
                Collections.sort(listeCategorieDepense);
                buildDropdownMenu(listeCategorieDepense, AfficherDepenseActivity.this, selectCategorieDepense);
                selectCategorieDepense.setOnItemClickListener(this);
            }
            uncheckedAllChipsFrequence();
            showAllChipsFrequence();
            chipFixe.setClickable(false);
            chipCourante.setClickable(true);
            chipOccasionnelle.setClickable(true);
        }
    }
    @OnClick(R.id.chipCourante)
    public void setChipCouranteClick() {
        if (chipCourante.isChecked()) {
            listeCategorieDepense = new ArrayList<>();
            List<CategorieDepense> liste = categorieDepenseDao.loadAll();
            if (liste.size() > 0) {
                for (CategorieDepense currentCategorieDepense : liste) {
                    if (currentCategorieDepense.getTypeDepense() == TypeDepense.Courante) {
                        listeCategorieDepense.add(currentCategorieDepense);
                    }
                }
                Collections.sort(listeCategorieDepense);
                buildDropdownMenu(listeCategorieDepense, AfficherDepenseActivity.this, selectCategorieDepense);
                selectCategorieDepense.setOnItemClickListener(this);
            }
            uncheckedAllChipsFrequence();
            hideAllChipsFrequenceExceptOne(null);

            chipFixe.setClickable(true);
            chipCourante.setClickable(false);
            chipOccasionnelle.setClickable(true);
        }
    }
    @OnClick(R.id.chipOccasionnelle)
    public void setChipOccasionnelleClick() {
        if (chipOccasionnelle.isChecked()) {
            listeCategorieDepense = new ArrayList<>();
            List<CategorieDepense> liste = categorieDepenseDao.loadAll();
            if (liste.size() > 0) {
                for (CategorieDepense currentCategorieDepense : liste) {
                    if (currentCategorieDepense.getTypeDepense() == TypeDepense.Occasionnelle) {
                        listeCategorieDepense.add(currentCategorieDepense);
                    }
                }
                Collections.sort(listeCategorieDepense);
                buildDropdownMenu(listeCategorieDepense, AfficherDepenseActivity.this, selectCategorieDepense);
                selectCategorieDepense.setOnItemClickListener(this);
            }
            uncheckedAllChipsFrequence();
            hideAllChipsFrequenceExceptOne(null);

            chipFixe.setClickable(true);
            chipCourante.setClickable(true);
            chipOccasionnelle.setClickable(false);
        }
    }

    @OnClick(R.id.fabSave)
    public void setfabSaveClick() {
        if (isFullRempli()) {
            Depense depense = new Depense();
            if (currentDepense != null) {
                depense = currentDepense;
            }

            if (chipCourante.isChecked() || chipOccasionnelle.isChecked()) {

                depense.setCategorieDepense(categorieDepenseSelected);
                depense.setMontant(Double.parseDouble(textMontant.getText().toString()));
                date = convertStringToDate(textDate.getText().toString());
                depense.setDate(date);
                depense.setDateString(DateUtils.ecrireDate(date));
                depense.setMois(Integer.parseInt(DateUtils.recupMois(date)));
                depense.setAnnee(Integer.parseInt(DateUtils.recupAnnee(date)));
                depense.setDetail(textDetail.getText().toString());
                depense.setIsBudgeted(false);
                depense.setIsRecurrent(switchRecurrent.isChecked());
                if (currentDepense != null) {
                    depense.setIsBudgeted(false);
                    nettoyerBudget(depense);
                    depenseDao.update(depense);
                } else {
                    depenseDao.insert(depense);
                }

            } else if (chipFixe.isChecked()) {
                //Depense depense = new Depense();
                depense.setCategorieDepense(categorieDepenseSelected);
                depense.setFrequenceDepense(frequenceDepenseSelected);
                depense.setIsRecurrent(switchRecurrent.isChecked());
                depense.setMontant(Double.parseDouble(textMontant.getText().toString()));
                date = convertStringToDate(textDate.getText().toString());
                depense.setDate(date);
                depense.setDateString(DateUtils.ecrireDate(date));
                depense.setMois(Integer.parseInt(DateUtils.recupMois(date)));
                depense.setAnnee(Integer.parseInt(DateUtils.recupAnnee(date)));
                depense.setDetail(textDetail.getText().toString());
                depense.setIsBudgeted(false);
                if (currentDepense != null) {
                    depense.setIsBudgeted(false);
                    nettoyerBudget(depense);
                    depenseDao.update(depense);
                } else {
                    depenseDao.insert(depense);
                }
            }
            ouvrirActiviteSuivante(AfficherDepenseActivity.this, AccueilActivity.class,false);
        }
    }

    @OnClick(R.id.fabDelete)
    public void setfabDeleteClick() {
        nettoyerBudget(currentDepense);
        depenseDao.delete(currentDepense);
        ouvrirActiviteSuivante(AfficherDepenseActivity.this, AccueilActivity.class,false);
    }

    private void nettoyerBudget(Depense depense) {
        List<Budget> budgets = budgetDao.queryBuilder()
                .where(BudgetDao.Properties.DepenseId.eq(depense.getId()))
                .list();
        for (Budget budget : budgets) {
            budgetDao.delete(budget);
        }
    }

    private boolean isFullRempli() {
        boolean bool = true;
        if (!chipFixe.isChecked() && !chipCourante.isChecked() && !chipOccasionnelle.isChecked()) {
            bool = false;
            Snackbar.make(chipFixe, "Veuillez Selectionner un type de dépense", Snackbar.LENGTH_SHORT).setAnchorView(chipFixe).show();
        }
        if (!isFilled(selectCategorieDepense)) {
            bool = false;
            listCategorieDepense.setError("Obligatoire");
        }
        if (chipFixe.isChecked() && !chipPonctuel.isChecked()
        && !chipMensuel.isChecked() && !chipBimestriel.isChecked() && !chipTrimestriel.isChecked()
        && !chipQuadrimestriel.isChecked() && !chipSemestriel.isChecked() && !chipAnnuel.isChecked()) {
            bool = false;
            Snackbar.make(chipPonctuel, "Veuillez Selectionner une frequence de dépense", Snackbar.LENGTH_SHORT).setAnchorView(chipPonctuel).show();
        }
        if (!isFilled(textMontant)) {
            textMontant.setError("Obligatoire");
            bool = false;
        }
        if (!isFilled(textDate)) {
            textDate.setError("Obligatoire");
            bool = false;
        }
        return bool;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        categorieDepenseSelected = listeCategorieDepense.get(position);
    }
}
