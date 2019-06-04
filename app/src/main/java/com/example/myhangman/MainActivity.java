package com.example.myhangman;

import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private String answer;
    private String initial_word;
    private String[] words;
    private int required_num;
    private int num_of_guess_left = 6;
    private HashSet<String> hset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        words = getResources().getStringArray(R.array.words);
        setup_new_word();

        Button bt1 = (Button) findViewById(R.id.new_word);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setup_new_word();
            }
        });

        Button bt2 = (Button) findViewById(R.id.submit_word);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.input_word);
                String input_word = et.getText().toString();

                if (input_word.length() != 1) {
                    Toast.makeText(MainActivity.this, "Input only a character", Toast.LENGTH_SHORT).show();
                } else {
                    if (hset.contains(input_word)) {
                        Toast.makeText(MainActivity.this, "Letter has been selected", Toast.LENGTH_SHORT).show();
                    } else {
                        hset.add(input_word);
                        guess_the_word(input_word.charAt(0));
                    }
                }
            }
        });
    }

    public void guess_the_word(Character one_word) {
        boolean success_guess = false;

        StringBuilder sb2 = new StringBuilder("");

        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == one_word) {
                sb2.append(one_word);

                success_guess = true;
                required_num++;
            } else {
                sb2.append(initial_word.charAt(i));
            }
        }

        initial_word = sb2.toString();
        String update_life = "You have guessed: " + hset.toString() + " (" + num_of_guess_left + " guesses left)";
        TextView tv1 = (TextView) findViewById(R.id.num_guess_left);
        tv1.setText(update_life);

        if (required_num == answer.length()) {
            doWinTheDialog();
        } else {
            if (success_guess) {
                TextView tv = (TextView) findViewById(R.id.word_to_guess);
                tv.setText(initial_word);
            } else {
                num_of_guess_left--;

                change_image(num_of_guess_left);

                if (num_of_guess_left == 0) {
                    doLostTheDialog();
                }
            }
        }
    }

    private void doWinTheDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("You Win! The word is \"" + answer + "\"");
        builder.setMessage("Game will restarted after you click OK");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                setup_new_word();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void doLostTheDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("You Lost");
        builder.setMessage("Game will restarted after you click OK");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                setup_new_word();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void change_image(int id_picture) {
        ImageView iv = (ImageView) findViewById(R.id.hang_image);
        String hangword = "hangman" + Integer.toString(6 - id_picture);
        int fileID = getResources().getIdentifier(hangword.toLowerCase(), "drawable", getPackageName());
        iv.setImageResource(fileID);
    }

    public void setup_new_word() {
        hset = new HashSet<>();
        required_num = 0;
        num_of_guess_left = 6;

        change_image(num_of_guess_left);

        Random randy = new Random();
        int rand1 = randy.nextInt(words.length);

        answer = words[rand1];
        initial_word = "";

        String update_life = "You have guessed: (6 guesses left)";
        TextView tv1 = (TextView) findViewById(R.id.num_guess_left);
        tv1.setText(update_life);

        StringBuilder sb = new StringBuilder(initial_word);

        for(int i = 0; i < answer.length(); i++) {
            sb.append("?");
        }

        initial_word = sb.toString();

        TextView tv = (TextView) findViewById(R.id.word_to_guess);
        tv.setText(initial_word);
    }

}
