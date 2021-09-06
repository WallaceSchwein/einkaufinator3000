package de.hwr.willi.einkaufinator3000.model;

import org.jetbrains.annotations.NotNull;

public class Product {

    //Attribute
    private String productName; //Name des Produkts.
    private String productType; //Typ, nach dem der Einkauf spÃ¤ter sortiert werden kann.
    private int id, quantity;

    //Konstruktor
    public Product(int id, String name, String type, int quantity) {
        this.id = id;
        this.productName = name;
        this.productType = type;
        this.quantity = quantity;
    }

    //Methoden

    @NotNull
    @Override
    public String toString() {
        return this.productName + " (" + this.quantity + "x)";
    }

    public int getID() { return this.id; }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public int getQuant() {
        return quantity;
    }

}
