package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {
	
	
	public void getAllBusiness(Map<String,Business> idMap){
		String sql = "SELECT * FROM Business";
	
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
			idMap.put(business.getBusinessId(), business);
			}
			res.close();
			st.close();
			conn.close();
		
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public void getAllReviews(Map<String,Review> idMap){
		String sql = "SELECT * FROM Reviews";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				idMap.put(review.getReviewId(), review);
			}
			res.close();
			st.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<String> getCitta(){
		String sql = "SELECT DISTINCT b.city AS c "
				+ "FROM business b "
				+ "ORDER BY c";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String s=res.getString("c");
				
				result.add(s);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Business> getLocali(String citta,Map<String,Business> idMap){
		String sql = "SELECT b.business_id as id,b.business_name "
				+ "FROM business b "
				+ "WHERE b.city=? "
				+"order by b.business_name";
		List<Business> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business b= idMap.get(res.getString("id"));
				result.add(b);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Review> getVertici(Business business,Map<String,Review> idMap,String citta){
		String sql = "SELECT r.review_id as id "
				+ "FROM business b,reviews r "
				+ "WHERE b.business_id=r.business_id AND b.business_id=? AND b.city=? ";
		List<Review> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, business.getBusinessId());
			st.setString(2,citta);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review r=idMap.get(res.getString("id"));
				result.add(r);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Adiacenza> getArchi(Business business,Map<String,Review> idMap,String citta){
		String sql = "SELECT r1.review_id AS id1,r2.review_id AS id2,r1.review_date AS re1,r2.review_date AS re2 "
				+ "FROM business b1,reviews r1,reviews r2 "
				+ "WHERE b1.business_id=r1.business_id AND b1.business_id=? AND b1.city=? "
				+ "AND b1.business_id=r2.business_id AND r1.review_id< r2.review_id "
				+ "AND r1.review_date!=r2.review_date";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, business.getBusinessId());
			st.setString(2,citta);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review r1=idMap.get(res.getString("id1"));
				Review r2=idMap.get(res.getString("id2"));
				Date data1=res.getDate("re1");
				Date data2=res.getDate("re2");
				LocalDate dat1=data1.toLocalDate();
				LocalDate dat2=data2.toLocalDate();
				
				double peso=ChronoUnit.DAYS.between(dat1, dat2);
				Adiacenza a = new Adiacenza(r1,r2,peso);
				result.add(a);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
