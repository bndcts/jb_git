import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GitRepositoryTest{

    @Test
    void createCommit_initialCommit_commitSizeOne() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);

        assertEquals(1, repo.getAllCommits().size());
    }

    @Test
    void createCommit_twoCommits_commitSizeTwo() {
        Tree initialTree = new Tree("root");
        Tree initialTree2 = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree2);


        assertEquals(2, repo.getAllCommits().size());
    }

    @Test
    void createCommit_twoCommitsSameTreeName_OneObject() {
        Tree initialTree = new Tree("root");
        Tree initialTree2 = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree2);

        LinkedList<Commit> commits = repo.getAllCommits();
        assertEquals(commits.get(0).getTree(), commits.get(1).getTree());
    }

    @Test
    void createCommit_twoCommitsSameTree_OneObject() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree);

        LinkedList<Commit> commits = repo.getAllCommits();
        assertEquals(commits.get(0).getTree(), commits.get(1).getTree());
    }

    @Test
    void createCommit_twoCommitsDifferentTrees_differentObjects() {
        Tree initialTree = new Tree("root");
        Tree initialTree2 = new Tree("root2");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree2);

        LinkedList<Commit> commits = repo.getAllCommits();
        assertNotEquals(commits.get(0).getTree(), commits.get(1).getTree());
    }

    @Test
    void createCommit_twoCommitsDifferentTreeSize_differentObjects() {
        Tree initialTree = new Tree("root");
        Tree initialTree2 = new Tree("root");
        initialTree2.addEntry("test", new Tree("test"));
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree2);

        LinkedList<Commit> commits = repo.getAllCommits();
        assertNotEquals(commits.get(0).getTree(), commits.get(1).getTree());
    }

    @Test
    void createCommit_TreeContainsObjectAlreadySeen_SameObject() {
        Tree initialTree = new Tree("root");
        Tree differentTreeRoot = new Tree("root2");
        Blob initialBlob = new Blob("blobe", "blob1");
        Blob differentObjectSameHash = new Blob("blobe", "blob1");

        initialTree.addEntry("blob1", initialBlob);
        differentTreeRoot.addEntry("blob1", differentObjectSameHash);
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", differentTreeRoot);

        LinkedList<Commit> commits = repo.getAllCommits();
        HashMap<String, GitObject> expectedTreeEntries = commits.get(0).getTree().getEntries();
        HashMap<String, GitObject> actualTreeEntries = commits.get(1).getTree().getEntries();
        assertTrue(expectedTreeEntries.get("blob1").trueEquals(actualTreeEntries.get("blob1")));
    }

    @Test
    void createCommit_twoCommitsDifferentBranches_branchCommitsSizeOne() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createBranch("branch");
        repo.switchBranch("branch");
        repo.createCommit("author", "message", initialTree);

        assertEquals(1, repo.getBranchCommits("branch").size());
        assertEquals(1, repo.getBranchCommits("main").size());
    }

    @Test
    void createCommit_oneCommitTwoBranches_noCommitOnSecondBranch() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createBranch("branch");
        repo.switchBranch("branch");

        assertEquals(0, repo.getBranchCommits("branch").size());
        assertEquals(1, repo.getBranchCommits("main").size());
    }

    @Test
    void switchBranch_branchDoesNotExist_throwsError() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);

        assertThrows(IllegalArgumentException.class, () -> repo.switchBranch("branch"));
    }

    @Test
    void createBranch_branchDoesExist_throwsErrors() {
        GitRepository repo = new GitRepository();

        assertThrows(IllegalArgumentException.class, () -> repo.createBranch("main"));
    }
    @Test
    void searchCommitByMetadata_commitExists_findsCommit() {
        Tree initialTree = new Tree("root");
        GitRepository repo = new GitRepository();

        repo.createCommit("author", "message", initialTree);
        repo.createCommit("author", "message", initialTree);
        LinkedList<Commit> commits = repo.getAllCommits();
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
        LinkedList<Commit> commits = repo.getAllCommits();
        LocalDateTime firstCommitTime = commits.getFirst().getTime();
        String createdHash = DigestUtils.sha1Hex("author" + "message" + firstCommitTime);

        Commit actualFirstCommit = repo.searchCommitByHash(createdHash);
        Commit expectedFirstCommit = commits.getFirst();
        assertEquals(expectedFirstCommit, actualFirstCommit);
    }

    @Test
    void searchCommitByHash_commitDoesNotExist_returnsNull() {
        GitRepository repo = new GitRepository();
        Tree initialTree = new Tree("root");

        repo.createCommit("author", "message", initialTree);

        assertNull(repo.searchCommitByHash("1234567890"));
    }

}