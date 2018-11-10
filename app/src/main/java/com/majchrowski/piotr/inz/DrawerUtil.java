package com.majchrowski.piotr.inz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;



public class DrawerUtil {
    private static AccountHeader headerResult;


    public static void getDrawer(final Activity activity, Toolbar toolbar) {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem drawerEmptyItem= new PrimaryDrawerItem().withIdentifier(0).withName("");
        drawerEmptyItem.withEnabled(false);

        PrimaryDrawerItem drawerItemMain = new PrimaryDrawerItem()
                .withIdentifier(0).withName("Main list").withIcon(android.R.drawable.ic_menu_info_details);

        PrimaryDrawerItem drawerItemAdd = new PrimaryDrawerItem()
                .withIdentifier(1).withName("Add Entry").withIcon(android.R.drawable.ic_menu_add);
        PrimaryDrawerItem drawerItemLastWeek = new PrimaryDrawerItem().withIdentifier(3)
                .withName("Last week").withIcon(activity.getResources().getDrawable(android.R.drawable.ic_menu_agenda));
        PrimaryDrawerItem drawerItemCalendar = new PrimaryDrawerItem()
                .withIdentifier(2).withName("Calendar").withIcon(android.R.drawable.ic_menu_my_calendar);
        PrimaryDrawerItem drawerItemMonth = new PrimaryDrawerItem()
                .withIdentifier(4).withName("Month summary").withIcon(android.R.drawable.ic_dialog_info);
        PrimaryDrawerItem drawerItemCategory = new PrimaryDrawerItem()
                .withIdentifier(5).withName("Category summary").withIcon(android.R.drawable.ic_dialog_info);
        PrimaryDrawerItem drawerItemSettings = new PrimaryDrawerItem()
                .withIdentifier(6).withName("Settings").withIcon(android.R.drawable.ic_menu_agenda);


/*
        SecondaryDrawerItem drawerItemSettings = new SecondaryDrawerItem().withIdentifier(3)
                .withName(R.string.settings).withIcon(R.drawable.ic_settings_black_48px);
        SecondaryDrawerItem drawerItemAbout = new SecondaryDrawerItem().withIdentifier(4)
                .withName(R.string.about).withIcon(R.drawable.ic_info_black_24px);
        SecondaryDrawerItem drawerItemHelp = new SecondaryDrawerItem().withIdentifier(5)
                .withName(R.string.help).withIcon(R.drawable.ic_help_black_24px);
        SecondaryDrawerItem drawerItemDonate = new SecondaryDrawerItem().withIdentifier(6)
                .withName(R.string.donate).withIcon(R.drawable.ic_payment_black_24px);
                */



        headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.header)
                .build();

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult)
                .withSelectedItem(-1)
                .addDrawerItems(
                        drawerEmptyItem,drawerEmptyItem,
                        drawerItemMain,
                        drawerItemLastWeek,
                        drawerItemCalendar,
                        drawerItemAdd,
                        drawerItemMonth,
                        drawerItemCategory,
                        drawerItemSettings,
                        new DividerDrawerItem()

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem.getIdentifier() == 0&& !(activity instanceof MainActivity)) {
                            // load tournament screen
                            Intent intent = new Intent(activity, MainActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 1) {
                            // load tournament screen
                            Intent intent = new Intent(activity, AddEntryActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2 ) {
                            // load tournament screen
                            Intent intent = new Intent(activity, CalendarActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 3) {
                            // load tournament screen
                            Intent intent = new Intent(activity, LastWeekActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 4) {
                            // load tournament screen
                            Intent intent = new Intent(activity, SummaryActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 5) {
                            // load tournament screen
                            Intent intent = new Intent(activity, CategorySummaryActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 6) {
                            // load tournament screen
                            Intent intent = new Intent(activity, SettingsActivity.class);
                            view.getContext().startActivity(intent);
                        }
                        return true;
                    }
                })
                .build();
    }
}