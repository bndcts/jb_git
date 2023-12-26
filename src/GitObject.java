public abstract class GitObject {
    protected String hash;

    protected abstract String generateHash();

    public String getHash() {
        if (hash == null) {
            hash = generateHash();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GitObject) {
            return getHash().equals(((GitObject) obj).getHash());
        }
        return false;
    }
}
