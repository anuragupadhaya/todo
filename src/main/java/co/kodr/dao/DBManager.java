package co.kodr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

import co.kodr.objects.Note;

public class DBManager {
	private final static Logger logger = Logger.getLogger(DBManager.class);
	private String driver;
	private String url;
	private String database;
	private String user;
	private String password;
	private String note_table;
	private final String DB_PROPERTIES = "db.properties";

	private String INSERT_NOTE = "INSERT INTO {0} VALUES (DEFAULT,?)";

	private String GET_ALL_NOTES = "SELECT * FROM {0}";

	public Connection getDBConnection() {
		initializeDBProperties();
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url + database, user, password);

		} catch (SQLException e) {
			logger.error("SQL exception in connecting to DB:", e);
		} catch (ClassNotFoundException e) {
			logger.error("Exception in initializing DB driver class", e);
		} finally {

		}
		if (connection != null) {
			logger.debug("Successfully connected to Database!");
			return connection;
		}
		return null;
	}

	private void initializeDBProperties() {
		Properties config = new Properties();
		try {
			config.load(getClass().getClassLoader().getResourceAsStream(DB_PROPERTIES));
			driver = config.getProperty("driver");
			url = config.getProperty("url");
			database = config.getProperty("database");
			user = config.getProperty("user");
			password = config.getProperty("password");
			note_table = config.getProperty("note_table");

			INSERT_NOTE = MessageFormat.format(INSERT_NOTE, note_table);
			GET_ALL_NOTES = MessageFormat.format(GET_ALL_NOTES, note_table);
		} catch (Exception e) {
			logger.error("Error in initializing DB properties:", e);
		} finally {

		}

	}

	public Note addNote(Note note) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Note newNote = null;
		try {
			connection = getDBConnection();
			preparedStatement = connection.prepareStatement(INSERT_NOTE);
			preparedStatement.setString(1, note.getText());
			int result = preparedStatement.executeUpdate();

			if (result == 1) {
				newNote = new Note();
				newNote.setText(note.getText());
				statement = connection.createStatement();
				resultSet = statement.executeQuery(GET_ALL_NOTES);
				if (resultSet != null) {
					resultSet.last();
					newNote.setId(resultSet.getInt(1));
				}
			}

		} catch (Exception e) {
			logger.error("SQL error in adding Note:" + note.toString(), e);
		} finally {
			logger.debug("Successfully added Note:" + newNote.toString());
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Error in closing connection:", e);
			}
		}

		return newNote;
	}

}
