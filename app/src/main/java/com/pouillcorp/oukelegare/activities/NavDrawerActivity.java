package com.pouillcorp.oukelegare.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.pouillcorp.oukelegare.R;
import com.pouillcorp.oukelegare.activities.ajouter.AjouterLieuActivity;
import com.pouillcorp.oukelegare.activities.ajouter.AjouterLieuEnregistreActivity;
import com.pouillcorp.oukelegare.dao.AppOpenHelper;
import com.pouillcorp.oukelegare.dao.DaoMaster;
import com.pouillcorp.oukelegare.dao.DaoSession;
import com.pouillcorp.oukelegare.dao.LieuDao;
import com.pouillcorp.oukelegare.dao.LieuEnregistreDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import icepick.Icepick;

public class NavDrawerActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected BottomNavigationView bottomNavigationView;

    protected DaoSession daoSession;

    protected LieuEnregistreDao lieuEnregistreDao;
    protected LieuDao lieuDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiserDao();
        lieuEnregistreDao = daoSession.getLieuEnregistreDao();
        lieuDao = daoSession.getLieuDao();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myProfilActivity;
      return true;
    }

    public void configureBottomView(){
        this.bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.bottom_navigation_home:
                                ouvrirActiviteSuivante(NavDrawerActivity.this, AccueilActivity.class, true);
                                break;
                            case R.id.bottom_navigation_add_lieu:
                                ouvrirActiviteSuivante(NavDrawerActivity.this, AjouterLieuActivity.class, true);
                                break;
                            case R.id.bottom_navigation_add_lieu_enregistre:
                                ouvrirActiviteSuivante(NavDrawerActivity.this, AjouterLieuEnregistreActivity.class, true);
                                break;
                        }
                        return true;
                    }
                });
    }

    public void configureToolBar() {
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    public void ouvrirActiviteSuivante(Context context, Class classe, boolean bool) {
        Intent intent = new Intent(context, classe);
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

    protected boolean isFilled(TextInputEditText textInputEditText){
        boolean bool;
        bool = textInputEditText.length() > 0;
        return bool;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void initialiserDao() {
        AppOpenHelper helper = new AppOpenHelper(this, "oukelegare_final_db", null);
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
}
