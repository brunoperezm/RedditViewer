package com.example.brpper.redditviewer;

import org.junit.Test;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
	@Test
	public void addition_isCorrect () {
		assertEquals(4, 2 + 2);
	}

	public static void compute(Integer integer) {
		try {
			System.out.println("Computando el entero: " + integer);
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void probando()  {
		Observable.range(1,1_000_000)
				.observeOn(Schedulers.computation())
				.subscribe( ExampleUnitTest::compute );

	}

	@Test
	public void anotherTest() {
		final String[] result = {""};
		String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
		Observable<String> observable = Observable.fromArray(letters);
		observable.subscribe(
				i -> result[0] += i,  //OnNext
				Throwable::printStackTrace, //OnError
				() -> result[0] += "_Completed" //OnCompleted
		);
		assertTrue(result[0].equals("abcdefg_Completed"));
	}
}