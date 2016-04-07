public class Intepreter {

	public static void main(String args[]) {
		Scanner scanner = new Scanner();
		java.util.Scanner reader = new java.util.Scanner(System.in);
		while (reader.hasNextLine()) {
			scanner.appendContent(reader.nextLine());
		}
		reader.close();

		/**
		 * scanner.content = new StringBuffer(
		 * " (DEFUN F23 (X) (PLUS EOF X 12 55))\n()() hello" +
		 * "\r () ( ADC123");
		 */
		String token = scanner.getNextToken();

		while (!token.equals("EOF")
				|| scanner.getCurIndex() < scanner.getContentLength()) {
			if (token.equals("(")) {
				scanner.addOpenParentheses();
			} else if (token.equals(")")) {
				scanner.addCloseParentheses();
			} else if (scanner.checkNumber(token)) {
				scanner.addNumber(Integer.parseInt(token));
			} else {
				scanner.addString(token);
			}
			token = scanner.getNextToken();
		}

		/**
		 * LITERAL ATOMS: number of atoms, atom1, atom2, ... NUMERIC ATOMS:
		 * number of atoms, sum of all atoms OPEN PARENTHESES: number of atoms
		 * CLOSING PARENTHESES: number of atoms
		 */

		/**
		 * LITERAL ATOMS: 5, DEFUN, F23, X, PLUS, X NUMERIC ATOMS: 2, 67 OPEN
		 * PARENTHESES: 3 CLOSING PARENTHESES: 3
		 */
		System.out.println("LITERAL ATOMS: " + scanner.getLiteralAtomsSize()
				+ "," + scanner.showLiteralAtoms());
		System.out.println("NUMERIC ATOMS: " + scanner.getNumericAtomsSize()
				+ ", " + scanner.getNumericTotal());
		System.out.println("OPEN PARENTHESES: " + scanner.getOpenParenSize());
		System.out.println("CLOSING PARENTHESES: "
				+ scanner.getCloseParenSize());
	}
}
