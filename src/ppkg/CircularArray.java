package ppkg;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a java implementation of a CircularArray data structure. Space in the
 * array is efficiently used via tracking the logical head, tail, and next
 * available indices. Gaps are avoided with the indices tracking. Any data type
 * can be used.
 * 
 * @author henryh
 *
 * @param <K>
 */
public class CircularArray<K> {

	private List<K> list;
	private int headIndex;
	private int tailIndex;
	private int nextAvailableIndex;
	private int numberOfElements;
	private int maxSize;
	final private int RESIZE_FACTOR;

	/**
	 * This is the default constructor for a Circular Array.
	 * 
	 * @param size         sets the initial capacity.
	 * @param resizeFactor sets how resizing the capacity will grow by.
	 */
	public CircularArray(int size, int resizeFactor) {
		list = new ArrayList<K>(size);
		maxSize = size;
		headIndex = 0;
		numberOfElements = 0;
		nextAvailableIndex = 0;
		tailIndex = size - 1;
		RESIZE_FACTOR = resizeFactor;

		// Initial population of the empty list.
		for (int i = 0; i < maxSize; i++) {
			list.add(null);
		}
	}

	/**
	 * This is called when the list is filled at capacity while a new element is
	 * being added to the list.
	 */
	private ArrayList<K> resizeList(List<K> originalList) {
		// fillCount tracks how many new empty spaces need to be filled with null.
		int fillCount = maxSize - 1;
		maxSize *= RESIZE_FACTOR;
		ArrayList<K> resizedList = new ArrayList<K>(maxSize);
		int i = headIndex;

		// Copy existing values over starting at headIndex to tailIndex. Wrap around if
		// needed.
		while (i != tailIndex) {
			resizedList.add(originalList.get(i));
			if (i < originalList.size() - 1) {
				++i;
			} else {
				i = 0;
			}
		}
		// Add the tail value.
		resizedList.add(originalList.get(i));

		// Fill the expanded array with dummy null values.
		while (fillCount < maxSize - 1) {
			resizedList.add(null);
			++fillCount;
		}
		return resizedList;
	}

	public boolean isEmpty() {
		return (numberOfElements == 0);
	}

	// A helper method to determine if the adjacent index to the right is null.
	private boolean isNextEmpty(int index) {
		int nextIndex = index + 1;
		if (nextIndex > list.size() - 1) {
			index = 0;
		}

		return list.get(nextIndex) == (null);
	}

	public boolean remove(int index) {
		if (index > maxSize - 1 || index < 0) {
			return false;
		}

		int logicalIndex = getLogicalIndex(index);

		if (list.get(logicalIndex) == null) {
			return false;
		}

		if (logicalIndex == headIndex) {
			// Case 1: Delete Head; shift head +1, may reach out bounds.
			// Set the tailIndex to be the previous headIndex.
			list.set(logicalIndex, null);
			tailIndex = headIndex;
			if (headIndex + 1 > list.size() - 1) {
				headIndex = 0;
			} else {
				++headIndex;
			}
		} else if (logicalIndex == tailIndex) {
			// Case 2: Deleting tail, simply remove element
			list.set(logicalIndex, null);
		} else {
			// Case 3: Deleting non-tail element adjacent to null on the right and move next
			// index.
			if (isNextEmpty(logicalIndex)) {
				list.set(logicalIndex, null);
				nextAvailableIndex = logicalIndex;
			} else {
				// Case 4: This is a non-tail middle portion.
				shiftElements(logicalIndex, nextAvailableIndex);

				// Adjust the next index to the left unless it is on the tail.
				if (nextAvailableIndex != tailIndex) {
					--nextAvailableIndex;
				}
				// Check for out of bounds on the left and adjust.
				if (nextAvailableIndex < 0) {
					nextAvailableIndex = list.size() - 1;
				}
			}
		}
		--numberOfElements;
		return true;
	}

	// Used for deletion of an element that is adjacent to a non-null element.
	// All elements from currentIndex shift over 1 to the left
	private void shiftElements(int startIndex, int endIndex) {
		int currentIndex = startIndex;
		Object nextObj = null;
		do {
			if (currentIndex == endIndex) {
				// Check if the end is reached, set to null.
				nextObj = null;
			} else if (currentIndex + 1 == maxSize) {
				// Wrap around when hitting bounds
				nextObj = list.get(0);
			} else {
				// Get the adjacent element to the right.
				nextObj = list.get(currentIndex + 1);
			}

			list.set(currentIndex, (K) nextObj);

			++currentIndex;
			// Wrap around index.
			if (currentIndex > maxSize - 1) {
				currentIndex = 0;
			}
		} while (nextObj != null);
	}

	// Adding an element first checks if list needs resizing, then adds and moves
	// next index.
	public void add(K object) {
		if (numberOfElements == maxSize) {
			list = resizeList(list);
			headIndex = 0;
			tailIndex = maxSize - 1;
			nextAvailableIndex = numberOfElements;
		}
		// Add the element into the next spot.
		list.set(nextAvailableIndex, object);

		// Move the nextAvailableIndex if it has not reached tailIndex, otherwise leave
		// it at tail.
		++numberOfElements;
		if (numberOfElements != maxSize) {
			++nextAvailableIndex;
			if (nextAvailableIndex > maxSize - 1) {
				nextAvailableIndex = 0;
			}
		}
	}

	// Return the number of non-null elements in list.
	public int getSize() {
		return numberOfElements;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public K getElement(int index) {
		int logicalIndex = getLogicalIndex(index);
		return list.get(logicalIndex);
	}

	// This is a helper method to get the offset of logical index if exceeding
	// bounds.
	private int getLogicalIndex(int index) {
		int logicalIndex = index + headIndex;
		if (logicalIndex > list.size() - 1) {
			logicalIndex -= list.size();
		}
		return logicalIndex;
	}

	// Returns a string to show where tail, head, and next indices are.
	public String toStringState() {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			String value = " ";
			if (i == headIndex) {
				value = "H";
			}
			if (i == tailIndex) {
				value = value.replace(" ", "") + "T";
			}
			if (i == nextAvailableIndex) {
				value = value.replace(" ", "") + "N";
			}
			str += "[" + value + "]";
		}
		return str;
	}

	public void clear() {
		headIndex = 0;
		tailIndex = maxSize - 1;
		numberOfElements = 0;
		nextAvailableIndex = 0;
		for (int i = 0; i < maxSize; i++) {
			list.set(i, null);
		}
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			String value = " ";
			if (list.get(i) != null) {
				value = list.get(i).toString();
			}
			str += "[" + value + "]";
		}
		return str;
	}
}
