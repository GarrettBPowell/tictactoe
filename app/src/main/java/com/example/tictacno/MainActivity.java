package com.example.tictacno;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton[][] buttons = new ImageButton[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

//set all tags to "trash" and all background images to the trashcan
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setBackgroundResource(R.drawable.trash);
                buttons[i][j].setTag("trash");
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

//use tags to check the state of the image button
    @Override
    public void onClick(View v) {
        if (!((ImageButton) v).getTag().equals("trash")){
            return;
        }
        if (player1Turn) {
            ((ImageButton) v).setBackgroundResource(R.drawable.screaming);
            ((ImageButton) v).setTag("scream");
        }
        else {
            ((ImageButton) v).setBackgroundResource(R.drawable.racoon);
            ((ImageButton) v).setTag("rac");
        }

        roundCount++;

        if (checkForWin())
        {
            if (player1Turn)
            {
                player1Wins();
            }
            else
            {
                player2Wins();
            }
        }
        else if (roundCount == 9)
        {
            draw();
        }
        else
        {
            player1Turn = !player1Turn;
        }

    }

    private boolean checkForWin() {
        Object[][] field = new Object[3][3];

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
                field[i][j] = buttons[i][j].getTag();
        }

        for (int i = 0; i < 3; i++)
        {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("trash"))
                return true;
        }

        for (int i = 0; i < 3; i++)
        {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("trash"))
                return true;
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("trash"))
            return true;

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("trash")) {
            return true;
        }

        return false;
    }

    private void player1Wins()
    {
        MediaPlayer yosh = MediaPlayer.create(this, R.raw.yoshi);
        yosh.start();
        player1Points++;
        Toast.makeText(this, "Opossum wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
    //delay reset board
        new Handler().postDelayed(new Runnable()
        {
            public void run () {
                resetBoard();
            }
        }, 2000);
    }

    private void player2Wins()
    {
       MediaPlayer yosh = MediaPlayer.create(this, R.raw.yoshi);
       yosh.start();
        player2Points++;
        Toast.makeText(this, "Raccoon wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();

    //delay reset board
        new Handler().postDelayed(new Runnable()
        {
            public void run ()
            {
                resetBoard();
            }
        }, 2000);
    }

    private void draw()
    {
        MediaPlayer ahh = MediaPlayer.create(this, R.raw.ahh);
        ahh.start();
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();

        //delay reset board
        new Handler().postDelayed(new Runnable() {
            public void run () {
                resetBoard();
            }
        }, 1000);
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setBackgroundResource(R.drawable.trash);
                buttons[i][j].setTag("trash");
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}