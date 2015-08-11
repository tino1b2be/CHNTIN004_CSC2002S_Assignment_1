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
		double[] dataSetP = null;
		//double[] dataSetP2 = null;
		System.out.println("Loading the input files...");
		try {
			dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/inp1.txt");
			//dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/test.txt");
			dataSet2 = FileUtil.loadFile("SampleDataForAssignment1/inp2.txt");
			dataSet3 = FileUtil.loadFile("SampleDataForAssignment1/inp3.txt");
			dataSet4 = FileUtil.loadFile("SampleDataForAssignment1/inp4.txt");
			dataSetP = FileUtil.loadFile("SampleDataForAssignment1/CustomSet.txt"); // Personal Data set of 2 million entries
			//dataSetP2 = FileUtil.loadFile("SampleDataForAssignment1/CustomSet2.txt");// file with 4,00,,000 (too big got github)
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scanner sc = new Scanner(System.in);
		ForkJoinPool fjPool = new ForkJoinPool();
		
		//Testing different inputs for defaults filter size
		do{
			System.out.println("Select input to use:\n"
					+ "Outputs are saved in the cu"
					+ "1: input1\n"
					+ "2: input2\n"
					+ "3: input3\n"
					+ "4: input4\n"
					+ "5: Test Filter sizes (3-21)\n"
					+ "6: Test Sequential cut-offs\n"
					+ "7: Test Warm up times\n"
					+ "8: Personal Input Size\n"
					+ "9:  Universal Speed Up Test (Best filter (3) and cut off (1,500))\n"
					+ "10: Universal Speed Up Test (Worst filter (21) and cut off (1,500))\n"
					+ "11: Change filter size (current one is " + Filter.FILTER_SIZE + ")\n"
					+ "12: Change the sequential threshold (current one is " + Filter.SEQUENTIAL_THRESHOLD + ")\n"
					+ "13: Universal Speed Up Test for MEAN Filter (Best filter (3) and cut off (1,500))\n"
					+ "14: Universal Speed Up Test for MEAN Filter (Best filter (21) and cut off(100))\n"
					+ "q: Quit");
			
			String input = sc.nextLine();
			
			if (input.equals("q")){
				System.exit(0);
			}
			else if (input.equals("5")){
				try {
					Tests tests = new Tests(dataSetP);
					long ttime1 = System.currentTimeMillis();
					tests.runFilterTests();
					System.out.println("Tests lasted " + (System.currentTimeMillis() - ttime1) + "ms");
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
			else if (input.equals("6")){
				try {
					Tests tests = new Tests(dataSet1);
					long ttime1 = System.currentTimeMillis();
					tests.runCutOffTests();
					System.out.println("Tests lasted " + (System.currentTimeMillis() - ttime1) + "ms");
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
			else if (input.equals("7")){
				try {
					Tests tests = new Tests(dataSet4);
					long ttime1 = System.currentTimeMillis();
					tests.runWarmUpTests();
					System.out.println("Tests lasted " + (System.currentTimeMillis() - ttime1) + "ms");
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
			else if (input.equals("8")){
				System.out.print("Please enter the input size (between 15000 and 2,000,000): ");
				do{
					int input2 = Integer.parseInt(sc.nextLine());
					
					if(input2 < 15000 || input2 > 2000000){
						System.out.println("invalid input. try again");
						continue;
					}
					else {
						Tests newCustom = new Tests(dataSetP, input2);
						try {
							newCustom.runSpeedUpTest();
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				}while(true);
			}
			else if (input.equals("9")){			// Universal Test. Test speed ups for different data sizes (best performance)
				Tests newCustom = new Tests(dataSetP);
				try {
					newCustom.runUniversalSpeedUpTestBC();
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
			else if (input.equals("10")){			// Universal Test. Test speed ups for different data sizes (worst performance)
				Tests newCustom = new Tests(dataSetP);
				try {
					newCustom.runUniversalSpeedUpTestWC();
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
			else if (input.equals("11")){
				System.out.println("Please enter desired filter size: ");
				Filter.setFilterSize(sc.nextInt());
				continue;
			}
			else if (input.equals("12")){
				System.out.println("Please enter desired Threshold: ");
				Filter.setCuttOff(sc.nextInt());
				continue;
			}
			else if (input.equals("13")){
				Tests newCustom = new Tests(dataSetP);
				try {
					newCustom.runMeanUniversalSpeedUpTestBC();
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
			else if (input.equals("14")){
				Tests newCustom = new Tests(dataSetP);
				try {
					newCustom.runMeanUniversalSpeedUpTestWC();
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
			else if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4")){
	
				long sqtime,sqtime2, ptime, ptime2;
				
				switch(input){
				case("1"):
					//Filter the data and export
					Filter set01 = new Filter(dataSet1);
					double[] seqOut1 = set01.sequential();
					double[] parOut1 = fjPool.invoke(set01);
					try {
						FileUtil.writeToFile(parOut1, "OutPut Data.csv");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Tests test = new Tests(dataSet1);		// Run the tests on data set 1
					try {
						test.runSpeedUpTest();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
	
				case("2"):
					//Filter the data and export
					Filter set02 = new Filter(dataSet2);
					double[] seqOut2 = set02.sequential();
					double[] parOut2 = fjPool.invoke(set02);
					try {
						FileUtil.writeToFile(parOut2, "OutPut Data.csv");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Tests test2 = new Tests(dataSet2);
					try {
						test2.runSpeedUpTest();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;	
	
				case("3"):
					Tests test3 = new Tests(dataSet3);
					//Filter the data and export
					Filter set03 = new Filter(dataSet3);
					double[] seqOut3 = set03.sequential();
					double[] parOut3 = fjPool.invoke(set03);
					try {
						FileUtil.writeToFile(parOut3, "OutPut Data.csv");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						test3.runSpeedUpTest();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
		
				case("4"):
					Tests test4 = new Tests(dataSet4);
					//Filter the data and export
					Filter set04 = new Filter(dataSet4);
					double[] seqOut4 = set04.sequential();
					double[] parOut4 = fjPool.invoke(set04);
					try {
						FileUtil.writeToFile(parOut4, "OutPut Data.csv");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						test4.runSpeedUpTest();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				
				default:	
					break;
				}
			}	//end of switch
			else{			// if wrong input is entered
				System.out.println("\nTry AGAIN!");
				continue;
			}
		}while(true);
		//end of GUI while loop
	}
}
