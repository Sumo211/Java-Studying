package com.leon.algorithm;

import java.util.*;

class InterviewCake {

    /**
     * @see <a href="https://www.interviewcake.com/question/java/product-of-other-numbers">Source</a>
     * Start with a brute force solution, look for 'repeat work' in that solution, and modify it to only do that work once.
     * O(n) time and O(1) space
     */
    int[] getProductsOfAllIntsExceptAtIndex(int[] input) {
        if (input.length < 2) {
            throw new IllegalArgumentException("Getting the product of numbers at other indices requires at least 2 numbers");
        }

        // we make an array with the length of the input array to hold our products
        int[] productsOfAllIntsExceptIndex = new int[input.length];

        // for each integer, we find the product of all the integers before it, storing the total product so far each time
        int productSoFar = 1;
        for (int i = 0; i < input.length; i++) {
            productsOfAllIntsExceptIndex[i] = productSoFar;
            productSoFar *= input[i];
        }

        /* for each integer, we find the product of all the integers after it.
        Since each index in products already has the product of all the integers before it, now we're storing
        the total product of all other integers */
        productSoFar = 1;
        for (int i = input.length - 1; i >= 0; i--) {
            productsOfAllIntsExceptIndex[i] *= productSoFar;
            productSoFar *= input[i];
        }

        return productsOfAllIntsExceptIndex;
    }

    /**
     * @see <a href="https://www.interviewcake.com/question/java/stock-price">Source</a>
     * Suppose we could come up with the answer in one pass through the input, by simply updating the 'best answer so far' as we went.
     * What 'additional values' would we need to keep updated as we looked at each item in our set, in order to be able to update
     * the 'best answer so far' in constant time?
     * O(n) time and O(1) space
     */
    int getMaxProfit(int[] stockPricesYesterday) {
        // make sure we have at least 2 prices
        if (stockPricesYesterday.length < 2) {
            throw new IllegalArgumentException("Getting a profit requires at least 2 prices");
        }

        // we'll greedily update minPrice and maxProfit, so we initialize them to the first price and the first possible profit
        int minPrice = stockPricesYesterday[0];
        int maxProfit = stockPricesYesterday[1] - stockPricesYesterday[0];

        /* start at the second (index 1) time, we can't sell at the first time, since we must buy first, and we can't buy and sell at the same time!
        If we started at index 0, we'd try to buy *and* sell at time 0. This would give a profit of 0, which is a problem if our
        maxProfit is supposed to be *negative*--we'd return 0 */
        for (int i = 1; i < stockPricesYesterday.length; i++) {
            int currentPrice = stockPricesYesterday[i];

            // see what our profit would be if we bought at the min price and sold at the current price
            int potentialProfit = currentPrice - minPrice;

            // update maxProfit if we can do better
            maxProfit = Math.max(maxProfit, potentialProfit);

            // update minPrice so it's always the lowest price we've seen so far
            minPrice = Math.min(minPrice, currentPrice);
        }

        return maxProfit;
    }

    /**
     * @see <a href="https://www.interviewcake.com/question/java/highest-product-of-3">Source</a>
     * Does your method work with negative numbers?
     * A great way to get O(n) runtime is to use a greedy approach. How can we keep track of the highestProductOf3 "so far" as we do one walk through the array?
     * Greedy algorithm design is a big part of that 'way of thinking'.
     * O(n) time and O(1) space
     */
    int highestProductOf3(int[] input) {
        if (input.length < 3) {
            throw new IllegalArgumentException("Less than 3 items!");
        }

        /* we're going to start at the 3rd item (at index 2), so pre-populate highests and lowests based on the first 2 items
        We could also start these as null and check below if they're set but this is arguably cleaner */
        int highest = Math.max(input[0], input[1]);
        int lowest = Math.min(input[0], input[1]);

        int highestProductOf2 = input[0] * input[1];
        int lowestProductOf2 = input[0] * input[1];

        // except this one -- we pre-populate it for the first *3* items. This means in our first pass it'll check against itself, which is fine
        int highestProductOf3 = input[0] * input[1] * input[2];

        // walk through items, starting at index 2
        for (int i = 2; i < input.length; i++) {
            int current = input[i];

            // do we have a new highest product of 3? It's either the current highest, or the current times the highest product of two or the current times the lowest product of two
            highestProductOf3 = Math.max(Math.max(highestProductOf3, highestProductOf2 * current), lowestProductOf2 * current);

            // do we have a new highest product of two?
            highestProductOf2 = Math.max(Math.max(highestProductOf2, highest * current), lowest * current);

            // do we have a new lowest product of two?
            lowestProductOf2 = Math.min(Math.min(lowestProductOf2, highest * current), lowest * current);

            // do we have a new highest?
            highest = Math.max(highest, current);

            // do we have a new lowest?
            lowest = Math.min(lowest, current);
        }

        return highestProductOf3;
    }

