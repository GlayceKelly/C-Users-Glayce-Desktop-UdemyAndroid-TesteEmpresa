package com.example.glayce.testeempresa.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.glayce.testeempresa.R;

import java.util.ArrayList;

/**
 * Created by Glayce on 04/12/2019.
 */

public class AdapterItens extends RecyclerView.Adapter<AdapterItens.MyViewHolder>
{
    private ArrayList<String> arrStringFavoritos = null;

    public AdapterItens(ArrayList<String> arrStringFavoritosParam)
    {
        arrStringFavoritos = arrStringFavoritosParam;
    }

    @Override
    public AdapterItens.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lst_itens_favoritos, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterItens.MyViewHolder holder, int position)
    {
        // Obtem o item
        String sPaisFavorito = arrStringFavoritos.get(position);

        // Define na label
        holder.lblPais.setText(sPaisFavorito);
    }

    @Override
    public int getItemCount()
    {
        return arrStringFavoritos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView lblPais = null;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            // Obtem a referencia do xml
            lblPais = itemView.findViewById(R.id.lblListPais);
        }
    }
}
