package dk.nsi.minlog.web;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.jsp.JspWriter;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IsAliveTest {
	@Mock(answer=Answers.RETURNS_DEEP_STUBS)
	DataSource dataSource;
	
	@Mock
	JspWriter out;
	
	@Test
	public void checkAll() throws SQLException, IOException {
		when(dataSource.getConnection().createStatement().executeQuery("SELECT 1").next()).thenReturn(true);
		when(dataSource.getConnection().createStatement().executeQuery("SELECT 1").getInt(1)).thenReturn(1);
		
		IsAlive isAlive = new IsAlive();
		isAlive.dataSource = dataSource;
		
		isAlive.checkAll(out);
	}
	
	@Test
	public void checkAllFail() throws SQLException, IOException {
		when(dataSource.getConnection().createStatement().executeQuery("SELECT 1").next()).thenReturn(false);
		when(dataSource.getConnection().createStatement().executeQuery("SELECT 1").getInt(1)).thenReturn(1);
		
		IsAlive isAlive = new IsAlive();
		isAlive.dataSource = dataSource;
		
		isAlive.checkAll(out);
	}

}
