
/* NetId(s):pjf73

 * Name(s): Parker Feinerman
 * What I thought about this assignment:
 * I thought the assignment was fine and interesting,
 * but I felt like there weren't adequate explanations
 * for things and that I had to search through piazza
 * to find things that I think should've been in the
 * assignment handout.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/** An instance of BugTree represents the spread of a Bug among a Network of people. <br>
 * In this model, each person can "catch" the bug from only a single person. <br>
 * The root of the BugTree is the person who first got the bug. <br>
 * From there, each person in the BugTree is the child of the person who gave <br>
 * them the bug. For example, for the tree:
 *
 * <pre>
 *       A
 *      / \
 *     B   C
 *        / \
 *       D   E
 * </pre>
 *
 * Person A originally got the bug, B and C caught the bug from A, and<br>
 * D and E caught the bug from C.
 *
 * Important note: The name of each person in the bug tree is unique. */
public class BugTree {

	/** Replace "-1" by the time you spent on A2 in hours.<br>
	 * Example: for 3 hours 15 minutes, use 3.25<br>
	 * Example: for 4 hours 30 minutes, use 4.50<br>
	 * Example: for 5 hours, use 5 or 5.0 */
	public static double timeSpent= 7.5;

	/** The String to be used as a separator in toString() */
	public static final String SEPARATOR= " - ";

	/** The String that marks the start of children in toString() */
	public static final String START_CHILDREN_DELIMITER= "[";

	/** The String that divides children in toString() */
	public static final String DELIMITER= ", ";

	/** The String that marks the end of children in toString() */
	public static final String END_CHILDREN_DELIMITER= "]";

	/** The String that is the space increment in toStringVerbose() */
	public static final String VERBOSE_SPACE_INCREMENT= "\t";

	/** The person at the root of this BugTree. <br>
	 * This is the bug ancestor of everyone in this BugTree: <br>
	 * the person who got sick first and indirectly caused everyone in it to get sick. <br>
	 * root is non-null. <br>
	 * All Person's in a BugTree have different names. There are no duplicates */
	private Person root;

	/** The children of this BugTree node. <br>
	 * Each element of children got the bug from the person at this node. <br>
	 * root is non-null but will be an empty set if this is a leaf. */
	private Set<BugTree> children;

	/** Constructor: a new BugTree with root p and no children. <br>
	 * Throw an IllegalArgumentException if p is null. */
	public BugTree(Person p) throws IllegalArgumentException {
		if (p == null)
			throw new IllegalArgumentException("Can't construct BugTree with null root");
		root= p;
		children= new HashSet<>();
	}

	/** Constructor: a new BugTree that is a copy of tree p. <br>
	 * Tree p and its copy have no node in common (but nodes can share a Person). <br>
	 * Throw an IllegalArgumentException if p is null. */
	public BugTree(BugTree p) throws IllegalArgumentException {
		if (p == null)
			throw new IllegalArgumentException("Can't construct copy of null BugTree");
		root= p.root;
		children= new HashSet<>();

		for (BugTree dt : p.children) {
			children.add(new BugTree(dt));
		}
	}

	/** = the person that is at the root of this BugTree. */
	public Person getRoot() {
		return root;
	}

	/** = the number of direct children of this BugTree. */
	public int numberOfChildren() {
		return children.size();
	}

	/** = a COPY of the set of children of this BugTree. */
	public Set<BugTree> copyOfChildren() {
		return new HashSet<>(children);
	}

	/** = the BugTree object in this tree whose root is p. <br>
	 * (null if p is not in this tree). */
	public BugTree getTree(Person p) {
		if (root == p) return this; // Base case

		// Recursive case - ask children to look
		for (BugTree dt : children) {
			BugTree node= dt.getTree(p);
			if (node != null) return node;
		}

		return null; // p is not in the tree
	}

