package com.polimi.childcare.shared.entities.tuples;

import com.polimi.childcare.shared.entities.Persona;

import java.io.Serializable;

public class MenuAvviso implements Serializable
{
    public enum Severity{ Info(), Warning(), Critical() }

    private Persona linkedPersona;
    private String message;
    private Severity severity;

    public MenuAvviso(Persona linkedPersona, String message, Severity severity) {
        this.linkedPersona = linkedPersona;
        this.message = message;
        this.severity = severity;
    }

    public Persona getLinkedPersona() {
        return linkedPersona;
    }

    public void setLinkedPersona(Persona linkedPersona) {
        this.linkedPersona = linkedPersona;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
}
