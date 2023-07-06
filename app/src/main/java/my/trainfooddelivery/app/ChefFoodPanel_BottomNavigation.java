package my.trainfooddelivery.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;


import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import my.trainfooddelivery.app.ChefFoodPanel.ChefHomeFragment;
import my.trainfooddelivery.app.ChefFoodPanel.ChefOrderFragment;
import my.trainfooddelivery.app.ChefFoodPanel.ChefPendingOrderFragment;
import my.trainfooddelivery.app.ChefFoodPanel.ChefPostFragment;
import my.trainfooddelivery.app.ChefFoodPanel.ChefProfileFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import my.trainfooddelivery.app.ChefFoodPanel.ChefHomeFragment;
import my.trainfooddelivery.app.ChefFoodPanel.ChefOrderFragment;
import my.trainfooddelivery.app.ChefFoodPanel.ChefPendingOrderFragment;
import my.trainfooddelivery.app.ChefFoodPanel.ChefPostFragment;

public class ChefFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_food_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.chef_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.chefHome:
                fragment=new ChefHomeFragment();
                break;
            case R.id.PendingOrders:
                fragment=new ChefPendingOrderFragment();
                break;
            case R.id.Orders:
                fragment=new ChefOrderFragment();
                break;
            case R.id.chefPost:
                fragment=new ChefPostFragment();
                break;
            case R.id.chefProfile:
                fragment=new ChefProfileFragment();
        }
        return loadcheffragment(fragment);
    }
    private boolean loadcheffragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}