package com.caderea.battleapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.caderea.battleapp.core.Battle;
import com.caderea.battleapp.R;
import com.caderea.battleapp.action.BattleAction;
import com.caderea.battleapp.queue.BattleQueue;
import com.caderea.battleapp.queue.QueueLayout;
import com.caderea.battleapp.view.NewBattleView;

import java.util.ArrayList;

public class BattleActivity extends Activity {
    private ArrayList<String> output;
    private TextView battleText;
    private Battle battle;

    private float dpHeight;
    private float dpWidth;

    private Button[] battleButtons;
    private NewBattleView newBattleView;
//    private QueueLayout queueLayout;

    public Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_screen);

        //get screen resolution in dp
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        //initialize the string to hold output and the textview to show it
        output = new ArrayList<String>(10);
//        battleText = (TextView)findViewById(R.id.battle_display);
//        battleText.setHeight((int)(dpHeight*3/4));

        battleButtons = new Button[4];
        battleButtons[0] = (Button)findViewById(R.id.button1);
        battleButtons[1] = (Button)findViewById(R.id.button2);
        battleButtons[2] = (Button)findViewById(R.id.button3);
        battleButtons[3] = (Button)findViewById(R.id.button4);

        newBattleView = findViewById(R.id.newBattleView);
        newBattleView.initQueues();

        //this handler can be passed to other threads to refer to the UI thread
        uiHandler = new Handler() {
            //responds to uiHandler.sendMessage()
            public void handleMessage(Message msg) {
                update((String)msg.obj);
            }
        };

        //create a new Battle and pass this activity
        battle = new Battle(this);
        //start the battle
        (new Thread(battle)).start();
    }

    public void update(String msg) {
        while(output.size() > 9) {
            output.remove(0);
        }

        output.add(msg);

        String str = "";
        for(String s:output) {
            str += s +"\n";
        }

        Log.d("BATTLE", "update: " + str);
//        battleText.setText(str);
    }

    public void buttonPushed(View view) {
        switch(view.getId()) {
            case R.id.button1:
                battle.queueAction(0);
                break;
            case R.id.button2:
                battle.queueAction(1);
                break;
            case R.id.button3:
                battle.queueAction(2);
                break;
            case R.id.button4:
                battle.queueAction(3);
                break;
            case R.id.buttonStop:
                stopBattle();
                break;
            default:
                update("default");
                break;
        }
    }

    private void stopBattle() {
        battle.stop();
    }

    public void notifyDone() {
        update("Battle Finished");

        //change button 4 to return to the main menu
        Button btn = (Button)findViewById(R.id.buttonStop);
        btn.setText("Return to Menu");

        final Activity thisBattleScreen = this;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(thisBattleScreen, MainActivity.class));
            }
        });
    }

    public void updateButtons(BattleAction[] buttons) {
        for (int i = 0; i < buttons.length; ++i) {
            if (buttons[i] != null) {
                battleButtons[i].setText(buttons[i].getName());
            } else {
                battleButtons[i].setText("");
            }
        }
    }

    public void updateTickProgress(int progress) {
        newBattleView.updateTickProgress(progress);
    }

    public void setQueues(BattleQueue q1, BattleQueue q2) {
        newBattleView.setQueues(q1, q2);
        refreshQueues();
    }

    public void refreshQueues() {
        newBattleView.refreshQueues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.battle_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
