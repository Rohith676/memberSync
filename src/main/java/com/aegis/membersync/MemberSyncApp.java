package com.aegis.membersync;

import com.aegis.membersync.service.MemberSyncService;

public class MemberSyncApp {

	public static void main(String[] args) {

	    String env = args.length > 0 ? args[0] : "dev";

	    System.setProperty("env", env); // pass to ConfigLoader

	    MemberSyncService service = new MemberSyncService();
	    service.startScheduler();

	    try {
	        Thread.sleep(Long.MAX_VALUE);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
}