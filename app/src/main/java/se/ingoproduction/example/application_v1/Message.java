package se.ingoproduction.example.application_v1;

public class Message {
    private String name;
    private String info;

    public Message(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public String name() {
        return name;
    }

    public String info() {
        return info;
    }

    @Override
    public String toString() {
        return "\n"+"Substance: " + this.name + "\n" +
                this.name +" " + this.info + "\n" +
                "----------" +"\n";
    }

}
