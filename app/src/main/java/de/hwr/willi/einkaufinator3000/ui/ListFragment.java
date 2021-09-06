package de.hwr.willi.einkaufinator3000.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import de.hwr.willi.einkaufinator3000.DBManager;
import de.hwr.willi.einkaufinator3000.R;
import de.hwr.willi.einkaufinator3000.databinding.FragmentListBinding;
import de.hwr.willi.einkaufinator3000.model.Aldi;
import de.hwr.willi.einkaufinator3000.model.Edeka;
import de.hwr.willi.einkaufinator3000.model.Lidl;
import de.hwr.willi.einkaufinator3000.model.Product;
import de.hwr.willi.einkaufinator3000.model.Rewe;
import de.hwr.willi.einkaufinator3000.model.ShoppingList;

public class ListFragment extends Fragment {

    //Alle Elemente werden initialisiert.
    private FragmentListBinding binding;
    Context con;
    DBManager dbm;
    ArrayAdapter<Product> listAdapter;
    ArrayAdapter<CharSequence> spinMarketAdapter, spinProdAdapter;

    Button btnAdd, btnSort, btnComplete;
    EditText inpProd, inpQuant, inpPrice;
    Spinner spinProdType, spinMarket;
    ListView lvProd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Alle Elemente werden deklariert.
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        con = container.getContext();
        dbm = new DBManager(con);

        btnAdd = binding.btnAdd;
        btnSort = binding.btnSort;
        btnComplete = binding.btnComplete;
        inpProd = binding.inpProd;
        inpQuant = binding.inpQuant;
        inpPrice = binding.inpPrice;
        spinProdType = binding.spinProdType;
        spinMarket = binding.spinMarket;
        lvProd = binding.listProd;

        setList(dbm);

