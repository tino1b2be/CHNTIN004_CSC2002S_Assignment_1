import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

public class MainTest {

	@Test
	/**
	 * Check if both methods produce same results
	 */
	public void newDataSetEqualityTest() {
		double[] dataSet1 = null;
		try {
			dataSet1 = FileUtil.loadFile("SampleDataForAssignment1/inp1.txt");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Filter set01 = new Filter(dataSet1);
		double[] seq01 = set01.sequential();
		
		ForkJoinPool fjPool = new ForkJoinPool();
		Filter set011 = new Filter(dataSet1);
		double[] par01 = fjPool.invoke(set011);
		
		boolean flag = true;
		if (seq01.length != par01.length)
	    	flag = false;
	    else {
	    	
	    	for (int i = 0; i < par01.length; i++){
	    		if (par01[i] != seq01[i]){
	    			flag = false;
	    			break;
	    		}
	    	}
	    }
		assertEquals(true,flag);
	
	}

	//@Test
	/**
	 * Method to test the filtering
	 */
	public void filterTest() {
		//fail("Not yet implemented");
	}
}
