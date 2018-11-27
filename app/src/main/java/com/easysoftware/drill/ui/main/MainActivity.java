package com.easysoftware.drill.ui.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.easysoftware.drill.R;
import com.easysoftware.drill.app.DrillApp;
import com.easysoftware.drill.ui.recognition.idiom.IdiomRecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem5.Poem5RecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem7.Poem7RecognitionActivity;
import com.easysoftware.drill.ui.solitaire.keywordheadandtail.idiom.IdiomSolitaireWithKeywordHeadAndTailActivity;
import com.easysoftware.drill.ui.solitaire.keywordheadandtail.poem.PoemSolitaireWithKeywordHeadAndTailActivity;
import com.easysoftware.drill.ui.solitaire.keywordinside.poem.PoemSolitaireWithKeywordInsideActivity;
import com.easysoftware.drill.ui.util.HelpDlgFragment;
import com.easysoftware.drill.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainContract.View,
        NavigationView.OnNavigationItemSelectedListener {

    @Inject
    MainPoemPresenter mPoemPresenter;

    @Inject
    MainIdiomPresenter mIdiomPresenter;

    private ProgressBar mProgressBar;
    private DrawerLayout mDrawer;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Make the activity only in portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // DI injection
        ((DrillApp) getApplication()).createActivityComponent().inject(this);

        // presenters
        mPoemPresenter.start(this);
        mIdiomPresenter.start(this);

        // progressbar
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        //
        // sliding menu
        //

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
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
                mSearchView.setIconifiedByDefault(true);
                mSearchView.setFocusable(true);
                mSearchView.setIconified(false);
                mSearchView.requestFocusFromTouch();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //
        // content tabs
        //

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewPager);

        // Fragments
        final CFItemFragment poemFragment = CFItemFragment.newInstance(mPoemPresenter,
                getResources().getString(R.string.poem));
        final CFItemFragment idiomFragment = CFItemFragment.newInstance(mIdiomPresenter,
                getResources().getString(R.string.idiom));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(poemFragment);
        fragments.add(idiomFragment);

        // Create an adapter that knows which fragment should be shown on each page
        CFItemFragmentPagerAdapter adapter = new CFItemFragmentPagerAdapter(fragments, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //
        // search view
        //

        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                poemFragment.filter(newText);
                idiomFragment.filter(newText);
                return false;
            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
        if (mPoemPresenter != null) {
            mPoemPresenter.stop();
        }
        if (mIdiomPresenter != null) {
            mIdiomPresenter.stop();
        }
        ((DrillApp) getApplication()).releaseActivityComponent();
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
        switch (id) {
            case R.id.nav_study:
                break;
            case R.id.nav_idiom_recognition:
                intent = new Intent(MainActivity.this, IdiomRecognitionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_idiom_solitaire_keyword_head_and_tail:
                intent = new Intent(MainActivity.this, IdiomSolitaireWithKeywordHeadAndTailActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_poem5_recognition:
                intent = new Intent(MainActivity.this, Poem5RecognitionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_poem7_recognition:
                intent = new Intent(MainActivity.this, Poem7RecognitionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_poem_solitaire_keyword_head_and_tail:
                intent = new Intent(MainActivity.this, PoemSolitaireWithKeywordHeadAndTailActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_poem_solitaire_keyword_inside:
                intent = new Intent(MainActivity.this, PoemSolitaireWithKeywordInsideActivity.class);
                startActivity(intent);
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayItem(List<String> texts, int type) {
        HelpDlgFragment ihf  = HelpDlgFragment.newInstance(type, (ArrayList<String>) texts);
        ihf.show(getSupportFragmentManager(), "Display Details of Search Item");
    }

}
