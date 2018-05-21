package home.com.tamagotchi2.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import home.com.tamagotchi2.R;


public class FoodAdapter extends ArrayAdapter<Food> {

    public FoodAdapter(Context context, Food[] arr) {
        super(context, R.layout.food_item, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Food food = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item, null);
        }

        // Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.tv_food_title)).setText(food.getFood());
        ((TextView) convertView.findViewById(R.id.tv_food_calories)).setText(String.valueOf(food.getCalories()));

        // Выбираем картинку для еды
        if (food.getFood().equals("CHICKEN"))
            ((ImageView) convertView.findViewById(R.id.iv_food)).setImageResource(R.drawable.chicken_foot);
        if (food.getFood().equals("CAKE"))
            ((ImageView) convertView.findViewById(R.id.iv_food)).setImageResource(R.drawable.cake);
        if (food.getFood().equals("HOT DOG"))
            ((ImageView) convertView.findViewById(R.id.iv_food)).setImageResource(R.drawable.hot_dog);
        if (food.getFood().equals("PIZZA"))
            ((ImageView) convertView.findViewById(R.id.iv_food)).setImageResource(R.drawable.pizza);
        if (food.getFood().equals("FISH"))
            ((ImageView) convertView.findViewById(R.id.iv_food)).setImageResource(R.drawable.fish);
        if (food.getFood().equals("STRAWBERRY"))
            ((ImageView) convertView.findViewById(R.id.iv_food)).setImageResource(R.drawable.strawberry);
        if (food.getFood().equals("OMELET"))
            ((ImageView) convertView.findViewById(R.id.iv_food)).setImageResource(R.drawable.omelet);

        return convertView;
    }
}
