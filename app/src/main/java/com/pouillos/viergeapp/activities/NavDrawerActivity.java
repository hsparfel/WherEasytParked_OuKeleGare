package com.pouillos.viergeapp.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.pouillos.viergeapp.R;
import com.pouillos.viergeapp.activities.afficher.AfficherDepenseActivity;
import com.pouillos.viergeapp.activities.afficher.AfficherListeDepenseActivity;
import com.pouillos.viergeapp.activities.ajouter.AjouterCategorieDepenseActivity;

import com.pouillos.viergeapp.dao.BudgetDao;

import com.pouillos.viergeapp.dao.CategorieDepenseDao;
import com.pouillos.viergeapp.dao.DaoMaster;
import com.pouillos.viergeapp.dao.DaoSession;
import com.pouillos.viergeapp.dao.DepenseDao;
import com.pouillos.viergeapp.fragments.DatePickerFragment;
import com.pouillos.viergeapp.utils.DateUtils;

import org.greenrobot.greendao.database.Database;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icepick.Icepick;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //FOR DESIGN

    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    protected DaoSession daoSession;

    protected CategorieDepenseDao categorieDepenseDao;
    protected DepenseDao depenseDao;
    protected BudgetDao budgetDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //a redefinit à chq fois
        super.onCreate(savedInstanceState);
        //initialiser greenDAO
        initialiserDao();
        categorieDepenseDao = daoSession.getCategorieDepenseDao();
        depenseDao = daoSession.getDepenseDao();
        budgetDao = daoSession.getBudgetDao();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        //getMenuInflater().inflate(R.menu.menu_add_item_to_db, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();
        Intent myProfilActivity;

        switch (id) {
            case R.id.activity_main_drawer_home:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AccueilActivity.class,true);
                break;

            case R.id.activity_main_drawer_add_depense:
                //Toast.makeText(this, "à implementer", Toast.LENGTH_LONG).show();
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherDepenseActivity.class);
                startActivity(myProfilActivity);
                break;

            case R.id.activity_main_drawer_lister_depenses:
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherListeDepenseActivity.class);
                startActivity(myProfilActivity);
                break;

            case R.id.activity_main_drawer_lister_budget_mensuel:

                break;

            case R.id.activity_main_drawer_lister_budget_annuel:

                break;

            case R.id.activity_main_drawer_add_categorie_depense:
                //Toast.makeText(this, "à implementer", Toast.LENGTH_LONG).show();
                myProfilActivity = new Intent(NavDrawerActivity.this, AjouterCategorieDepenseActivity.class);
                startActivity(myProfilActivity);
                break;

            case R.id.activity_main_drawer_raz:
                //Toast.makeText(this, "à implementer", Toast.LENGTH_LONG).show();
                raz();
                Toast.makeText(this, "RAZ done", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    protected void raz() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myProfilActivity; // = new Intent(NavDrawerActivity.this, ChercherContactActivity.class);
        //startActivity(myProfilActivity);
        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            /*case R.id.menu_activity_main_params:
                Toast.makeText(this, "Il n'y a rien à paramétrer ici, passez votre chemin...", Toast.LENGTH_LONG).show();
                return true;*/
            case R.id.menu_activity_main_add_depense:
                //Toast.makeText(this, "Recherche indisponible, demandez plutôt l'avis de Google, c'est mieux et plus rapide.", Toast.LENGTH_LONG).show();
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherDepenseActivity.class);
                startActivity(myProfilActivity);
                return true;

            case R.id.importEtablissement:
              //  myProfilActivity = new Intent(NavDrawerActivity.this, ImportEtablissementActivity.class);
              //  startActivity(myProfilActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    public void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
    public void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    public void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void ouvrirActiviteSuivante(Context context, Class classe, boolean bool) {
        Intent intent = new Intent(context, classe);
        startActivity(intent);
        if (bool) {
            finish();
        }
    }

    public void revenirActivitePrecedente(String sharedName, String dataName, Long itemId) {
        SharedPreferences preferences=getSharedPreferences(sharedName,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putLong(dataName,itemId);
        editor.commit();
        finish();
    }

    public void ouvrirActiviteSuivante(Context context, Class classe, String nomExtra, Long objetIdExtra, boolean bool) {
        Intent intent = new Intent(context, classe);
        intent.putExtra(nomExtra, objetIdExtra);
        startActivity(intent);
        if (bool) {
            finish();
        }
    }

    public void ouvrirActiviteSuivante(Context context, Class classe, String nomExtra, String objetExtra, String nomExtra2, Long objetIdExtra2, boolean bool) {
        Intent intent = new Intent(context, classe);
        intent.putExtra(nomExtra, objetExtra);
        intent.putExtra(nomExtra2, objetIdExtra2);
        startActivity(intent);
        if (bool) {
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected Date ActualiserDate(Date date, String time){
        Date dateActualisee = date;
        int nbHour = Integer.parseInt(time.substring(0,2));
        int nbMinute = Integer.parseInt(time.substring(3));
        dateActualisee = DateUtils.ajouterHeure(dateActualisee,nbHour);
        dateActualisee = DateUtils.ajouterMinute(dateActualisee,nbMinute);
        return dateActualisee;
    }

    protected <T> void buildDropdownMenu(List<T> listObj, Context context, AutoCompleteTextView textView) {
        List<String> listString = new ArrayList<>();
        String[] listDeroulante;
        listDeroulante = new String[listObj.size()];
        for (T object : listObj) {
            listString.add(object.toString());
        }
        listString.toArray(listDeroulante);
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.list_item, listDeroulante);
        textView.setAdapter(adapter);
    }

    @Override
    public Executor getMainExecutor() {
        return super.getMainExecutor();
    }

    protected boolean isChecked(ChipGroup chipGroup) {
        boolean bool;
        if (chipGroup.getCheckedChipId() != -1) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    protected boolean isFilled(TextInputEditText textInputEditText){
        boolean bool;
        if (textInputEditText.length()>0) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    protected boolean isFilled(AutoCompleteTextView textView){
        boolean bool;
        if (textView != null) {
            if (!textView.getText().toString().equalsIgnoreCase("")) {
                bool = true;
            } else {
                bool = false;
            }
        } else {
            bool = false;
        }
        return bool;
    }

    protected boolean isFilled(Object object){
        boolean bool;
        if (object!=null) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    protected boolean isValidTel(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && textView.getText().length() <10) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean isValidZip(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && textView.getText().length() <5) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean isEmailAdress(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();
    }
    protected boolean isValidEmail(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && !isEmailAdress(textView.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    protected static float floatArrondi(float number, int decimalPlace) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void showDatePickerDialog(View v,TextInputEditText textView, boolean hasDateMin, boolean hasDateMax,Date dateMin,Date dateMax) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragment.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if (hasDateMin) {
                    datePicker.setMinDate(dateMin.getTime());
                }
                if (hasDateMax) {
                    datePicker.setMaxDate(dateMax.getTime());
                }

                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()+1<10) {
                    dateMois = "0"+dateMois;
                }
                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;

                textView.setText(dateString);
            }
        });
    }

    public Date convertStringToDate(String dateString) {
        DateFormat df = new SimpleDateFormat(getResources().getString(R.string.format_date));
        Date dateToReturn = new Date();
        try{
                dateToReturn = df.parse(dateString);
             }catch(ParseException e){
                 System.out.println(getResources().getString(R.string.error));
            }
        return dateToReturn;
    }

    public void initialiserDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "my_depenses_db", null);
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
}
