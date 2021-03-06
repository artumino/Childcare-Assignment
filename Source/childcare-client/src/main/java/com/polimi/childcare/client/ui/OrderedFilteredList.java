package com.polimi.childcare.client.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Classe che gestisce i 3 wrapper necessari per ottenere una lista filtrata ordinata ed osservabile dato un dataset non osservabile
 * @param <T> Tipo di oggetti nel dataset
 */
public class OrderedFilteredList<T>
{
    private ObservableList<T> observableList;
    private FilteredList<T> filteredList;
    private SortedList<T> sortedList;

    private SimpleIntegerProperty totalSize;


    public OrderedFilteredList(List<T> dataSet)
    {
        this.observableList = dataSet != null ? FXCollections.observableArrayList(dataSet) : FXCollections.observableArrayList();
        this.filteredList = new FilteredList<>(this.observableList, p -> true);
        this.sortedList = new SortedList<>(filteredList);

        totalSize = new SimpleIntegerProperty(0);

        this.observableList.addListener((ListChangeListener<T>) c -> {
            while (c.next())
                totalSize.set(totalSize.get() - c.getRemovedSize() + c.getAddedSize());
        });
    }

    public OrderedFilteredList()
    {
        this(null);
    }

    public ObjectProperty<Comparator<? super T>> comparatorProperty()
    {
        return this.sortedList.comparatorProperty();
    }

    public ObjectProperty<Predicate<? super T>> predicateProperty()
    {
        return this.filteredList.predicateProperty();
    }

    public SortedList<T> list()
    {
        return sortedList;
    }

    public void updateDataSet(List<T> newDataSet)
    {
        this.observableList.clear();
        if(newDataSet != null)
            this.observableList.addAll(newDataSet);
    }

    public boolean unfilteredContains(T obj)
    {
        return observableList.contains(obj);
    }

    public void add(T obj)
    {
        if(!observableList.contains(obj))
            observableList.add(obj);
    }

    public T get(int index)
    {
        return observableList.get(index);
    }

    public void remove(T obj)
    {
        observableList.remove(obj);
    }

    public int total() {
        return observableList.size();
    }

    public ObservableIntegerValue totalItemsProperty() { return totalSize; }
}
