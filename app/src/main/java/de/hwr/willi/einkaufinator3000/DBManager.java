package de.hwr.willi.einkaufinator3000;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.hwr.willi.einkaufinator3000.model.Product;
import de.hwr.willi.einkaufinator3000.model.ShoppingList;

public class DBManager extends SQLiteOpenHelper {

    //Konstanten für Tables und Columns
    public static final String PRODUCT_TABLE = "product_table";
    public static final String LIST_TABLE = "list_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
    public static final String COLUMN_PRODUCT_TYPE = "product_type";
    public static final String COLUMN_LIST_DATE = "list_date";
    public static final String COLUMN_LIST_PRICE = "list_price";
    public static final String COLUMN_LIST_MARKET = "list_market";
    private static final String EDEKA_TABLE = "edeka_table";
    private static final String REWE_TABLE = "rewe_table";
    private static final String LIDL_TABLE = "lidl_table";
    private static final String ALDI_TABLE = "aldi_table";

    private static final boolean DBG = true;
    private static final String TAG = "DbManager";



    public DBManager(@Nullable Context con) {
        super(con, "einkaufinator_base.db", null, 1);
    }

    /**
     * Initiales anlegen der Datenbank-Tables: product_table, list_table, edeka_table, rewe_table,
     * lidl_table, aldi_table und befüllen der Supermarkt-Tables mit einem initialen Aufbau.
     * Dieser liegt für jeden Supermarkt in einem String-Array vor.
     *
     * @param db (SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String prodTableStatement = "CREATE TABLE " + PRODUCT_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_NAME + " TEXT, " + COLUMN_PRODUCT_TYPE + " TEXT," + COLUMN_PRODUCT_QUANTITY + " INT)";
        db.execSQL(prodTableStatement);

        String listTableStatement = "CREATE TABLE " + LIST_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LIST_DATE + " TEXT, " + COLUMN_LIST_PRICE + " FLOAT, " + COLUMN_LIST_MARKET + " TEXT)";
        db.execSQL(listTableStatement);

        String edekaTableStatement = "CREATE TABLE " + EDEKA_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_TYPE + " TEXT)";
        db.execSQL(edekaTableStatement);

        String[] edeka = {"Frischetheke", "Obst & Gemüse", "Marmelade & Honig", "Müsli", "Vegetarisches & Salate", "Käse",
                "Wurstwaren", "Brot", "Backwaren", "Tee", "Backzubereitung", "Kaffee", "Fleisch & Fisch", "Pasta", "Reis & Getreide",
                "Fertigessen", "Babybedarf", "Haustierbedarf", "Haushaltswaren", "Hygieneartikel", "Reinigungsartikel",
                "Konserven & Gläser", "Soßen", "Bier", "Limo", "Wein", "Wasser", "Saft", "Süßkram", "Schnaps", "Milchprodukte (frisch)",
                "Milchprodukte (haltbar)", "Kuchen & Kekse", "Tiefkühl", "Knabberzeug"};
        for (String value : edeka) {
            this.fillEdeka(value);
        }

        String reweTableStatement = "CREATE TABLE " + REWE_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_TYPE + " TEXT)";
        db.execSQL(reweTableStatement);

        String[] rewe = {"Frischetheke", "Obst & Gemüse", "Marmelade & Honig", "Müsli", "Vegetarisches & Salate", "Käse",
                "Wurstwaren", "Brot", "Backwaren", "Tee", "Backzubereitung", "Kaffee", "Fleisch & Fisch", "Pasta", "Reis & Getreide",
                "Fertigessen", "Babybedarf", "Haustierbedarf", "Haushaltswaren", "Hygieneartikel", "Reinigungsartikel",
                "Konserven & Gläser", "Soßen", "Bier", "Limo", "Wein", "Wasser", "Saft", "Süßkram", "Schnaps", "Milchprodukte (frisch)",
                "Milchprodukte (haltbar)", "Kuchen & Kekse", "Tiefkühl", "Knabberzeug"};
        for (String value : rewe) {
            this.fillRewe(value);
        }

        String lidlTableStatement = "CREATE TABLE " + LIDL_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_TYPE + " TEXT)";
        db.execSQL(lidlTableStatement);

        String[] lidl = {"Wasser", "Müsli", "Tee", "Kaffee", "Marmelade & Honig", "Obst & Gemüse", "Brot", "Backwaren", "Soßen",
                "Frischetheke", "Vegetarisches & Salate", "Fertigessen", "Fleisch & Fisch", "Backzubereitung", "Reis & Getreide", "Süßkram",
                "Kuchen & Kekse", "Limo", "Wein", "Bier", "Haushaltswaren", "Wurstwaren", "Käse", "Tiefkühl", "Milchprodukte (frisch)",
                "Milchprodukte (haltbar)", "Hygieneartikel", "Pasta", "Saft", "Konserven & Gläser", "Knabberzeug", "Babybedarf",
                "Reinigungsartikel", "Tiernahrung", "Schnaps"};
        for (String value : lidl) {
            this.fillLidl(value);
        }

        String aldiableStatement = "CREATE TABLE " + ALDI_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_TYPE + " TEXT)";
        db.execSQL(aldiableStatement);

        String[] aldi = {"Backwaren", "Brot", "Marmelade & Honig", "Kaffee", "Tee", "Kuchen & Kekse", "Müsli",
                "Vegetarisches & Salate", "Frischetheke", "Fleisch & Fisch", "Obst & Gemüse", "Wurstwaren", "Käse", "Süßkram", "Soßen",
                "Fertigessen", "Backzubereitung", "Pasta", "Reis & Getreide", "Konserven & Gläser", "Haushaltswaren",
                "Milchprodukte (frisch)", "Wein", "Bier", "Knabberzeug", "Milchprodukte (haltbar)", "Wasser", "Saft", "Limo",
                "Hygieneartikel", "Tiefkühl", "Tiernahrung", "Babybedarf", "Reinigungsartikel", "Schnaps"};
        for (String value : aldi) {
            this.fillAldi(value);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Keine Verwendung bisher.
    }

    /**
     * Funktion um im Frontend angelegte Produkte in die Datenbank zu schreiben.
     *
     * @param prod übergebenes Produkt
     * @return true/false - je nach Erfolg
     */
    public boolean addProd(Product prod){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRODUCT_NAME, prod.getProductName());
        cv.put(COLUMN_PRODUCT_TYPE, prod.getProductType());
        cv.put(COLUMN_PRODUCT_QUANTITY, prod.getQuant());