    /**
     * @see <a href="https://www.interviewcake.com/question/java/find-duplicate-optimize-for-space">Source</a>
     * We got there by reasoning about the expected runtime:
     * 1. We started with an O(n^2) 'brute force' solution and wondered if we could do better.
     * 2. We knew to beat O(n^2) we'd probably do O(n) or O(nlgn), so we started thinking of ways we might get an O(nlgn) runtime.
     * 3. lg(n) usually comes from iteratively cutting stuff in half, so we arrived at the final algorithm by exploring that idea.
     * O(nlgn) time and O(1) space
     */
    int findFirstRepeatedNumber(int[] input) {
        int floor = 1;
        int ceiling = input.length - 1;

        while (floor < ceiling) {
            /* divide our range 1..n into an upper range and lower range (such that they don't overlap)
            lower range is floor..midpoint, upper range is midpoint+1..ceiling */
            int midPoint = floor + ((ceiling - floor) / 2);
            int lowerRangeFloor = floor;
            int lowerRangeCeiling = midPoint;
            int upperRangeFloor = midPoint + 1;
            int upperRangeCeiling = ceiling;

            // count number of items in lower range
            int itemsInLowerRange = 0;
            for (int item : input) {
                // is it in the lower range?
                if (item >= lowerRangeFloor && item <= lowerRangeCeiling) {
                    itemsInLowerRange++;
                }
            }

            int distinctPossibleIntegersInLowerRange = lowerRangeCeiling - lowerRangeFloor + 1;
            if (itemsInLowerRange > distinctPossibleIntegersInLowerRange) {
                // there must be a duplicate in the lower range so use the same approach iteratively on that range
                floor = lowerRangeFloor;
                ceiling = lowerRangeCeiling;
            } else {
                // there must be a duplicate in the upper range so use the same approach iteratively on that range
                floor = upperRangeFloor;
                ceiling = upperRangeCeiling;
            }
        }

        // floor and ceiling have converged, we found a number that repeats!
        return floor;
    }

    // TODO: 11/29/2017 Write unit tests
    /**
     * @see <a href="https://www.interviewcake.com/question/java/mesh-message">Source</a>
     * How do we find the shortest path from a start node to an end node in an unweighted, undirected graph?
     * In general, it's helpful to think of backtracking as two steps:
     * 1. Figuring out 'what additional information' we need to store in order to rebuild our path at the end (howWeReachedNodes, in this case).
     * 2. Figuring out how to to reconstruct the path from that information.
     * O(N + M) time where N is the number of users and M is the number of connections between them and O(N) space
     */
    String[] shortestPathUsingBFS(Map<String, String[]> graph, String startNode, String endNode) {
        if (!graph.containsKey(startNode)) {
            throw new IllegalArgumentException("Start node is not in graph");
        }

        if (!graph.containsKey(endNode)) {
            throw new IllegalArgumentException("End node is not in graph");
        }

        /* we're using an ArrayDeque instead of an ArrayList because we want an efficient first-in-first-out (FIFO) structure with O(1) inserts and removes.
        If we used an ArrayList, appending would be O(1), but removing elements from the front would be O(n) */
        Queue<String> nodesToVisit = new ArrayDeque<>();
        nodesToVisit.add(startNode);

        /* keep track of how we got to each node
        we'll use this to reconstruct the shortest path at the end, we'll ALSO use this to keep track of which nodes we've already visited */
        Map<String, String> howWeReachedNodes = new HashMap<>();
        howWeReachedNodes.put(startNode, null);

        while (!nodesToVisit.isEmpty()) {
            String currentNode = nodesToVisit.remove();

            // stop when we reach the end node
            if (currentNode.equals(endNode)) {
                return reconstructPath(howWeReachedNodes, startNode, endNode);
            }

            for (String neighbour : graph.get(currentNode)) {
                if (!howWeReachedNodes.containsKey(neighbour)) {
                    nodesToVisit.add(neighbour);
                    howWeReachedNodes.put(neighbour, currentNode);
                }
            }
        }

        // if we get here, then we never found the end node so there's NO path from start node to end node
        return null;
    }

    private String[] reconstructPath(Map<String, String> previousNode, String startNode, String endNode) {
        List<String> reverseShortestPath = new ArrayList<>();

        // start from the end of the path and work backwards
        String currentNode = endNode;

        while (currentNode != null) {
            reverseShortestPath.add(currentNode);
            currentNode = previousNode.get(currentNode);
        }

        // reserve our path to get the right order by flipping it around, in place
        Collections.reverse(reverseShortestPath);
        return reverseShortestPath.toArray(new String[reverseShortestPath.size()]);
    }

