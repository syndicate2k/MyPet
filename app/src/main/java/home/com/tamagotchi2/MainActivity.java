package home.com.tamagotchi2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Observer {

    private ProgressBar pb_hunger;
    private ProgressBar pb_energy;
    private ProgressBar pb_hygiene;
    private ProgressBar pb_fun;

    private ImageView iv_pet;

    private Button btn_eat;
    private Button btn_sleep;
    private Button btn_wash;
    private Button btn_heal;
    private Button btn_play;
    private Button btn_task;

    private Tamagotchi tamagotchi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tamagotchi = loadConfig();
        tamagotchi.setObserverUI(this);

        initUI();

        tamagotchi.lifeCycle();
    }

    private void initUI() {
        pb_hunger = findViewById(R.id.pb_hunger);
        pb_hunger.setProgress(tamagotchi.getHunger());

        pb_energy = findViewById(R.id.pb_energy);
        pb_energy.setProgress(tamagotchi.getEnergy());

        pb_hygiene = findViewById(R.id.pb_hygiene);
        pb_hygiene.setProgress(tamagotchi.getHygiene());

        pb_fun = findViewById(R.id.pb_fun);
        pb_fun.setProgress(tamagotchi.getFun());

        Animation in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        iv_pet = findViewById(R.id.iv_pet);
        //iv_pet.setInAnimation(in);
        //iv_pet.setOutAnimation(out);

        btn_eat = findViewById(R.id.btn_eat);
        btn_eat.setOnClickListener(this);

        btn_sleep = findViewById(R.id.btn_sleep);
        btn_sleep.setOnClickListener(this);

        btn_wash = findViewById(R.id.btn_wash);
        btn_wash.setOnClickListener(this);

        btn_heal = findViewById(R.id.btn_heal);
        btn_heal.setOnClickListener(this);

        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);

        btn_task = findViewById(R.id.btn_task);
        btn_task.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_eat:
                //tamagotchi.eat(10);
                Intent intent = new Intent(this, FoodActivity.class);
                startActivityForResult(intent, 1);
                iv_pet.setImageResource(R.drawable.cat_eat);
                iv_pet.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_pet.setImageResource(R.drawable.cat);
                    }
                }, 6000);
                break;
            case R.id.btn_heal:
                iv_pet.setImageResource(R.drawable.cat_heal);
                iv_pet.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_pet.setImageResource(R.drawable.cat);
                    }
                }, 5000);
                tamagotchi.heal();
                break;
            case R.id.btn_wash:
                iv_pet.setImageResource(R.drawable.cat_wash);
                iv_pet.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_pet.setImageResource(R.drawable.cat);
                    }
                }, 5000);
                tamagotchi.wash();
                break;
            case R.id.btn_sleep:
                iv_pet.setImageResource(R.drawable.cat_sleep);
                iv_pet.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_pet.setImageResource(R.drawable.cat);
                    }
                }, 5000);
                tamagotchi.sleep();
                break;
            case R.id.btn_play:
                Intent play_intent = new Intent(this, GameActivity.class);
                startActivityForResult(play_intent, 1);
                break;
            case R.id.btn_task:
                writeToFile("hungry " + tamagotchi.getHunger(), getApplicationContext());
                writeToFile("energy " + tamagotchi.getEnergy(), getApplicationContext());
                writeToFile("hygiene " + tamagotchi.getHygiene(), getApplicationContext());
                writeToFile("fun " + tamagotchi.getFun(), getApplicationContext());
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        pb_hunger.setProgress(tamagotchi.getHunger());
        pb_energy.setProgress(tamagotchi.getEnergy());
        pb_hygiene.setProgress(tamagotchi.getHygiene());
        pb_fun.setProgress(tamagotchi.getFun());

        if(tamagotchi.isHungry()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv_pet.setImageResource(R.drawable.cat_need_eat);
                }
            });
        }

        /*
        if(tamagotchi.getHunger() < 5){
            is_pet.setImageResource(R.drawable.cat_need_eat);
        }
        if(tamagotchi.getHygiene() < 5){
            is_pet.setImageResource(R.drawable.cat_need_wash);
        }
        if(tamagotchi.getEnergy() < 5){
            is_pet.setImageResource(R.drawable.cat_need_heal);
        }
        if(tamagotchi.getEnergy() >= 5 && tamagotchi.getHygiene() >= 5 && tamagotchi.getHunger() >= 5){
            is_pet.setImageResource(R.drawable.cat);
        }
        */


        if(!tamagotchi.isAlive()){
            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Кот умер", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        String game_result = data.getStringExtra("game_result");
        if (game_result != null) {
            if (game_result.equals("win")) {
                Toast.makeText(getApplicationContext(), "You win", Toast.LENGTH_LONG).show();
                tamagotchi.setFun(tamagotchi.getFun() + 25);
            } else {
                Toast.makeText(getApplicationContext(), "You lose", Toast.LENGTH_LONG).show();
                tamagotchi.setFun(tamagotchi.getFun() - 5);
            }
        }

        int food_result = data.getIntExtra("food_result", 0);
        if (food_result > 0) {
            Toast.makeText(getApplicationContext(), "Вы накормили котика " + food_result, Toast.LENGTH_LONG).show();
            tamagotchi.setFun(tamagotchi.getFun() + 25);
            tamagotchi.setHunger(tamagotchi.getHunger() + food_result);
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private Tamagotchi loadConfig() {
        int hungry = 100, energy = 100, hygiene = 100, fun = 100;
        try {
            InputStream inputStream = openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    String[] vals = receiveString.split(" ");
                    String key = vals[0];
                    int value = Integer.parseInt(vals[1]);
                    switch(key) {
                        case "hungry": hungry = value; break;
                        case "energy": energy = value; break;
                        case "hygiene": hygiene = value; break;
                        case "fun": fun = value; break;
                    }
                    Log.d("12345", key);
                    Log.d("12345", value+"");
                }
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return new Tamagotchi(hungry, energy, hygiene, fun);
    }

}
