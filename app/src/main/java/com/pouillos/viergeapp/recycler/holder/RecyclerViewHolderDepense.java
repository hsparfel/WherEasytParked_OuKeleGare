package com.pouillos.viergeapp.recycler.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.viergeapp.R;
import com.pouillos.viergeapp.entities.Depense;
import com.pouillos.viergeapp.recycler.adapter.RecyclerAdapterDepense;
import com.pouillos.viergeapp.utils.DateUtils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewHolderDepense extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.depense)
    TextView depense;


    private WeakReference<RecyclerAdapterDepense.Listener> callbackWeakRef;

    public RecyclerViewHolderDepense(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //ButterKnife.bind(this);
    }

    public void updateWithDepense(Depense depense, RecyclerAdapterDepense.Listener callback){
       // double distance = ChercherContactActivity.calculerDistance(latitude,longitude,contact.getLatitude(),contact.getLongitude());

//todo revoir l'affichage en limitan apres la virgule - FAIT OK
       // NumberFormat formatter = new DecimalFormat("#0.0");
       // System.out.println(formatter.format(4.0));

      //  this.detail.setText(contact.getRaisonSocial() + " - " + formatter.format(distance)+ " km");
        this.depense.setText(depense.getCategorieDepense() + " - " + depense.getMontant() + " € - " + DateUtils.ecrireDate(depense.getDate()));
        this.depense.setOnClickListener(this);
        //4 - Create a new weak Reference to our Listener
        this.callbackWeakRef = new WeakReference<RecyclerAdapterDepense.Listener>(callback);
    }


    @Override
    public void onClick(View view) {
        // 5 - When a click happens, we fire our listener.
        RecyclerAdapterDepense.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDepenseButton(getAdapterPosition());
    }
}
