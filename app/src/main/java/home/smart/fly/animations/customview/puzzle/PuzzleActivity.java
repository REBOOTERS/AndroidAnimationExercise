package home.smart.fly.animations.customview.puzzle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.puzzle.view.GamePintuLayout;

public class PuzzleActivity extends AppCompatActivity {
    private GamePintuLayout mGamePintuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mGamePintuLayout = (GamePintuLayout) findViewById(R.id.id_gameview);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int level;
        switch (item.getItemId()) {
            case R.id.easy:
                level = 3;
                break;
            case R.id.hard:
                level = 4;
                break;
            case R.id.crazy:
                level = 5;
                break;
            default:
                level = 3;
                break;
        }
        mGamePintuLayout.setColumn(level);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_puzzle_level, menu);
        return true;
    }
}
