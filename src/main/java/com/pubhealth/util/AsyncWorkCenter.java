package com.pubhealth.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/*
 * @author melo
*/
@Component
public class AsyncWorkCenter {

	private static ExecutorService services = Executors.newCachedThreadPool();

	public static void addJob(Runnable runable) {
		services.submit(runable);
	}

	public static void addJob(Callable<?> callable) {
		
	}

	public void shutdown() {
		services.shutdown();
	}

}
