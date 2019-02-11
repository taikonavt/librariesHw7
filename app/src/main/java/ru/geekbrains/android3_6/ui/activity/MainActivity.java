package ru.geekbrains.android3_6.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import ru.geekbrains.android3_6.App;
import ru.geekbrains.android3_6.R;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private Navigator navigator = new SupportAppNavigator(this, R.id.container) {
        @Override
        protected void applyCommand(Command command) {
            if(command instanceof Replace){
                Replace replace = (Replace) command;
            }
        }
    };

    @Inject
    NavigatorHolder navigatorHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (savedInstanceState == null && fragment == null) {
            Command[] commands = {new Replace(new ru.geekbrains.android3_6.navigation.Screens.MainScreen("123"))};
            navigator.applyCommands(commands);
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigatorHolder.removeNavigator();
    }
}
