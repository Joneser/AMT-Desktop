package test.utilities;

import static org.junit.Assert.*;

import org.junit.Test;

import utilities.MathUtility;

public class MathUtilityTest {

	@Test
	public void testGetAbs() {
		float[] floatArray = new float[]{-1, -2, -3};
		floatArray = MathUtility.getAbs(floatArray);
		assertArrayEquals(new float[]{1, 2, 3}, floatArray, 0.0f);
	}
	
	@Test
	public void removeITest() {
		float[] floatArray = new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		floatArray = MathUtility.removeI(floatArray);
		assertArrayEquals(new float[]{1, 3, 5, 7, 9}, floatArray, 0.0f);
	}
	
	@Test
	public void normaliseTest() {
		float[] input = new float[]{1, 2, 3, 2, 1, 1};
		assertArrayEquals(new float[]{0.1f, 0.2f, 0.3f, 0.2f, 0.1f, 0.1f}, MathUtility.normalise(input), 0.0f);
	}
	
	@Test
	public void getConjTest() {
		float[] input = new float[]{4, 2, 3, 2, 5, 6, 2, 3, 9, 1};
		assertArrayEquals(new float[]{20, 13, 61, 13, 82}, MathUtility.getConj(input), 0.0f);
	}
	
	@Test
	public void getMaxTest() {
		float[] array = new float[]{1, 21, 13, 46, 3, 1, 2, 6, 73, 19};
		assertTrue(73 == MathUtility.getMax(array));
	}
	
	@Test
	public void getMaxIndexTest() {
		float[] array = new float[]{1, 21, 13, 46, 3, 1, 2, 6, 73, 19};
		assertTrue(8 == MathUtility.getMaxIndex(array));
	}
	
	@Test
	public void getMinTest() {
		float[] array = new float[]{1, 21, 13, 46, 3, 1, 2, 6, 73, 19};
		assertTrue(1 == MathUtility.getMin(array));
	}
	
	@Test
	public void dotMultiplyTest() {
		float[] inputX = new float[]{4, 2, 3};
		float[] inputY = new float[]{2, 2, 1};
		assertArrayEquals(new float[]{8, 4, 3}, MathUtility.dotMultiply(inputX, inputY), 0.0f);
	}
	
	@Test
	public void getMatchValueTest() {
		float[] input = new float[]{1, 2, 3};
		float[] library = new float[]{3, 2, 1};
		assertTrue(10 == MathUtility.getMatchValue(input, library));
	}
	
	@Test
	public void dotDivideTest() {
		float[] inputX = new float[]{4, 2, 3};
		float[] inputY = new float[]{2, 2, 1};
		assertArrayEquals(new float[]{2, 1, 3}, MathUtility.dotDivide(inputX, inputY), 0.0f);
	}
	
	@Test
	public void getTotalTest() {
		float[] floatArray = new float[]{1, 2, 3, 4, 5};
		assertTrue(15 == MathUtility.getTotal(floatArray));
	}

}
