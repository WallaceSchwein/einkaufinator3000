package de.hwr.willi.einkaufinator3000.model;

public class Edeka {

    //List<String> edeka;
    private String[] edeka = new String[35];

    public Edeka(String[] edeka){
        this.edeka = edeka;
    }

    public String[] getBuild(){ return this.edeka; }
}
