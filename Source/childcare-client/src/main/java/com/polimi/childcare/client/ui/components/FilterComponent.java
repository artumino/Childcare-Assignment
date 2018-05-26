package com.polimi.childcare.client.ui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.function.Predicate;

/**
 * Classe generica per la creazione veloce di filtri per FilteredList con campi multipli
 * @param <T> Tipo di oggetto contenuto nella lista
 */
public class FilterComponent<T> implements ChangeListener
{
    private ObjectProperty<Predicate<? super T>> filteredListPredicate;
    private HashMap<ObservableValue, Predicate<T>> filterFields;


    public FilterComponent(ObjectProperty<Predicate<? super T>> filteredListPredicate, HashMap<ObservableValue, Predicate<T>> filterFields)
    {
        this.filteredListPredicate = filteredListPredicate;
        this.filterFields = new HashMap<>();

        addAllFilterFields(filterFields);

        this.filteredListPredicate.set(p -> true);
    }

    public FilterComponent(ObjectProperty<Predicate<? super T>> filteredListPredicate)
    {
        this(filteredListPredicate, null);
    }

    public void addAllFilterFields(HashMap<ObservableValue, Predicate<T>> filterFields)
    {
        if(filterFields == null)
            return;

        for (ObservableValue observableValue: filterFields.keySet() )
            addFilterField(observableValue, filterFields.get(observableValue));
    }

    public void addFilterField(ObservableValue observableValue, Predicate<T> predicate)
    {
        this.filterFields.put(observableValue, predicate);
        observableValue.addListener(this);
    }

    public void removeFilterField(ObservableValue observableValue)
    {
        if(this.filterFields.containsKey(observableValue))
            observableValue.removeListener(this);

        this.filterFields.remove(observableValue);
        filter();
    }

    private void filter()
    {
        this.filteredListPredicate.set(p -> true);
        for (ObservableValue observableValue: filterFields.keySet() )
        {
            if(observableValue.getValue() != null)
            {
                if(observableValue.getValue() instanceof String && observableValue.getValue().toString().isEmpty())
                    continue;

                this.filteredListPredicate.set(filterFields.get(observableValue).and(this.filteredListPredicate.get()));

            }
        }
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue)
    {
        if(!newValue.equals(oldValue))
            filter();
    }
}
