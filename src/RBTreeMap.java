public class RBTreeMap {
    static class RBTree {
        private static final int RED = 0;
        private static final int BLACK = 1;
        static RBTree EMPTY = new EmptyTree();

        private String key;
        private Object value;
        public int color;
        public RBTree left, right;

        static RBTree newInstance(String key, Object value) {
            return new RBTree(key, value);
        }

        RBTree(String key, Object value) {
            this.key = key;
            this.value = value;
            this.left = this.right = EMPTY;
            this.color = RED;
        }

        void makeAsRoot() {
            this.color = BLACK;
        }

        boolean isEmpty() {
            return false;
        }

        boolean isRed() {
            return this.color == RED;
        }

        boolean isBlack() {
            return this.color == BLACK;
        }

        RBTree insert(String key, Object value) {
            int comp = key.compareTo(this.key);
            if (comp == 0) {
                this.value = value;
            } else if (comp < 0) {
                this.left = left.insert(key, value);
            } else {
                this.right = right.insert(key, value);
            }
            // Rebalance of Left leaning red-black trees
            // http://www.cs.princeton.edu/~rs/talks/LLRB/LLRB.pdf
            return insertRotateLeft().insertRotateRight().insertColorFlip();
        }

        Object retrieve(String key) {
            RBTree ptr = this;
            while (!ptr.isEmpty()) {
                int comp = key.compareTo(ptr.key);
                if (comp == 0) {
                    return ptr.value;
                } else if (comp < 0) {
                    ptr = ptr.left;
                } else {
                    ptr = ptr.right;
                }
            }
            return null;
        }

        int height() {
            return this.left.height() + (isBlack() ? 1 : 0);
        }

        static class EmptyTree extends RBTree {
            EmptyTree() {
                super(null, null);
                this.color = BLACK;
            }

            @Override
            boolean isEmpty() {
                return true;
            }

            @Override
            RBTree insert(String key, Object value) {
                return RBTree.newInstance(key, value);
            }

            @Override
            int height() {
                return 0;
            }
        }

        private RBTree insertRotateLeft() {
            if (this.left.isBlack() && this.right.isRed()) {
                return rotateLeft();
            } else {
                return this;
            }
        }

        private RBTree insertRotateRight() {
            if (this.left.isRed() && this.left.left.isRed()) {
                return rotateRight();
            } else {
                return this;
            }
        }

        private RBTree insertColorFlip() {
            if (this.left.isRed() && this.right.isRed()) {
                return colorFlip();
            } else {
                return this;
            }
        }

        private void swapColor(RBTree other) {
            int tmp = this.color;
            this.color = other.color;
            other.color = tmp;
        }

        /*
         * Right single rotation
         * (b a (D c E)) where D and E are RED --> (d (B a c) E)
         * 
         * b d
         * / \ / \
         * a D -> B E
         * / \ / \
         * c E a c
         */
        private RBTree rotateLeft() {
            RBTree root = this.right;
            this.right = root.left;
            root.left = this;
            root.swapColor(root.left);
            return root;
        }

        /*
         * Left single rotation
         * (d (B A c) e) where A and B are RED --> (b A (D c e))
         * 
         * d b
         * / \ / \
         * B e -> A D
         * / \ / \
         * A c c e
         */
        private RBTree rotateRight() {
            RBTree root = this.left;
            this.left = root.right;
            root.right = this;
            root.swapColor(root.right);
            return root;
        }

        /*
         * Flip colors between red children and black parent
         * (b (A C)) where A and C are RED --> (B (a c))
         * 
         * b B
         * / \ -> / \
         * A C a c
         */
        private RBTree colorFlip() {
            this.left.color = this.right.color = BLACK;
            this.color = RED;
            return this;
        }
    }

    public static RBTreeMap newInstance() {
        return new RBTreeMap();
    }

    private RBTree root;

    RBTreeMap() {
        this.root = RBTree.EMPTY;
    }

    public Object get(String key) {
        return this.root.retrieve(key);
    }

    public void put(String key, Object value) {
        this.root = this.root.insert(key, value);
        this.root.makeAsRoot();
    }

    public int height() {
        return this.root.height();
    }
}
