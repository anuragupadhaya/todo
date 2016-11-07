package co.kodr.exception;

import co.kodr.objects.Note;

/**
 * Custom exception wrapper for Note
 */
public class NoteException extends Exception {

	private static final long serialVersionUID = 819527788048062878L;

	private Note note;

	public NoteException(String message) {
		super(message);
	}

	public NoteException(String message, Note note) {
		super(message);
		this.note = note;
	}

	public NoteException(String message, Note note, Throwable cause) {
		super(message, cause);
		this.note = note;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + ":" + note;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
