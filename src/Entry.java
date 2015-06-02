import java.util.ArrayList;

public class Entry {
	String label;
	int value;
	// it stores the possible values for the entry
	ArrayList<Integer> possibleValues;
	// the values that has already been checked
	ArrayList<Integer> checkedValue;
	// the variables which has been updated due to assignment to this entry
	ArrayList<Integer> updatedVariables;

	Entry() {

	}

	Entry(String label, int value, int row, int column, boolean set, int obNum) {
		this.label = label;
		this.value = value;
		this.row = row;
		this.column = column;
		this.obNum = obNum;
		this.possibleValues = new ArrayList<Integer>();
		this.checkedValue = new ArrayList<Integer>();
		this.updatedVariables = new ArrayList<Integer>();

		if (set) {
			countOfPossible = SodokuSolver.setValue;
		} else {
			for (int i = 1; i < SodokuSolver.sodokuLevel + 1; i++)
				possibleValues.add(i);

			countOfPossible = SodokuSolver.sodokuLevel;
		}

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	int countOfPossible;
	int row, column;
	int obNum;

	public int getObNum() {
		return obNum;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public ArrayList<Integer> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(ArrayList<Integer> possibleValues) {
		this.possibleValues = possibleValues;
	}

	public int getCountOfPossible() {
		return countOfPossible;
	}

	public void setCountOfPossible(int countOfPossible) {
		this.countOfPossible = countOfPossible;
	}

}
