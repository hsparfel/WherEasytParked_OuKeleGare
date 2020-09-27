package com.pouillos.whereismycar.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.pouillos.whereismycar.R;
import com.pouillos.whereismycar.entities.LieuEnregistre;
import com.pouillos.whereismycar.recycler.holder.RecyclerViewHolderLieuEnregistre;

import java.util.List;

public class RecyclerAdapterLieuEnregistre extends RecyclerView.Adapter<RecyclerViewHolderLieuEnregistre> {

        private List<LieuEnregistre> listLieuEnregistre;

    public interface Listener {
        void onClickLieuEnregistreButton(int position);
    }

    private final Listener callback;


    public RecyclerAdapterLieuEnregistre(List<LieuEnregistre> listLieuEnregistre, Listener callback) {
        this.listLieuEnregistre = listLieuEnregistre;
        this.callback = callback;
    }

        @Override
        public RecyclerViewHolderLieuEnregistre onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.recycler_list_lieu_enregistre, parent, false);

            return new RecyclerViewHolderLieuEnregistre(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolderLieuEnregistre viewHolder, int position) {
            viewHolder.updateWithLieuEnregistre(this.listLieuEnregistre.get(position),this.callback);
        }

        @Override
        public int getItemCount() {
            return this.listLieuEnregistre.size();
        }

    public LieuEnregistre getLieuEnregistre(int position){
        return this.listLieuEnregistre.get(position);
    }

}
