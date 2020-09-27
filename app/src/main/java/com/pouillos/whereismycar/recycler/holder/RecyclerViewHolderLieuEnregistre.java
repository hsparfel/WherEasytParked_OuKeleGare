package com.pouillos.whereismycar.recycler.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.whereismycar.R;
import com.pouillos.whereismycar.entities.LieuEnregistre;
import com.pouillos.whereismycar.recycler.adapter.RecyclerAdapterLieuEnregistre;
import com.pouillos.whereismycar.utils.DateUtils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewHolderLieuEnregistre extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.lieuEnregistre)
    TextView lieuEnregistre;


    private WeakReference<RecyclerAdapterLieuEnregistre.Listener> callbackWeakRef;

    public RecyclerViewHolderLieuEnregistre(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //ButterKnife.bind(this);
    }

    public void updateWithLieuEnregistre(LieuEnregistre lieuEnregistre, RecyclerAdapterLieuEnregistre.Listener callback){
       // double distance = ChercherContactActivity.calculerDistance(latitude,longitude,contact.getLatitude(),contact.getLongitude());

//todo revoir l'affichage en limitan apres la virgule - FAIT OK
       // NumberFormat formatter = new DecimalFormat("#0.0");
       // System.out.println(formatter.format(4.0));

      //  this.detail.setText(contact.getRaisonSocial() + " - " + formatter.format(distance)+ " km");
        this.lieuEnregistre.setText(lieuEnregistre.getNom());
        this.lieuEnregistre.setOnClickListener(this);
        //4 - Create a new weak Reference to our Listener
        this.callbackWeakRef = new WeakReference<RecyclerAdapterLieuEnregistre.Listener>(callback);
    }


    @Override
    public void onClick(View view) {
        // 5 - When a click happens, we fire our listener.
        RecyclerAdapterLieuEnregistre.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickLieuEnregistreButton(getAdapterPosition());
    }
}