import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Class for file handling
 * 
 * @author Tinotenda Chemvura
 *
 */
public class FileUtil {

	/**
	 * Method to open a text file and load the data set
	 * @param fileName - Name of the file to be loaded
	 * @return	An array of the data set
	 * @throws NumberFormatException incorrect data in the file
	 * @throws IOException When file cannot be found
	 */
	public static double[] loadFile(String fileName) throws NumberFormatException, IOException{

		// Open the file
		FileInputStream fstream = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		int dataSize = Integer.parseInt(br.readLine());							// First line of file is number of lines
		double[] dataSet = new double[dataSize];								// Array to keep the data
		String line;
		String[] lineSplit;
		int lineNumber = 0;
		
		//Read File Line By Line
		while ((line = br.readLine()) != null)   {
			lineSplit = line.split(" ");
			if (lineSplit.length == 2){
				dataSet[lineNumber] = Double.parseDouble(lineSplit[1]);
				lineNumber++;
			}
		}

		//Close the input stream
		br.close();
		return dataSet;
	}
	
	/**
	 * Method to export data to a .csv file
	 * 
	 * @param newData data to be written to file
	 * @throws IOException
	 */
	public static void writeToFile(double[] newData, String fileName) throws IOException{
		
		PrintWriter pw = new PrintWriter(new FileWriter(fileName));
		 
		for (int i = 0; i < newData.length; i++) {
			pw.write(newData[i] + "\n");
		}
	 
		pw.close();
	}

	public static void writeToFile(ArrayList<Double> seqDataOut, String fileName) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(fileName));
		 
		for (int i = 0; i < seqDataOut.size(); i++) {
			pw.write(seqDataOut.get(i)+ "\n");
		}
		pw.close();
	}
}