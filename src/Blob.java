import org.apache.commons.codec.digest.DigestUtils;
public class Blob extends GitObject{
    private String content;
    private String name;

    public Blob(String content, String name) {
        this.content = content;
        this.name = name;
    }

    @Override
    protected String generateHash() {
        return DigestUtils.sha1Hex(content);
    }

    @Override
    public String toString() {
        return "Blob{" +
                "content='" + content + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
