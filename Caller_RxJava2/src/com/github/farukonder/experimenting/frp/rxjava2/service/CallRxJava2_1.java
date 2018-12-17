package com.github.farukonder.experimenting.frp.rxjava2.service;

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

public class CallRxJava2_1 {

	static Web4_Service web4 = new Web4_Service();

	public static void main(String[] args) throws InterruptedException {

		Flowable.range(1000, 10)
		.map(w -> Observable.create(emitter -> web4.getService4().newOperationAsync(w, createHandlerJava8(emitter))))
		.subscribe(k -> {
			k.subscribeOn(Schedulers.io());
			 k .observeOn(Schedulers.single());
			k.subscribe((i) -> System.out.println("o: " + ((NewOperationResponse) i).getOut()), (m) -> System.out.println("e: " + m));});
		

		Thread.sleep(25000);
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

		AsyncHandler<NewOperationResponse> back = new AsyncHandler() {

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
