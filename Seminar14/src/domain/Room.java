package domain;

public class Room {
    private int number;
    private String type;
    private int PricePerNight;
    private String description;

    public Room(int number, String type, int pricePerNight, String description) {
        this.number = number;
        this.type = type;
        PricePerNight = pricePerNight;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPricePerNight() {
        return PricePerNight;
    }

    public void setPricePerNight(int pricePerNight) {
        PricePerNight = pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number=" + number +
                ", type='" + type + '\'' +
                ", PricePerNight=" + PricePerNight +
                ", description='" + description + '\'' +
                '}';
    }
}
