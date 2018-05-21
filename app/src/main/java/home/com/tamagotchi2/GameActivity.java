package home.com.tamagotchi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView tv_secret_word;
    private TextView tv_alphabet;
    private Button btn_ok;
    private EditText et_letter;
    private ImageView iv_hangman;

    private static int MAX_MISTAKES = 6;

    private ArrayList<String> dictionary;
    private String word;
    private StringBuilder secretWord;
    private int mistakes;
    private StringBuilder alphabet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initUI();
        startNewGame();
    }

    private void initUI() {
        tv_secret_word = findViewById(R.id.tv_secret_word);
        tv_alphabet = findViewById(R.id.tv_alphabet);
        btn_ok = findViewById(R.id.btn_ok);
        et_letter = findViewById(R.id.et_letter);
        iv_hangman = findViewById(R.id.iv_hangman);
    }

    private void startNewGame() {
        dictionary = FileHelper.getDictionary(getApplicationContext(), "dictionary.txt");
        word = getRandomWord(dictionary);
        secretWord = getSecretWord(word);
        mistakes = 0;
        alphabet = new StringBuilder();

        tv_secret_word.setText(secretWord);
        tv_alphabet.setText("");
    }

    private StringBuilder getSecretWord(String word) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            stringBuilder.append('*');
        }
        return stringBuilder;
    }

    private String getRandomWord(ArrayList<String> dictionary) {
        Random random = new Random();
        int index = random.nextInt(dictionary.size());
        return dictionary.get(index);
    }

    public void btnOkListener(View v) {
        String s = et_letter.getText().toString().toUpperCase();
        if (s.length() == 0) {
            return;
        }
        char c = s.charAt(0);
        boolean mistake = true;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == c) {
                secretWord.setCharAt(i, c);
                mistake = false;
            }
        }
        if (mistake) {
            mistakes++;
        }

        if(mistakes == MAX_MISTAKES){
            Intent intent = new Intent();
            intent.putExtra("game_result", "lose");
            setResult(RESULT_OK, intent);
            finish();
        }

        if(word.equals(secretWord.toString())){
            Intent intent = new Intent();
            intent.putExtra("game_result", "win");
            setResult(RESULT_OK, intent);
            finish();
        }

        if(alphabet.indexOf(s) == -1){
            alphabet.append(s).append(" ");
        }
        tv_secret_word.setText(secretWord);
        tv_alphabet.setText(alphabet);
        et_letter.setText("");
    }
}
