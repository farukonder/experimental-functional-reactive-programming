package com.github.farukonder.experimenting.frp.java;

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

public class Caller_Java {
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
	}
}
