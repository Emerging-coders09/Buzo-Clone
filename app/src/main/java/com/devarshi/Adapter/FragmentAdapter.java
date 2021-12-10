package com.devarshi.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.devarshi.buzoclone.StatusSaverFragment;
import com.devarshi.buzoclone.StatusViewerFragment;

import org.jetbrains.annotations.NotNull;

public class FragmentAdapter extends FragmentPagerAdapter {

    int tabCount;

    public FragmentAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        tabCount = behavior;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new StatusViewerFragment();
            case 1: return new StatusSaverFragment();
        }
        return new StatusViewerFragment();
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return POSITION_NONE;
    }

    /*@Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        Updatable f = (Updatable) object;
        if (f != null) {
            f.update();
        }
        return super.getItemPosition(object);
    }

    public interface Updatable{
        void update();
    }*/
}
