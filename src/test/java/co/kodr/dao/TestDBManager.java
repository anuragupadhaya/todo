package co.kodr.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.Test;

import co.kodr.objects.Note;
import co.kodr.utils.db.DBUtils;

public class TestDBManager {

	@Test
	public final void testGetDBConnection() {
		Connection connection = DBUtils.getConnection();
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

	@Test
	public final void testUpdateNote() {
		String text = "New note";
		Note note = new Note();
		note.setText(text);
		Note newNote = new DBManager().addNote(note);
		assertNotNull(newNote.getId());
		text = "Updated note";
		assertTrue(new DBManager().updateNote(newNote, text));
	}

}
