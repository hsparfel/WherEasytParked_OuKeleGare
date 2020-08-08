package com.pouillos.viergeapp.activities.afficher;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pouillos.viergeapp.R;
import com.pouillos.viergeapp.activities.NavDrawerActivity;
import com.pouillos.viergeapp.entities.Depense;
import com.pouillos.viergeapp.recycler.adapter.RecyclerAdapterDepense;
import com.pouillos.viergeapp.utils.ItemClickSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AfficherListeDepenseActivity extends NavDrawerActivity implements RecyclerAdapterDepense.Listener {



    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;


    private List<Depense> listDepense;
    private List<Depense> listDepenseBD;

    private RecyclerAdapterDepense adapter;

    @BindView(R.id.listeDepense)
    RecyclerView listeDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_afficher_liste_depense);
        // 6 - Configure all views
      //  this.configureToolBar();
       // this.configureDrawerLayout();
      //  this.configureNavigationView();
        ButterKnife.bind(this);
      //  activeUser = findActiveUser();

       // traiterIntent();
        listDepenseBD = depenseDao.loadAll();

            // 6 - Configure all views
            this.configureToolBar();
            this.configureDrawerLayout();
            this.configureNavigationView();


        configureRecyclerView();
        configureOnClickRecyclerView();
    }

    public void configureRecyclerView() {
        adapter = new RecyclerAdapterDepense(listDepenseBD,this);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        listeDepense.setAdapter(adapter);
        // 3.4 - Set layout manager to position the items
        //this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listeDepense.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(listeDepense, R.layout.recycler_list_depense)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                    }
                });
    }

    @Override
    public void onClickDepenseButton(int position) {
        Depense depense = adapter.getDepense(position);
        Toast.makeText(AfficherListeDepenseActivity.this, "a faire click depense", Toast.LENGTH_SHORT).show();
        //depense.delete();
        ouvrirActiviteSuivante(AfficherListeDepenseActivity.this,AfficherDepenseActivity.class,"depenseId",depense.getId(),true);

        //listDepenseBD.remove(position);
        //adapter.notifyItemRemoved(position);
    }
    
    @OnClick(R.id.fabAdd)
    public void setfabAddClick() {
        ouvrirActiviteSuivante(AfficherListeDepenseActivity.this, AfficherDepenseActivity.class,false);
    }


}
