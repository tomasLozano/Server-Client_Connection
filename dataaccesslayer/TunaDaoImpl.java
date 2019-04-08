package dataaccesslayer;
/* File: TunaDaoImpl.java
 * Author: Stanley Pieda
 * Date: Sept, 2017
 * References:
 * Ram N. (2013). Data Access Object Design Pattern or DAO Pattern [blog] Retrieved from
 * http://ramj2ee.blogspot.in/2013/08/data-access-object-design-pattern-or.html
 */ 
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import datatransfer.Tuna;

/**
 * @author Tomas Lozano from code of Stanley Pineda
 * Course: CST8277_300
 * Date: 2018-10-12
 * 
 * Description: TunaDaoImpl implements TunaDao with the object of accept a tuna object, return a tuna object or clean the tuna objects.
 */

public class TunaDaoImpl implements TunaDao{
	
	/** 
	 * Returns a reference to a Tuna object, loaded with data
	 * from the database, based on lookup using a UUID as String
	 * @param uuid String based UUID
	 * @return Tuna transfer object, or null if no match based on uuid found
	 * @throws SQLException
	 * @author Stanley Pieda
	 */
	@Override
	public Tuna getTunaByUUID(String uuid) throws SQLException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Tuna tuna = null;
		try{
			DataSource source = new DataSource();
			con = source.getConnection();
			pstmt = con.prepareStatement(
					"SELECT * FROM Tunas WHERE uuid = ?");
			pstmt.setString(1, uuid);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			tuna = new Tuna();
			tuna.setId(rs.getInt("id"));
			tuna.setRecordNumber(rs.getInt("recordnumber"));
			tuna.setOmega(rs.getString("omega"));
			tuna.setDelta(rs.getString("delta"));
			tuna.setTheta(rs.getString("theta"));
			tuna.setUUID(rs.getString("uuid"));
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}
		finally{
			try{ if(rs != null){ rs.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			try{ if(pstmt != null){ pstmt.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			try{ if(con != null){ con.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
		}
		return tuna;
	}

    /**
	 * Accepts a Tuna object reference, inserting the encapsulated data into database.
	 * @param tuna with data for record insertion
	 * @throws SQLException
	 * @author Stanley Pieda
     * @return 
	 */
	public boolean insertTuna(Tuna tuna) throws SQLException{
    	DataSource source = new DataSource();
		Connection con = source.getConnection();
		PreparedStatement pstmt = null;
		try{
			pstmt = con.prepareStatement(
					"INSERT INTO Tunas (recordnumber, omega, delta, theta, uuid) " +
					"VALUES(?, ?, ?, ?, ?)");
			pstmt.setInt(1, tuna.getRecordNumber());
			pstmt.setString(2, tuna.getOmega());
			pstmt.setString(3, tuna.getDelta());
			pstmt.setString(4, tuna.getTheta());
			pstmt.setString(5, tuna.getUUID());
			pstmt.executeUpdate();
			return true;
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}
		finally{
			try{ if(pstmt != null){ pstmt.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			try{ if(con != null){ con.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			
		}
	}

	/**
	 * Method to clean the database in case you want to start a new record of Tunas
	 * 
	 * @return if succesfully deleted the files.
	 * @throws SQLException in case it loses the connection or can't delete the tunas for lack of connection
	 */
	public boolean deleteTunas() throws SQLException{
		DataSource source = new DataSource();
		Connection conn = source.getConnection();
		PreparedStatement pstmt = null;
		try{
			pstmt = conn.prepareStatement(
					"TRUNCATE TABLE tunas");
			pstmt.executeUpdate();
			return true;
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}
		finally{
			try{ if(pstmt != null){ pstmt.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			try{ if(conn != null){ conn.close(); }}
			catch(SQLException ex){System.out.println(ex.getMessage());}
			
		}
	}
}
