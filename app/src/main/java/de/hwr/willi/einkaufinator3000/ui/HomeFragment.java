package de.hwr.willi.einkaufinator3000.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import de.hwr.willi.einkaufinator3000.R;
import de.hwr.willi.einkaufinator3000.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    //Alle Elemente werden initialisiert.
    private FragmentHomeBinding binding;

    Button btnList, btnOver, btnMarkets;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Alle Elemente werden deklariert.
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnList = binding.btnList;
        btnOver = binding.btnOver;
        btnMarkets = binding.btnMarkets;

        //Button für Fragmentwechsel.
        binding.btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_ListFragment);
            }
        });
        //Button für Fragmentwechsel.
        binding.btnOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_OverFragment);
            }
        });
        //Button für Fragmentwechsel.
        binding.btnMarkets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_MarketsFragment);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}