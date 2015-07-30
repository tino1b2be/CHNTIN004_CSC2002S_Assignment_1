import java.io.IOException;

/**
 * Main class to run the tests
 * 
 * @author Tinotenda Chemvura
 *
 */

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Load the data set
		double[] dataSet = null;
		try {
			dataSet = FileUtil.loadFile("SampleDataForAssignment1/inp1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//process is sequentially
		double[] newData;
		Filter filter01 = new Filter(dataSet);
		newData = filter01.Sequential(3);
		
		// write to file
		try {
			FileUtil.writeToFile(newData, "out.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
