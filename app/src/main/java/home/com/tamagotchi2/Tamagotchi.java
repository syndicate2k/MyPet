package home.com.tamagotchi2;

import java.util.Observer;
import java.util.Random;

public class Tamagotchi {
    private static final int MAX_VALUE = 100;
    private static final int MIN_VALUE = 0;

    private static final int DELTA_HUNGRY = 5;
    private static final int DELTA_ENERGY = 5;
    private static final int DELTA_HYGIENE = 9;
    private static final int DELTA_FUN = 15;

    private static final int TIME_HUNGRY = 600;
    private static final int TIME_ENERGY = 15000;
    private static final int TIME_HYGIENE = 30000;
    private static final int TIME_FUN = 100000;

    private boolean isSleeping;
    private boolean isHungry;
    private boolean isShit;

    private int hunger;
    private int energy;
    private int hygiene;
    private int fun;

    private boolean isAlive;

    private Observer observerUI;

    public Tamagotchi(int hungry, int energy, int hygiene, int fun) {
        this.hunger = hungry;
        this.energy = energy;
        this.hygiene = hygiene;
        this.fun = fun;
        this.isSleeping = false;
        this.isHungry = false;
        this.isShit = false;
        this.isAlive = true;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = checkValue(hunger);
        sendUpdates();
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = checkValue(energy);
        sendUpdates();
    }

    public int getHygiene() {
        return hygiene;
    }

    public void setHygiene(int hygiene) {
        this.hygiene = checkValue(hygiene);
        sendUpdates();
    }

    public int getFun() {
        return fun;
    }

    public void setFun(int fun) {
        this.fun = checkValue(fun);
        sendUpdates();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public void setSleeping(boolean sleeping) {
        isSleeping = sleeping;
    }

    public Observer getObserverUI() {
        return observerUI;
    }

    public void setObserverUI(Observer observerUI) {
        this.observerUI = observerUI;
    }

    public boolean isHungry() {
        return isHungry;
    }

    public void eat(int food_amt) {
        if (hunger == MAX_VALUE) {
            setFun(this.fun - DELTA_FUN);
        } else {
            setFun(this.fun + DELTA_FUN);
        }

        setHunger(this.hunger + food_amt);
        setEnergy(this.energy + DELTA_ENERGY);
        setHygiene(this.hygiene - DELTA_HYGIENE);
    }

    public void heal() {
        setFun(this.fun - DELTA_FUN);
        setEnergy(this.energy + DELTA_ENERGY);
    }

    public void wash() {
        setHygiene(this.hygiene + DELTA_HYGIENE);
        this.isShit = false;
    }

    public void sleep(){
        //isSleeping = !isSleeping;
        //sendUpdates();
        setFun(this.fun - DELTA_FUN);
        setEnergy(this.energy + DELTA_ENERGY);
    }

    private int checkValue(int val) {
        if (val > MAX_VALUE) {
            val = MAX_VALUE;
        }

        if(val < MIN_VALUE){
            val = MIN_VALUE;
        }

        if(hunger == 0 && energy == 0 && hygiene == 0 && fun == 0){
            isAlive = false;
        }

        return val;
    }

    public void sendUpdates() {
        observerUI.update(null, null);
    }

    // Этот метод вызывается из главного потока GUI.
    public void lifeCycle() {
        startHungry();
        startEnergy();
        startHygiene();
        startFun();
        startDigestion();
    }

    private void startHungry() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TIME_HUNGRY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setHunger(hunger - 1);
                    if(getHunger() < 15){
                        isHungry = true;
                    }else{
                        isHungry = false;
                    }
                }
            }
        });
        thread.start();
    }

    private void startEnergy() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TIME_ENERGY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isSleeping){
                        setEnergy(energy + 1);
                    }else{
                        setEnergy(energy - 1);
                    }
                }
            }
        });
        thread.start();
    }

    private void startHygiene() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TIME_HYGIENE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isShit){
                        setHygiene(hygiene - 1);
                    }
                }
            }
        });
        thread.start();
    }

    private void startDigestion() {
        final int MIN_TIME = 3000;
        final int MAX_TIME = 10000;
        final Random random = new Random();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int timeDelay = MIN_TIME + random.nextInt(MAX_TIME - MIN_TIME);
                        Thread.sleep(timeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isShit = true;
                }
            }
        });
        thread.start();
    }

    private void startFun() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TIME_FUN);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isSleeping){
                        setFun(fun + 1);
                    }else{
                        setFun(fun - 1);
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public String toString() {
        return "Tamagotchi{" +
                "isSleeping=" + isSleeping +
                ", isShit=" + isShit +
                ", hunger=" + hunger +
                ", energy=" + energy +
                ", hygiene=" + hygiene +
                ", fun=" + fun +
                '}';
    }
}
