package com.polimi.childcare.client.android.adapters;

import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.polimi.childcare.client.android.viewholders.GenericViewHolder;
import com.polimi.childcare.client.android.viewholders.annotations.LayoutAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Classe per la gestione di Adapter generici con ViewHolder di oggetti generici
 * @param <T> Tipo di oggeto che rappresenta un ViewHolder
 * @param <VH> Classe ViewHolder per la creazione ed update degli elementi grafici dato un oggetto di tipo T
 */
public class GenericViewHolderAdapter<T, VH extends GenericViewHolder<T>> extends RecyclerView.Adapter<GenericViewHolder<T>>
{

    private Class<VH> viewHolder;
    private SortedList<T> availableItems;
    private HashMap<T, GenericViewHolder<T>> shownItems;
    private Comparator<T> compareMode;

    public GenericViewHolderAdapter(Class<VH> viewHolder, Class<T> tClass, List<T> items, Comparator<T> defaultCompareMode)
    {
        this.viewHolder = viewHolder;

        this.compareMode = defaultCompareMode;
        this.availableItems = new SortedList<>(tClass, sortedListCallback, items != null ? items.size() : 10);

        if(items != null)
            this.availableItems.addAll(items);

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
        availableItems.add(newItem);
    }

    public void addRange(Collection<T> newItems)
    {
        availableItems.addAll(newItems);
    }

    public void remove(int position)
    {
        if(availableItems.size() > position)
        {
            T item = availableItems.get(position);
            shownItems.remove(item);
            availableItems.remove(item);
        }
    }

    public void remove(T item)
    {
        shownItems.remove(item);
        availableItems.remove(item);
    }

    public void remove(List<T> items)
    {
        availableItems.beginBatchedUpdates();
        for (T item : items) {
            shownItems.remove(item);
            availableItems.remove(item);
        }
        availableItems.endBatchedUpdates();
    }

    public void replaceAll(List<T> items)
    {
        availableItems.beginBatchedUpdates();
        for (int i = availableItems.size() - 1; i >= 0; i--)
        {
            final T item = availableItems.get(i);
            if (!items.contains(item)) {
                shownItems.remove(item);
                availableItems.remove(item);
            }
        }
        availableItems.addAll(items);
        availableItems.endBatchedUpdates();
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

    public void setCompareMode(Comparator<T> compareMode)
    {
        this.compareMode = compareMode;
    }

    private final SortedList.Callback<T> sortedListCallback = new SortedList.Callback<T>()
    {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public int compare(T o1, T o2) {
            return compareMode.compare(o1, o2);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(T oldItem, T newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(T item1, T item2) {
            return item1.hashCode() == item2.hashCode();
        }
    };
}
