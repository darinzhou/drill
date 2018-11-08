package com.easysoftware.drill.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.easysoftware.drill.R;
import com.easysoftware.drill.ui.recognition.idiom.IdiomRecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem5.Poem5RecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem7.Poem7RecognitionActivity;
import com.easysoftware.drill.ui.solitaire.keywordheadandtail.idiom.IdiomSolitaireWithKeywordHeadAndTailActivity;
import com.easysoftware.drill.ui.solitaire.keywordheadandtail.poem.PoemSolitaireWithKeywordHeadAndTailActivity;
import com.easysoftware.drill.ui.solitaire.keywordinside.poem.PoemSolitaireWithKeywordInsideActivity;
import com.easysoftware.drill.ui.util.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private SearchView mSearchView;
    private boolean mShowingMain = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // hide keyboard
                Utils.hideKeyboard(MainActivity.this);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // focus on search view
                if (mShowingMain) {
                    mSearchView.setIconifiedByDefault(true);
                    mSearchView.setFocusable(true);
                    mSearchView.setIconified(false);
                    mSearchView.requestFocusFromTouch();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mSearchView = findViewById(R.id.searchView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDrawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        mShowingMain = true;
        switch (id) {
            case R.id.nav_study:
                mShowingMain = true;
                break;
            case R.id.nav_idiom_recognition:
                mShowingMain = false;
                intent = new Intent(MainActivity.this, IdiomRecognitionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_idiom_solitaire_keyword_head_and_tail:
                mShowingMain = false;
                intent = new Intent(MainActivity.this, IdiomSolitaireWithKeywordHeadAndTailActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_poem5_recognition:
                mShowingMain = false;
                intent = new Intent(MainActivity.this, Poem5RecognitionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_poem7_recognition:
                mShowingMain = false;
                intent = new Intent(MainActivity.this, Poem7RecognitionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_poem_solitaire_keyword_head_and_tail:
                mShowingMain = false;
                intent = new Intent(MainActivity.this, PoemSolitaireWithKeywordHeadAndTailActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_poem_solitaire_keyword_inside:
                mShowingMain = false;
                intent = new Intent(MainActivity.this, PoemSolitaireWithKeywordInsideActivity.class);
                startActivity(intent);
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
