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
	 * Method to run tests to determine the best filter size
	 * @throws IOException
	 */
	public void runFilterTests() throws IOException{
		for (int i = 3; i < 22; i+=2){
			System.out.println("Filter being tested: " + i);
			Filter.setFilterSize(i);
			long[] seqTimes = new long[20];				// arrays to keep the times for each run to be averaged later 
			long[] parTimes = new long[20];				
			
			long sqtime,sqtime2, ptime, ptime2;
			//run the process 10 times to warm up, then 10 times for recording
			
			for (int j = 0; j< 10; j++){
				Filter set04 = new Filter(dataSet);
				set04.sequential();
				fjPool.invoke(set04);
			}
			System.out.println("done with warm up of " + i);
			
			for (int k = 0; k < 20; k++){
				if (k==5)
					System.out.println("half way through the filter:" + i);
				Filter set04 = new Filter(dataSet);
				double[] par04, seq04;
				sqtime = System.nanoTime();
				seq04 = set04.sequential();
				sqtime2 = System.nanoTime();

				ptime = System.nanoTime();
				par04 = fjPool.invoke(set04);
				ptime2 = System.nanoTime();

				seqTimes[k] = sqtime2-sqtime;
				parTimes[k] = ptime2-ptime;
			}
			
			System.out.println("done running filter: " + i + "\n");
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
				for (int i = 100; i < 15100; i+=200){
					Filter.setCuttOff(i);						//change the static cut off number 
					System.out.println("Cut off being tested: " + i); 
					long[] parTimes = new long[20];				// array to keep the times for each run to be averaged later
					long ptime, ptime2;							// variables for the timer

																// warm up loop...does 10 runs for warm up
					for (int j = 0; j< 10; j++){
						Filter set04 = new Filter(dataSet);
						fjPool.invoke(set04);
					}
					System.out.println("Done with warm up");
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
		}
		FileUtil.writeToFile(seqDataOut, "Warm up times for sequential runs.csv");
		FileUtil.writeToFile(parDataOut, "Warm up times for parallel runs.csv");
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
}
