import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;

public class Commit {
    private String author;
    private String message;
    private LocalDateTime time;
    private Tree tree;
    private String hash;

    public Commit(String author, String message, Tree tree) {
        this.author = author;
        this.message = message;
        this.time = LocalDateTime.now();
        this.tree = tree;
        this.hash = DigestUtils.sha1Hex(author + message + time);
    }

    public boolean compareByMetadata(String author, String message, LocalDateTime time) {
        return this.author.equals(author) && this.message.equals(message) && this.time.equals(time);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Commit) {
            return hash.equals(((Commit) obj).hash);
        } else if (obj instanceof String) {
            return hash.equals(obj);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "author='" + author + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", tree=" + tree +
                ", hash='" + hash + '\'' +
                '}';
    }

    public Tree getTree() {
        return tree;
    }

    public LocalDateTime getTime() {
        return time;
    }


}
