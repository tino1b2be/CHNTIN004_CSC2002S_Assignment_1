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
		System.out.println("Loading the input files...");
		try {
			dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/inp1.txt");
			//dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/test.txt");
			dataSet2 = FileUtil.loadFile("SampleDataForAssignment1/inp2.txt");
			dataSet3 = FileUtil.loadFile("SampleDataForAssignment1/inp3.txt");
			dataSet4 = FileUtil.loadFile("SampleDataForAssignment1/inp4.txt");
			dataSetP = FileUtil.loadFile("SampleDataForAssignment1/CustomSet.txt"); // Personal Data set of 2 million entries
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scanner sc = new Scanner(System.in);
		ForkJoinPool fjPool = new ForkJoinPool();
		
		//Testing different inputs for defaults filter size
		do{
			System.out.println("Select input to use:\n"
					+ "1: input1\n"
					+ "2: input2\n"
					+ "3: input3\n"
					+ "4: input4\n"
					+ "5: Test Filter sizes (3-21)\n"
					+ "6: Test Sequential cut-offs\n"
					+ "7: Test Warm up times\n"
					+ "8: Personal Input Size\n"
					+ "9: Universal Speed Up Test (Best filter and cut off)\n"
					+ "10: Universal Speed Up Test (Worst filter and cut off)\n"
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
			else if (input.endsWith("6")){
				try {
					Tests tests = new Tests(dataSetP);
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
				System.out.print("Please enter the input size (between 10000 and 1999856): ");
				do{
					int input2 = Integer.parseInt(sc.nextLine());
					
					if(input2 < 10000 || input2 > 2000000){
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
				break;
			}
			else if (input.equals("10")){			// Universal Test. Test speed ups for different data sizes (worst performance)
				Tests newCustom = new Tests(dataSetP);
				try {
					newCustom.runUniversalSpeedUpTestWC();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			else if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4")){
	
				long sqtime,sqtime2, ptime, ptime2;
				
				switch(input){
				case("1"):
					Tests test = new Tests(dataSet1);
					try {
						test.runSpeedUpTest();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
	
				case("2"):
					Tests test2 = new Tests(dataSet2);
					try {
						test2.runSpeedUpTest();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;	
	
				case("3"):
					Tests test3 = new Tests(dataSet3);
					try {
						test3.runSpeedUpTest();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
		
				case("4"):
					Tests test4 = new Tests(dataSet4);
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
			else{
				System.out.println("\nTry AGAIN!");
				continue;
			}
		}while(true);
	}
}