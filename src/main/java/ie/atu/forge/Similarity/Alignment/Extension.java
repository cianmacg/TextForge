package main.java.ie.atu.forge.Similarity.Alignment;

// A record for each extension. This helps prevent duplicates.
public record Extension (int subjectPos, int queryPos, String text) {
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Extension)) return false;
        Extension other = (Extension) o;
        return this.subjectPos == other.subjectPos() && this.queryPos == other.queryPos() && this.text.equals(other.text);
    }
}
