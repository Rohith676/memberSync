package com.aegis.membersync.db;

import com.aegis.membersync.config.ConfigLoader;
import com.aegis.membersync.model.MemberRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBService {

	public Connection getConnection() throws Exception {
		String url = ConfigLoader.getEnvProperty("db.url");
		String user = ConfigLoader.getEnvProperty("db.user");
		String pass = ConfigLoader.getEnvProperty("db.password");

		return DriverManager.getConnection(url, user, pass);
	}

	/**
	 * Fetch all OPEN requests from DB
	 */
	public List<MemberRequest> getOpenRequests(Connection conn) throws Exception {
		List<MemberRequest> list = new ArrayList<>();

		String query = "SELECT TICKET_NUMBER, REQUEST_STATUS FROM MEMBER_SERVICE_REQUEST WHERE REQUEST_STATUS='OPEN'";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				MemberRequest req = new MemberRequest();
				req.setRequestNumber(rs.getString("TICKET_NUMBER"));
				req.setStatus(rs.getString("REQUEST_STATUS"));
				list.add(req);
			}
		}

		return list;
	}

	/**
	 * Update request status + closed details Updates only if status has changed
	 */
	public void updateRequest(Connection conn, MemberRequest req) throws Exception {

		String update = "UPDATE MEMBER_SERVICE_REQUEST " + "SET REQUEST_STATUS = ?, CLOSED_BY = ?, CLOSED_AT = ? "
				+ "WHERE TICKET_NUMBER = ? AND REQUEST_STATUS <> ?";

		try (PreparedStatement ps = conn.prepareStatement(update)) {

			ps.setString(1, req.getStatus());
			ps.setString(2, req.getClosedBy());

			if (req.getClosedAt() != null && !req.getClosedAt().isEmpty()) {
				ps.setTimestamp(3, Timestamp.valueOf(req.getClosedAt()));
			} else {
				ps.setNull(3, Types.TIMESTAMP);
			}

			ps.setString(4, req.getRequestNumber());
			ps.setString(5, req.getStatus()); // condition

			int rows = ps.executeUpdate();

			if (rows > 0) {
				System.out.println("Updated: " + req.getRequestNumber());
			} else {
				System.out.println("No change: " + req.getRequestNumber());
			}
		}
	}
}