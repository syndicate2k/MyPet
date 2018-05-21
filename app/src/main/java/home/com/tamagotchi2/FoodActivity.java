package home.com.tamagotchi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import home.com.tamagotchi2.food.Food;
import home.com.tamagotchi2.food.FoodAdapter;

public class FoodActivity extends AppCompatActivity {

    protected Food[] food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        food = makeFood();

        FoodAdapter adapter = new FoodAdapter(this, food);
        ListView lv = findViewById(R.id.lv_food_list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("food_result", food[i].getCalories());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    Food[] makeFood() {
        Food[] arr = new Food[7];
        arr[0] = new Food(10,"CHICKEN");
        arr[1] = new Food(30,"CAKE");
        arr[2] = new Food(5,"HOT DOG");
        arr[3] = new Food(15, "PIZZA");
        arr[4] = new Food(12, "FISH");
        arr[5] = new Food(2, "STRAWBERRY");
        arr[6] = new Food(8, "OMELET");
        return arr;
    }
}