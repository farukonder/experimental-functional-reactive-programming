package org.example.web4.client;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.example.web4.Web4_Service;

/**
 * 
 * @author IBMFONDER
 * 
 *         https://blog.godatadriven.com/jaxws-reactive-client
 * 
 *         written with help of above blog
 *
 */

public class CallClient {
	static Web4_Service web4 = new Web4_Service();

	public static void main(String[] args) throws InterruptedException {
		System.out.println("sync " + web4.getService4().newOperation(100));
		
		web4.getService4().newOperationAsync(200, res -> {
			try {
				System.out.println("async plain 200" + res.get().getOut());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		});
		
		web4.getService4().newOperationAsync(100, res -> {
			try {
				System.out.println("async plain 100" + res.get().getOut());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		});
		
		Thread.sleep(99999999);
		if(1!=2){
			return;
		}
		
		CallClient callClient = new CallClient();
//		callClient.operation(250).subscribe(i -> System.out.println("async " + ((NewOperationResponse) i).getOut()), i -> System.out.println("async error" + i.getMessage()));
		Random r = new Random();

		while (true) {
			int count = 0;
			while (count < 1000) {

				int low = 700;
				int high = 2000;
				int waitInExternalOperation = r.nextInt(high - low) + low;

				Thread.sleep(9);

				System.out.println(count++);
//				callClient.operation(waitInExternalOperation).subscribe(i -> System.out.println("async loop " + ((NewOperationResponse) i).getOut()), i -> System.out.println("async error" + i.getMessage()));
			}
			System.out.println("lets wait a while");
			Thread.sleep(1000 * 222222);
		}
	}
	
//    @WebMethod(operationName = "NewOperation", action = "http://www.example.org/web4/NewOperation")
//    @RequestWrapper(localName = "NewOperation", targetNamespace = "http://www.example.org/web4/", className = "org.example.web4.NewOperation")
//    @ResponseWrapper(localName = "NewOperationResponse", targetNamespace = "http://www.example.org/web4/", className = "org.example.web4.NewOperationResponse")
//    public Future<?> newOperationAsync(
//        @WebParam(name = "in", targetNamespace = "")
//        int in,
//        @WebParam(name = "asyncHandler", targetNamespace = "")
//        AsyncHandler<NewOperationResponse> asyncHandler);

	/*
	public Mono<NewOperationResponse> operation(int input) {
		// return Mono.create(sink -> portType.operation(input, into(sink)));
		return Mono.create(sink -> web4.getService4().newOperationAsync(input, createHandler(sink)));
	}

	public static <T> AsyncHandler<T> createHandler(MonoSink<T> sink) {

		AsyncHandler<T> back = res -> {
			try {
				sink.success(res.get());
			} catch (InterruptedException | ExecutionException e) {
				sink.error(e);
			}
		};

		return back;
	}
	
	*/
}
