package entities.enums;

public enum AnimalType{
    WOLF(50.0,30,3,8.0,15, 2),
    FOX(8.0,30,1,3.0,10, 2),
    BEAR(500.0,5,2,80.0,30, 3),
    BOA(15.0,30,1,3.0,15, 3),
    EAGLE(6.0,20,3,1.0,10, 1),
    HORSE(400.0, 20, 4,60.0,20, 4),
    DEER(300.0,20,4,50.0,15, 4),
    RABBIT(2.0,150,2,0.45,5, 1),
    GOAT(60.0,140,3,10.0,10, 1),
    SHEEP(70.0,140,3,15.0,12, 1),
    BUFFALO(700.0,10,3,100.0,20, 5),
    MOUSE(0.05,500,1,0.01,1, 1),
    BOAR(400.0,50,2,50.0,15, 2),
    DUCK(1.0,200,4,0.15,5, 1),
    CATERPILLAR(0.01,1000,0,0.0,1, 0);

    private final double weight;
    private final int maxPopulation;
    private final int maxSpeed;
    private final double foodRequired;
    private final int maxAge;
    private final int reproductionCooldown;

    AnimalType(double weight, int maxPopulation, int maxSpeed, double foodRequired, int maxAge, int reproductionCooldown){
        this.weight = weight;
        this.maxPopulation = maxPopulation;
        this.maxSpeed = maxSpeed;
        this.foodRequired = foodRequired;
        this.maxAge = maxAge;
        this.reproductionCooldown = reproductionCooldown;
    }

    public double getWeight() {
        return weight;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public double getFoodRequired() {
        return foodRequired;
    }

    public int getMaxPopulation() {
        return maxPopulation;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getReproductionCooldown() {
        return reproductionCooldown;
    }
}
