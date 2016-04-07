import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by chen on 15/9/28.
 */
public class IntepreterBefore {

    public static class IllegalInputException extends RuntimeException {
        public IllegalInputException(){
            super();
        }

    }

    public static class LispScanner{
        public StringBuffer input;
        public int curIdx;
        LispScanner (String input){
            this.input = new StringBuffer(input);
            this.curIdx = 0;
        }

        public String getNextToken(){
            StringBuffer ret = new StringBuffer();
            HashSet<Character> whiteSpace = new HashSet<Character>();
            whiteSpace.add(' ');
            whiteSpace.add('\n');
            whiteSpace.add('\r');
            while (curIdx < input.length() && whiteSpace.contains(input.charAt(curIdx))) curIdx++;

            if (curIdx < input.length()) {

                if (input.charAt(curIdx) == '(') {

                    ret = new StringBuffer("(");
                    curIdx++;

                } else if (input.charAt(curIdx) == ')') {

                    ret = new StringBuffer(")");
                    curIdx++;

                } else if (input.charAt(curIdx) == '.') {

                    ret = new StringBuffer(".");
                    curIdx++;

                } else {

                    int start = curIdx;
                    while (curIdx < input.length() && (Character.isAlphabetic(input.charAt(curIdx))
                            || Character.isDigit(input.charAt(curIdx)))) {

                        curIdx++;
                    }
                    ret = new StringBuffer(input.substring(start, curIdx));

                }
            }
            while (curIdx < input.length() && whiteSpace.contains(input.charAt(curIdx))) curIdx++;
            return ret.toString();
        }

        public boolean hasNext(){
            return (curIdx < input.length());
        }

    }

    public static class TreeNode{
        TreeNode lef, rig;
        String val;
        boolean isList;

        TreeNode(){
            lef = null;
            rig = null;
            this.val = "@";
            isList = false;
        }

    }

    public static class LispParser{
        public static LispScanner scanner;

        public static boolean isAtom(String s){

            boolean hasChar = false;
            for (char ch: s.toCharArray()) {
                if (Character.isAlphabetic(ch)) {
                    if (!hasChar) hasChar = true;
                } else if (!Character.isDigit(ch)) {
                    return false;
                }
            }
            if (hasChar && !Character.isAlphabetic(s.charAt(0)))
                return false;
            return true;
        }

        LispParser(LispScanner sc) {
            this.scanner = sc;
        }

        public static void parseStart(TreeNode root){

            root.lef = new TreeNode();
            parseSexp(root.lef);

            if (scanner.hasNext()) {

                root.rig = new TreeNode();
                parseStart(root.rig);

            }
        }

        public static void parseSexp(TreeNode root){

            String token = scanner.getNextToken();
            try {
            if (!token.equals("(") && !isAtom(token)) {
                throw new IllegalInputException();
            }

            else if (token.equals("(")){

                root.lef = new TreeNode();
                parseSexp(root.lef);
                if (!scanner.getNextToken().equals("."))
                    throw  new IllegalInputException();
                root.rig = new TreeNode();
                parseSexp(root.rig);
                if (!scanner.getNextToken().equals(")"))
                    throw  new IllegalInputException();
                if (root.rig.isList) root.isList = true;


            } else {

                root.val = token;
                if (token.equals("NIL")) root.isList = true;
            }

            } catch (Exception e) {
                System.out.print("ERROR: the input is not a valid S-expression");
                System.exit(0);
            }
        }
    }

    public static boolean printAsList(TreeNode root) {

        if (root == null) return true;
        if (root.lef == null && root.rig == null) return true;
        return printAsList(root.lef) && printAsList(root.rig) && root.isList;

    }

    public static void printSExpression(TreeNode root, int lefFlag){  //0 for right 1 for left

        if (root == null) return;
        if (!printAsList(root)) {
            if (root.lef == null && root.rig == null) {
                System.out.print(root.val);
            } else {
                System.out.print("(");
                printSExpression(root.lef, 1);
                System.out.print(".");
                printSExpression(root.rig, 0);
                System.out.print(")");
            }
        } else {
            if (root.lef == null && root.rig == null) {
                System.out.print(root.val);
            } else {
                System.out.print("(");
                printSExpression(root.lef, 0);
                while (!(root.lef == null && root.rig == null)) {
                    root = root.rig;
                    if (root.rig != null) System.out.print(" ");
                    printSExpression(root.lef, 1);
                }
                System.out.print(")");
            }
        }

    }


    public static void main(String args[]) {
        Scanner reader = new Scanner(System.in);
        String progInput = "";
        while (reader.hasNextLine()) {
            progInput += reader.nextLine();
        }
        reader.close();
//        String progInput = "(S . (T . (W . (Z . NIL))))\n" +
//                "((A . (B . NIL)) . (C . NIL))\n" +
//                "(A . (B . ((C . (D . NIL)) . NIL)))\n" +
//                "((A . NIL) . NIL)\n" +
//                "(A . ((B . C) . NIL))";

        LispScanner sc = new LispScanner(progInput);

        LispParser lp = new LispParser(sc);
        TreeNode root = new TreeNode();
        lp.parseStart(root);
        while (!(root == null)) {
            printSExpression(root.lef, 1);
            System.out.println();
            root = root.rig;
        }
    }
}
