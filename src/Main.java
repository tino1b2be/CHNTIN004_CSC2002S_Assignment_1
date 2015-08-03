import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

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
		double[] dataSet1 = null;
		double[] dataSet2 = null;
		double[] dataSet3 = null;
		double[] dataSet4 = null;
		try {
			//dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/inp1.txt");
			dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/test.txt");
			dataSet2 = FileUtil.loadFile("SampleDataForAssignment1/inp2.txt");
			dataSet3 = FileUtil.loadFile("SampleDataForAssignment1/inp3.txt");
			dataSet4 = FileUtil.loadFile("SampleDataForAssignment1/inp4.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		long time, time2;
		
		//create the filter objects
		double[] seq01, seq04;//, seq02, seq03, ;
		
		Filter set01 = new Filter(dataSet1);
	//	Filter set02 = new Filter(dataSet2);
//		Filter set03 = new Filter(dataSet3);
		Filter set04 = new Filter(dataSet4);

		//process is sequentially
		
		time = System.currentTimeMillis();
		seq01 = set01.sequential();
		time2 = System.currentTimeMillis();
		System.out.println("Seq. input01 time (ms):" + (time2 - time));
/*
		time = System.currentTimeMillis();
		seq02 = set02.sequential();
		time2 = System.currentTimeMillis();
		System.out.println("Seq. input02 time (ms):" + (time2 - time));
		
		time = System.currentTimeMillis();
		seq03 = set03.sequential();
		time2 = System.currentTimeMillis();
		System.out.println("Seq. input03 time (ms):" + (time2 - time));
		*/
		time = System.currentTimeMillis();
		seq04 = set04.sequential();
		time2 = System.currentTimeMillis();
		System.out.println("Seq. input04 time (ms):" + (time2 - time));
		
		
		// SEQUENTIAL PROCESSING
		ForkJoinPool fjPool = new ForkJoinPool();
		Filter set011 = new Filter(dataSet1);
		//Filter set022 = new Filter(dataSet2);
		//Filter set033 = new Filter(dataSet3);
		Filter set044 = new Filter(dataSet4);
		
		double[] par01;
		
		time = System.currentTimeMillis();
		par01 = fjPool.invoke(set011);
		time2 = System.currentTimeMillis();
		System.out.println("Par. input04 time (ms):" + (time2 - time));
		
		time = System.currentTimeMillis();
		par01 = fjPool.invoke(set011);
		time2 = System.currentTimeMillis();
		System.out.println("Par. input04 time (ms):" + (time2 - time));
		
		
		/*
		// write to file
		try {
			FileUtil.writeToFile(newData, "out.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

}
