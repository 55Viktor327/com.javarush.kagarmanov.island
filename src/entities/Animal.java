package entities;

import config.Config;
import entities.enums.AnimalType;
import entities.enums.Gender;
import island.Location;

import java.util.*;

public abstract class Animal implements Eatable{
    private AnimalType type;
    private Gender gender;
    private Location location;
    private int age;
    private int health;
    private boolean isAlive;
    private int reproductionCooldown;
    private double currentWeight;

    protected Animal(AnimalType type, Gender gender, Location location){
        this.type = type;
        this.gender = gender;
        this.location = location;
        this.age = Config.AGE;
        this.health = Config.START_HEALTH;
        this.isAlive = Config.IS_ALIVE;
        this.reproductionCooldown = Config.START_REPRODUCTION_COOLDOWN;
        this.currentWeight = Config.WEIGHT_OF_ANIMAL_IN_KG.get(this.getType());
    }

    protected abstract void eat(Eatable food);

    protected abstract void move();

    protected abstract Optional<Animal> reproduce(Animal partner);

    public void die(){
        isAlive = false;
        health = 0;
    }

    public void age(){
        age++;
        if(age >= type.getMaxAge()){
            die();
        }
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

    public void gainWeight(double amount) {
        currentWeight = Math.min(currentWeight + amount, Config.WEIGHT_OF_ANIMAL_IN_KG.get(this.getType()));
    }

    public void loseWeight(double amount) {
        currentWeight -= amount;
        if (currentWeight <= Config.WEIGHT_OF_ANIMAL_IN_KG.get(this.getType()) * 0.3) { // Умирает при 30% от максимума
            die();
        }
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public double getNutritionalValue() {
        return currentWeight;
    }
}
