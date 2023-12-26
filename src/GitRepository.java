import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

public class GitRepository {
    private LinkedList<Commit> commits;
    private HashMap<String, GitObject> objects;

    public GitRepository() {
        this.commits = new LinkedList<>();
        this.objects = new HashMap<>();
    }

    public void createCommit(String author, String message, Tree tree) {
        if (objects.containsKey(tree.getHash())) {
            tree = (Tree) objects.get(tree.getHash());
        } else {
            HashMap<String, GitObject> treeEntries = tree.getEntries();
            for (String key : treeEntries.keySet()) {
                if (!objects.containsKey(key)) {
                    objects.put(key, treeEntries.get(key));
                } else {
                    treeEntries.put(key, objects.get(key));
                }
            }
        }
        commits.add(new Commit(author, message, tree));
    }

    public LinkedList<Commit> getCommits() {
        return commits;
    }

    public Commit searchCommitByMetadata(String author, String message, LocalDateTime time) {
        for (Commit commit : commits) {
            if (commit.compareByMetadata(author, message, time)) {
                return commit;
            }
        }
        return null;
    }

    public Commit searchCommitByHash(String hash) {
        for (Commit commit : commits) {
            if (commit.equals(hash)) {
                return commit;
            }
        }
        return null;
    }
}
