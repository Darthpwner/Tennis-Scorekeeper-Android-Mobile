package matthewallenlinsoftware.tennisscorekeeperapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    // Set scores
    TextView player_1_set_1_text_view;
    TextView player_2_set_1_text_view;

    TextView player_1_set_2_text_view;
    TextView set_2_dash_text_view;
    TextView player_2_set_2_text_view;

    TextView player_1_set_3_text_view;
    TextView set_3_dash_text_view;
    TextView player_2_set_3_text_view;

    // Game scores
    ImageView player_1_serving_image_view;
    TextView player_1_game_score_text_view;
    TextView player_2_game_score_text_view;
    ImageView player_2_serving_image_view;

    // Player text views
    TextView player_1_text_view;
    TextView player_2_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize TextViews and ImageViews
        // Initialize set score TextViews
        player_1_set_1_text_view = (TextView) findViewById(R.id.player_1_set_1_text_view);
        player_2_set_1_text_view = (TextView) findViewById(R.id.player_2_set_1_text_view);

        player_1_set_2_text_view = (TextView) findViewById(R.id.player_1_set_2_text_view);
        set_2_dash_text_view = (TextView) findViewById(R.id.set_2_dash_text_view);
        player_2_set_2_text_view = (TextView) findViewById(R.id.player_2_set_2_text_view);

        player_1_set_3_text_view = (TextView) findViewById(R.id.player_1_set_3_text_view);
        set_3_dash_text_view = (TextView) findViewById(R.id.set_3_dash_text_view);
        player_2_set_3_text_view = (TextView) findViewById(R.id.player_2_set_3_text_view);

        // Initialize game score TextViews and ImageViews
        player_1_serving_image_view = (ImageView) findViewById(R.id.player_1_serving_image_view);
        player_1_game_score_text_view = (TextView) findViewById(R.id.player_1_game_score_text_view);
        player_2_game_score_text_view = (TextView) findViewById(R.id.player_2_game_score_text_view);
        player_2_serving_image_view = (ImageView) findViewById(R.id.player_2_serving_image_view);

        // Initialize player TextViews
        player_1_text_view = (TextView) findViewById(R.id.player_1_text_view);
        player_2_text_view = (TextView) findViewById(R.id.player_2_text_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score, menu);
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
}