        long insert = db.insert(PRODUCT_TABLE, null, cv);
        return insert != -1;
    }

    /**
     * Funktion um alle Produkte, die sch bereits auf der Einkaufsliste befinden aus der DB abzurufen.
     *
     * @return Listen-Objekt, in dem die Produkte enthalten sind
     */
    public List<Product> getProds(){

        List<Product> list = new ArrayList<>();

        String getQuery = "SELECT * FROM " + PRODUCT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String prodName = cursor.getString(1);
                String prodType = cursor.getString(2);
                int quantity = cursor.getInt(3);

                Product prod = new Product(id, prodName, prodType, quantity);
                list.add(prod);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    /**
     * Funktion um Produkt von der EInkaufsliste, also aus der DB zu löschen.
     *
     * @return true/false - je nach Erfolg
     */
    public boolean delProd(Product prod){

        String delQuery = "DELETE FROM " + PRODUCT_TABLE + " WHERE " + COLUMN_ID + " = " + prod.getID();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(delQuery, null);

        return cursor.moveToFirst();
    }

    /**
     * Abgeschlossene Einkaufsliste löschen.
     */
    public void dropList(){

        SQLiteDatabase db = this.getWritableDatabase();

        String dropQuery = "DROP TABLE " + PRODUCT_TABLE;
        db.execSQL(dropQuery);
    }

    /**
     * Eine neue leere Einkaufsliste anlegen.
     */
    public void createList(){

        SQLiteDatabase db = this.getWritableDatabase();

        String prodTableStatement = "CREATE TABLE " + PRODUCT_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_NAME + " TEXT, " + COLUMN_PRODUCT_TYPE + " TEXT," + COLUMN_PRODUCT_QUANTITY + " INT)";
        db.execSQL(prodTableStatement);
    }

    /**
     * Abgeschlossene Einkaufslisten zur Listenhistorie hinzufügen.
     *
     * @param list Einkaufsliste, die hinzugefügt werden soll
     * @return true/false - je nach Erfolg
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean addList(ShoppingList list){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LIST_DATE, LocalDate.now().toString());
        cv.put(COLUMN_LIST_PRICE, list.getTotalPrice());
        cv.put(COLUMN_LIST_MARKET, list.getMarket());

        long insert = db.insert(LIST_TABLE, null, cv);
        return insert != -1;
    }

    /**
     * Abgeschlossene Einkaufslisten aus der DB abrufen.
     *
     * @return Listen-Objekt, in dem die abgeschlossenen Einkaufslisten enthaltne sind
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<ShoppingList> getLists(){

        List<ShoppingList> list = new ArrayList<>();

        String getQuery = "SELECT * FROM " + LIST_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                double price = cursor.getDouble(2);
                String market = cursor.getString(3);

                ShoppingList shoppingList = new ShoppingList(id, date, price, market);
                list.add(shoppingList);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    /**
     * Funktion um eine Einkaufsliste aus der Historie, also der DB zu löschen.
     *
     * @param list hinzuzufügende Einkaufsliste
     * @return true/false - je nach Erfolg
     */
    public boolean delList(ShoppingList list){

        String delQuery = "DELETE FROM " + LIST_TABLE + " WHERE " + COLUMN_ID + " = " + list.getID();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(delQuery, null);

        return cursor.moveToFirst();
    }

    /**
     * Legt neuen Aufbau für Edeka an.
     */
    public void createEdeka(){
        SQLiteDatabase db = this.getWritableDatabase();

        String edekaTableStatement = "CREATE TABLE " + EDEKA_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_TYPE + " TEXT)";
        db.execSQL(edekaTableStatement);
    }

    /**
     * Löscht den bisherigen Aufbau für Edeka
     */
    public void dropEdeka(){

        SQLiteDatabase db = this.getWritableDatabase();

        String dropQuery = "DROP TABLE " + EDEKA_TABLE;
        db.execSQL(dropQuery);
    }

    /**
     * Neuen Aufbau von Edeka mit Producttypen befüllen.
     *
     * @param fill Produktkategorie, die dem Aufbau des Supermarktes hinzugefügt werden soll, als String
     * @return true/false - je nach Erfolg
     */
    public boolean fillEdeka(String fill){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRODUCT_TYPE, fill);

        long insert = db.insert(EDEKA_TABLE, null, cv);
        return insert != -1;
    }

    /**
     * Ruft den aktuellen Aufbau von Edeka ab und gibt ihn in einem String-Array zurück.
     *
     * @return Gibt Supermarktaufbau in einem String-Array zurück
     */
    public String[] getEdeka(){

        String[] edeka = new String[35];

        String getQuery = "SELECT * FROM " + EDEKA_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()){
            int i = 0;
            do {
                String item = cursor.getString(1);

                edeka[i] = item;
                i++;
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return edeka;
    }

    /**
     * Legt neuen Aufbau für Rewe an.
     */
    public void createRewe(){
        SQLiteDatabase db = this.getWritableDatabase();

        String reweTableStatement = "CREATE TABLE " + REWE_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_TYPE + " TEXT)";
        db.execSQL(reweTableStatement);
    }

    /**
     * Löscht den bisherigen Aufbau für Rewe.
     */
    public void dropRewe(){

        SQLiteDatabase db = this.getWritableDatabase();

        String dropQuery = "DROP TABLE " + REWE_TABLE;
        db.execSQL(dropQuery);
    }

    /**
     * Neuen Aufbau von Rewe mit Producttypen befüllen.
     *
     * @param fill Produktkategorie, die dem Aufbau des Supermarktes hinzugefügt werden soll, als String
     * @return true/false - je nach Erfolg
     */
    public boolean fillRewe(String fill){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRODUCT_TYPE, fill);

        long insert = db.insert(REWE_TABLE, null, cv);
        return insert != -1;
    }

    /**
     * Ruft den aktuellen Aufbau von Rewe ab und gibt ihn in einem String-Array zurück.
     *
     * @return Gibt Supermarktaufbau in einem String-Array zurück
     */
    public String[] getRewe(){

        String[] rewe = new String[35];

        String getQuery = "SELECT * FROM " + REWE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()){
            int i = 0;
            do {
                String item = cursor.getString(1);

                rewe[i] = item;
                i++;
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return rewe;
    }

    /**
     * Legt neuen Aufbau für Lidl an.
     */
    public void createLidl(){
        SQLiteDatabase db = this.getWritableDatabase();

        String reweTableStatement = "CREATE TABLE " + LIDL_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_TYPE + " TEXT)";
        db.execSQL(reweTableStatement);
    }

    /**
     * Löscht den bisherigen Aufbau für Lidl.
     */
    public void dropLidl(){

        SQLiteDatabase db = this.getWritableDatabase();

        String dropQuery = "DROP TABLE " + LIDL_TABLE;
        db.execSQL(dropQuery);
    }

    /**
     * Neuen Aufbau von Lidl. mit Producttypen befüllen.
     *
     * @param fill Produktkategorie, die dem Aufbau des Supermarktes hinzugefügt werden soll, als String
     * @return true/false - je nach Erfolg
     */
    public boolean fillLidl(String fill){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRODUCT_TYPE, fill);

        long insert = db.insert(LIDL_TABLE, null, cv);
        return insert != -1;
    }

    /**
     * Ruft den aktuellen Aufbau von Lidl ab und gibt ihn in einem String-Array zurück.
     *
     * @return Gibt Supermarktaufbau in einem String-Array zurück
     */
    public String[] getLidl(){

        String[] lidl = new String[35];

        String getQuery = "SELECT * FROM " + LIDL_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()){
            int i = 0;
            do {
                String item = cursor.getString(1);

                lidl[i] = item;
                i++;
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lidl;
    }

    /**
     * Legt neuen Aufbau für Aldi an.
     */
    public void createAldi(){
        SQLiteDatabase db = this.getWritableDatabase();

        String reweTableStatement = "CREATE TABLE " + ALDI_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PRODUCT_TYPE + " TEXT)";
        db.execSQL(reweTableStatement);
    }

    /**
     * Löscht den bisherigen Aufbau für Aldi.
     */
    public void dropAldi(){

        SQLiteDatabase db = this.getWritableDatabase();

        String dropQuery = "DROP TABLE " + ALDI_TABLE;
        db.execSQL(dropQuery);
    }

    /**
     * Neuen Aufbau von Aldi mit Producttypen befüllen.
     *
     * @param fill Produktkategorie, die dem Aufbau des Supermarktes hinzugefügt werden soll, als String
     * @return true/false - je nach Erfolg
     */
    public boolean fillAldi(String fill){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRODUCT_TYPE, fill);

        long insert = db.insert(ALDI_TABLE, null, cv);
        return insert != -1;
    }

    /**
     * Ruft den aktuellen Aufbau von Aldi ab und gibt ihnin einem String-Array zurück.
     *
     * @return Gibt Supermarktaufbau in einem String-Array zurück
     */
    public String[] getAldi(){

        String[] aldi = new String[35];

        String getQuery = "SELECT * FROM " + ALDI_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()){
            int i = 0;
            do {
                String item = cursor.getString(1);

                aldi[i] = item;
                i++;
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return aldi;
    }
}
