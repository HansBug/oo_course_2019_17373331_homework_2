package homework;

import homework.expression.TriProdTermTree;
import homework.expression.core.Expression;
import homework.expression.parse.ExpParser;

import java.io.InputStream;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println(getAns(System.in));
    }

    static String getAns(InputStream stream) {
        return getAns(stream, false);
    }

    static String getAns(InputStream stream, boolean debug) {
        Scanner scanner = new Scanner(stream);
        String line;
        if (scanner.hasNextLine()) {
            line = scanner.nextLine();
        } else {
            line = "";
        }
        return getAns(line, debug);
    }

    private static String getAns(String line) {
        return getAns(line, false);
    }

    static String getAns(String line, boolean debug) {
        Expression exp = ExpParser.getInstance().parseExp(line, debug);
        if (debug) {
            System.out.println("Main.getAns:");
            System.out.println("input exp = " + exp);
        }
        if (exp == null) {
            return "WRONG FORMAT!";
        }
        exp = exp.diff();
        if (debug) {
            System.out.println("Main.getAns:");
            System.out.println("diff exp = " + exp);
        }
        return ((TriProdTermTree) exp.compute(new TriProdTermTree()))
            .toTriExp().toString();
    }
}
