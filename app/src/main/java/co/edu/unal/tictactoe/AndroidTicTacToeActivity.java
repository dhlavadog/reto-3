package co.edu.unal.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;


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

    private static final int DIALOG_DIFFICULTY_ID = 0;
    private static final int DIALOG_QUIT_ID = 1;
    private static final int DIALOG_ABOUT_ID = 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_game) {
            startNewGame();
            return true;
        } else if (item.getItemId() == R.id.difficulty) {
            showDialog(DIALOG_DIFFICULTY_ID);
            return true;
        } else if (item.getItemId() == R.id.quit) {
            showDialog(DIALOG_QUIT_ID);
            return true;
        } else if (item.getItemId() == R.id.about) {
            showDialog(DIALOG_ABOUT_ID);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DIFFICULTY_ID:
                return new AlertDialog.Builder(this)
                        .setTitle("Difficulty")
                        .setSingleChoiceItems(
                                new String[]{"Easy", "Harder", "Expert"},
                                mGame.getDifficultyLevel().ordinal(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mGame.setDifficultyLevel(
                                                TicTacToeGame.DifficultyLevel.values()[which]
                                        );
                                        dialog.dismiss();
                                    }
                                })
                        .create();

            case DIALOG_QUIT_ID:
                return new AlertDialog.Builder(this)
                        .setTitle("Quit")
                        .setMessage("Are you sure you want to quit?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("No", null)
                        .create();

            case DIALOG_ABOUT_ID:
                LayoutInflater inflater = getLayoutInflater();
                View aboutView = inflater.inflate(R.layout.about_dialog, null);

                return new AlertDialog.Builder(this)
                        .setTitle("About")
                        .setView(aboutView)
                        .setPositiveButton("OK", null)
                        .create();

            default:
                return null;
        }
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