	/** Add c to this BugTree as a child of p and return the BugTree <br>
	 * whose root is the new child. <br>
	 * Throw an IllegalArgumentException if:<br>
	 * -- p or c is null,<br>
	 * -- c is already in this BugTree, or<br>
	 * -- p is not in this BugTree<br>
	 *** There is a simple, non-recursive implementation of this function */
	public BugTree insert(Person p, Person c) throws IllegalArgumentException {
		// TODO 1
		// This method need not be recursive. You should use method getTree, above.
		// and no methods that are below.
		// DO NOT traverse the tree twice looking for the same node
		// ---don't duplicate work.
		if (p == null || c == null)
			throw new IllegalArgumentException("Persons cannot be null");
		BugTree a= this.getTree(c);
		BugTree b= this.getTree(p);
		if (a != null) throw new IllegalArgumentException("Person cannot already be in this tree");
		if (b == null) throw new IllegalArgumentException("Person must be in this tree");
		BugTree x= new BugTree(c);
		b.children.add(x);
		return x;
	}

	/** = the number of persons in this BugTree. <br>
	 * Note: If this is a leaf, the size is 1 (just the root) */
	public int size() {
		// TODO 2
		if (numberOfChildren() == 0) return 1;
		int y= 1;
		for (BugTree x : children) {
			y= y + x.size();
		}
		return y;
	}

	/** = "this BugTree contains p." */
	public boolean contains(Person p) {
		// TODO 3
		/* Note: This BugTree contains p iff the root of this BugTree is
		 * p or if one of p's children contains p. */
		if (this.getTree(p) == null) return false;
		return true;
		/*
		if (root == p) return true;
		boolean y= false;
		for (BugTree x : children) {
			y= y || x.contains(p);
		}
		return y;
		*/
	}

	/*** Return the depth at which p occurs in this BugTree, or <br>
	 * -1 if p is not in the BugTree. <br>
	 * Note: depthOf(root) is 0. <br>
	 * If p is a child of this BugTree, then depth(p) is 1. etc. */
	public int depthOf(Person p) {
		// TODO 4
		// Note: Do NOT call function contains(p) to find out whether p is in
		// the tree. The recursive case consists of looking for the depth of p
		// in each child. If looking in one child finds the depth, just return
		// the answer, thus terminating execution of the method.
		// If checking each child recursively doesn't find that p is in the tree,
		// return -1 at the end of the method.
		if (root == p) return 0;
		for (BugTree x : children) {
			int y= 1;
			y= y + x.depthOf(p);
			if (y >= 1) return y;
		}
		return -1;
	}

	/** Return the width of this tree at depth d <br>
	 * (i.e. the number of bugTrees that occur at depth d).<br>
	 * Remember: the depth of the root is 0; the depth of its children is 1, etc.<br>
	 * Throw an IllegalArgumentException if d < 0.<br>
	 * Thus, for the following tree : Depth level: 0.<br>
	 *
	 * <pre>
	  * Depth  level:
	 *  0         A
	 *           / \
	 *  1       B   C
	 *         /   / \
	 *  2     D   E   F
	 *             \
	 *  3           G
	 * </pre>
	 *
	 * A.widthAtDepth(0) = 1, A.widthAtDepth(1) = 2,<br>
	 * A.widthAtDepth(2) = 3, A.widthAtDepth(3) = 1,<br>
	 * A.widthAtDepth(4) = 0.<br>
	 * C.widthAtDepth(0) = 1, C.widthAtDepth(1) = 2 */
	public int widthAtDepth(int d) throws IllegalArgumentException {
		// TODO 5
		// Hint: Use this recursive definition. If d = 0, the answer is 1.
		// If d > 0, the answer is: sum of widths of the children at depth d-1.

		if (d < 0) throw new IllegalArgumentException("Depth must be greater than 0");
		if (d == 0) return 1;
		int y= 0;
		for (BugTree x : children) {
			y= y + x.widthAtDepth(d - 1);
		}
		return y;
	}

