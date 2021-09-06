package de.hwr.willi.einkaufinator3000.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import de.hwr.willi.einkaufinator3000.DBManager;
import de.hwr.willi.einkaufinator3000.R;
import de.hwr.willi.einkaufinator3000.databinding.FragmentStatsBinding;
import de.hwr.willi.einkaufinator3000.model.ShoppingList;

public class StatsFragment extends Fragment {

    //Alle Elemente werden initialisiert.
    private FragmentStatsBinding binding;
    Context con;
    DBManager dbm;

    Button btnFrom, btnTo, btnGo;
    Spinner statsSpin;
    TextView statsTxtSpendings, statsTxtMarket;
    ImageView statsImgSpendings, statsImgMarket;

    ArrayAdapter<CharSequence> spinAdapter;
    DatePickerDialog.OnDateSetListener dslFrom, dslTo;
    List<ShoppingList> listOfLists;
    String fromDate, toDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Alle Elemente werden deklariert.
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        con = container.getContext();
        dbm = new DBManager(con);
        //2021-05-30
        btnFrom = binding.btnFrom;
        btnTo = binding.btnTo;
        btnGo = binding.statsGo;
        statsSpin = binding.statsSpin;
        statsTxtSpendings = binding.statsTxtSpendings;
        statsTxtMarket = binding.statsTxtMarket;
        statsImgSpendings = binding.statsImgSpendings;
        statsImgMarket = binding.statsImgMarket;

        fromDate = "null";
        toDate = "null";
        listOfLists = dbm.getLists();

        //OnClickListener für from-Button wird gesetzt
        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Startpunkt-Datum kann gewählt werden um statistisch relevante Zeitperiode zu definieren.
             * Default: 01.01.1900
             */
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(con, AlertDialog.THEME_HOLO_LIGHT, dslFrom, year, month, day);

                dpd.show();
            }
        });
        dslFrom = (view, year, month, day) -> {
            month = month + 1;
            fromDate = year + "-" + month + "-" + day;
        };

        //OnClickListener für to-Button wird gesetzt
        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Endpunkt-Datum kann gewählt werden um statistisch relevante Zeitperiode zu definieren.
             * Default: 01.01.2199
             */
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(con, AlertDialog.THEME_HOLO_LIGHT, dslTo, year, month, day);

                dpd.show();
            }
        });
        dslTo = (view, year, month, day) -> {
            month = month + 1;
            toDate = year + "-" + month + "-" + day;
        };

        //OnClickListener für go_Button wird gesetzt
        btnGo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            /**
             * Bei nicht gewähltem Start- und/oder Enddatum wird der Default gesetzt.
             * Falls ein Supermarkt gewählt wurde, wird dieser ausgelesen.
             * Listenhistorie wird durchiteriert und Datum und Supermarkt abgeglichen.
             * Bei Treffer wird der Preis des Einkaufs mit aufaddiert.
             * Final werden TextView und ImgView deklariert und die Summe jeweils dynamisch eingefügt.
             */
            public void onClick(View v) {

                String market;
                double periodSpent = 0.00, marketSpent = 0.00;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                if (fromDate.equals("null")){
                    fromDate = "1900-01-01";
                }
                if (toDate.equals("null")){
                    toDate = "2199-01-01";
                }

                if(statsSpin.getSelectedItem().toString().equals("*Market")){
                    market = "null";
                } else {
                    market = statsSpin.getSelectedItem().toString();
                }

                for (ShoppingList list : listOfLists) {
                    if (LocalDate.parse(list.getDate(), formatter).isAfter(LocalDate.parse(fromDate, formatter).minusDays(1)) && LocalDate.parse(list.getDate(), formatter).isBefore(LocalDate.parse(toDate, formatter).plusDays(1))) {
                        periodSpent += list.getTotalPrice();
                    } else {
                        continue;
                    }
                }

                if (!market.equals("null")) {
                    for (ShoppingList list : listOfLists) {
                        if (list.getMarket().equals(market)) {
                            marketSpent += list.getTotalPrice();
                        } else {
                            continue;
                        }

                    }
                }

                statsTxtSpendings.setText("In the selected time period, you have spended: " + periodSpent + "€ on your grocery!");
                statsImgSpendings.setImageResource(R.drawable.spendings);

                if (!market.equals("null")) {
                    statsTxtMarket.setText("In " + market + " market, you have spent: " + marketSpent + "€ in total!");
                    switch (market){
                        case "Edeka":
                            statsImgMarket.setImageResource(R.drawable.edeka);
                            break;
                        case "Rewe":
                            statsImgMarket.setImageResource(R.drawable.rewe);
                            break;
                        case "Lidl":
                            statsImgMarket.setImageResource(R.drawable.lidl);
                            break;
                        case "Aldi":
                            statsImgMarket.setImageResource(R.drawable.aldi);
                            break;
                    }
                }
            }
        });

        //Adapter für Supermarkt-Spinner
        spinAdapter = ArrayAdapter.createFromResource(con, R.array.spinner_market_txt, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statsSpin.setAdapter(spinAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}