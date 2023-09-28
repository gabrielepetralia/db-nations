package org.java.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws SQLException {
	
		final String url = "jdbc:mysql://localhost:3306/db-nations";
		final String user = "root";
		final String password = "";
		
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("Enter a string to filter the countries: ");
			String strFilter = sc.nextLine();
			
			String sqlQuery = null;
			
			System.out.println("\n---------------------------------------------\n");
			
			sqlQuery = " SELECT c.country_id, c.name AS 'country', r.name AS 'region', c2.name AS 'continent' "
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
			
			System.out.print("Enter a country ID to show details: ");
			int selectedId = Integer.valueOf(sc.nextLine());
			
			System.out.println("\n---------------------------------------------\n");
			
			sqlQuery = " SELECT l.`language` "
					 	+ " FROM countries c "
					 	+ " JOIN country_languages cl "
					 	+ " ON c.country_id = cl.country_id "
					 	+ " JOIN languages l "
					 	+ " ON cl.language_id = l.language_id "
					 	+ " WHERE c.country_id = ? ";
			
			List<String> languages = new ArrayList<>();
			
			try {
				PreparedStatement ps = conn.prepareStatement(sqlQuery);	
				ps.setInt(1, selectedId);
				
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					languages.add(rs.getString("language"));
				}
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			sqlQuery = " SELECT c.name, cs.`year`, cs.population, cs.gdp "
						+ " FROM countries c "
						+ " JOIN country_stats cs  ON  c.country_id  = cs.country_id "
						+ " WHERE c.country_id = ? "
						+ "	AND cs.`year` = ( "
						+ "		SELECT cs.`year` "
						+ "		FROM countries c "
						+ "		JOIN country_stats cs  ON  c.country_id  = cs.country_id "
						+ "		WHERE c.country_id = ? "
						+ "		ORDER BY cs.`year` DESC "
						+ "		LIMIT 1	)";
			
			try {
				PreparedStatement ps = conn.prepareStatement(sqlQuery);	
				ps.setInt(1, selectedId);
				ps.setInt(2, selectedId);
				
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					
					String country = rs.getString("name");
					int year = rs.getInt("year");
					int population = rs.getInt("population");
					long gdp = rs.getLong("gdp");
					
					System.out.println("Details for: " + country + "\n");
					
					System.out.print("Languages: ");
					
					for (int i=0; i<languages.size(); i++) {
						System.out.print(languages.get(i));
						if(i<languages.size() - 1) {
							System.out.print(", ");
						}
					}
					
					System.out.println("\n\nMost recent stats");
					System.out.println("Year: " + year);
					System.out.println("Population: " + population);
					System.out.println("GDP: " + gdp);
					
					System.out.println("\n---------------------------------------------\n");
				} 
				
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		
				
	}		

}