    /**
     * @see <a href="https://www.interviewcake.com/question/java/inflight-entertainment">Source</a>
     * Are you sure your method won’t give a false positive if the array has one element that is half flightLength?
     * What data structure gives us convenient constant-time lookups?
     * Using hash-based data structures, like hash maps or hash sets, is 'so common' in coding challenge solutions, it should always be your first thought.
     * O(n) time and O(n) space
     */
    boolean canTwoMoviesFillFlight(int[] moviesLength, int flightLength) {
        // movie lengths we've seen so far
        Set<Integer> moviesLengthSeen = new HashSet<>();

        for (int firstMovieLength : moviesLength) {
            int matchingSecondMovieLength = flightLength - firstMovieLength;
            if (moviesLengthSeen.contains(matchingSecondMovieLength)) {
                return true;
            }

            moviesLengthSeen.add(firstMovieLength);
        }

        // we never found a match, so return false
        return false;
    }

    // FIXME: 12/12/2017 Write unit test with the input is {"d", "x", "a", "e", "i", "n", "v", "s"}
    /**
     * @see <a href="https://www.interviewcake.com/question/java/find-rotation-point">Source</a>
     * How can we fix our method to return 0 for an unrotated array?
     * Binary search teaches us that when an array is sorted or mostly sorted:
     * 1. The value at a given index tells us a lot about what's to the left and what's to the right.
     * 2. We don't have to look at every item in the array. By inspecting the middle item, we can 'rule out' half of the array.
     * 3. We can use this approach over and over, cutting the problem in half until we have the answer. This is sometimes called 'divide and conquer'.
     * O(lgn) time and O(1) space
     */
    int findRotationPoint(String[] words) {
        final String firstWord = words[0];

        int floorIndex = 0;
        int ceilingIndex = words.length - 1;

        while (floorIndex < ceilingIndex) {
            // guess a point halfway between floor and ceiling
            int guessIndex = floorIndex + ((ceilingIndex - floorIndex) / 2);

            // if guess comes after first word or is the first word
            if (words[guessIndex].compareTo(firstWord) >= 0) {
                // go right
                floorIndex = guessIndex;
            } else {
                // go left
                ceilingIndex = guessIndex;
            }

            // if floor and ceiling have converged
            if (floorIndex + 1 == ceilingIndex) {
                // between floor and ceiling is where we flipped to the beginning so ceiling is alphabetically first
                break;
            }
        }

        return ceilingIndex;
    }

    /**
     * @see <a href="https://www.interviewcake.com/question/java/compress-url-list">Source</a>
     * For all possible URLs of length n or fewer, our total storage space is: O(n26^n).
     * With trie, for all URLs of length n or fewer, we have O(26^n).
     * We started with a strategy for compressing a common prefix ("www") and then we asked ourselves, "How can we take this 'idea' even further?"
     * That gave us the idea to treat each character as a common prefix.
     * Starting with a small optimization and asking, "How can we take this same 'idea' even further?"—is hugely powerful.
     */
    static class TrieNode {

        private Map<Character, TrieNode> nodeChildren;

        TrieNode() {
            this.nodeChildren = new HashMap<>();
        }

        public boolean hasChildNode(char character) {
            return this.nodeChildren.containsKey(character);
        }

        public void makeChildNode(char character) {
            this.nodeChildren.put(character, new TrieNode());
        }

        public TrieNode getChildNode(char character) {
            return this.nodeChildren.get(character);
        }

    }

    static class Trie {

        private TrieNode rootNode;

        private final char endOfWordMaker = '\0';

        Trie() {
            this.rootNode = new TrieNode();
        }

        public boolean checkPresentAndAdd(String word) {
            TrieNode currentNode = rootNode;
            boolean isNewWord = false;

            // work downwards through the trie, adding nodes as needed, and keeping track of whether we add any nodes.
            for (char character : word.toCharArray()) {
                if (!currentNode.hasChildNode(character)) {
                    isNewWord = true;
                    currentNode.makeChildNode(character);
                }

                currentNode = currentNode.getChildNode(character);
            }

            // explicitly mark the end of a word. Otherwise, we might say a word is present if it is a prefix of a different, longer word that was added earlier.
            if (!currentNode.hasChildNode(endOfWordMaker)) {
                isNewWord = true;
                currentNode.makeChildNode(endOfWordMaker);
            }

            return isNewWord;
        }

    }

    /**
     * @see <a href="https://www.interviewcake.com/question/java/reverse-linked-list">Source</a>
     * Even the 'right approach' will fail if done in the 'wrong order'.
     * Write out a sample linked list and walk through your code by hand, step by step,
     * running each operation on your sample input to see if the final output is what you expect.
     * O(n) time and O(1) space
     */
    static class LinkedListNode {

        private int value;

        private LinkedListNode next;

    }

