package de.hwr.willi.einkaufinator3000.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class ShoppingList {

    //Attribute
    private int id;
    private String date;
    private double totalPrice;
    private String market;

    //Konstruktor
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ShoppingList(int id, String date, double totalPrice, String market) {

        this.id = id;
        this.date = date;
        this.totalPrice = totalPrice;
        this.market = market;

    }

    //Methoden

    @Override
    public String toString() {
        return "Grocery from: " + date + ", Total Price: " + totalPrice + "€,  (@" + market + ")";
    }

    public int getID() { return this.id; }

    public String getMarket() { return this.market; }

    public String getDate() { return date; }

    public double getTotalPrice() { return totalPrice; }

}