        //OnClickListener für den add-Button wird gesetzt.
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Parameter werden aus den Eingabefeldern ausgelesen.
             * Ein Produkt-Objekt wird erstellt, in die Datebank
             * geschrieben und abschließen wird die ListView aktualisiert
             */
            public void onClick(View v) {

                String prodName, prodType;
                int quantity;

                if(inpProd.getText().toString().equals("")){
                    AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                    adb.setMessage(R.string.prod_name_alert);
                    adb.setCancelable(true);
                    adb.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                    adb.show();
                    return;
                }else{
                    prodName = inpProd.getText().toString();
                }
                if(inpQuant.getText().toString().equals("")){
                    quantity = 1;
                }else{
                    quantity = Integer.parseInt(inpQuant.getText().toString());
                }
                if(spinProdType.getSelectedItem().toString().equals("*Product Type")){
                    AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                    adb.setMessage(R.string.prod_type_alert);
                    adb.setCancelable(true);
                    adb.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                    adb.show();
                    return;
                }else{
                    prodType = spinProdType.getSelectedItem().toString();
                }

                Product newProd;
                try {
                    newProd = new Product(-1, prodName, prodType, quantity);
                    Snackbar.make(v, newProd.toString() + " added!", Snackbar.LENGTH_SHORT).show();
                } catch (Exception e){
                    newProd = new Product(-1, "error", "unspecified", -1);
                    Snackbar.make(v, "Error adding product!", Snackbar.LENGTH_SHORT).show();
                }

                boolean success = dbm.addProd(newProd);
                setList(dbm);

                inpProd.getText().clear();
                inpQuant.getText().clear();

            }
        });

        //OnClickListener für den sortieren-Button wird gesetzt
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Der Aufbau, des gewählten Supermarkts, nach dem sortiert werden soll, wird abgerufen.
             * Die Produkttypen der Produkte auf der Einkaufsliste werden nach einander mit dem Supermarktaufbau
             * abgeglichen und in desssen Reihenfolge in die sortierte Liste gespeichert. Anschließen
             * wird die ListView mit der sortierten Liste aktualisiert.
             */
            public void onClick(View v) {

                String market;
                List<Product> shoppingList = dbm.getProds();
                List<Product> sortedList = new ArrayList<>();

                if(spinMarket.getSelectedItem().toString().equals("*Market")){
                    AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                    adb.setMessage(R.string.market_alert);
                    adb.setCancelable(true);
                    adb.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                    adb.show();
                    return;
                }else{
                    market = spinMarket.getSelectedItem().toString();
                }

                if (market.equals("Edeka")) {
                    Edeka edeka = new Edeka(dbm.getEdeka());
                    for (String prodType : edeka.getBuild()) {
                        for (Product prod : shoppingList) {
                            if (prod.getProductType().equals(prodType) ) {
                                sortedList.add(prod);
                            } else {





                                continue;
                            }
                        }
                    }
                } else if (market.equals("Rewe")) {
                    Rewe rewe = new Rewe(dbm.getRewe());
                    for (String prodType : rewe.getBuild()) {
                        for (Product prod : shoppingList) {
                            if (prod.getProductType().equals(prodType) ) {
                                sortedList.add(prod);
                            } else {
                                continue;
                            }
                        }
                    }
                } else if (market.equals("Aldi")) {
                    Aldi aldi = new Aldi(dbm.getAldi());
                    for (String prodType : aldi.getBuild()) {
                        for (Product prod : shoppingList) {
                            if (prod.getProductType().equals(prodType) ) {
                                sortedList.add(prod);
                            } else {
                                continue;
                            }
                        }
                    }
                } else if (market.equals("Lidl")) {
                    Lidl lidl = new Lidl(dbm.getLidl());
                    for (String prodType : lidl.getBuild()) {
                        for (Product prod : shoppingList) {
                            if (prod.getProductType().equals(prodType) ) {
                                sortedList.add(prod);
                            } else {
                                continue;
                            }
                        }
                    }
                }
                dbm.dropList();
                dbm.createList();
                for (Product prod : sortedList) {
                    dbm.addProd(prod);
                }
                setList(dbm);
            }
        });

        //OnClickListener für den complete-Button wird gesetzt
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            /**
             * Preis für Einkauf wird ausgelesen und mit dem aktuellen Datum als Parameter genutzt,
             * um eine abgeschlossene Einkaufsliste zu erstellen. Diese wird zu Listen-Historie hinzu
             * gefügt. Das Fragment wird gewechselt und die abgeschlossene Liste wird zusätzlich in
             * der ListView des Overview-Fragments angezeigt.
             */
            public void onClick(View v) {
                double price;
                String market;

                try {
                    price = Double.parseDouble(inpPrice.getText().toString());
                } catch (NumberFormatException e) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                    adb.setMessage(R.string.price_alert);
                    adb.setCancelable(true);
                    adb.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                    adb.show();
                    return;
                }

                if(spinMarket.getSelectedItem().toString().equals("*Market")){
                    AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                    adb.setMessage(R.string.market_alert);
                    adb.setCancelable(true);
                    adb.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                    adb.show();
                    return;
                }else{
                    market = spinMarket.getSelectedItem().toString();
                }

                ShoppingList newList;
                try {
                    newList = new ShoppingList(-1, "", price, market);
                    Snackbar.make(v, "List comlpeted!", Snackbar.LENGTH_SHORT).show();
                } catch (Exception e){
                    newList = new ShoppingList(-1, "", 0.00, "unspecified");
                    Snackbar.make(v, "Error adding List!", Snackbar.LENGTH_SHORT).show();
                }

                boolean success = dbm.addList(newList);
                inpPrice.getText().clear();
                dbm.dropList();
                dbm.createList();

                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.action_ListFragment_to_OverFragment);
            }
        });

        //OnItemClickListener wird gesetzt
        lvProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /**
             * Per Klick kann das entsprechende Produkt wieder von der Einkaufsliste gelöscht werden.
             */
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Product selectedProd = (Product) parent.getItemAtPosition(position);
                dbm.delProd(selectedProd);
                setList(dbm);
            }
        });

        //Adapter für den Produkttyp-Spinner
        spinProdAdapter = ArrayAdapter.createFromResource(con, R.array.spinner_prod_txt, android.R.layout.simple_spinner_item);
        spinProdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinProdType.setAdapter(spinProdAdapter);

        //Adapter für den Supermarkt-Spinner
        spinMarketAdapter = ArrayAdapter.createFromResource(con, R.array.spinner_market_txt, android.R.layout.simple_spinner_item);
        spinMarketAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMarket.setAdapter(spinMarketAdapter);

        return root;
    }

    /**
     * Konfiguriert die Einkaufsliste, mit dem dazugehörigen Adapter.
     * Dieser liest die angezeigte Liste an Produkten per .getProds() aus der Datenbank aus.
     *
     * @param dbm - Datenbankmanager für Zugriff auf DB
     */
    private void setList(DBManager dbm){
        listAdapter = new ArrayAdapter<>(con, android.R.layout.simple_list_item_1, dbm.getProds());
        lvProd.setAdapter(listAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}