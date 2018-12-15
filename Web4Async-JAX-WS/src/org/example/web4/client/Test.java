package org.example.web4.client;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		
		Observable.create(emitter -> {
		     while (!emitter.isDisposed()) {
		         long time = System.currentTimeMillis();
		         emitter.onNext(time);
		         if (time % 2 != 0) {
		             emitter.onError(new IllegalStateException("Odd millisecond!"));
		             break;
		         }
		     }
		})
		.subscribe(System.out::println, Throwable::printStackTrace);
		
		
		Flowable.fromCallable(() -> {
			System.out.println(Thread.currentThread().getName() + "1");
		    Thread.sleep(1000); //  imitate expensive computation
		    return "Done";
		})
		  .subscribeOn(Schedulers.io())
		  .observeOn(Schedulers.single())
		  .subscribe((m)-> System.out.println(Thread.currentThread().getName() + m), (m)-> System.out.println("e: "+m));
		System.out.println(Thread.currentThread().getName() + "2");
		Thread.sleep(2000); // <--- wait for the flow to finish
	}

}
