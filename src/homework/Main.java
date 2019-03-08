package homework;

import homework.poly.PolyExp;
import homework.poly.parse.PolyParser;

import java.io.InputStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println(getAns(System.in));
    }

    static String getAns(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        String line;
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
        } else {
            line = "";
        }
        return getAns(line);
    }

    private static String getAns(String line) {
        return getAns(line, false);
    }

    static String getAns(String line, boolean debug) {
        return getAnsByPolyMultiMatcher(line, debug);
    }

    private static String getAnsByPolyMultiMatcher(String input,
                                                   boolean debug) {
        PolyExp exp = PolyParser.getInstance().parseExp(input, debug);
        if (exp == null) {
            return "WRONG FORMAT!";
        } else {
            PolyExp der = exp.getDerivative();
            return der.toString();
        }
    }
}
