package ppkg;

import java.util.ArrayList;
import java.util.List;

public class CircularArray<K> {

	private List<K> list;
	private int headIndex;
	private int tailIndex;
	private int nextAvailableIndex;
	private int numberOfElements;
	private int maxSize;

	public CircularArray(int size) {
		list = new ArrayList<K>(size);
		maxSize = size;
		headIndex = 0;
		numberOfElements = 0;
		nextAvailableIndex = 0;
		tailIndex = size - 1;
		
		// Initial population
		for (int i = 0; i < maxSize; i++) {
			list.add(null);
		}
	}

	// Resize is called when there are no gaps and the list is completely full.
	private ArrayList<K> resizeList(List<K> originalList) {
		// Copy elements to the new list.
		int fillCount = maxSize - 1;
		maxSize *= 2;
		ArrayList<K> resizedList = new ArrayList<K>(maxSize);
		int i = headIndex;
		while (i != tailIndex) {
			// Start at logical index and check wrap around
			// Then copy the value over
			if (i < originalList.size() - 1) {
				resizedList.add(originalList.get(i));
				++i;
			} else {
				i = 0;
				resizedList.add(originalList.get(i));
			}
		}
		// Add the tail value.
		resizedList.add(originalList.get(i));
		
		// Fill the expanded array with dummy null values.
		while(fillCount < maxSize) {
			resizedList.add(null);
			++fillCount;
		}
		return resizedList;
	}
	
	private boolean isNextEmpty(int index) {
		int nextIndex = index + 1;
		if (nextIndex > list.size() - 1) {
			index = 0;
		}
		
		return list.get(nextIndex) == (null);
	}
		
	public boolean removeElement(int index) {
		if (index > maxSize - 1 || index < 0) {
			return false;
		}
		
		int logicalIndex = getLogicalIndex(index);
		
		if (list.get(logicalIndex) == null) {
			return false;
		}
		
		if (logicalIndex == headIndex) {
			// Case 1: Delete Head; shift head +1, may reach out bounds.
			// Set the tailindex to be the previous headindex.
			list.set(logicalIndex, null);
			tailIndex = headIndex;
			if (headIndex + 1 > list.size() - 1) {
				headIndex = 0;
			} else {
				++headIndex;
			}
		} else if (logicalIndex == tailIndex) {
			// Case 2: Deleting tail; 
			list.set(logicalIndex, null);
		} else {
			// Case 3: Deleting non-tail, move next index
			if (isNextEmpty(logicalIndex)) {
				// This is a non-tail end
				list.set(logicalIndex, null);
				nextAvailableIndex = logicalIndex;
			} else {
				// This is a non-tail middle portion.
				// Shifting elements when list is full should not include Tail-next.
				shiftElements(logicalIndex, nextAvailableIndex);
				
				// Adjust the next index, check for wrap around.
				--nextAvailableIndex;
				if (nextAvailableIndex < 0) {
					nextAvailableIndex = list.size() - 1;
				}
			}
		}
		--numberOfElements;
		return true;
	}
	
	// Used for deletion of a middle element.
	private void shiftElements(int startIndex, int endIndex) { 
		int currentIndex = startIndex;
		while (currentIndex != endIndex) {
			Object nextObj;
			// While shifting values 1 to the left, if already at the physical end
			// you must grab the wrapping value.
			if (currentIndex + 1 == list.size()) {
				nextObj = list.get(0);
			} else {
				nextObj = list.get(currentIndex + 1);
			}
			
			list.set(currentIndex, (K) nextObj);
						
			++currentIndex;
			// Wrap around index.
			if (currentIndex > list.size() - 1) {
				currentIndex = 0;
			}
		}
	}

	public void addElement(K object) {
		// Check if list is full; resize if necessary and realign indexes.
		if (numberOfElements == maxSize) {
			list = resizeList(list);
			headIndex = 0;
			tailIndex = maxSize - 1;
			nextAvailableIndex = numberOfElements;
		}
		// Add the element into the next spot.
		list.set(nextAvailableIndex, object);
		
		// Update indexes, wrap around if reached array size.
		// If the list will get full, nextAvailableIndex will automatically adjust.
		++nextAvailableIndex;
		if (nextAvailableIndex > maxSize - 1) {
			nextAvailableIndex = 0;
		}
		++numberOfElements;
	}

	public int getSize() {
		return list.size();
	}
	
	public int getMaxSize() {
		return maxSize;
	}

	public K getElement(int index) {
		int logicalIndex = getLogicalIndex(index);
		return list.get(logicalIndex);
	}
	
	private int getLogicalIndex(int index) {
		int logicalIndex = index + headIndex;
		// Offset the logicalIndex if it exceeds size.
		if (logicalIndex > list.size() - 1) {
			logicalIndex -= list.size();
		}
		return logicalIndex;
	}
	
	public String toStringState() {
		String str = "";
		for (int i = 0; i < maxSize; i++) {
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
	
	public String toString() {
		String str = "";
		for (int i = 0; i < maxSize; i++) {
			String value = " ";
			if (list.get(i) != null) {
				value = list.get(i).toString();
			}
			str += "[" + value + "]";
		}
		return str;
	}
}
