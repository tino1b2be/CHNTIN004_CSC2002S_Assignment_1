import java.io.IOException;
import java.util.Scanner;
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
	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) {
		
		//Load the data set
		double[] dataSet1 = null;
		double[] dataSet2 = null;
		double[] dataSet3 = null;
		double[] dataSet4 = null;
		try {
			dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/inp1.txt");
			//dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/test.txt");
			dataSet2 = FileUtil.loadFile("SampleDataForAssignment1/inp2.txt");
			dataSet3 = FileUtil.loadFile("SampleDataForAssignment1/inp3.txt");
			dataSet4 = FileUtil.loadFile("SampleDataForAssignment1/inp4.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scanner sc = new Scanner(System.in);
		ForkJoinPool fjPool = new ForkJoinPool();
		
		do{
			System.out.println("Select input to use:\n"
					+ "1: input1\n"
					+ "2: input2\n"
					+ "3: input3\n"
					+ "4: input4\n"
					+ "5: Quit");
			
			String input = sc.nextLine();
			
			if (input.equals("5")){
				System.exit(0);
			}
			else if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4")){
	
				long sqtime,sqtime2, ptime, ptime2;
				switch(input){
				case("1"):
					Filter set01 = new Filter(dataSet1);		//Create a filter object to perform the filtering
					double[] par01, seq01;						//create the arrays to store the filtered data
		
					sqtime = System.nanoTime();
					seq01 = set01.sequential();					//process the data sequentially
					sqtime2 = System.nanoTime();
		
					ptime = System.nanoTime();
					par01 = fjPool.invoke(set01);				//process the data using the parallel method
					ptime2 = System.nanoTime();
		
					System.out.println("Sequential input01 time (ns):" + (sqtime2 - sqtime));	//print the time taken to process sequentially
					System.out.println("Parallel input01 time (ns):" + (ptime2 - ptime));		//print the time taken to process in parallel
					if((ptime2 - ptime)!=0){
						System.out.println(String.format("Speed up is: %.5f\n ",((sqtime2 - sqtime)*1.0)/(1.0*(ptime2 - ptime))));	//divide the seq time by the par time to get the speed up
					}
					else
						System.out.println("too fast");
					break;
	
				case("2"):
					Filter set02 = new Filter(dataSet2);
					double[] par02, seq02;
		
					sqtime = System.nanoTime();
					seq02 = set02.sequential();
					sqtime2 = System.nanoTime();
		
					ptime = System.nanoTime();
					par02 = fjPool.invoke(set02);
					ptime2 = System.nanoTime();
		
					System.out.println("Sequential input02 time (ns):" + (sqtime2 - sqtime));
					System.out.println("Parallel input02 time (ns):" + (ptime2 - ptime));
					if((ptime2 - ptime)!=0){
						System.out.println(String.format("Speed up is: %.5f\n ",((sqtime2 - sqtime)*1.0)/(1.0*(ptime2 - ptime))));	//divide the seq time by the par time to get the speed up
					}
					break;	
	
				case("3"):
					Filter set03 = new Filter(dataSet3);
					double[] par03, seq03;
					sqtime = System.nanoTime();
					seq03 = set03.sequential();
					sqtime2 = System.nanoTime();
		
					ptime = System.nanoTime();
					par03 = fjPool.invoke(set03);
					ptime2 = System.nanoTime();
		
					System.out.println("Sequential input03 time (ns):" + (sqtime2 - sqtime));
					System.out.println("Parallel input03 time (ns):" + (ptime2 - ptime));
					if((ptime2 - ptime)!=0){
						System.out.println(String.format("Speed up is: %.5f\n ",((sqtime2 - sqtime)*1.0)/(1.0*(ptime2 - ptime))));	//divide the seq time by the par time to get the speed up
					}
					break;
		
					case("4"):
						Filter set04 = new Filter(dataSet4);
					double[] par04, seq04;
					sqtime = System.nanoTime();
					seq04 = set04.sequential();
					sqtime2 = System.nanoTime();
		
					ptime = System.nanoTime();
					par04 = fjPool.invoke(set04);
					ptime2 = System.nanoTime();
		
					System.out.println(String.format("Sequential input04 time (ns): %.0f", (double)(sqtime2 - sqtime)));
					System.out.println(String.format("Parallel input04 time (ns): %.0f", (double)(ptime2 - ptime)));
					if((ptime2 - ptime)!=0){
						System.out.println(String.format("Speed up is: %.5f\n ",((sqtime2 - sqtime)*1.0)/(1.0*(ptime2 - ptime))));	//divide the seq time by the par time to get the speed up
					}
					break;
				
				default:
					break;
	
				}
			}	//end of if statement
			else{
				System.out.println("\nTry AGAIN!");
				continue;
			}
		}while(true);
	}
}
