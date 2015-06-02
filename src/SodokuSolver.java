import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class SodokuSolver {

	HashMap<String, Entry> entryRecord;
	private String sCurrentLine;
	PriorityQueue<Entry> pq;

	public static int grid[][];
	public static Entry entryPool[];
	public static final int setValue = 50;
	public static final int sodokuLevel = 9;
	public int count_of_assignments = 0;

	public SodokuSolver() {
		// TODO Auto-generated constructor stub
		entryRecord = new HashMap<String, Entry>();
		pq = new PriorityQueue<Entry>(100, new EntryComparator());
		entryPool = new Entry[sodokuLevel * sodokuLevel];
		grid = new int[sodokuLevel][sodokuLevel];
	}

	/*
	 * Below function reads the input and and store the values in a grid and
	 * maintains an entryPool from where i can get values when ever required.
	 */
	public void readInput(String pathOfFile) throws IOException {
		int i = 0, obNum = 0, value;

		BufferedReader br = new BufferedReader(new FileReader(pathOfFile));
		String sCurrentline;
		String arrs[];
		while ((sCurrentLine = br.readLine()) != null && i < sodokuLevel) {
			arrs = sCurrentLine.split(" ");
			for (int j = 0; j < arrs.length; j++) {
				boolean set = false;
				value = Integer.parseInt(arrs[j]);

				grid[i][j] = value;

				if (value != 0) {
					set = true;
				}
				entryPool[obNum] = new Entry("A" + i + j, value, i, j, set,
						obNum);
				entryRecord.put("A" + i + j, entryPool[obNum]);
				obNum++;
			}
			i++;
		}

	}

	/*
	 * Main functIon for solving sodoku and applying heuristics. it takes input
	 * as the current grid
	 */
	public boolean solveSodoku(int[][] grid1) {
		// TODO Auto-generated method stub
		int lcv;
		Entry mcv = getMostConstarinedVariable(grid1);

		if (mcv == null)
			return true;

		for (int i = 0; i < mcv.getPossibleValues().size(); i++) {
			count_of_assignments++;
			lcv = getLeastConstrainingValue(mcv);
			grid1[mcv.getRow()][mcv.getColumn()] = lcv;
			entryPool[mcv.getObNum()].setValue(lcv);

			if (prunePossibleValues(mcv.getRow(), mcv.getColumn(), grid1)) {

				if (solveSodoku(grid1))
					return true;

				restoreValuesBeforePruning(mcv);
				entryPool[mcv.getObNum()].setValue(0);
				grid1[mcv.row][mcv.column] = 0;

			}

		}
		restoreValuesBeforePruning(mcv);
		entryPool[mcv.getObNum()].setValue(0);
		grid1[mcv.row][mcv.column] = 0;
		mcv.checkedValue.clear();
		count_of_assignments++;
		System.out.println("Backtrack");
		return false;
	}

	/*
	 * If we get a domain wipe out while forward checking this function restore
	 * the changes done in that particular cycle with the help of
	 * updatedVariables that is present in mcv
	 */

	private void restoreValuesBeforePruning(Entry mcv) {
		// TODO Auto-generated method stub
		Iterator t = mcv.updatedVariables.iterator();
		while (t.hasNext()) {
			int k = (Integer) t.next();
			entryPool[k].possibleValues.add(mcv.value);
			entryPool[k].setCountOfPossible(entryPool[k].possibleValues.size());

			t.remove();
		}

	}

	/*
	 * This function returns the least constraining value for a mcv This uses
	 * the count of row,column and box constraining variables to judge which
	 * value is least constraining and then return that value.
	 */
	private int getLeastConstrainingValue(Entry mcv) {

		ArrayList<Integer> a = mcv.getPossibleValues();
		int value = 0, lowestValue = -1, tkey = 0, count1 = 0;
		for (int i = 0; i < a.size(); i++) {
			// looping in column and box for the values that
			// are not zero and has possible value as this one.
			count1 = 0;
			value = a.get(i);
			if (mcv.checkedValue.contains(value))
				continue;
			count1 += countOfRowAndColumnConstrainings(mcv, value);
			count1 += countOfBoxConstrainings(mcv, value);

			if ((count1 < lowestValue || lowestValue == -1)
					&& !(mcv.checkedValue.contains(a.get(i)))) {
				lowestValue = count1;
				tkey = a.get(i);
			}
		}
		mcv.checkedValue.add(tkey);
		return tkey;
	}

	/*
	 * This function counts the number of constraining variables in row and
	 * column
	 */
	private int countOfRowAndColumnConstrainings(Entry mcv, int value) {
		// TODO Auto-generated method stub
		int count = 0;
		for (int k = 0; k < sodokuLevel; k++) {

			if (SodokuSolver.grid[mcv.row][k] == 0
					&& !(mcv.column == k)
					&& entryPool[(mcv.row * SodokuSolver.sodokuLevel + k)].possibleValues
							.contains(value))
				count += 1;
			if (SodokuSolver.grid[k][mcv.column] == 0
					&& !(mcv.row == k)
					&& entryPool[(k * SodokuSolver.sodokuLevel + mcv.column)].possibleValues
							.contains(value))
				count += 1;
		}
		return count;
	}

	/*
	 * This function counts the number of constraining variables in box
	 */
	private int countOfBoxConstrainings(Entry n, int value) {
		// TODO Auto-generated method stub
		int count = 0;
		int boxSize = (int) (SodokuSolver.sodokuLevel / Math
				.sqrt(SodokuSolver.sodokuLevel));
		int rowStart = n.row - n.row % boxSize;
		int colStart = n.column - n.column % boxSize;
		for (int i = 0; i < boxSize; i++)
			for (int j = 0; j < boxSize; j++) {
				if ((SodokuSolver.grid[rowStart + i][colStart + j] == 0)
						&& !((rowStart + i) == n.row || (colStart + j) == n.column)
						&& entryPool[((rowStart + i) * SodokuSolver.sodokuLevel
								+ colStart + j)].possibleValues.contains(value))
					count++;
			}
		return count;
	}

	/*
	 * This function returns the most Constraining variable It uses a
	 * priorityQueue and an EntryComparator class to obtain the most
	 * Constraining variable.
	 */

	private Entry getMostConstarinedVariable(int[][] grid1) {
		// TODO Auto-generated method stub

		pq.clear();
		int mostConstrained = 0;
		Iterator it = entryRecord.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Entry> map = (Map.Entry) it.next();
			int ob = ((Entry) map.getValue()).getObNum();
			if (entryPool[ob].getValue() == 0) {

				if (entryPool[ob].getPossibleValues().size() <= mostConstrained
						|| mostConstrained == 0) {
					mostConstrained = entryPool[ob].getPossibleValues().size();
					pq.add(entryPool[ob]);
				}
			}
		}
		if (pq.isEmpty())
			return null;

		return pq.remove();

	}

	/*
	 * It prints the final solution
	 */
	public void printSolution() {
		// TODO Auto-generated method stub
		for (int i = 0; i < sodokuLevel; i++) {
			for (int j = 0; j < sodokuLevel; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println("");
		}
	}

	/*
	 * This function updates the domains depending upon the initial values
	 * given.
	 */
	public void preProcessing() {
		// TODO Auto-generated method stub
		for (int i = 0; i < sodokuLevel; i++)
			for (int j = 0; j < sodokuLevel; j++) {
				if (grid[i][j] != 0)
					prunePossibleValues(i, j, grid);
			}

	}

	/*
	 * This function prunes the domains of the variables that are affected by
	 * setting the value of the variable at param 1 : row of the variable whose
	 * value is set. param 2 : column of the variable whose value is set. param
	 * 3 : copy of the grid.
	 */
	private boolean prunePossibleValues(int row, int column, int[][] grid) {
		// TODO Auto-generated method stub
		if (!dependingOnRowAndColumn(row, column)) {
			System.out.println("sending false depending on row and column");
			return false;
		}
		if (!dependingOnBox(row, column)) {
			System.out.println("sending false depending on box");
			return false;
		}
		return true;
	}

	/*
	 * Prunes the values of the variables depending on Box
	 */
	private boolean dependingOnBox(int row, int column) {
		// TODO Auto-generated method stub

		int mcvNum = row * SodokuSolver.sodokuLevel + column;
		int boxSize = (int) (sodokuLevel / Math.sqrt(sodokuLevel));
		int rowStart = row - row % boxSize;
		int colStart = column - column % boxSize;
		for (int i = 0; i < boxSize; i++)
			for (int j = 0; j < boxSize; j++) {
				if (grid[rowStart + i][colStart + j] == 0) {
					String name = "A" + (rowStart + i) + (colStart + j);
					Entry temp = entryRecord.get(name);
					int obnum = temp.getObNum();
					ArrayList<Integer> a = entryPool[obnum].getPossibleValues();
					if (a.contains(grid[row][column])) {
						entryPool[mcvNum].updatedVariables.add(obnum);
						a.remove((Integer) grid[row][column]);
					}
					if (a.size() < 1)
						return false;

					entryPool[obnum].setPossibleValues(a);
					entryPool[obnum].setCountOfPossible(entryPool[obnum]
							.getPossibleValues().size());
				}

			}
		return true;
	}

	/*
	 * Prunes the values of the variables depending on row and column
	 * It updates the values in entryPool
	 */
	private boolean dependingOnRowAndColumn(int row, int column) {
		// TODO Auto-generated method stub
		int mcvNum = row * SodokuSolver.sodokuLevel + column;

		for (int k = 0; k < sodokuLevel; k++) {
			if (grid[row][k] == 0) {
				String name = "A" + row + k;
				Entry temp = entryRecord.get(name);
				/*
				 * The below code gets the possible value
				 * and of the entry and if that contains the 
				 * value assigned to current entry as possible value
				 * then that value is removed from entry.
				 */if (entryPool[temp.getObNum()].possibleValues
						.contains(grid[row][column])) {
					entryPool[mcvNum].updatedVariables.add(temp.getObNum());
					entryPool[temp.getObNum()].possibleValues
							.remove((Integer) grid[row][column]);
				}

				entryPool[temp.getObNum()].setCountOfPossible(entryPool[temp
						.getObNum()].getPossibleValues().size());
				if (entryPool[temp.getObNum()].possibleValues.size() < 1)
					return false;

			}
			if (grid[k][column] == 0) {
				String name = "A" + k + column;
				Entry temp = entryRecord.get(name);
				/*
				 * The below code gets the possible value
				 * and of the entry and if that contains the 
				 * value assigned to current entry as possible value
				 * then that value is removed from entry.
				 */
				if (entryPool[temp.getObNum()].possibleValues
						.contains(grid[row][column])) {
					entryPool[mcvNum].updatedVariables.add(temp.getObNum());
					entryPool[temp.getObNum()].possibleValues
							.remove((Integer) grid[row][column]);
				}

				entryPool[temp.getObNum()].setPossibleValues(entryPool[temp
						.getObNum()].possibleValues);
				entryPool[temp.getObNum()].setCountOfPossible(entryPool[temp
						.getObNum()].getPossibleValues().size());
				if (entryPool[temp.getObNum()].possibleValues.size() < 1)
					return false;

			}

		}

		return true;
	}

}
