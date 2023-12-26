import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GitRepositoryTest{

    @Test
    void createCommit_initialCommit_commitSizeOne() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);

        assertEquals(1, repo.getCommits().size());
    }

    @Test
    void createCommit_twoCommits_commitSizeTwo() {
        Tree initialTree = new Tree("root");
        Tree initialTree2 = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree2);


        assertEquals(2, repo.getCommits().size());
    }

    @Test
    void createCommit_twoCommitsSameTreeName_OneObject() {
        Tree initialTree = new Tree("root");
        Tree initialTree2 = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree2);

        LinkedList<Commit> commits = repo.getCommits();
        assertEquals(commits.get(0).getTree(), commits.get(1).getTree());
    }

    @Test
    void createCommit_twoCommitsSameTree_OneObject() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree);

        LinkedList<Commit> commits = repo.getCommits();
        assertEquals(commits.get(0).getTree(), commits.get(1).getTree());
    }

    @Test
    void createCommit_twoCommitsDifferentTrees_OneObject() {
        Tree initialTree = new Tree("root");
        Tree initialTree2 = new Tree("root2");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree2);

        LinkedList<Commit> commits = repo.getCommits();
        assertNotEquals(commits.get(0).getTree(), commits.get(1).getTree());
    }

    @Test
    void searchCommitByMetadata_commitExists_findsCommit() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree);
        LinkedList<Commit> commits = repo.getCommits();
        LocalDateTime firstCommitTime = commits.getFirst().getTime();

        Commit actualFirstCommit = repo.searchCommitByMetadata("author", "message", firstCommitTime);
        Commit expectedFirstCommit = commits.getFirst();

        assertEquals(expectedFirstCommit, actualFirstCommit);
    }

    @Test
    void searchCommitByMetadata_commitDoesNotExist_returnsNull() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("auth2or", "message", initialTree);

        assertNull(repo.searchCommitByMetadata("autho2r", "message", LocalDateTime.now()));
    }

    @Test
    void searchCommitByHash_commitExists_findsCommit() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author2", "message", initialTree);
        LinkedList<Commit> commits = repo.getCommits();
        LocalDateTime firstCommitTime = commits.getFirst().getTime();
        String createdHash = DigestUtils.sha1Hex("author" + "message" + firstCommitTime);

        Commit actualFirstCommit = repo.searchCommitByHash(createdHash);
        Commit expectedFirstCommit = commits.getFirst();
        assertEquals(expectedFirstCommit, actualFirstCommit);
    }

    @Test
    void searchCommitByHash_commitDoesNotExist_returnsNull() {
        GitRepository repo = new GitRepository();

        assertNull(repo.searchCommitByHash("1234567890"));
    }

}