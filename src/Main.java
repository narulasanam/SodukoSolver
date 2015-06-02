import java.io.IOException;

public class Main {

	public static void main(String arg[]) throws IOException {
		SodokuSolver ss = new SodokuSolver();
		try {
			// you just need to put the path of teh file on your local system
			// and everything will work fine
			ss.readInput("E:\\UW first term\\AI\\assignment 3\\problems\\problems\\21\\8.sd");
			// ss.readInput("Hello.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ss.preProcessing();
		if (ss.solveSodoku(ss.grid)) {
			ss.printSolution();
			System.out.println("value is : " + ss.count_of_assignments);
			ss.count_of_assignments = 0;

		} else {
			System.out.println("the solution does not exist");

		}
	}

}