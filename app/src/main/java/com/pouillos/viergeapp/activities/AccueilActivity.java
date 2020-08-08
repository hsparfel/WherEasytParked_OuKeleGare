package com.pouillos.viergeapp.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pouillos.viergeapp.R;
import com.pouillos.viergeapp.activities.afficher.AfficherDepenseActivity;

import com.pouillos.viergeapp.dao.BudgetDao;

import com.pouillos.viergeapp.entities.Budget;
import com.pouillos.viergeapp.entities.CategorieDepense;
import com.pouillos.viergeapp.entities.Depense;
import com.pouillos.viergeapp.enumeration.TypeDepense;
import com.pouillos.viergeapp.utils.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AccueilActivity extends NavDrawerActivity {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.fabAddDepense)
    FloatingActionButton fabAddDepense;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    int nbMois = 60;

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

    }

    @OnClick(R.id.fabAddDepense)
    public void setFabAddDepenseClick() {
        ouvrirActiviteSuivante(this,AfficherDepenseActivity.class,false);
    }

    private class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            publishProgress(10);
            remplirBDDefaut();
            publishProgress(50);
            remplirBDExemple();
            publishProgress(60);
            updateBudget();
            publishProgress(70);

            publishProgress(80);

            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(AccueilActivity.this, R.string.text_DB_created, Toast.LENGTH_LONG).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    private void remplirBDDefaut() {
        remplirBDCategorieDepense();
    }

    private void remplirBDCategorieDepense() {
        //todo
        if (categorieDepenseDao.count() == 0) {

            CategorieDepense categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Alimentaire");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Electricite");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Telephone");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Internet");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Gaz");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Eau");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Credit Immobilier");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Credit Automobile");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Credit Consommation");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Assurance Habitation");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Assurance Automobile");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Impôt Revenu");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Taxe Habitation");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Impôt Foncier");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Loyer");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Charge Copropriete");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Essence");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Transport");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Restaurant");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Sante");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Scolarite");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Garde");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Loisirs");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Vacances");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Decoration");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Habits");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Cantine");
            categorieDepense.setTypeDepense(TypeDepense.Courante);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Frais Bancaire");
            categorieDepense.setTypeDepense(TypeDepense.Fixe);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Travaux");
            categorieDepense.setTypeDepense(TypeDepense.Occasionnelle);
            categorieDepenseDao.insert(categorieDepense);

            categorieDepense = new CategorieDepense();
            categorieDepense.setNom("Reparation");
            categorieDepense.setTypeDepense(TypeDepense.Occasionnelle);
            categorieDepenseDao.insert(categorieDepense);
        }
    }

    private void remplirBDExemple() {
        //todo
    }

    private void updateBudget() {
        List<Depense> listDepenseBD = depenseDao.loadAll();
        Budget budget;
        for (Depense currentDepense : listDepenseBD) {
            if (!currentDepense.getIsBudgeted()) {
                if (currentDepense.getCategorieDepense().getTypeDepense() == TypeDepense.Courante
                || currentDepense.getCategorieDepense().getTypeDepense() == TypeDepense.Occasionnelle) {
                    budget = new Budget();
                    budget.setDepense(currentDepense);
                    Date date = DateUtils.dateDebutMois(currentDepense.getDate());
                    budget.setDate(date);
                    budget.setDateString(DateUtils.ecrireDate(date));
                    budget.setAnnee(currentDepense.getAnnee());
                    budget.setMois(currentDepense.getMois());
                    budget.setMontant(currentDepense.getMontant());
                    budgetDao.insert(budget);
                    currentDepense.setIsBudgeted(true);
                    depenseDao.update(currentDepense);
                } else if (currentDepense.getCategorieDepense().getTypeDepense() == TypeDepense.Fixe) {
                    if (!currentDepense.getIsRecurrent()) {
                        switch(currentDepense.getFrequenceDepense()) {
                            case Ponctuel:
                                enregistrerDepenseFixe(currentDepense,1,1);
                                break;
                            case Mensuel:
                                enregistrerDepenseFixe(currentDepense,1,1);
                                break;
                            case Bimestriel:
                                enregistrerDepenseFixe(currentDepense,2,2);
                                break;
                            case Trimestriel:
                                enregistrerDepenseFixe(currentDepense,3,3);
                                break;
                            case Quadrimestriel:
                                enregistrerDepenseFixe(currentDepense,4,4);
                                break;
                            case Semestriel:
                                enregistrerDepenseFixe(currentDepense,6,6);
                                break;
                            case Annuel:
                                enregistrerDepenseFixe(currentDepense,12,12);
                                break;
                            case Default:
                                break;
                        }
                    } else {
                        switch (currentDepense.getFrequenceDepense()) {
                            case Ponctuel:
                                enregistrerDepenseFixe(currentDepense, 1, 60);
                                break;
                            case Mensuel:
                                enregistrerDepenseFixe(currentDepense, 1, 60);
                                break;
                            case Bimestriel:
                                enregistrerDepenseFixe(currentDepense, 2, 60);
                                break;
                            case Trimestriel:
                                enregistrerDepenseFixe(currentDepense, 3, 60);
                                break;
                            case Quadrimestriel:
                                enregistrerDepenseFixe(currentDepense, 4, 60);
                                break;
                            case Semestriel:
                                enregistrerDepenseFixe(currentDepense, 6, 60);
                                break;
                            case Annuel:
                                enregistrerDepenseFixe(currentDepense, 12, 60);
                                break;
                            case Default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void enregistrerDepenseFixe(Depense depense, int nbMoisLissage, int nbMoisRepetition ) {
        for (int i=0;i<nbMoisRepetition;i++) {
            Budget budget = new Budget();
            budget.setDepense(depense);
            Date date = DateUtils.ajouterMois(DateUtils.dateDebutMois(depense.getDate()),i);
            budget.setDate(date);
            budget.setDateString(DateUtils.ecrireDate(date));
            budget.setAnnee(Integer.parseInt(DateUtils.recupAnnee(date)));
            budget.setMois(Integer.parseInt(DateUtils.recupMois(date)));
            budget.setMontant(Math.round(depense.getMontant()/nbMoisLissage * 100.0) / 100.0);
            budgetDao.insert(budget);
        }
        depense.setIsBudgeted(true);
        depenseDao.update(depense);
    }




}
