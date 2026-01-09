package enums;

import java.util.Random;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender random(){
        Random random = new Random();
        return random.nextBoolean() ? MALE : FEMALE;
    }

    public String getDisplayName(){
        return this == MALE ? "Самец" : "Самка";
    }
}
