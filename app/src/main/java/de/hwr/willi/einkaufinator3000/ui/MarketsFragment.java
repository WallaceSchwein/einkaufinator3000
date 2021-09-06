package de.hwr.willi.einkaufinator3000.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hwr.willi.einkaufinator3000.DBManager;
import de.hwr.willi.einkaufinator3000.R;
import de.hwr.willi.einkaufinator3000.databinding.FragmentMarketsBinding;

public class MarketsFragment extends Fragment {

    //Alle Elemente werden initialisiert.
    private FragmentMarketsBinding binding;
    Context con;
    DBManager dbm;
    ArrayAdapter listAdapter;
    ArrayAdapter<CharSequence> spinAdapter;
    List<String> newBuild;
    List<String> moddedList;
    List<String> moddedCopy;

    Button btnGo, btnBuild;
    Spinner spinMarkets;
    ListView lvMarkets;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Alle Elemente werden deklariert.
        binding = FragmentMarketsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        con = container.getContext();
        dbm = new DBManager(con);

        newBuild = new ArrayList<>();
        moddedList = new ArrayList<>();
        moddedCopy = new ArrayList<>();

        btnGo = binding.btnGo;
        btnBuild = binding.btnBuild;
        spinMarkets = binding.marketsSpin;
        lvMarkets = binding.listMarkets;

        //OnClickListener für go_Button wird gesetzt
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Der gewählte Supermarkt wird ausgelesen und der dazugehörige Aufbau aus der DB ausgelesen.
             * Anschließend wird dieser im ListView des Fragments angezeigt.
             */
            public void onClick(View v) {
                String market;

                if(spinMarkets.getSelectedItem().toString().equals("*Market")){
                    AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                    adb.setMessage(R.string.market_alert);
                    adb.setCancelable(true);
                    adb.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                    adb.show();
                    return;
                }else{
                    market = spinMarkets.getSelectedItem().toString();
                }

                switch (market) {
                    case "Edeka":
                        moddedList = Arrays.asList(dbm.getEdeka());
                        listAdapter = new ArrayAdapter<>(con, android.R.layout.simple_list_item_1, moddedList);
                        break;
                    case "Rewe":
                        moddedList = Arrays.asList(dbm.getRewe());
                        listAdapter = new ArrayAdapter<>(con, android.R.layout.simple_list_item_1, moddedList);
                        break;
                    case "Aldi":
                        moddedList = Arrays.asList(dbm.getAldi());
                        listAdapter = new ArrayAdapter<>(con, android.R.layout.simple_list_item_1, moddedList);
                        break;
                    case "Lidl":
                        moddedList = Arrays.asList(dbm.getLidl());
                        listAdapter = new ArrayAdapter<>(con, android.R.layout.simple_list_item_1, moddedList);
                        break;
                }

                lvMarkets.setAdapter(listAdapter);
            }
        });

        //OnClickListener für den build-Button wird gesetzt
        btnBuild.setOnClickListener(new View.OnClickListener() {
            String market;

            @Override
            /**
             * Es wird überprüft, ob der Nutzer auch alle Produkttypen ausgewählt hat.
             * Anschließen wird der neue Aufbau des Supermarkts aus dem erstellten String-Array
             * ausgelesen und in die Datenbank geschrieben.
             */
            public void onClick(View v) {
                if (newBuild.size() != 35){
                    AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                    adb.setMessage(R.string.new_build_alert);
                    adb.setCancelable(true);
                    adb.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                    adb.show();
                    return;
                }

                if(spinMarkets.getSelectedItem().toString().equals("*Market")){
                    AlertDialog.Builder adb = new AlertDialog.Builder(requireActivity());
                    adb.setMessage(R.string.market_alert);
                    adb.setCancelable(true);
                    adb.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                    adb.show();
                    return;
                }else{
                    market = spinMarkets.getSelectedItem().toString();
                }

                switch (market) {
                    case "Edeka":
                        dbm.dropEdeka();
                        dbm.createEdeka();
                        for (String value : newBuild) {
                            dbm.fillEdeka(value);
                        }
                        break;
                    case "Rewe":
                        dbm.dropRewe();
                        dbm.createRewe();
                        for (String value : newBuild) {
                            dbm.fillRewe(value);
                        }
                        break;
                    case "Aldi":
                        dbm.dropAldi();
                        dbm.createAldi();
                        for (String value : newBuild) {
                            dbm.fillAldi(value);
                        }
                        break;
                    case "Lidl":
                        dbm.dropLidl();
                        dbm.createLidl();
                        for (String value : newBuild) {
                            dbm.fillLidl(value);
                        }
                        break;
                }
            }
        });

        //OnItemClickListener für ListView wird gesetzt
        lvMarkets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /**
             * Der Nutzer kann die Produkttyp-Elemente dem Aufbau des gewünschten Supermarktes
             * entsprechend anklicken. Diese werden dann der Reihe nach in einem String-Array gespeichert
             * und aus der ListView gelöscht.
             */
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);

                newBuild.add(item);

                for (String value : moddedList) {
                    if (!value.equals(item)){
                        moddedCopy.add(value);
                    }else{
                        continue;
                    }
                }
                moddedList = moddedCopy;
                moddedCopy = new ArrayList<>();
                listAdapter = new ArrayAdapter<>(con, android.R.layout.simple_list_item_1, moddedList);
                lvMarkets.setAdapter(listAdapter);
            }
        });

        //Adapter für Supermarkt-Spinner
        spinAdapter = ArrayAdapter.createFromResource(con, R.array.spinner_market_txt, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMarkets.setAdapter(spinAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}