	/** Return the route the bug took to get from "here" (the root of <br>
	 * this BugTree) to descendant c. If there is no such route, return null.<br>
	 * For example, for this tree:
	 *
	 * <pre>
	 * Depth:
	 *    0           A
	 *               / \
	 *    1         B   C
	 *             /   / \
	 *    2       D   E   F
	 *             \
	 *    3         G
	 * </pre>
	 *
	 * A.bugRouteTo(E) should return a list of [A,C,E].<br>
	 * A.bugRouteTo(A) should return [A]. <br>
	 * A.bugRouteTo(X) should return null.<br>
	 * B.bugRouteTo(C) should return null. <br>
	 * B.bugRouteTo(D) should return [B,D] */
	public List<Person> bugRouteTo(Person c) {
		// TODO 6
		// Note: You have to return a List<Person>. But List is an
		// interface, so use something that implements it.
		// LinkedList<Person> is preferred to ArrayList<Person>, because
		// prepend (or its equivalent) may have to be used.
		// Base Case: The root of this BugTree is c. Route is just [c].

		if (getRoot() == c) {
			LinkedList<Person> r= new LinkedList<>();
			r.add(c);
			return r;
		}
		for (BugTree x : children) {
			LinkedList<Person> y= (LinkedList<Person>) x.bugRouteTo(c);
			if (y != null) {
				y.addFirst(getRoot());
				return y;
			}
		}
		return null;
	}

	/** If either child1 or child2 is null or is not in this BugTree, return null.<br>
	 * Otherwise, return the person at the root of the smallest subtree of this<br>
	 * BugTree that contains child1 and child2.<br>
	 *
	 * Examples. For the following tree (which does not contain H):
	 *
	 * <pre>
	 * Depth:
	 *    0      A
	 *          / \
	 *    1    B   C
	 *        /   / \
	 *    2  D   E   F
	 *        \
	 *    3    G
	 * </pre>
	 *
	 * A.sharedAncestorOf(B, A) is A<br>
	 * A.sharedAncestorOf(B, B) is B<br>
	 * A.sharedAncestorOf(B, C) is A<br>
	 * A.sharedAncestorOf(A, C) is A<br>
	 * A.sharedAncestorOf(E, F) is C<br>
	 * A.sharedAncestorOf(G, F) is A<br>
	 * B.sharedAncestorOf(B, E) is null<br>
	 * B.sharedAncestorOf(B, A) is null<br>
	 * B.sharedAncestorOf(D, F) is null<br>
	 * B.sharedAncestorOf(D, H) is null<br>
	 * A.sharedAncestorOf(null, C) is null */
	public Person sharedAncestorOf(Person child1, Person child2) {
		// TODO 7
		/* RESTRICTION: Do not use method getParent(), which is far below.
		 * Its use over and over again is inefficient. Instead, find the
		 * bug routes l1 and l2 to the two children. If they are not null,
		 * then the answer l1[i] is the largest i such that l1[0..i] = l2[0..i].
		 * If this is not clear, draw an example. This, then, can be found using
		 * a loop. No recursion is needed.
		 *
		 * Now you have a problem of writing this loop efficiently. You can't
		 * use a foreach loop on both of them simultaneously. The simplest thing
		 * to do is to use List's function toArray and then work with the
		 * array representation of the two lists.
		 */

		if (child1 == null || child2 == null) return null;
		if (getTree(child1) == null || getTree(child2) == null) return null;
		if (root == child1) return child1;
		if (root == child2) return child2;
		int i= Math.min(bugRouteTo(child1).size(), bugRouteTo(child2).size());
		Person[] a= bugRouteTo(child1).toArray(new Person[i]);
		Person[] b= bugRouteTo(child2).toArray(new Person[i]);
		for (int x= i - 1; x > 0; x-- ) {
			if (a[x] == b[x]) return a[x];
			sharedAncestorOf(a[x], b[x - 1]);
		}
		return null;
	}

