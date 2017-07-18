package matthewallenlinsoftware.tennisscorekeeperapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class ScoreActivity extends AppCompatActivity implements
        DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

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

    // For UI output
    String player_1_game_score_string = "";
    String player_2_game_score_string = "";
    int player_1_game_score = 0;
    int player_2_game_score = 0;

    // Player text views
    TextView player_1_text_view;
    TextView player_2_text_view;

    // Miscellaneous
    int player_serving = 0;
    int player_won = -1;
    Boolean is_tiebreak = false;
    String match_length = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Set up Google Api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle connectionHint) {
                            Log.d("TAG", "onConnected: " + connectionHint);
                            // Now you can use the Data Layer API
                        }
                        @Override
                        public void onConnectionSuspended(int cause) {
                            Log.d("TAG", "onConnectionSuspended: " + cause);
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.d("TAG", "onConnectionFailed: " + result);
                        }
                    })
                    // Request access only to the Wearable API
                .addApi(Wearable.API)
                    .build();
            mGoogleApiClient.connect();

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

    // Syncing data with the Android wear device
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (Log.isLoggable("TAG", Log.DEBUG)) {
            Log.d("TAG", "Connected to Google Api Service");
        }
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        System.out.println("cause: " + cause);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("result: " + result);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        System.out.println("ENTER: Data changed!");

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/applicationDict") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();

                    System.out.println("Data changed!");

                    //asAS

                    //Update game score
                    player_1_game_score = dataMap.getInt("player_1_game_score_label");
                    player_2_game_score = dataMap.getInt("player_2_game_score_label");
                    player_1_game_score_string = Integer.toString(player_1_game_score);
                    player_2_game_score_string = Integer.toString(player_2_game_score);

                    // Update miscellaneous
                    player_serving = dataMap.getInt("player_serving");
                    player_won = dataMap.getInt("player_won");
                    is_tiebreak = dataMap.getBoolean("is_tiebreak");
                    match_length = dataMap.getString("match_length");

                    // Obtain game score
                    if(!is_tiebreak) {
                        // Basic P1 scores
                        if(player_1_game_score == 0) {
                            player_1_game_score_string = "0";
                        } else if(player_1_game_score == 1) {
                            player_1_game_score_string = "15";
                        } else if(player_1_game_score == 2) {
                            player_1_game_score_string = "30";
                        } else if((player_1_game_score == 3) || (player_1_game_score == player_2_game_score)) {
                            player_1_game_score_string = "40";
                        } else if(player_1_game_score - player_2_game_score == 1) {
                            player_1_game_score_string = "AD";
                            player_2_game_score_string = "40";
                        }

                        // Basic P2 scores
                        if(player_2_game_score == 0) {
                            player_2_game_score_string = "0";
                        } else if(player_2_game_score == 1) {
                            player_2_game_score_string = "15";
                        } else if(player_2_game_score == 2) {
                            player_2_game_score_string = "30";
                        } else if((player_2_game_score == 3) || (player_1_game_score == player_2_game_score)) {
                            player_2_game_score_string = "40";
                        } else if(player_2_game_score - player_1_game_score == 1) {
                            player_2_game_score_string = "AD";
                            player_1_game_score_string = "40";
                        }
                    }

                    // If best of 1 set, hide the other set labels
                    if(match_length == "best_of_1") {
                        player_1_set_2_text_view.setVisibility(View.INVISIBLE);
                        set_2_dash_text_view.setVisibility(View.INVISIBLE);
                        player_2_set_2_text_view.setVisibility(View.INVISIBLE);

                        player_1_set_3_text_view.setVisibility(View.INVISIBLE);
                        set_3_dash_text_view.setVisibility(View.INVISIBLE);
                        player_2_set_3_text_view.setVisibility(View.INVISIBLE);
                    } else {
                        player_1_set_2_text_view.setVisibility(View.VISIBLE);
                        set_2_dash_text_view.setVisibility(View.VISIBLE);
                        player_2_set_2_text_view.setVisibility(View.VISIBLE);

                        player_1_set_3_text_view.setVisibility(View.VISIBLE);
                        set_3_dash_text_view.setVisibility(View.VISIBLE);
                        player_2_set_3_text_view.setVisibility(View.VISIBLE);
                    }

                    // Update Set scores
                    player_1_set_1_text_view.setText(dataMap.getInt("player_1_set_1_score_label"));
                    player_2_set_1_text_view.setText(dataMap.getInt("player_2_set_1_score_label"));

                    player_1_set_2_text_view.setText(dataMap.getInt("player_1_set_2_score_label"));
                    player_2_set_2_text_view.setText(dataMap.getInt("player_2_set_2_score_label"));

                    player_1_set_3_text_view.setText(dataMap.getInt("player_1_set_3_score_label"));
                    player_2_set_3_text_view.setText(dataMap.getInt("player_2_set_3_score_label"));

                    player_1_game_score_text_view.setText(player_1_game_score_string);
                    player_2_game_score_text_view.setText(player_2_game_score_string);

                    // Change server after each game
                    if(player_serving == 0) {
                        player_1_serving_image_view.setVisibility(View.VISIBLE);
                        player_2_serving_image_view.setVisibility(View.INVISIBLE);
                    } else  {
                        player_1_serving_image_view.setVisibility(View.INVISIBLE);
                        player_2_serving_image_view.setVisibility(View.VISIBLE);
                    }

                    // Check if a player won and hide the serving image when match is over
                    if(player_won == 0) {
                        player_1_text_view.setTextColor(Color.GREEN);
                        player_1_serving_image_view.setVisibility(View.INVISIBLE);
                        player_2_serving_image_view.setVisibility(View.INVISIBLE);
                    } else if(player_won == 1) {
                        player_2_text_view.setTextColor(Color.GREEN);
                        player_1_serving_image_view.setVisibility(View.INVISIBLE);
                        player_2_serving_image_view.setVisibility(View.INVISIBLE);
                    } else {    // Reset the color if the match is NOT over!
                        player_1_text_view.setTextColor(Color.WHITE);
                        player_2_text_view.setTextColor(Color.WHITE);
                    }
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }
}

