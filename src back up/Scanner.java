import java.util.*;

public class Scanner {
	private ArrayList<String> literalAtoms;
	private ArrayList<Integer> numericAtoms;
	private int openParentheses;
	private int closeParentheses;
	private int curIndex;
	private StringBuffer content;
	private HashSet<Character> delim;

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

	public int getContentLength() {
		return this.content.length();
	}

	public int getCurIndex() {
		return this.curIndex;
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

	public int getLiteralAtomsSize() {
		return this.literalAtoms.size();
	}

	public int getNumericAtomsSize() {
		return this.numericAtoms.size();
	}

	public int getNumericTotal() {
		int total = 0;
		for (int i = 0; i < numericAtoms.size(); i++) {
			total += numericAtoms.get(i);
		}
		return total;
	}

	public String showLiteralAtoms() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < literalAtoms.size(); i++) {
			sb.append(" " + literalAtoms.get(i) + ",");
		}
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
			sb = sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	// Since the str transferred there does not have any line separator so I add
	// it manually
	public void appendContent(String str) {
		this.content.append(str + "\n");
	}

	/**
	 * This method is to get the next token back
	 * @return the next token
	 */
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

	public boolean checkNumber(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
}
