package de.hwr.willi.einkaufinator3000.model;

public class Lidl {

    private String[] lidl = new String[35];

    public Lidl(String[] lidl){
        this.lidl = lidl;
    }

    public String[] getBuild(){ return this.lidl; }
}
