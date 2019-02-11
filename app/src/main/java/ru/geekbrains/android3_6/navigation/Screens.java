package ru.geekbrains.android3_6.navigation;

import android.support.v4.app.Fragment;
import ru.geekbrains.android3_6.ui.fragment.MainFragment;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {

    public static class MainScreen extends SupportAppScreen {
        String arg;

        public MainScreen(String arg) {
            this.arg = arg;
        }

        @Override
        public Fragment getFragment() {
            return MainFragment.getInstance(arg);
        }
    }

}
