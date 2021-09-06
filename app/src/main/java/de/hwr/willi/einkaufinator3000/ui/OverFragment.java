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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import de.hwr.willi.einkaufinator3000.DBManager;
import de.hwr.willi.einkaufinator3000.R;
import de.hwr.willi.einkaufinator3000.databinding.FragmentOverBinding;
import de.hwr.willi.einkaufinator3000.model.ShoppingList;

public class OverFragment extends Fragment {

    //Alle Elemente werden initialisiert.
    private FragmentOverBinding binding;
    Context con;
    DBManager dbm;
    ArrayAdapter<ShoppingList> adapter_list;

    Button btnStats;
    ListView lvOver;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Alle Elemente werden deklariert.
        binding = FragmentOverBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        con = container.getContext();
        dbm = new DBManager(con);

        btnStats = binding.btnStats;
        lvOver = binding.listOverview;

        setList(dbm);

        //Button für Fragmentwechsel.
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(OverFragment.this)
                        .navigate(R.id.action_OverFragment_to_StatsFragment);
            }
        });

        //OnItemClickListener wird gesetzt.
        lvOver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /**
             * Per Klick können abgeschlossene Einkaufslisten aus der Listenhistorie wieder heraus
             * gelöscht werden.
             */
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShoppingList selectedList = (ShoppingList) parent.getItemAtPosition(position);
                dbm.delList(selectedList);
                setList(dbm);
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     * Konfiguriert die Historienliste, mit dem dazugehörigen Adapter.
     * Dieser liest die angezeigte Liste an Einkaufslisten per .getLists() aus der Datenbank aus.
     *
     * @param dbm - Datenbankmanager für Zugriff auf DB
     */
    private void setList(DBManager dbm) {
        adapter_list = new ArrayAdapter<>(con, android.R.layout.simple_list_item_1, dbm.getLists());
        lvOver.setAdapter(adapter_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}