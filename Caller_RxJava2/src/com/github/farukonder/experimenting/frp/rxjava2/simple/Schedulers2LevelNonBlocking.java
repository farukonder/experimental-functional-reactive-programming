package com.github.farukonder.experimenting.frp.rxjava2.simple;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Schedulers2LevelNonBlocking {
	

	static ExecutorService observeOn_1 = Executors.newFixedThreadPool(5, new WorkerThreadFactory("observeOn1"));
	static ExecutorService subscribeOn_1 = Executors.newFixedThreadPool(5, new WorkerThreadFactory("subscribeOn1"));

	static ExecutorService observeOn_2 = Executors.newFixedThreadPool(5, new WorkerThreadFactory("observeOn2"));
	static ExecutorService subscribeOn_2 = Executors.newFixedThreadPool(5, new WorkerThreadFactory("subscribeOn2"));
	
	static String getlog(String operation,String threadName,String values){
		return getlogg(operation, threadName, values, "");
	}
	static String getlogg(String operation,String threadName,String values,String i){
		return padRight(operation, 10) + ", " + padRight(threadName, 15) + ", "+ padRight(values, 25) + ", "+ i.replaceAll("\\s+"," ");
	}
	
	
	static Observable<String> flatMapV2(String threadName, Object v1){

		System.out.println(getlog("flatMapV2",threadName,"v1: " + v1));
        
		 return Observable.create(emitter -> {
			 int count = 0;
		     while (count++<2) {
		         System.out.println(getlog("create2",Thread.currentThread().getName(),"v1: " + v1 + " count: " + count));
		         emitter.onNext(""  + count);
		     }
		     emitter.onComplete();
			})
		    .observeOn(Schedulers.from(observeOn_2))
		    .subscribeOn(Schedulers.from(subscribeOn_2))
		    .doOnNext(i -> System.out.println(getlogg("doOnNext2",Thread.currentThread().getName(),"v1: " + v1," i: " + i)))
		    .map(i -> getlogg("map2",Thread.currentThread().getName(),"",i.toString()));
			
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		
		
		Observable.create(s -> { 
	 			 int count = 0;
			     while (count++ < 2) {
			         System.out.println(getlog("create1",Thread.currentThread().getName(),"v1: " + count));
			         s.onNext(count);
			     }
				s.onComplete();
				}).<String>flatMap(v -> flatMapV2(Thread.currentThread().getName(),v))
						.observeOn(Schedulers.from(observeOn_1))
						.subscribeOn(Schedulers.from(subscribeOn_1))
						.doOnNext(i -> System.out.println(getlogg("doOnNext",Thread.currentThread().getName(),""," i: " + i)))
						.map(i -> getlogg("map1",Thread.currentThread().getName(),"",i))
						.subscribe
						(s -> System.out.println(getlogg("subscribe", Thread.currentThread().getName(), "", " s: " + s)));
		
		System.out.println("start thread sleep");
		
		Thread.sleep(15000);
		
		observeOn_1.shutdown();
		subscribeOn_1.shutdown();
		
		observeOn_2.shutdown();
		subscribeOn_2.shutdown();
		
	}
	
	public static class WorkerThreadFactory implements ThreadFactory {
		   private int counter = 0;
		   private String prefix = "";

		   public WorkerThreadFactory(String prefix) {
		     this.prefix = prefix;
		   }

		   public Thread newThread(Runnable r) {
		     return new Thread(r, prefix + "_" + counter++);
		   }
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}


}
