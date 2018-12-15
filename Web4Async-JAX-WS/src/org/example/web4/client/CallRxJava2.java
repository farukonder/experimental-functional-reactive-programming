package org.example.web4.client;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import org.example.web4.NewOperationResponse;
import org.example.web4.Web4_Service;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public class CallRxJava2 {
	
	static Web4_Service web4 = new Web4_Service();
	
	public static void main(String[] args) throws InterruptedException {
		
		
//		web4.getService4().newOperationAsync(200, res -> {
//			try {
//				System.out.println("async plain 200" + res.get().getOut());
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			}
//		});
		
		
//		Observable.create(emitter -> {
//		     while (!emitter.isDisposed()) {
//		         long time = System.currentTimeMillis();
//		         emitter.onNext(time);
//		         if (time % 2 ==3 ) {
//		             emitter.onError(new IllegalStateException("Odd millisecond!"));
//		             break;
//		         }
//		     }
//		})
//		.subscribe(System.out::println, Throwable::printStackTrace);
		
		
//		Observable.create(emitter -> web4.getService4().newOperationAsync(100,createHandlerJava8(emitter)))
//		.subscribe((m)-> System.out.println("o: "+m), (m)-> System.out.println("e: "+m));
		
		
		
	      	Flowable.range(1000,10)
	        .subscribeOn(Schedulers.single())
	        .map(w -> Observable.create(emitter -> web4.getService4().newOperationAsync(w,createHandlerJava8(emitter))))
		  .subscribe(k -> k.subscribe((i)-> System.out.println("o: "+ ((NewOperationResponse) i).getOut()), (m)-> System.out.println("e: "+m)));
		
		Thread.sleep(999999);
	}
	
	public static <T> AsyncHandler<NewOperationResponse> createHandlerJava8(Emitter<Object> emitter) {

		AsyncHandler<NewOperationResponse> back = res -> {
			try {
				emitter.onNext(res.get());
			} catch (InterruptedException | ExecutionException e) {
				emitter.onError(e);
			}
		};
		
		return back;
	}
	
	public static <T> AsyncHandler<NewOperationResponse> createHandlerJava6(ObservableEmitter<Object> emitter) {

		AsyncHandler<NewOperationResponse> back = new AsyncHandler(){

			@Override
			public void handleResponse(Response res) {
				try {
					emitter.onNext(res.get());
				} catch (InterruptedException | ExecutionException e) {
					emitter.onError(e);
				}
			}
			
		};

		return back;
	}
	
}
