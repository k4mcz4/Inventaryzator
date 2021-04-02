package com.example.inventura;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.inventura.fragments.HistoryFragment;
import com.example.inventura.fragments.InventoryFragment;
import com.example.inventura.fragments.ProductFragment;

public class PagerAdapter extends FragmentStateAdapter {


    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {

            case 1: return ProductFragment.newInstance();
            case 2: return HistoryFragment.newInstance();
            default: return InventoryFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}