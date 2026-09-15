package co.edu.unal.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;


public class AndroidTicTacToeActivity extends Activity {


    // Represents the internal state of the game
    private TicTacToeGame mGame;

    // Buttons making up the board
    private Button mBoardButtons[];

    // Various text displayed
    private TextView mInfoTextView;
    private boolean mGameOver;

    private TextView mHumanWinsTextView;
    private TextView mAndroidWinsTextView;
    private TextView mTiesTextView;

    private int mHumanWins;
    private int mAndroidWins;
    private int mTies;

    private boolean mHumanStarts = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.new_game) {

            startNewGame();
            return true;

        } else if (item.getItemId() == R.id.exit) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];

        mBoardButtons[0] = findViewById(R.id.one);
        mBoardButtons[1] = findViewById(R.id.two);
        mBoardButtons[2] = findViewById(R.id.three);

        mBoardButtons[3] = findViewById(R.id.four);
        mBoardButtons[4] = findViewById(R.id.five);
        mBoardButtons[5] = findViewById(R.id.six);

        mBoardButtons[6] = findViewById(R.id.seven);
        mBoardButtons[7] = findViewById(R.id.eight);
        mBoardButtons[8] = findViewById(R.id.nine);

        mInfoTextView = findViewById(R.id.information);

        mHumanWinsTextView = findViewById(R.id.human_wins);
        mAndroidWinsTextView = findViewById(R.id.android_wins);
        mTiesTextView = findViewById(R.id.ties);

        mGame = new TicTacToeGame();

        mHumanWins = 0;
        mAndroidWins = 0;
        mTies = 0;

        updateScore();

        startNewGame();
    }

    private void updateScore() {
        mHumanWinsTextView.setText("Human wins: " + mHumanWins);
        mAndroidWinsTextView.setText("Android wins: " + mAndroidWins);
        mTiesTextView.setText("Ties: " + mTies);
    }

    private void startNewGame() {

        mGame.clearBoard();

        mGameOver = false;

        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        // Alternate player turn
        if (mHumanStarts) {
            // Human goes first
            mInfoTextView.setText(R.string.you_go_first);
        } else {
            // Android goes first
            mInfoTextView.setText(R.string.android_turn);

            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);

            mInfoTextView.setText(R.string.your_turn);
        }

        mHumanStarts = !mHumanStarts;
    }

    private void setMove(char player, int location) {

        mGame.setMove(player, location);

        mBoardButtons[location].setEnabled(false);

        mBoardButtons[location].setText(String.valueOf(player));

        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

    private class ButtonClickListener implements View.OnClickListener {

        private int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View view) {

            if (!mGameOver && mBoardButtons[location].isEnabled()) {

                // Human makes a move
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                // Check if there is a winner
                int winner = mGame.checkForWinner();

                // If no winner yet, let the computer make a move
                if (winner == 0) {

                    mInfoTextView.setText(R.string.you_go_first);

                    int move = mGame.getComputerMove();

                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);

                    winner = mGame.checkForWinner();
                }

                // Display the result
                // Display the result
                if (winner == 0) {
                    mInfoTextView.setText(R.string.your_turn);
                } else if (winner == 1) {
                    mInfoTextView.setText(R.string.tie);
                    mTies++;
                    updateScore();
                } else if (winner == 2) {
                    mInfoTextView.setText(R.string.you_won);
                    mHumanWins++;
                    updateScore();
                } else {
                    mInfoTextView.setText(R.string.android_won);
                    mAndroidWins++;
                    updateScore();
                }

                if(winner != 0 )
                    mGameOver = true;
            }
        }
    }
}