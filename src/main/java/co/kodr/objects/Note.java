package co.kodr.objects;

public class Note {
	Integer id;
	String text;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Note [id=" + id + ", text=" + text + "]";
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Note) {
			Note n = (Note) obj;
			if (this.text.equals(n.text)) {
				return true;
			}
		}
		return false;
	}

}
