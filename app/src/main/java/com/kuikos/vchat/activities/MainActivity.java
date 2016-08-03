package com.kuikos.vchat.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.androidchat.R;
import com.kuikos.vchat.ChatApplication;
import com.kuikos.vchat.fragments.ChatFragment;
import com.kuikos.vchat.fragments.ConfigFragment;
import com.kuikos.vchat.fragments.HelpFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean first_back;
    ChatApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView logo = (TextView) toolbar.findViewById(R.id.logo_vchat);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fuente.ttf");
        logo.setTypeface(myTypeface);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        application= (ChatApplication) this.getApplication();

        if (getSupportFragmentManager().getBackStackEntryCount()==0){
            ChatFragment chatFragment = new ChatFragment();
            fragmentTransaction(chatFragment);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!first_back) {
                first_back = true;
                Toast.makeText(getApplicationContext(), "Presiona otra vez para cerrar la aplicación.", Toast.LENGTH_SHORT).show();
            }else
                super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
          */
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chat) {
            fragmentTransaction(new ChatFragment());
        } else if (id == R.id.nav_configuracion) {
            fragmentTransaction(new ConfigFragment());
        }else if (id == R.id.nav_help) {
            fragmentTransaction(new HelpFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fragmentTransaction(Fragment nuevo){
        first_back=false;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //transaction.addToBackStack(null);
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_container, nuevo);
        transaction.commit();
    }

    private void clearBackStack(){
        FragmentManager fm = this.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
