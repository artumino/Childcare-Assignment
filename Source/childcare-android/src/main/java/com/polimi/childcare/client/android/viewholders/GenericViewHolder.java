package com.polimi.childcare.client.android.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Classe per legare un oggetto di tipo T ad un item di una lista
 * @see com.polimi.childcare.client.android.viewholders.annotations.LayoutAnnotation Necessita di una annotation di tipo LayoutAnnotation per definire l'ID da usare per il layout
 * @param <T> Tipo dell'oggetto trattato dal ViewHolder
 */
public abstract class GenericViewHolder<T> extends RecyclerView.ViewHolder
{
    GenericViewHolder(View itemView) { super(itemView); }

    public abstract void bindView(T element);
    public abstract void updateView();
}
