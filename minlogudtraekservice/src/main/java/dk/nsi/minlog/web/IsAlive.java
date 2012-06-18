package dk.nsi.minlog.web;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.servlet.jsp.JspWriter;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

@Repository("isAlive")
public class IsAlive {
    
	@Inject
	DataSource dataSource;
	
	public void checkAll(JspWriter out) throws IOException, SQLException{
		out.println("Check database connection");		
		ResultSet rs = dataSource.getConnection().createStatement().executeQuery("SELECT 1");		
		if(!rs.next() || rs.getInt(1) != 1){
			throw new RuntimeException("Invalid result from database");
		}		
		out.println("Check database connection - OK");
	}
}