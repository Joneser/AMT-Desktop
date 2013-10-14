package test.core;

import static org.junit.Assert.*;

import org.junit.Test;

import core.AbcManager;

public class AbcManagerTest {

	@Test
	public void testPopulateTable() {
		assertEquals(AbcManager.notes.size(), 0);
		AbcManager.populateTable();
		assertEquals(AbcManager.notes.size(), 25);
	}

}
