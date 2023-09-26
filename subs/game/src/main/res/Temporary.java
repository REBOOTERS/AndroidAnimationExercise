/**
 * Created by Dimitris on 13/9/2017.
 */

public class Temporary {
    int minutes = 123 / (60 * 1000);
    int seconds = (123 / 1000) % 60;
    String str = String.format("%d:%02d", minutes, seconds);
    System.out.println(str);
}