	/** Return true iff this is equal to ob.<br>
	 * 1. If this and ob are not of the same class, they are not equal, so return false.<br>
	 * 2. Two BugTrees are equal if<br>
	 * -- (1) they have the same root Person object (==) AND<br>
	 * -- (2) their children sets are the same size AND<br>
	 * -- (3) their children sets are equal.<br>
	 * -- Since their sizes are equal, this requires:<br>
	 * -- for every BugTree dt1 in one set there is a BugTree<br>
	 * -- dt2 in the other set for which dt1.equals(dt2) is true.<br>
	 *
	 * -- Otherwise the two BugTrees are not equal.<br>
	 * Do not use any of the toString functions to write equals(). Do not use Set's function equals. */
	@Override
	public boolean equals(Object ob) {
		// TODO 8
		// Hint about checking whether each child of one tree equals SOME
		// tree of the other tree's children.
		// First, you have to check them all until you find an equal one (or
		// return false if you don't.)
		// Second, A child of one tree cannot equal more than one child of
		// tree because the names of Person's are all unique;
		// there are no duplicates.

		if (getClass() != ob.getClass()) return false;
		BugTree a= (BugTree) ob;
		if (root != a.getRoot()) return false;
		if (numberOfChildren() != a.numberOfChildren()) return false;
		for (BugTree x : children) {
			if (!help(x, a.copyOfChildren())) return false;
		}
		return true;

	}

	private boolean help(BugTree st, Set<BugTree> s2) {
		for (BugTree x : s2) {
			if (st.equals(x)) return true;
		}
		return false;
	}

	/* ========================================================================
	 * ========================================================================
	 * ========================================================================
	 * Do not use the methods written below. They are used to calculate data
	 * for the GUI.
	 * Feel free to read/study them. */

	/** Return the maximum depth of this BugTree, <br>
	 * i.e. the longest path from the root to a leaf.<br>
	 * Example. If this BugTree is a leaf, return 0. */
	public int maxDepth() {
		int maxDepth= 0;
		for (BugTree dt : children) {
			maxDepth= Math.max(maxDepth, dt.maxDepth() + 1);
		}
		return maxDepth;
	}

	/** Return the immediate parent of c (null if c is not in this BugTree).<br>
	 * Thus, for the following tree:
	 *
	 * <pre>
	 * Depth:
	 *    0      A
	 *          / \
	 *    1    B   C
	 *        /   / \
	 *    2  D   E   F
	 *        \
	 *    3    G
	 * </pre>
	 *
	 * A.getParent(E) returns C.<br>
	 * C.getParent(E) returns C.<br>
	 * A.getParent(B) returns A.<br>
	 * E.getParent(F) returns null. */
	public Person getParent(Person c) {
		// Base case
		for (BugTree dt : children) {
			if (dt.root == c) return root;
		}

		// Recursive case - ask children to look
		for (BugTree dt : children) {
			Person parent= dt.getParent(c);
			if (parent != null) return parent;
		}

		return null; // Not found
	}

	/** Return the maximum width of all the widths in this tree, <br>
	 * i.e. the maximum value that could be returned from widthAtDepth for this tree. */
	public int maxWidth() {
		return maxWidthImplementationTwo(this);
	}

	/** Simple implementation of maxWidth. <br>
	 * Relies on widthAtDepth. <br>
	 * Takes time proportional to the square of the size of the t. */
	static int maxWidthImplementationOne(BugTree t) {
		int width= 0;
		int depth= t.maxDepth();
		for (int i= 0; i <= depth; i++ ) {
			width= Math.max(width, t.widthAtDepth(i));
		}
		return width;
	}

	/** Better implementation of maxWidth. Caches results in an array. <br>
	 * Takes time proportional to the size of t. */
	static int maxWidthImplementationTwo(BugTree t) {
		// For each integer d, 0 <= d <= maximum depth of t, store in
		// widths[d] the number of nodes at depth d in t.
		// The calculation is done by calling recursive procedure addToWidths.
		int[] widths= new int[t.maxDepth() + 1];   // initially, contains 0's
		t.addToWidths(0, widths);

		int max= 0;
		for (int width : widths) {
			max= Math.max(max, width);
		}
		return max;
	}

	/** For each node of this BugTree, which is at some depth d in this BugTree,<br>
	 * add 1 to widths[depth + d]. */
	private void addToWidths(int depth, int[] widths) {
		widths[depth]++ ;        // the root of this BugTree is at depth d = 0
		for (BugTree dt : children) {
			dt.addToWidths(depth + 1, widths);
		}
	}

