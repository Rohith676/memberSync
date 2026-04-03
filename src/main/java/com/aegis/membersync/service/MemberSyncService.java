package com.aegis.membersync.service;

import com.aegis.membersync.db.DBService;
import com.aegis.membersync.model.MemberRequest;

import java.sql.Connection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberSyncService {

	private static final Logger LOGGER = Logger.getLogger(MemberSyncService.class.getName());

	private DBService dbService = new DBService();
	private ServiceNowService snService = new ServiceNowService();

	// 🔁 Scheduler - runs every 2 minutes
	public void startScheduler() {

		LOGGER.info("Starting Member Sync Scheduler...");

		Timer timer = new Timer(true);

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sync();
			}
		}, 0, 2 * 60 * 1000); // 2 minutes
	}

	// 🔄 Main Sync Logic
	public void sync() {

	    LOGGER.info("Sync started...");

	    try {

	        List<MemberRequest> requests = dbService.getOpenRequests();

	        LOGGER.info("Total OPEN requests: " + requests.size());

	        for (MemberRequest req : requests) {

	            try {
	                LOGGER.info("Processing ticket: " + req.getRequestNumber());

	                MemberRequest updated =
	                        snService.getRequestDetails(req.getRequestNumber());

	                if (updated == null) {
	                    LOGGER.warning("No response for: " + req.getRequestNumber());
	                    continue;
	                }

	                if (!updated.getStatus().equalsIgnoreCase(req.getStatus())) {

	                    dbService.updateRequest(updated);

	                    LOGGER.info("Updated: " + req.getRequestNumber() +
	                            " → " + updated.getStatus());
	                }

	            } catch (Exception ex) {
	                LOGGER.log(Level.SEVERE,
	                        "Error processing ticket: " + req.getRequestNumber(), ex);
	            }
	        }

	        LOGGER.info("Sync completed successfully.");

	    } catch (Exception e) {
	        LOGGER.log(Level.SEVERE, "Sync failed", e);
	    }
	}
}