    // TODO: 12/28/2017 Write unit tests
    LinkedListNode reverse(LinkedListNode headOfList) {
        LinkedListNode currentNode = headOfList;
        LinkedListNode previousNode = null;
        LinkedListNode nextNode = null;

        // until we have 'fallen off' the end of the list
        while (currentNode != null) {
            // copy a pointer to the next element before we overwrite current.next
            nextNode = currentNode.next;

            // reverse the 'next' pointer
            currentNode.next = previousNode;

            // step forward in the list
            previousNode = currentNode;
            currentNode = nextNode;
        }

        return previousNode;
    }

    /**
     * @see <a href="https://www.interviewcake.com/question/java/rectangular-love">Source</a>
     * What if there is no intersection?
     * What if one rectangle is entirely contained in the other?
     * What if the rectangles don't really intersect but share an edge?
     * Do some parts of your method seem very similar? Can they be refactored so you repeat yourself less?
     * The hard part isn't the time or space optimization—it's getting something that works and is readable.
     * To keep your thoughts clear and avoid bugs, take time to:
     * 1. Think up and draw out all the possible cases.
     * 2. Use very specific and descriptive variable names.
     * O(1) time and O(1) space
     */
    static class Rectangle {

        // coordinates of bottom left corner
        private int leftX;

        private int bottomY;

        // dimensions
        private int width;

        private int height;

        Rectangle(int leftX, int bottomY, int width, int height) {
            this.leftX = leftX;
            this.bottomY = bottomY;
            this.width = width;
            this.height = height;
        }

        int getLeftX() {
            return leftX;
        }

        int getBottomY() {
            return bottomY;
        }

        int getWidth() {
            return width;
        }

        int getHeight() {
            return height;
        }

        private static RangeOverlap findRangeOverlap(int point1, int length1, int point2, int length2) {
            // find the highest start point and lowest end point
            // the highest ("rightmost" or "upmost") start point is the start point of the overlap
            // the lowest end point is the end point of the overlap
            int highestStartPoint = Math.max(point1, point2);
            int lowestEndPoint = Math.min(point1 + length1, point2 + length2);

            // return empty overlap if there is no overlap
            if (highestStartPoint >= lowestEndPoint) {
                return new RangeOverlap(0, 0);
            }

            // compute the overlap length
            int overlapLength = lowestEndPoint - highestStartPoint;

            return new RangeOverlap(highestStartPoint, overlapLength);
        }

        static Rectangle findRectangleOverlap(Rectangle rec1, Rectangle rec2) {
            // get the x and y overlap points and lengths
            RangeOverlap xOverlap = findRangeOverlap(rec1.getLeftX(), rec1.getWidth(), rec2.getLeftX(), rec2.getWidth());
            RangeOverlap yOverlap = findRangeOverlap(rec1.getBottomY(), rec1.getHeight(), rec2.getBottomY(), rec2.getHeight());

            // return "zero" rectangle if there is no overlap
            if (xOverlap.getLength() == 0 || yOverlap.getLength() == 0) {
                return new Rectangle(0, 0, 0, 0);
            }

            return new Rectangle(xOverlap.getStartPoint(), yOverlap.getStartPoint(), xOverlap.getLength(), yOverlap.getLength());
        }

        static class RangeOverlap {

            private int startPoint;

            private int length;

            RangeOverlap(int startPoint, int length) {
                this.startPoint = startPoint;
                this.length = length;
            }

            int getStartPoint() {
                return startPoint;
            }

            int getLength() {
                return length;
            }

        }

    }

    // TODO: 1/24/2018 Write unit tests
    /**
     * @see <a href="https://www.interviewcake.com/question/java/nth-fibonacci">Source</a>
     * The tradeoff we sometimes have between code cleanliness and efficiency.
     * Recursive approach <> Iterative approach (Top-down <> Bottom-up)
     * In general, whenever you have a recursive solution to a problem, think about what's actually happening on the call stack.
     * An iterative solution might be more efficient.
     * O(n) time and O(1) space
     */
    int computeNthFib(int n) {
        // TODO: 1/24/2018 self-implemented
        return 0;
    }

    /**
     * @see <a href="https://www.interviewcake.com/question/java/find-unique-int-among-duplicates">Source</a>
     * The power we can unlock by knowing what's happening at the 'bit level'.
     * How do you know when bit manipulation might be the key to solving a problem? Here are some signs to watch out for:
     * 1. You want to multiply or divide by 2 (use a left shift to multiply by 2, right shift to divide by 2).
     * 2. You want to 'cancel out' matching numbers (use XOR).
     * O(n) time and O(1) space
     */
    int findUniqueDeliveryId(int[] deliveryIds) {
        int uniqueDeliveryId = 0;

        for (int index : deliveryIds) {
            uniqueDeliveryId ^= index;
        }

        return uniqueDeliveryId;
    }

}
