package com.aap.rss.lentaru.Activities;



import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.aap.rss.lentaru.Fragments.ArticleFragment;
import com.aap.rss.lentaru.Fragments.MainNewsFragment;
import com.aap.rss.lentaru.R;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "MainActivity";

    private final int RC_SIGN_IN = 9001;

    private Menu mMainMenu;

    private boolean mIsLoading = false;

    public static ProgressDialog mProgressDialog;

    private MainNewsFragment mFirstFragment;

    private Toolbar mToolbar;

    private GoogleApiClient mGoogleApiClient;

    private GoogleSignInOptions mGso;

    private boolean isLogin;

    private  OptionalPendingResult<GoogleSignInResult> mOpr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();

        mOpr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (findViewById(R.id.frame_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            if (mFirstFragment ==null) {

                mFirstFragment = new MainNewsFragment();
                Bundle bundle = getIntent().getExtras();
                if (bundle == null) {
                    bundle = new Bundle();
                }
               // bundle.putString("url", "https://nonick.000webhostapp.com");
                bundle.putString("url", "https://lenta.ru");
                mFirstFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.frame_container, mFirstFragment).commit();
                Log.d(TAG, "added new fragment.");
            }

        } else {
            Log.e(TAG, "container null");
        }
    }



    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess() + " " + result.getStatus());
        ImageView userLogo = (ImageView)findViewById(R.id.userLogo);
         if (result.isSuccess()) {
             Log.d(TAG, "resut ok");
             GoogleSignInAccount acct = result.getSignInAccount();
             Picasso.with(this).load(acct.getPhotoUrl()).into(userLogo);
             TextView userDescr = (TextView)findViewById(R.id.userName);
             userDescr.setText(acct.getEmail());
             updateUI(true);
         } else {
             Log.e(TAG, "resut false");
                    updateUI(false);
                }
    }

    private void signIn() {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // [START_EXCLUDE]
                                updateUI(false);
                                // [END_EXCLUDE]
                            }
                        });
    }


    @Override
     public void onConnectionFailed(ConnectionResult connectionResult) {
                // An unresolvable error has occurred and Google APIs (including Sign-In) will not
                // be available.
                Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setMessage(getString(R.string.loading));
                    mProgressDialog.setIndeterminate(true);
                }
                // setFinishOnTouchOutside(false);
                mProgressDialog.show();

    }

    private void hideProgressDialog() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.hide();
                    //setFinishOnTouchOutside(true);
                }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.mMainMenu=menu;
        mMainMenu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (isLogin){
                    signOut();
                } else {
                    signIn();
                }
                return true;
            }
        });



        if (mOpr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = mOpr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            mOpr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_login) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment newsFragment;
        showProgress();
        int id = item.getItemId();
        switch (id){
            case R.id.main_page:
                newsFragment = new MainNewsFragment();
            break;
            case R.id.second_page:
                newsFragment= new MainNewsFragment();
            break;
            default:
                newsFragment= new MainNewsFragment();
            break;
        }
        transaction.replace(R.id.frame_container, newsFragment);
        transaction.commit();
        return true;
    }

    private void showProgress() {
        if (!mIsLoading) {
            ProgressBar progressBar = new ProgressBar(this);
            mProgressDialog = ProgressDialog.show(this, null, null);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
            mProgressDialog.setContentView(new ProgressBar(this));
            mIsLoading =true;
        }
    }


    public void loadNews(String url) {
        Fragment webViewFragment = new ArticleFragment();
        FragmentTransaction fm =  getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        webViewFragment.setArguments(bundle);

        fm.addToBackStack(null);
        fm.add(R.id.frame_container, webViewFragment).commit();
        Log.d(TAG, "backstack:"  + getFragmentManager().getBackStackEntryCount());
     }


    @Override
    public void onBackPressed() {
        findViewById(R.id.frame_container).setVisibility(View.VISIBLE);
        mToolbar.setVisibility(View.VISIBLE);

        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.d(TAG, "popping backstack");
            fm.popBackStack();

        } else {
            Log.d(TAG, "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }



    private void updateUI(boolean signedIn) {
        if (signedIn) {
            if (mMainMenu!=null ) {
                Drawable icon = getDrawable(R.drawable.lock);
                icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP );
                mMainMenu.getItem(0).setIcon(icon);
            }
            isLogin=true;
        } else {
            if (mMainMenu!=null ) {
                Drawable icon = getDrawable(R.drawable.common_google_signin_btn_icon_dark);
                icon.setColorFilter(Color.GRAY, PorterDuff.Mode.DST_ATOP);
                mMainMenu.getItem(0).setIcon(icon);
                ImageView userLogo = (ImageView)findViewById(R.id.userLogo);
                userLogo.setImageBitmap(null);
                TextView userDescr = (TextView)findViewById(R.id.userName);
                userDescr.setText("");
            }

            isLogin=false;
        }
        Log.d("login", "updateUI "+signedIn);
    }





}
