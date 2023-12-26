import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
public class Tree extends GitObject{
    private HashMap<String, GitObject> entries;
    private String name;

    public Tree(String name) {
        this.entries = new HashMap<>();
        this.name = name;
        generateHash();
    }

    public void addEntry(String name, GitObject object) {
        entries.put(name, object);
        generateHash();
    }
    @Override
    protected String generateHash() {
        return DigestUtils.sha1Hex(entries.toString() + name);
    }

    public HashMap<String, GitObject> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tree " + hash + "{");
        for (GitObject object : entries.values()) {
            sb.append(object);
        }
        sb.append("}");
        return sb.toString();
    }
}
