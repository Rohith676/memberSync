package com.aegis.membersync.service;

import com.aegis.membersync.config.ConfigLoader;
import com.aegis.membersync.model.MemberRequest;
import com.aegis.membersync.util.HttpUtil;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceNowService {
	
	private static final Logger LOGGER = Logger.getLogger(ServiceNowService.class.getName());

	public MemberRequest getRequestDetails(String requestNumber) {

	    try {
	        String baseUrl = ConfigLoader.getEnvProperty("OrgUrl");
	        String endpoint = ConfigLoader.get("restEndPoint");

	        String url = baseUrl + endpoint + "?number=" + requestNumber;

	        String username = ConfigLoader.getEnvProperty("userName");
	        String password = ConfigLoader.getEnvProperty("password");
	        
	        // 🔹 LOG API CALL
            LOGGER.info("Calling ServiceNow API for ticket: " + requestNumber);
            LOGGER.info("URL: " + url);

	        String response = HttpUtil.get(url, username, password);

	        JSONObject json = new JSONObject(response);
	        JSONArray result = json.getJSONArray("result");

	        if (result.length() > 0) {

	            JSONObject obj = result.getJSONObject(0);

	            MemberRequest request = new MemberRequest();

	            // ticket number
	            request.setRequestNumber(obj.getString("number"));

	            // status
	            String state = obj.getString("state");
	            request.setStatus(mapState(state));

	         // Handle closed_by safely
	            Object closedByObj = obj.get("closed_by");
	            if (closedByObj instanceof JSONObject) {
	            	request.setClosedBy(((JSONObject) closedByObj).optString("value", null));
	            } else {
	            	request.setClosedBy(null);
	            }

	         // closed_at
	            String closedAt = obj.optString("closed_at", null);
	            request.setClosedAt(closedAt);
	            
	            // 🔥 IMPORTANT LOG
	            LOGGER.info("ServiceNow Response → Ticket: " + requestNumber +
	                    " | State: " + state +
	                    " | Closed By: " + closedByObj +
	                    " | Closed At: " + closedAt);

	            return request;
	        } else {
                LOGGER.warning("No result found in ServiceNow for ticket: " + requestNumber);
            }	

	    } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error fetching details for ticket: " + requestNumber, e);
        }

	    return null;
	}
	
	private String mapState(String state) {

	    if ("7".equals(state)) { // In Service now 7 means closed
	        return "CLOSED";
	    } else {
	        return "OPEN";  // treat all others as Open
	    }
	}
}