package com.polimi.childcare.server;

import com.polimi.childcare.server.database.DatabaseSession;
import org.hibernate.cfg.Configuration;

public class Main
{
    public static void main(String... args)
    {
        System.out.println("Hello Server...");
        System.out.println("Setting up server...");
        DatabaseSession.getInstance().setUp();
        System.out.println("Server setup complete " + DatabaseSession.getInstance().getCurrentConnectionURL());
        DatabaseSession.getInstance().close();
    }
}
