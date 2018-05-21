package com.polimi.childcare.client.android.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.polimi.childcare.client.android.viewholders.GenericViewHolder;
import com.polimi.childcare.client.android.viewholders.annotations.LayoutAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Classe per la gestione di Adapter generici con ViewHolder di oggetti generici
 * @param <T> Tipo di oggeto che rappresenta un ViewHolder
 * @param <VH> Classe ViewHolder per la creazione ed update degli elementi grafici dato un oggetto di tipo T
 */
public class GenericViewHolderAdapter<T, VH extends GenericViewHolder<T>> extends RecyclerView.Adapter<GenericViewHolder<T>>
{
    private Class<VH> viewHolder;
    private List<T> availableItems;
    private HashMap<T, GenericViewHolder<T>> shownItems;

    public GenericViewHolderAdapter(Class<VH> viewHolder, List<T> items)
    {
        this.viewHolder = viewHolder;
        if(items != null)
            this.availableItems = items;
        else
            this.availableItems = new ArrayList<>(1);

        this.shownItems = new HashMap<>(2);
    }


    @Override
    public GenericViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = null;
        try {
            LayoutAnnotation annotation = viewHolder.getAnnotation(LayoutAnnotation.class);

            if(annotation == null)
                throw new RuntimeException("No Layout Annotation for ViewHolder with type " + viewHolder.getTypeName());

            view = LayoutInflater.from(parent.getContext())
                    .inflate(annotation.LayoutID(), parent, false);
            return viewHolder.getDeclaredConstructor(View.class).newInstance(view);
        }
        catch (InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder<T> holder, int position)
    {
        T item = this.availableItems.get(position);
        holder.bindView(item);
        this.shownItems.put(item, holder);
    }

    @Override
    public int getItemCount() {
        return this.availableItems.size();
    }

    public void clear() {
        availableItems.clear();
        shownItems.clear();
        notifyDataSetChanged();
    }

    public void add(T newItem)
    {
        if(!availableItems.contains(newItem))
        {
            availableItems.add(newItem);
            notifyItemInserted(availableItems.indexOf(newItem));
        }
    }

    public void addRange(Collection<T> newItems)
    {
        if(newItems.size() > 0 && !availableItems.containsAll(newItems))
        {
            availableItems.addAll(newItems);
            notifyItemRangeInserted(availableItems.indexOf(newItems.iterator().next()), newItems.size());
        }
    }

    public void enqueue(T newItem)
    {
        if(!availableItems.contains(newItem))
        {
            availableItems.add(0, newItem);
            notifyItemInserted(0);
        }
    }

    public void enqueueRange(Collection<T> newItems)
    {
        if(newItems.size() > 0 && !availableItems.containsAll(newItems))
        {
            availableItems.addAll(0, newItems);
            notifyItemRangeInserted(0, newItems.size());
        }
    }

    public void insert(T newItem, int position)
    {
        if(!availableItems.contains(newItem))
        {
            availableItems.add(position, newItem);
            notifyItemInserted(availableItems.indexOf(newItem));
        }
    }

    public void insertRange(Collection<T> newItems, int position)
    {
        if(newItems.size() > 0 && !availableItems.containsAll(newItems))
        {
            availableItems.addAll(position, newItems);
            notifyItemRangeInserted(position, newItems.size());
        }
    }

    public void remove(int position)
    {
        if(availableItems.size() > position)
        {
            notifyItemRemoved(position);
            shownItems.remove(availableItems.get(position));
            availableItems.remove(position);
        }
    }

    public void update(T item)
    {
        if(this.shownItems.containsKey(item))
            this.shownItems.get(item).updateView();
        else
            this.add(item);
    }

    public void updateAll()
    {
        for(T item : shownItems.keySet())
            this.shownItems.get(item).updateView();
    }

    public T getItem(int position)
    {
        if(position >= 0 && position < getItemCount())
            return availableItems.get(position);
        return null;
    }
}
