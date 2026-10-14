import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Run with ./grade_lab05.sh after implementing Lab05.java. */
public class Lab05Tester {
    private interface Check {
        void run();
    }

    private static final class TestCase {
        final String name;
        final int points;
        final Check check;

        TestCase(String name, int points, Check check) {
            this.name = name;
            this.points = points;
            this.check = check;
        }
    }

    private static final class TestFailure extends AssertionError {
        TestFailure(String operation, Object expected, Object actual) {
            super(operation + " | expected: " + expected + " | actual: " + actual);
        }
    }

    private static void expect(String operation, Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new TestFailure(operation, expected, actual);
        }
    }

    private static void add(List<TestCase> tests, String name, int points, Check check) {
        tests.add(new TestCase(name, points, check));
    }

    private static List<TestCase> tests() {
        List<TestCase> tests = new ArrayList<TestCase>();

        add(tests, "test_LinkedStack_pop_change_state_of_stack", 4, () -> {
            LinkedStack<String> stack = new LinkedStack<String>();
            stack.push("first");
            stack.push("second");
            stack.pop();
            expect("size after pop", 1, stack.size());
            expect("top after pop", "first", stack.top());
            expect("isEmpty after pop", false, stack.isEmpty());
            stack.pop();
            expect("size after last pop", 0, stack.size());
            expect("isEmpty after last pop", true, stack.isEmpty());
        });

        add(tests, "test_ArrayStack_initial_state", 3, () -> {
            ArrayStack<Integer> stack = new ArrayStack<Integer>();
            expect("new stack size", 0, stack.size());
            expect("new stack isEmpty", true, stack.isEmpty());
        });

        add(tests, "test_LinkedStack_initial_state", 3, () -> {
            LinkedStack<Integer> stack = new LinkedStack<Integer>();
            expect("new stack size", 0, stack.size());
            expect("new stack isEmpty", true, stack.isEmpty());
        });

        add(tests, "test_LinkedStack_pop_return_null", 3, () -> {
            LinkedStack<Integer> stack = new LinkedStack<Integer>();
            expect("pop on empty stack", null, stack.pop());
        });

        add(tests, "test_ArrayStack_pop_change_state_of_stack", 4, () -> {
            ArrayStack<String> stack = new ArrayStack<String>();
            stack.push("first");
            stack.push("second");
            stack.pop();
            expect("size after pop", 1, stack.size());
            expect("top after pop", "first", stack.top());
            expect("isEmpty after pop", false, stack.isEmpty());
            stack.pop();
            expect("size after last pop", 0, stack.size());
            expect("isEmpty after last pop", true, stack.isEmpty());
        });

        add(tests, "test_LinkedQueue_dequeue_return_value", 4, () -> {
            LinkedQueue<String> queue = new LinkedQueue<String>();
            queue.enqueue("first");
            queue.enqueue("second");
            expect("first dequeue (FIFO)", "first", queue.dequeue());
            expect("second dequeue (FIFO)", "second", queue.dequeue());
        });

        add(tests, "test_ArrayQueue_front", 3, () -> {
            ArrayQueue<String> queue = new ArrayQueue<String>();
            queue.enqueue("first");
            queue.enqueue("second");
            expect("front before dequeue", "first", queue.front());
            queue.dequeue();
            expect("front after dequeue", "second", queue.front());
        });

        add(tests, "test_ArrayQueue_dequeue_return_null", 3, () -> {
            ArrayQueue<Integer> queue = new ArrayQueue<Integer>();
            expect("dequeue on empty queue", null, queue.dequeue());
        });

        add(tests, "test_ArrayQueue_dequeue_change_state", 4, () -> {
            ArrayQueue<String> queue = new ArrayQueue<String>();
            queue.enqueue("first");
            queue.enqueue("second");
            queue.dequeue();
            expect("size after dequeue", 1, queue.size());
            expect("front after dequeue", "second", queue.front());
            expect("isEmpty after dequeue", false, queue.isEmpty());
            queue.dequeue();
            expect("size after last dequeue", 0, queue.size());
            expect("isEmpty after last dequeue", true, queue.isEmpty());
        });

        add(tests, "test_ArrayQueue_initial_state", 3, () -> {
            ArrayQueue<Integer> queue = new ArrayQueue<Integer>();
            expect("new queue size", 0, queue.size());
            expect("new queue isEmpty", true, queue.isEmpty());
        });

        add(tests, "test_LinkedStack_top", 3, () -> {
            LinkedStack<String> stack = new LinkedStack<String>();
            stack.push("first");
            stack.push("second");
            expect("top after two pushes (LIFO)", "second", stack.top());
        });

        add(tests, "test_ArrayStack_top_no_change", 4, () -> {
            ArrayStack<String> stack = new ArrayStack<String>();
            stack.push("first");
            stack.push("second");
            expect("first top", "second", stack.top());
            expect("second top", "second", stack.top());
            expect("size after two top calls", 2, stack.size());
            expect("pop after top calls", "second", stack.pop());
        });

        add(tests, "test_LinkedQueue_initial_state", 3, () -> {
            LinkedQueue<Integer> queue = new LinkedQueue<Integer>();
            expect("new queue size", 0, queue.size());
            expect("new queue isEmpty", true, queue.isEmpty());
        });

        add(tests, "test_ArrayQueue_enqueue", 4, () -> {
            ArrayQueue<String> queue = new ArrayQueue<String>();
            queue.enqueue("first");
            queue.enqueue("second");
            expect("size after two enqueues", 2, queue.size());
            expect("isEmpty after enqueues", false, queue.isEmpty());
            expect("front after enqueues", "first", queue.front());
            queue.dequeue();
            queue.dequeue();
            queue.enqueue("third");
            expect("front after draining and reusing queue", "third", queue.front());
            expect("size after reuse", 1, queue.size());
        });

        add(tests, "test_ArrayStack_top_null", 3, () -> {
            ArrayStack<Integer> stack = new ArrayStack<Integer>();
            expect("top on empty stack", null, stack.top());
        });

        add(tests, "test_ArrayQueue_dequeue_return_value", 4, () -> {
            ArrayQueue<String> queue = new ArrayQueue<String>();
            queue.enqueue("first");
            queue.enqueue("second");
            expect("first dequeue (FIFO)", "first", queue.dequeue());
            expect("second dequeue (FIFO)", "second", queue.dequeue());
        });

        add(tests, "test_LinkedQueue_enqueue", 4, () -> {
            LinkedQueue<Integer> queue = new LinkedQueue<Integer>();
            for (int value = 1; value <= 5; value++) {
                queue.enqueue(value);
            }
            expect("size after five enqueues", 5, queue.size());
            expect("isEmpty after enqueues", false, queue.isEmpty());
            expect("front after enqueues", 1, queue.front());
        });

        add(tests, "test_LinkedQueue_front", 3, () -> {
            LinkedQueue<String> queue = new LinkedQueue<String>();
            queue.enqueue("first");
            queue.enqueue("second");
            expect("front before dequeue", "first", queue.front());
            queue.dequeue();
            expect("front after dequeue", "second", queue.front());
        });

        add(tests, "test_ArrayQueue_front_null", 3, () -> {
            ArrayQueue<Integer> queue = new ArrayQueue<Integer>();
            expect("front on empty queue", null, queue.front());
        });

        add(tests, "test_LinkedQueue_dequeue_return_null", 3, () -> {
            LinkedQueue<Integer> queue = new LinkedQueue<Integer>();
            expect("dequeue on empty queue", null, queue.dequeue());
        });

        add(tests, "test_LinkedStack_pop_return_value_of_top_elem", 4, () -> {
            LinkedStack<String> stack = new LinkedStack<String>();
            stack.push("first");
            stack.push("second");
            expect("first pop (LIFO)", "second", stack.pop());
            expect("second pop (LIFO)", "first", stack.pop());
        });

        add(tests, "test_LinkedStack_push", 4, () -> {
            LinkedStack<Integer> stack = new LinkedStack<Integer>();
            for (int value = 1; value <= 5; value++) {
                stack.push(value);
            }
            expect("size after five pushes", 5, stack.size());
            expect("isEmpty after pushes", false, stack.isEmpty());
            expect("top after pushes", 5, stack.top());
        });

        add(tests, "test_LinkedQueue_front_null", 3, () -> {
            LinkedQueue<Integer> queue = new LinkedQueue<Integer>();
            expect("front on empty queue", null, queue.front());
        });

        add(tests, "test_ArrayStack_pop_return_value_of_top_elem", 4, () -> {
            ArrayStack<String> stack = new ArrayStack<String>();
            stack.push("first");
            stack.push("second");
            expect("first pop (LIFO)", "second", stack.pop());
            expect("second pop (LIFO)", "first", stack.pop());
        });

        add(tests, "test_ArrayStack_top", 3, () -> {
            ArrayStack<String> stack = new ArrayStack<String>();
            stack.push("first");
            stack.push("second");
            expect("top after two pushes (LIFO)", "second", stack.top());
        });

        add(tests, "test_LinkedStack_top_null", 3, () -> {
            LinkedStack<Integer> stack = new LinkedStack<Integer>();
            expect("top on empty stack", null, stack.top());
        });

        add(tests, "test_LinkedQueue_dequeue_change_state", 4, () -> {
            LinkedQueue<String> queue = new LinkedQueue<String>();
            queue.enqueue("first");
            queue.enqueue("second");
            queue.dequeue();
            expect("size after dequeue", 1, queue.size());
            expect("front after dequeue", "second", queue.front());
            expect("isEmpty after dequeue", false, queue.isEmpty());
            queue.dequeue();
            expect("size after last dequeue", 0, queue.size());
            expect("isEmpty after last dequeue", true, queue.isEmpty());
            queue.enqueue("third");
            expect("front after draining and reusing queue", "third", queue.front());
            expect("size after reuse", 1, queue.size());
        });

        add(tests, "test_ArrayStack_pop_return_null", 3, () -> {
            ArrayStack<Integer> stack = new ArrayStack<Integer>();
            expect("pop on empty stack", null, stack.pop());
        });

        add(tests, "test_ArrayStack_push", 4, () -> {
            ArrayStack<Integer> stack = new ArrayStack<Integer>();
            for (int value = 1; value <= 4; value++) {
                stack.push(value);
            }
            expect("size after four pushes", 4, stack.size());
            expect("isEmpty after pushes", false, stack.isEmpty());
            expect("top after pushes", 4, stack.top());
        });

        return tests;
    }

    public static void main(String[] args) {
        List<TestCase> tests = tests();
        int earned = 0;
        int possible = 0;
        int passed = 0;

        System.out.println("Lab05 grader: " + tests.size() + " independent checks");
        for (TestCase test : tests) {
            possible += test.points;
            try {
                test.check.run();
                earned += test.points;
                passed++;
                System.out.println("[PASS] " + test.name + " (" + test.points + "/" + test.points + ")");
            } catch (Throwable failure) {
                System.out.println("[FAIL] " + test.name + " (0/" + test.points + ")");
                if (failure instanceof TestFailure) {
                    System.out.println("       " + failure.getMessage());
                } else {
                    String detail = failure.getMessage();
                    System.out.println("       expected: check completes without error | actual: "
                            + failure.getClass().getSimpleName()
                            + (detail == null ? "" : " (" + detail + ")"));
                }
            }
        }
        if (possible != 100 || tests.size() != 29) {
            throw new AssertionError("Grader configuration error: " + tests.size()
                    + " tests, " + possible + " points; expected 29 tests, 100 points");
        }
        System.out.println("FINAL SCORE: " + earned + " / " + possible
                + " | Passed: " + passed + " / " + tests.size());
    }
}
