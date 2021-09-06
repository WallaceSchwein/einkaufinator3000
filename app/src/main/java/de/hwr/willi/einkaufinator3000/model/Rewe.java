package de.hwr.willi.einkaufinator3000.model;

import java.util.List;

public class Rewe {

    private String[] rewe = new String[35];

    public Rewe(String[] rewe){
        this.rewe = rewe;
    }

    public String[] getBuild(){ return this.rewe; }
}
