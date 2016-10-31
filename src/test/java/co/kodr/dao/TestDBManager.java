package co.kodr.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.Test;

import co.kodr.objects.Note;

public class TestDBManager {

	@Test
	public final void testGetDBConnection() {
		Connection connection = new DBManager().getDBConnection();
		assertNotNull(connection);
		assertTrue(connection instanceof Connection);
	}

	@Test
	public final void testAddNote() {
		String text = "New note";
		Note note = new Note();
		note.setText(text);
		Note newNote = new DBManager().addNote(note);
		assertNotNull(newNote.getId());
	}

}
