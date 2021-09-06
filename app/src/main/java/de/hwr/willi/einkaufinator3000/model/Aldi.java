package de.hwr.willi.einkaufinator3000.model;

public class Aldi {

    private String[] aldi = new String[35];

    public Aldi(String[] aldi){
        this.aldi = aldi;
    }

    public String[] getBuild(){ return this.aldi; }
}
