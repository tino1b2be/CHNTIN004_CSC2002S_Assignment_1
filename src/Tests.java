import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class Tests {

	ArrayList<Double> seqDataOut = new ArrayList<Double>();
	ArrayList<Double> parDataOut = new ArrayList<Double>();
	ForkJoinPool fjPool = new ForkJoinPool();
	double[] dataSet;
	
	public Tests(double[] dataSet4){
		this.dataSet = dataSet4;
	}
	
	/**
	 * Constructor that carries out Tests with custom data set size
	 * @param dataSetP
	 * @param input2
	 */
	public Tests(double[] dataSetP, int size) {
		this.dataSet = sliceDataSet(size, dataSetP);
	}

	/**
	 * Method to run tests to determine the best filter size
	 * @throws IOException
	 */
	public void runFilterTests() throws IOException{
		warmUp();
		for (int i = 3; i < 22; i+=2){
			
			Filter.setFilterSize(i);
			System.out.println("Filter being tested: " + Filter.FILTER_SIZE);
			long[] seqTimes = new long[20];				// arrays to keep the times for each run to be averaged later 
			long[] parTimes = new long[20];				
			
			long sqtime,sqtime2, ptime, ptime2;
			
			for (int k = 0; k < 20; k++){
				if (k==5)
					System.out.println("half way through the filter:" + Filter.FILTER_SIZE);
				
				Filter set04 = new Filter(dataSet);
				
				sqtime = System.nanoTime();
				set04.sequential();
				sqtime2 = System.nanoTime();

				ptime = System.nanoTime();
				fjPool.invoke(set04);
				ptime2 = System.nanoTime();

				seqTimes[k] = sqtime2-sqtime;
				parTimes[k] = ptime2-ptime;
			}
			
			System.out.println("done running filter: " + Filter.FILTER_SIZE + "\n");
			
			//find the average time for the filter and add the average time for the filter into the dataOut array for both methods
			seqDataOut.add(((avg(seqTimes)*1.0)/1000000000.0));
			parDataOut.add(((avg(parTimes)*1.0)/1000000000.0));
		}
		
		FileUtil.writeToFile(seqDataOut, "filter times for sequential runs.csv");
		FileUtil.writeToFile(parDataOut, "filter times for parallel runs.csv");
		
	}
	
	/**
	 * Method to run tests to determine the best sequential cut off value
	 * @throws IOException
	 */
	public void runCutOffTests() throws IOException{
		//main loop that changes the sequential cut off time
		// warm up loop...does 10 runs for warm up
		warmUp();
		
		for (int i = 100; i < 15100; i+=100){
			Filter.setCuttOff(i);						//change the static cut off number 
			System.out.println("Cut off being tested: " + i); 
			long[] parTimes = new long[20];				// array to keep the times for each run to be averaged later
			long ptime, ptime2;							// variables for the timer


			// loop for recording values to average

			for (int k = 0; k < 20; k++){
				if (k == 5)
					System.out.println("Halfway through the cut off " + i);
				Filter set04 = new Filter(dataSet);
				double[] par04;

				ptime = System.nanoTime();
				par04 = fjPool.invoke(set04);
				ptime2 = System.nanoTime();

				parTimes[k] = ptime2-ptime;
			}

			System.out.println("done running cut off: " + i + "\n");
			//find the average time for the filter and add the average time for the filter into the dataOut array for both methods
			parDataOut.add(((avg(parTimes)*1.0)/1000000000.0));

		}
		///write the data to csv files
		FileUtil.writeToFile(parDataOut, "cut off times for parallel runs.csv");
	}

	/**
	 * Method to determine the most suitable time
	 * @throws IOException 
	 */
	
	public void runWarmUpTests() throws IOException{

		for (int j = 0; j< 15; j++){
			System.out.println("Run number " + j + " starting...");
			Filter set04 = new Filter(dataSet);
			long sqtime,sqtime2, ptime, ptime2;
			double[] seq01, par01;
			
			sqtime = System.nanoTime();
			seq01 = set04.sequential();					//process the data sequentially
			sqtime2 = System.nanoTime();

			ptime = System.nanoTime();
			par01 = fjPool.invoke(set04);				//process the data using the parallel method
			ptime2 = System.nanoTime();
			
			seqDataOut.add((double)(sqtime2-sqtime));
			parDataOut.add((double)(ptime2-ptime));
			System.out.println("Run number " + j + " is finished.\n");
		}
		FileUtil.writeToFile(seqDataOut, "Warm up times for sequential runs.csv");
		FileUtil.writeToFile(parDataOut, "Warm up times for parallel runs.csv");
		System.out.println("Done. CVS files exported\n");
	}
	
	/**
	 * Method to calculate the average of an array
	 * @param times array with the values to be processed
	 * @return average of the array
	 */
	private Long avg(long[] times) {
		long result = 0;
		for (int i = 0; i < times.length; i++){
			result+=times[i];
		}
		return (long) ((result*1.0)/(times.length * 1.0));
	}

	/**
	 * Method to run Speed up test
	 * @throws IOException 
	 */
	public void runSpeedUpTest() throws IOException {
		warmUp();
		long[] sqTimes = new long[50];
		long[] prTimes = new long[50];
		long sqtime,sqtime2, ptime, ptime2;

		for (int i = 0; i < 50; i++){
			Filter set01 = new Filter(dataSet);		//Create a filter object to perform the filtering
			
			sqtime = System.nanoTime();
			set01.sequential();					//process the data sequentially
			sqtime2 = System.nanoTime();

			ptime = System.nanoTime();
			fjPool.invoke(set01);				//process the data using the parallel method
			ptime2 = System.nanoTime();

			seqDataOut.add((double)(sqtime2-sqtime));
			parDataOut.add((double)(ptime2-ptime));
			
			sqTimes[i] = sqtime2-sqtime;		//add times to arrays
			prTimes[i] = ptime2-ptime;
		}
		
		double sqAvg = avg(sqTimes);
		double prAvg = avg(prTimes);
		
		FileUtil.writeToFile(seqDataOut, "seq times.csv");
		FileUtil.writeToFile(parDataOut, "par times.csv");
		
		System.out.println("Sequential input time (ns):" + sqAvg);	//print the time taken to process sequentially
		System.out.println("Parallel input time (ns):" + prAvg);		//print the time taken to process in parallel
		System.out.println(String.format("Speed up is: %.5f\n ",(sqAvg)/(prAvg)));	//divide the seq time by the par time to get the speed up
	}
	
	/**
	 * Method to warm up the program
	 */
	private void warmUp(){
		System.out.println("Warm-up started.");
		
		//Program runs 10 times for warm up
		for (int j = 0; j< 15; j++){
			Filter set04 = new Filter(dataSet);
			set04.sequential();
			fjPool.invoke(set04);
		}
		System.out.println("Warm up Done.");
	}
	
	/**
	 * Method to slice of given size from a given array
	 * @param size
	 * @param oldData
	 * @return
	 */
	private double[] sliceDataSet(int size, double[] oldData) {
		if (oldData.length < size)
			return null;
		double[] slice = new double[size];
		
		for (int i = 0; i < slice.length; i++){
			slice[i] = oldData[i];
		}
		return slice;
	}

	
	/**
	 * Method to run the Universal Speed Up Test
	 */
	public void runUniversalSpeedUpTest() throws IOException{
		
		
	}
}
