import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infobrain.siddiganesh.R;
import com.infobrain.siddiganesh.fragments.About_us_frag;
import com.infobrain.siddiganesh.fragments.Account_Statements_frag;
import com.infobrain.siddiganesh.fragments.Account_info_frag;
import com.infobrain.siddiganesh.fragments.Alert_message_frag;
import com.infobrain.siddiganesh.fragments.Change_password_frag;
import com.infobrain.siddiganesh.fragments.DashBoard_frag;
import com.infobrain.siddiganesh.fragments.Feedback_frag;

public class Siddhi_Nav_Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static long back_pressed;
    SharedPreferences sharedPreferences, prefinfo;
    String encodedImage, user_name, user_address, user_phone;
    ImageView imageView;
    TextView userName, userAddress, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siddhi__nav__drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                decodeImage();
                loadInfo();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        imageView = headerview.findViewById(R.id.imageView);
        userName = headerview.findViewById(R.id.user_name);
        userAddress = headerview.findViewById(R.id.user_address);
        userPhone = headerview.findViewById(R.id.user_phone_number);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefinfo = this.getSharedPreferences("INFO", 0);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        decodeImage();
        displayFragment(R.id.nav_home);
    }

    public void loadInfo() {
        user_name = prefinfo.getString("user_name", "");
        user_address = prefinfo.getString("user_address", "");
        user_phone = prefinfo.getString("user_phone", "");
        userName.setText(user_name);
        userAddress.setText(user_address);
        userPhone.setText(user_phone);
        Log.e("user_name", user_name);
        Log.e("user_address", user_address);
        Log.e("user_phone", user_phone);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity();


            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();

            }


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void displayFragment(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_home:
                fragment = new DashBoard_frag();
                break;
            case R.id.nav_statments:
                fragment = new Account_Statements_frag();
                break;
            case R.id.nav_accountinfo:
                fragment = new Account_info_frag();
                break;
            case R.id.nav_alertmsg:
                fragment = new Alert_message_frag();
                break;
            case R.id.nav_change_pass:
                fragment = new Change_password_frag();
                break;
            case R.id.nav_feedback:
                fragment = new Feedback_frag();
                break;
            case R.id.nav_about_us:
                fragment = new About_us_frag();
                break;
            case R.id.sign_out:
                final Intent intent = new Intent(this, MainActivity.class);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setMessage("Are you sure you want to exit?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayFragment(item.getItemId());

        return true;
    }

    public void decodeImage() {
        encodedImage = sharedPreferences.getString("my_image", "");
        Log.e("String image", encodedImage);
        if (!encodedImage.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageView.setImageBitmap(bitmap);
        }
    }


}

