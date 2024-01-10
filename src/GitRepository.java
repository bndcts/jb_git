import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

public class GitRepository {
    private final HashMap<String, GitObject> objects;
    private final HashMap<String, LinkedList<Commit>> branches;
    private String currentBranch;

    public GitRepository() {
        this.objects = new HashMap<>();
        this.branches = new HashMap<>();
        this.currentBranch = "main";
        this.branches.put(currentBranch, new LinkedList<>());
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
            objects.put(tree.getHash(), tree);
        }
        branches.get(currentBranch).add(new Commit(author, message, tree));
    }

    public void switchBranch(String name) {
        if (!branches.containsKey(name)) {
            throw new IllegalArgumentException("Branch does not exist");
        } else {
            currentBranch = name;
        }
    }

    public void createBranch(String name) {
        if (branches.containsKey(name)) {
            throw new IllegalArgumentException("Branch already exists");
        } else {
            branches.put(name, new LinkedList<>());
        }
    }

    public LinkedList<Commit> getAllCommits() {
        return branches.values().stream().reduce(new LinkedList<>(), (acc, branch) -> {
            acc.addAll(branch);
            return acc;
        });
    }

    public LinkedList<Commit> getBranchCommits(String branchName) {
        return branches.get(branchName);
    }

    public Commit searchCommitByMetadata(String author, String message, LocalDateTime time) {
        LinkedList<Commit> commits = getAllCommits();
        for (Commit commit : commits) {
            if (commit.compareByMetadata(author, message, time)) {
                return commit;
            }
        }
        return null;
    }

    public Commit searchCommitByHash(String hash) {
        LinkedList<Commit> commits = getAllCommits();
        for (Commit commit : commits) {
            if (commit.equals(hash)) {
                return commit;
            }
        }
        return null;
    }
}
