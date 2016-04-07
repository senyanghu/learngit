import java.util.*;

public class Scanner {
	public ArrayList<String> literalAtoms;
	public ArrayList<Integer> numericAtoms;
	public int openParentheses;
	public int closeParentheses;
	public int curIndex;
	public StringBuffer content;
	public HashSet<Character> delim;

	public Scanner() {
		this.literalAtoms = new ArrayList<String>();
		this.numericAtoms = new ArrayList<Integer>();
		this.openParentheses = 0;
		this.closeParentheses = 0;
		this.curIndex = 0;
		this.content = new StringBuffer();
		this.delim = new HashSet<Character>();
		this.delim.add(' ');
		this.delim.add('\n');
		this.delim.add('\r');
	}

	public void addOpenParentheses() {
		this.openParentheses++;
	}

	public void addCloseParentheses() {
		this.closeParentheses++;
	}

	public void addNumber(int num) {
		this.numericAtoms.add(num);
	}

	public void addString(String str) {
		this.literalAtoms.add(str);
	}

	public int getOpenParenSize() {
		return this.openParentheses;
	}

	public int getCloseParenSize() {
		return this.closeParentheses;
	}

	public String getNextToken() {
		// default
		StringBuffer res = new StringBuffer("EOF");

		// Skip the delim
		while (curIndex < content.length()
				&& delim.contains(content.charAt(curIndex))) {
			this.curIndex++;
		}

		// Judge the type of token
		if (curIndex < content.length()) {
			if (content.charAt(curIndex) == '(') {
				curIndex++;
				res = new StringBuffer("(");
			} else if (content.charAt(curIndex) == ')') {
				curIndex++;
				res = new StringBuffer(")");
			} else if (Character.isLetterOrDigit(content.charAt(curIndex))) {
				int startIndex = curIndex;
				while (curIndex < content.length()
						&& Character.isLetterOrDigit(content.charAt(curIndex))) {
					curIndex++;
				}
				res = new StringBuffer(content.substring(startIndex, curIndex));
				boolean isValid = checkValidity(res);
				if (!isValid) {
					System.out
							.println("ERROR: Invalid token " + res.toString());
					System.exit(0);
				}
			}
		}
		return res.toString();
	}

	private boolean checkValidity(StringBuffer res) {
		if (Character.isDigit(res.charAt(0))) {
			for (int i = 1; i < res.length(); i++) {
				if (Character.isLetter(res.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkNumber(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public static void main(String args[]) {
		Scanner scanner = new Scanner();
		java.util.Scanner reader = new java.util.Scanner(System.in);
		while (reader.hasNextLine()) {
			scanner.content.append(reader.nextLine());
		}
		reader.close();
		/**
		scanner.content = new StringBuffer(
				" (DEFUN F23 (X) (PLUS EOF X 12 55))\n()() hello"
						+ "\r () ( ADC123");
		*/
		String token = scanner.getNextToken();

		while (!token.equals("EOF")
				|| scanner.curIndex < scanner.content.length()) {
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
		System.out.println("LITERAL ATOMS:" + scanner.literalAtoms.size()
				+ scanner.literalAtoms.toString());
		System.out.println("NUMERIC ATOMS:" + scanner.numericAtoms.size()
				+ scanner.numericAtoms.toString());
		System.out.println("OPEN PARENTHESES:" + scanner.getOpenParenSize());
		System.out
				.println("CLOSING PARENTHESES:" + scanner.getCloseParenSize());
	}
}
