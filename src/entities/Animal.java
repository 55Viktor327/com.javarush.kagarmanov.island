package entities;

import config.Config;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;
import simulation.StepContext;

public abstract class Animal implements Eatable{
    private AnimalType type;
    private Gender gender;
    protected Location location;
    private int age;
    private int health;
    private volatile boolean isAlive;
    private int reproductionCooldown;
    private double currentWeight;
    private volatile boolean markedForRemoval;

    protected Animal(AnimalType type, Gender gender, Location location){
        this.type = type;
        this.gender = gender;
        this.location = location;
        this.markedForRemoval = false;
        this.age = Config.AGE;
        this.health = Config.START_HEALTH;
        this.isAlive = Config.IS_ALIVE;
        this.reproductionCooldown = Config.START_REPRODUCTION_COOLDOWN;
        this.currentWeight = Config.WEIGHT_OF_ANIMAL_IN_KG.get(this.getType());
    }

    public AnimalType getType() {
        return type;
    }

    public Gender getGender() {
        return gender;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getAge() {
        return age;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getReproductionCooldown() {
        return reproductionCooldown;
    }

    public void setReproductionCooldown(int reproductionCooldown) {
        this.reproductionCooldown = reproductionCooldown;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }

    public abstract void eat(StepContext context);

    public abstract void move(StepContext context);

    public abstract void reproduce(StepContext context);

    public void die(){
        isAlive = false;
        health = 0;
        markedForRemoval = true;
    }

    public void age(StepContext context){
        this.age++;
        this.decreaseReproductionCooldown();
        if (this.age >= this.getType().getMaxAge()) {
            if(context != null){
                context.markAnimalForRemoval(this);
            }
        }
    }

    public void gainWeight(double amount) {
        currentWeight = Math.min(currentWeight + amount, Config.WEIGHT_OF_ANIMAL_IN_KG.get(this.getType()));
    }

    public void loseWeight(double amount, StepContext context) {
        currentWeight -= amount;
        if (currentWeight <= Config.WEIGHT_OF_ANIMAL_IN_KG.get(this.getType()) * 0.3) { // Умирает при 30% от максимума
            if(context != null){
                context.markAnimalForRemoval(this);
            }
        }
    }


    public void decreaseReproductionCooldown() {
        if (reproductionCooldown > 0) {
            reproductionCooldown--;
        }
    }

    public boolean canReproduce(){
        return currentWeight >= (this.getCurrentWeight()*0.7) &&
                this.getReproductionCooldown() ==0 &&
                this.getAge() >= 1 &&
                this.getHealth() >= (health * 0.8);
    }
}
