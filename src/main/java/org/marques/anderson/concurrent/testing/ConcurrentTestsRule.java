package org.marques.anderson.concurrent.testing;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;

/**
 * TestRule that enables the concurrent tests
 */
public class ConcurrentTestsRule implements TestRule {

    /**
     * Defines if the concurrent tests calls will show result;
     */
    private boolean verbose = true;

    /**
     * Private constructor
     */
    private ConcurrentTestsRule() {
    }

    /**
     * Creates a new {@link ConcurrentTestsRule} with verbose tests
     *
     * @return newInstance of {@link ConcurrentTestsRule}
     */
    public static ConcurrentTestsRule verboseTests() {
        ConcurrentTestsRule newInstance = new ConcurrentTestsRule();
        newInstance.verbose = true;
        return newInstance;
    }

    /**
     * Creates a new {@link ConcurrentTestsRule} with silent tests
     *
     * @return newInstance of {@link ConcurrentTestsRule}
     */
    public static ConcurrentTestsRule silentTests() {
        ConcurrentTestsRule newInstance = new ConcurrentTestsRule();
        newInstance.verbose = false;
        return newInstance;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {

            private Throwable throwable;
            private int count;
            private long begin;
            private boolean complete;

            @Override
            public void evaluate() throws Throwable {
                ConcurrentTest concurrentTest = description.getAnnotation(ConcurrentTest.class);

                throwable = null;
                count = 0;
                begin = System.currentTimeMillis();
                complete = false;

                if (concurrentTest != null) {
                    final int requests = concurrentTest.requests();
                    int threads = concurrentTest.threads();
                    long maxTimeWaiting = concurrentTest.timeoutMillis();

                    ConcurrentExecutor.newInstance()
                            .requests(requests)
                            .threads(threads)
                            .timeoutMillis(maxTimeWaiting)
                            .execute(new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    if (throwable == null) {
                                        try {
                                            count++;
                                            if (verbose) {
                                                System.out.println("(" + count + ") " + description.getMethodName());
                                            }
                                            base.evaluate();
                                        } catch (Throwable e) {
                                            if (verbose) {
                                                System.out.println("(" + count + ") " + " exception: " + e.getMessage() + " OK!");
                                            }
                                            throwable = e;
                                        }

                                        if (verbose && (count == requests)) {
                                            System.out.println();
                                        }
                                    }
                                    return null;
                                }
                            });

                    long time = (System.currentTimeMillis() - begin);
                    if (count != requests){
                        throwable = new AssertionError("Time Exceeded! It was realized " + count +" tests in " + maxTimeWaiting + " milisseconds") ;
                    } else {
                        System.out.println(count + " concurrent tests realized in " + time + " milisseconds");
                    }
                } else {
                    base.evaluate();
                }

                if (throwable != null) {
                    throw throwable;
                }
            }
        };
    }
}