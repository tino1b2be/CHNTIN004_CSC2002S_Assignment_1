import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

@SuppressWarnings("serial")
public class Filter extends RecursiveTask<double[]>{
	
	public static int SEQUENTIAL_THRESHOLD = 10000;
	public static int FILTER_SIZE = 21;
	
	private double[] dataSet;
	private int hi;
	private int low;
	
	/**
	 * Constructor for the Filter Object
	 * @param dataSet
	 */
	public Filter(double[] dataSet) {
		this.dataSet = dataSet;
		hi = dataSet.length;
	}

	/**
	 * Copy constructor for the Filter Class
	 * @return A copy of the filter object
	 */
	public Filter(Filter filter){
		this.dataSet = filter.getDataSet();
	}

	/**
	 * Constructor for the parallel program
	 * 
	 * @param dataSet2 - Data Set to be processed
	 * @param low - The lower index for the slicing
	 * @param hi - The upper index for processing
	 */
	public Filter(double[] dataSet2, int low, int hi) {
		this.dataSet = dataSet2;
		this.low = low;
		this.hi = hi;
	}

	
	/**
	 * Getter method for the dataSet
	 * @return Data Set
	 */
	private double[] getDataSet() {
		return this.dataSet;
	}

	/**
	 * Method to filter the data
	 * @param size - Filter size
	 * @param index - the index in the array to be filtered
	 * @return
	 */
	public double filter(int index){
		
		//TODO Filter must work properly when bigger than 3
		
		int finish = index + ((Filter.FILTER_SIZE-1)/2);
		int start = index - ((Filter.FILTER_SIZE-1)/2);
		// slice off a chunk of the dataSet same size as filter size
		double[] filterBlock = sliceDataSet(start,finish);
		return median(filterBlock);
	}

	/**
	 * Method that returns the median of the elements of an array
	 * 
	 * @param filterBlock - Array with elements to be processed
	 * @return The median value of the given array
	 */
	private static double median(double[] filterBlock) {
		Arrays.sort(filterBlock);
		return filterBlock[filterBlock.length/2];
		//TODO Test this method in JUnit
	}

	/**
	 * Method to slice from the given start and finish indexes
	 * 
	 * @param start - index to start slicing
	 * @param finish - index to stop slicing
	 * @return
	 */
	private double[] sliceDataSet(int start, int finish) {
		
		double[] slice = new double[finish-start+1];
		int sliceIndex = 0;
		
		for (int i = start; i < slice.length + start; i++){
			slice[sliceIndex] = dataSet[i];
			sliceIndex++;
		}
		return slice;
		//TODO Test this method in JUnit
	}

	/**
	 * Method to sequentially filter the dataset (first and last values not filtered
	 * 
	 * @return
	 */
	public double[] sequential() {

		double[] newDataSet = new double[dataSet.length];

		// first and last elements aren't changed
		newDataSet[0] = dataSet[0];
		
		for (int j = 0; j < (Filter.FILTER_SIZE - 1)/2; j++){
			newDataSet[j] = dataSet[j];
		}
		
		for (int j = dataSet.length - (Filter.FILTER_SIZE - 1)/2; j < dataSet.length; j++){
			newDataSet[j] = dataSet[j];
		}
		
		//filter the rest of the data set
		for (int i = 0 + (Filter.FILTER_SIZE - 1)/2; i < dataSet.length - (Filter.FILTER_SIZE - 1)/2; i++){
			newDataSet[i] = filter(i);
		}

		return newDataSet;
	}

	//++++++++++_________SEQUENTIAL PROCESSING__________++++++++++++++++

	@Override
	protected double[] compute() {
		
		// If the array is small enough to process, do it sequentially
		if (hi - low <= SEQUENTIAL_THRESHOLD){
			
			double[] newDataSet = new double[hi-low];
			int count = 0;
			if (low == 0){									
				
				// Start filtering where the filter can "fit"
				for (int i = 0; i <(Filter.FILTER_SIZE - 1)/2; i++){
					newDataSet[i] = dataSet[i];
					count++;
				}
				
				//Filter the rest of the array
				for (int i = (Filter.FILTER_SIZE - 1)/2; i < hi; i++){
					newDataSet[count] = filter(i);
					count++;
				}
			}
			
			else if (hi == dataSet.length){									
				
				// Stop before end of the list (filter size) 
				for (int i = low; i < dataSet.length - (Filter.FILTER_SIZE - 1)/2; i++){
					newDataSet[count] = filter(i);
					count++;
				}
				
				// copy the end of the data set
				for (int i = dataSet.length - (Filter.FILTER_SIZE - 1)/2; i < dataSet.length; i++ ){
					newDataSet[count] = dataSet[i];
					count++;
				}
			}
			else {															// middle of the data set, process everything
				
				for (int i = low; i < hi; i++){
					newDataSet[count] = filter(i);
					count++;
				}
			}

			return newDataSet;
		}
		
		// If the array is too big to process, split it again
		else {
			
			Filter left = new Filter(dataSet,low,(hi+low)/2);
			Filter right = new Filter(dataSet,(hi+low)/2,hi);
			left.fork();
			double[] rightArray = right.compute();
			double[] leftArray = left.join();
			
			//merge the two arrays
			double[] filtered = merge(leftArray, rightArray);

			return filtered;
		}
	}

	/**
	 * Method that merges two arrays
	 * 
	 * @param rightArray
	 * @param leftArray
	 * 
	 * @return
	 */
	private static double[] merge(double[] leftArray, double[] rightArray){
		
        double[] result = new double[rightArray.length + leftArray.length];
        
        System.arraycopy(leftArray, 0, result, 0, leftArray.length);
        System.arraycopy(rightArray, 0, result, leftArray.length, rightArray.length);
        
        return result;

	}
	
	public static void setCuttOff(int cutOff){
		SEQUENTIAL_THRESHOLD = cutOff;
	}
	public static void setFilterSize(int size){
		FILTER_SIZE = size;
	}
}
