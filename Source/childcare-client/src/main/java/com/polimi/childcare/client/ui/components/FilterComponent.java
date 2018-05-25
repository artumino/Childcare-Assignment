package com.polimi.childcare.client.ui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.function.Predicate;

/**
 * Classe generica per la creazione veloce di filtri per FilteredList con campi multipli
 * @param <T> Tipo di oggetto contenuto nella lista
 */
public class FilterComponent<T> implements ChangeListener<String>
{
    private ObjectProperty<Predicate<? super T>> filteredListPredicate;

    private HashMap<TextField, Predicate<T>> filterTextFields;


    public FilterComponent(ObjectProperty<Predicate<? super T>> filteredListPredicate, HashMap<TextField, Predicate<T>> filterTextFields)
    {
        this.filteredListPredicate = filteredListPredicate;
        this.filterTextFields = new HashMap<>();

        addAllFilterFields(filterTextFields);

        this.filteredListPredicate.set(p -> true);
    }

    public FilterComponent(ObjectProperty<Predicate<? super T>> filteredListPredicate)
    {
        this(filteredListPredicate, null);
    }

    public void addAllFilterFields(HashMap<TextField, Predicate<T>> filterTextFields)
    {
        if(filterTextFields == null)
            return;

        for (TextField filterField: filterTextFields.keySet() )
            addFilterField(filterField, filterTextFields.get(filterField));
    }

    public void addFilterField(javafx.scene.control.TextField textField, Predicate<T> predicate)
    {
        this.filterTextFields.put(textField, predicate);
        textField.textProperty().addListener(this);
    }

    public void removeFilterField(TextField textField)
    {
        if(this.filterTextFields.containsKey(textField))
            textField.textProperty().removeListener(this);

        this.filterTextFields.remove(textField);
        filter();
    }

    private void filter()
    {
        this.filteredListPredicate.set(p -> true);
        for (TextField filterField: filterTextFields.keySet() )
        {
            if(!filterField.getText().isEmpty())
                this.filteredListPredicate.set(filterTextFields.get(filterField).and(this.filteredListPredicate.get()));
        }
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
    {
        if(!newValue.equals(oldValue))
            filter();
    }
}
