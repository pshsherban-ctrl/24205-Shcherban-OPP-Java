 
package model;

public class Auto {
    private final int id;
    private final Body body;
    private final Motor motor;
    private final Accessory accessory;

    public Auto(int id, Body body, Motor motor, Accessory accessory) {
        this.id = id;
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
    }

    public int getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }

    public Motor getMotor() {
        return motor;
    }

    public Accessory getAccessory() {
        return accessory;
    }
}