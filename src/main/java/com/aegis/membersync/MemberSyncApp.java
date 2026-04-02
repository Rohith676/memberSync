package com.aegis.membersync;

import com.aegis.membersync.service.MemberSyncService;

public class MemberSyncApp {

	public static void main(String[] args) throws Exception {

		MemberSyncService service = new MemberSyncService();
		service.startScheduler(); // 🔥 starts auto sync

		Thread.sleep(Long.MAX_VALUE);

		// Keep app running

	}
}