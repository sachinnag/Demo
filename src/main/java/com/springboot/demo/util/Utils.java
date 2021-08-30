package com.springboot.demo.util;

import java.util.Collection;
import java.util.Objects;

public class Utils {
	
	/** This utility method checks if the collection object is not null and not empty and returns true, else returns false.
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Collection c) {
		if(c != null && !c.isEmpty())
			return true;
		return false;
	}
	
	/** This utility method check if the object is not null and returns true, else returns false.
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		if(obj != null)
			return true;
		return false;
	}
	
	/** This utility method checks if the index is within the bound of the given size and returns true, else throws error and returns false.
	 * @param index
	 * @param size
	 * @return
	 */
	public static boolean ifIndexExists(int index, int size) {
		boolean isExists = true;
		try {
			Objects.checkIndex(index, size);
		} catch(IndexOutOfBoundsException e) {
			isExists = false;
		}
		return isExists;
	}
}
