import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {

	@Override
	public int compare(Entry n1, Entry n2) {
		if (n1.countOfPossible > n2.countOfPossible) {
			return 1;
		} else if (n1.countOfPossible < n2.countOfPossible)
			return -1;
		else {
			// return Most constraining variable
			return (countOfConstrainings(n1, n2));
		}

	}

	/*
	 * below function calculate the mostConstrainingVariable depending upon row
	 * and column
	 */
	private int countOfConstrainings(Entry n1, Entry n2) {
		// TODO Auto-generated method stub
		// get number of empty entries in row and columns.

		int count1 = 0, count2 = 0, count12 = 0, count22 = 0;
		// below loop is to calculate the most Constraining variable
		// depending on row and column.
		for (int k = 0; k < SodokuSolver.sodokuLevel; k++) {

			if (SodokuSolver.grid[n1.row][k] == 0 && !(n1.column == k))
				count1 += 1;
			if (SodokuSolver.grid[k][n1.column] == 0 && !(n1.row == k))
				count1 += 1;

			if (SodokuSolver.grid[n2.row][k] == 0 && !(n2.column == k))
				count2 += 1;
			if (SodokuSolver.grid[k][n2.column] == 0 && !(n2.row == k))
				count2 += 1;

		}
		count12 = countOfBoxConstrainings(n1);
		count22 = countOfBoxConstrainings(n2);

		if ((count1 + count12) > (count2 + count22))
			return -1;
		else if ((count1 + count12) > (count2 + count22))
			return 1;

		return 0;
	}

	private int countOfBoxConstrainings(Entry n) {
		// TODO Auto-generated method stub
		int count = 0;
		int boxSize = (int) (SodokuSolver.sodokuLevel / Math
				.sqrt(SodokuSolver.sodokuLevel));
		int rowStart = n.row - n.row % boxSize;
		int colStart = n.column - n.column % boxSize;
		for (int i = 0; i < boxSize; i++)
			for (int j = 0; j < boxSize; j++) {
				if ((SodokuSolver.grid[rowStart + i][colStart + j] == 0)
						&& !((rowStart + i) == n.row || (colStart + j) == n.column))
					count++;
			}
		return count;
	}

}
