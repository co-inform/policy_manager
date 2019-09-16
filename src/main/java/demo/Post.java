package demo;

public class Post {

    private float similarity;
    private float semantic;
    private float stance;
    private float veracity;
    private float credibility;

    private Post(PostBuilder builder) {
        this.similarity = builder.similarity;
        this.semantic = builder.semantic;
        this.stance = builder.stance;
        this.veracity = builder.veracity;
        this.credibility = builder.credibility;
    }

    public float getCredibility() {
        return credibility;
    }

    public float getSemantic() {
        return semantic;
    }

    public float getSimilarity() {
        return similarity;
    }

    public float getStance() {
        return stance;
    }

    public float getVeracity() {
        return veracity;
    }

    static class PostBuilder {
        private float similarity = 0;
        private float semantic = 0;
        private float stance = 0;
        private float veracity = 0;
        private float credibility = 0;

        public PostBuilder setSimilarity(float similarity) {
            this.similarity = similarity;
            return this;
        }

        public PostBuilder setSemantic(float semantic) {
            this.semantic = semantic;
            return this;
        }

        public PostBuilder setStance(float stance) {
            this.stance = stance;
            return this;
        }

        public PostBuilder setVeracity(float veracity) {
            this.veracity = veracity;
            return this;
        }

        public PostBuilder setCredibility(float credibility) {
            this.credibility = credibility;
            return this;
        }

        public Post getPost() {
            return new Post(this);
        }
    }

}
