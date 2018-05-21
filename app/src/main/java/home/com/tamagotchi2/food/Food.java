package home.com.tamagotchi2.food;



public class Food {
    private int calories;
    private String food;

    public Food() {
        this.calories = 0;
        this.food = "";
    }

    public Food(int calories, String food) {
        this.calories = calories;
        this.food = food;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
