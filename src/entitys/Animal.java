package entitys;

import config.Config;
import enums.AnimalType;
import enums.Gender;
import island.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Animal implements Eatable{
    private AnimalType type;
    private Gender gender;
    private Location location;
    private int age = Config.AGE;
    private int health = Config.START_HEALTH;
    private boolean isAlive = Config.IS_ALIVE;
    private int reproductionCooldown = Config.REPRODUCTION_COOLDOWN;
    private Map<AnimalType, Integer> probabilityOfEating;

    public Animal(AnimalType type, Gender gender, Location location){
        this.type = type;
        this.gender = gender;
        this.location = location;
        probabilityOfEating = new HashMap<>();
    }

    protected abstract void eat(Eatable food);

    protected abstract void move(Location location);

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

    public Map<AnimalType, Integer> getProbabilityOfEating() {
        return probabilityOfEating;
    }
}