	/** Better implementation of maxWidth. Caches results in a HashMap. <br>
	 * Takes time proportional to the size of t. */
	static int maxWidthImplementationThree(BugTree t) {
		// For each possible depth d >= 0 in tree t, widthMap will contain the
		// entry (d, number of nodes at depth d in t). The calculation is
		// done using recursive procedure addToWidthMap.

		// For each integer d, 0 <= d <= maximum depth of t, add to
		// widthMap an entry <d, 0>.
		HashMap<Integer, Integer> widthMap= new HashMap<>();
		for (int d= 0; d <= t.maxDepth() + 1; d++ ) {
			widthMap.put(d, 0);
		}

		t.addToWidthMap(0, widthMap);

		int max= 0;
		for (Integer w : widthMap.values()) {
			max= Math.max(max, w);
		}
		return max;
	}

	/** For each node of this BugTree, which is at some depth d in this BugTree,<br>
	 * add 1 to the value part of entry <depth + d, ...> of widthMap. */
	private void addToWidthMap(int depth, HashMap<Integer, Integer> widthMap) {
		widthMap.put(depth, widthMap.get(depth) + 1);  // the root is at depth d = 0
		for (BugTree dt : children) {
			dt.addToWidthMap(depth + 1, widthMap);
		}
	}

	/** Return a (single line) String representation of this BugTree.<br>
	 * If this BugTree has no children (it is a leaf), return the root's substring.<br>
	 * Otherwise, return<br>
	 * ... root's substring + SEPARATOR + START_CHILDREN_DELIMITER + each child's<br>
	 * ... toString, separated by DELIMITER, followed by END_CHILD_DELIMITER.<br>
	 *
	 * Make sure there is not an extra DELIMITER following the last child.<br>
	 *
	 * Finally, make sure to use the static final fields declared at the top of<br>
	 * BugTree.java.<br>
	 *
	 * Thus, for the following tree:
	 *
	 * <pre>
	 * Depth level:
	 *   0         A
	 *            / \
	 *   1        B  C
	 *           /  / \
	 *   2      D  E   F
	 *           \
	 *   3        G
	 *
	 * A.toString() should print:
	 * (A) - HEALTHY - [(C) - HEALTHY - [(F) - HEALTHY, (E) - HEALTHY - [(G) - HEALTHY]], (B) - HEALTHY - [(D) - HEALTHY]]
	 *
	 * C.toString() should print:
	 * (C) - HEALTHY - [(F) - HEALTHY, (E) - HEALTHY - [(G) - HEALTHY]]
	 * </pre>
	 */
	@Override
	public String toString() {
		if (children.isEmpty()) return root.toString();
		String s= root.toString() + SEPARATOR + START_CHILDREN_DELIMITER;
		for (BugTree dt : children) {
			s= s + dt.toString() + DELIMITER;
		}
		return s.substring(0, s.length() - 2) + END_CHILDREN_DELIMITER;
	}

	/** Return a verbose (multi-line) string representing this BugTree. */
	public String toStringVerbose() {
		return toStringVerbose(0);
	}

	/** Return a verbose (multi-line) string representing this BugTree.<br>
	 * Each human in the tree is on its own line, with indentation representing<br>
	 * what each human is a child of.<br>
	 * indent is the the amount of indentation to put before this line.<br>
	 * Should increase on recursive calls to children to create the above pattern.<br>
	 * Thus, for the following tree:
	 *
	 * <pre>
	 * Depth level:
	 *   0         A
	 *            / \
	 *   1       B   C
	 *        /   / \
	 *   2     D   E   F
	 *        \
	 *   3       G
	 *
	 * A.toStringVerbose(0) should return:
	 * (A) - HEALTHY
	 * (C) - HEALTHY
	 * (F) - HEALTHY
	 * (E) - HEALTHY
	 * (G) - HEALTHY
	 * (B) - HEALTHY
	 * (D) - HEALTHY
	 * </pre>
	 *
	 * Make sure to use VERBOSE_SPACE_INCREMENT for indentation. */
	private String toStringVerbose(int indent) {
		String s= "";
		for (int i= 0; i < indent; i++ ) {
			s= s + VERBOSE_SPACE_INCREMENT;
		}
		s= s + root.toString();

		if (children.isEmpty()) return s;

		for (BugTree dt : children) {
			s= s + "\n" + dt.toStringVerbose(indent + 1);
		}
		return s;
	}

}
