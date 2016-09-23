package com.joebeaulieu.rapidbrackets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * This is the {@code AppCompatActivity} for the first page that displays when the application loads.
 * From here, the user can navigate to {@code NewBracket} or {@code LoadBracket} via the "New" and
 * "Load" {@code Buttons}, respectively.
 *
 * @author Joe Beaulieu
 * @version 1.0.1
 * @since 1.0.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Creates the {@code MainActivity} {@code AppCompatActivity}. Determines whether or not the
     * {@code AppCompatActivity}'s image needs to be resized.
     *
     * @param savedInstanceState the Bundle that is associated with this AppCompatActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView img = (ImageView) findViewById(R.id.main_activity_image);
        img.post(new Runnable() {
            @Override
            public void run() {
                if (img.getHeight() != img.getWidth()) {
                    squareImage(img);
                }
            }
        });
    }

    /**
     * Inflates the {@code Menu} for the {@code MainActivity} {@code AppCompatActivity}, initializing
     * its contents.
     *
     * @param menu the Menu to be inflated
     * @return     a boolean value which tells the system whether or not to display the Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles clicks on the {@code MenuItems} in the {@code Menu}.
     *
     * @param  item the MenuItem the user selected
     * @return      returns false from the superclass's onOptionsItemSelected(MenuItem) method to
     *              allow normal Menu processing to proceed, or true if the superclass consumed it
     *              there
     * @see <a href="https://developer.android.com/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">Activity.onOptionsItemSelected(MenuItem)</a>
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Starts the {@code NewBracket} {@code AppCompatActivity}.
     *
     * @param view the View from which this method was called
     */
    public void newBracket(View view) {
        Intent intent = new Intent(this, NewBracket.class);
        startActivity(intent);
    }

    /**
     * Starts the {@code LoadBracket} {@code AppCompatActivity}.
     *
     * @param view the View from which this method was called
     */
    public void loadBracket(View view) {
        Intent intent = new Intent(this, LoadBracket.class);
        startActivity(intent);
    }

    /**
     * Squares the {@code ImageView} that is passed to it, making its height and width equal.
     *
     * @param img the ImageView that needs to be squared
     */
    private void squareImage(ImageView img) {
        ViewGroup.LayoutParams params = img.getLayoutParams();
        if (img.getHeight() < img.getWidth()) {
            params.width = img.getHeight();
        } else if (img.getWidth() < img.getHeight()) {
            params.height = img.getWidth();
        }
    }
}
