package org.java.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws SQLException {
	
		final String url = "jdbc:mysql://localhost:3306/db-nations";
		final String user = "root";
		final String password = "";
		
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("Inserisci una stringa per filtrare le nazioni: ");
			String strFilter = sc.nextLine();
			
			System.out.println("\n---------------------------------------------\n");
			
			final String sqlQuery = " SELECT c.country_id, c.name AS 'country', r.name AS 'region', c2.name AS 'continent' "
					 		 	+ " FROM countries c  "
								+ " JOIN regions r ON c.region_id = r.region_id "
								+ "	JOIN continents c2 ON r.continent_id = c2.continent_id "
								+ " WHERE c.name LIKE ? "
								+ "	ORDER BY c.name " ;
			
			try {
				PreparedStatement ps = conn.prepareStatement(sqlQuery);	
				ps.setString(1, "%" + strFilter + "%");
				
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					
					int countryId = rs.getInt("c.country_id");
					String country = rs.getString("country");
					String region = rs.getString("region");
					String continent = rs.getString("continent");
					
					System.out.println("Country ID: " + countryId + "\n");
					System.out.println("Country: " + country);
					System.out.println("Region: " + region);
					System.out.println("Continent: " + continent);
					
					System.out.println("\n---------------------------------------------\n");
				} 
				
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		
				
	}		

}
