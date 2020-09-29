package com.pouillos.whereismycar.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.pouillos.whereismycar.R;
import com.pouillos.whereismycar.activities.ajouter.AjouterLieuActivity;
import com.pouillos.whereismycar.activities.ajouter.AjouterLieuEnregistreActivity;
import com.pouillos.whereismycar.dao.AppOpenHelper;
import com.pouillos.whereismycar.dao.DaoMaster;
import com.pouillos.whereismycar.dao.DaoSession;
import com.pouillos.whereismycar.dao.LieuDao;
import com.pouillos.whereismycar.dao.LieuEnregistreDao;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import butterknife.BindView;
import icepick.Icepick;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //FOR DESIGN

    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected BottomNavigationView bottomNavigationView;

    protected DaoSession daoSession;

    protected LieuEnregistreDao lieuEnregistreDao;
    protected LieuDao lieuDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //a redefinit à chq fois
        super.onCreate(savedInstanceState);
        //initialiser greenDAO
        initialiserDao();
        lieuEnregistreDao = daoSession.getLieuEnregistreDao();
        lieuDao = daoSession.getLieuDao();

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

            case R.id.activity_main_drawer_add_lieu:
                myProfilActivity = new Intent(NavDrawerActivity.this, AjouterLieuActivity.class);
                startActivity(myProfilActivity);
                break;

            case R.id.activity_main_drawer_add_lieu_enregistre:
                myProfilActivity = new Intent(NavDrawerActivity.this, AjouterLieuEnregistreActivity.class);
                startActivity(myProfilActivity);
                break;

            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myProfilActivity;
        //3 - Handle actions on menu items
      /*  switch (item.getItemId()) {
            case R.id.menu_activity_main_add_lieu:
                myProfilActivity = new Intent(NavDrawerActivity.this, AjouterLieuActivity.class);
                startActivity(myProfilActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }*/
      return true;
    }
    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 2 - Configure BottomNavigationView Listener
    public void configureBottomView(){
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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
        //bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_add_lieu);
        //  bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }





    // 1 - Configure Toolbar
    public void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
   /* public void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }*/

    // 3 - Configure NavigationView
    //public void configureNavigationView() {
     //   this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
     //   navigationView.setNavigationItemSelectedListener(this);
   // }

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
        if (textInputEditText.length()>0) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void initialiserDao() {
        AppOpenHelper helper = new AppOpenHelper(this, "where_is_my_car_db", null);
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
}
