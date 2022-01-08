package banking;

public class Main {
    public static void main(String[] args) {
        CoolJDBC jdbc = new CoolJDBC(args[1]);
        try {
            jdbc.creatSchema();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Actions actions = new Actions(jdbc);
        actions.startPage();
    }
}