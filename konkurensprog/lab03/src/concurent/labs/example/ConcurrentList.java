package concurent.labs.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConcurrentList {

	public static void main(String[] args) throws InterruptedException {

		List<Integer> simpleList = new ArrayList<>();

		// Creating synchronized collections
		Collection<Integer> syncCollection = Collections.synchronizedCollection(new ArrayList<Integer>());
		List<Integer> syncList = Collections.synchronizedList(new ArrayList<Integer>());
		Map<String, String> syncMap = Collections.synchronizedMap(new HashMap<String, String>());
		Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<Integer>());

		// Init simple and sync List
		for (int i = 0; i < 10000000; i++) {
			simpleList.add(i);
			syncList.add(i);
		}

		// Incorrect iteration
		Thread th1 = new Thread(() -> {
			inCorrectIterate(simpleList);
		});
		Thread th2 = new Thread(() -> {
			inCorrectIterate(simpleList);
		});
		th1.start();
		th2.start();
		th1.join();
		th2.join();

		// Iterating through sync list with 2 threads
		th1 = new Thread(() -> {
			inCorrectIterate(syncList);
		});
		th2 = new Thread(() -> {
			inCorrectIterate(syncList);
		});
		th1.start();
		th2.start();
		th1.join();
		th2.join();

		// Correct iteration
		// Iterating through simple list with 2 threads
		th1 = new Thread(() -> {
			correctIterate(simpleList);
		});
		th2 = new Thread(() -> {
			correctIterate(simpleList);
		});
		th1.start();
		th2.start();
		th1.join();
		th2.join();

		// Iterating through sync list with 2 threads
		th1 = new Thread(() -> {
			correctIterate(syncList);
		});
		th2 = new Thread(() -> {
			correctIterate(syncList);
		});
		th1.start();
		th2.start();
		th1.join();
		th2.join();

	}

	public static void correctIterate(Collection<Integer> iterable) {
		// Note that synchronized block is still needed for synchronized collections
		// to avoid undeterministic behavior
		synchronized (iterable) {
			Iterator<Integer> iter = iterable.iterator();
			while(iter.hasNext()) {
				Integer item = iter.next();
			}
		}
	}

	public static void inCorrectIterate(Collection<Integer> iterable) {
		// Can cause undeterministic behavior
		Iterator<Integer> iter = iterable.iterator();
		while(iter.hasNext()) {
			Integer item = iter.next();
			System.out.println(item);
		}
	}

}
