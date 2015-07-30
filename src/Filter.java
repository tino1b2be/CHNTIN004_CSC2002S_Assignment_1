
public class Filter {

	private double[] dataSet;
	public Filter(double[] dataSet) {
		this.dataSet = dataSet;
	}
	
	/**
	 * Method to filter the data
	 * @param size - Filter size
	 * @param index - the index in the array to be filtered
	 * @return
	 */
	public double filter(int size, int index){
		
		int finish = index + ((size-1)/2);
		int start = index - ((size-1)/2);
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
	private double median(double[] filterBlock) {
		//TODO implement method to return the median value
		return 0;
	}

	/**
	 * Method to slice from the given start and finish indexes
	 * 
	 * @param start - index to start slicing
	 * @param finish - index to stop slicing
	 * @return
	 */
	private double[] sliceDataSet(int start, int finish) {
		// TODO implement method to slice the array
		return null;
	}

	/**
	 * Method to sequentially filter the dataset (first and last values not filtered
	 * 
	 * @param filterSize
	 * @return
	 */
	public double[] Sequential(int filterSize) {

		double[] newDataSet = new double[dataSet.length];

		for (int i = 1; i < dataSet.length - 1; i++){
			newDataSet[i] = filter(filterSize,i);
		}

		return newDataSet;
	}
}
