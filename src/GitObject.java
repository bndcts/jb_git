public abstract class GitObject {
    protected String hash;

    protected abstract String generateHash();

    public String getHash() {
        if (hash == null) {
            hash = generateHash();
        }
        return hash;
    }

    public boolean trueEquals(Object obj) {
        return this == obj;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GitObject) {
            return getHash().equals(((GitObject) obj).getHash());
        }
        return false;
    }
}
