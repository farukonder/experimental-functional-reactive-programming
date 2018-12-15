package tr.onder.experimental.frp.rxjava2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.ws.AsyncHandler;

import org.example.web4.NewOperationResponse;
import org.example.web4.Web4_Service;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class CallRxJava2_3 {

	static Web4_Service web4 = new Web4_Service();

	public static void main(String[] args) throws InterruptedException {
		
		AtomicInteger count = new AtomicInteger();
		
		List newList = Collections.synchronizedList(new ArrayList());

//		Single<List<Object>> l = 
//		Flowable.range(2000, 10)
		Flowable.just(100, 100, 100, 100, 100, 100, 1000)
//		.subscribeOn(Schedulers.single())
//		.observeOn(Schedulers.single())
		.flatMap(w -> Flowable.create(emitter -> web4.getService4().newOperationAsync(w, createHandlerJava8(emitter)),BackpressureStrategy.BUFFER))
		.blockingSubscribe((i) -> System.out.println("o: " + ((NewOperationResponse) i).getOut()), (e) -> System.out.println("e: " + e));
//		.toList()
//        //could implement more intelligent logic. eg. check that everything is successful
//        .map(results -> true);
//		.subscribe((i) -> System.out.println("o: " + ((NewOperationResponse) i).getOut()), (m) -> System.out.println("e: " + m));

//		Thread.sleep(25000);
		
	}

	public static <T> AsyncHandler<NewOperationResponse> createHandlerJava8(Emitter<Object> emitter) {

		AsyncHandler<NewOperationResponse> back = res -> {
			try {
				
				NewOperationResponse newOperationResponse = res.get();
				System.out.println("newOperationResponse: " + newOperationResponse);
				
				emitter.onNext(newOperationResponse);
				emitter.onComplete();
			
			} catch (InterruptedException | ExecutionException e) {
				
				emitter.onError(e);
			}
		};

		return back;
	}


}
