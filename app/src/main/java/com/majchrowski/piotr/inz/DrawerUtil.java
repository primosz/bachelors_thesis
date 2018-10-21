package com.majchrowski.piotr.inz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class DrawerUtil {
    public static void getDrawer(final Activity activity, Toolbar toolbar) {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem drawerEmptyItem= new PrimaryDrawerItem().withIdentifier(0).withName("");
        drawerEmptyItem.withEnabled(false);

        PrimaryDrawerItem drawerItemAdd = new PrimaryDrawerItem()
                .withIdentifier(1).withName("Add Entry").withIcon(android.R.drawable.ic_menu_add);
        PrimaryDrawerItem drawerItemLastWeek = new PrimaryDrawerItem().withIdentifier(3)
                .withName("Last week").withIcon(activity.getResources().getDrawable(android.R.drawable.ic_menu_agenda));
        PrimaryDrawerItem drawerItemCalendar = new PrimaryDrawerItem()
                .withIdentifier(2).withName("Calendar").withIcon(android.R.drawable.ic_menu_my_calendar);



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





        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        drawerEmptyItem,drawerEmptyItem,drawerEmptyItem,
                        drawerItemLastWeek,
                        drawerItemCalendar,
                        drawerItemAdd,
                        new DividerDrawerItem()

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
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
                        return true;
                    }
                })
                .build();
    }
}