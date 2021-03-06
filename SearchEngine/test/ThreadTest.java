import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ThreadTest.ThreadArgumentTest.class,
	ThreadTest.ThreadOutputTest.class
})
public class ThreadTest {

	/** Configure this on your system if you want to have a longer timeout. */
	public static final int TIMEOUT = 60000;

	/**
	 * This class tests whether {@link Driver} runs without throwing any
	 * exceptions, despite being given bad arguments.
	 */
	public static class ThreadArgumentTest {

		private static String[] getArguments(String threads) {
			String name = "search-simple-simple.txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "simple.txt");
			Path result = Paths.get(ProjectTest.RESULT_DIR, name);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString(),
					ProjectTest.THREAD_FLAG, threads
			};

			return args;
		}

		@Test(timeout = TIMEOUT)
		public void testNegativeThreads() {
			String[] args = getArguments("-1");
			ProjectTest.checkExceptions("Threads: -1", args);
		}

		@Test(timeout = TIMEOUT)
		public void testZeroThreads() {
			String[] args = getArguments("0");
			ProjectTest.checkExceptions("Threads: 0", args);
		}

		@Test(timeout = TIMEOUT)
		public void testFractionThreads() {
			String[] args = getArguments("3.14");
			ProjectTest.checkExceptions("Threads: 3.14", args);
		}

		@Test(timeout = TIMEOUT)
		public void testWordThreads() {
			String[] args = getArguments("fox");
			ProjectTest.checkExceptions("Threads: fox", args);
		}

		@Test(timeout = TIMEOUT)
		public void testSingleThread() {
			String[] args = getArguments("1");
			ProjectTest.checkExceptions("Threads: 1", args);
		}
	}

	/**
	 * This class tests whether {@link Driver} produces the correct
	 * inverted index and search result output for different numbers
	 * of threads.
	 */
	@RunWith(Parameterized.class)
	public static class ThreadOutputTest {
		@Parameters(name = "{0} Threads")
        public static Iterable<Object[]> data() {
        		return Arrays.asList(new Object[][]{
        				{"1"},	// test output with 1 worker thread
        				{"2"},	// test output with 2 worker threads
        				{"3"},	// test output with 3 worker threads
        				{"5"}	// test output with 3 worker threads
        		});
        }

        private String numThreads;

        public ThreadOutputTest(String numThreads) {
        		this.numThreads = numThreads;
        }

		@Test(timeout = TIMEOUT)
		public void testThreadIndexSimple() {
			String expect = "index-simple.txt";
			String actual = "index-simple-" + this.numThreads + ".txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, expect);
			Path result = Paths.get(ProjectTest.RESULT_DIR, actual);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.INDEX_FLAG, result.toString(),
					ProjectTest.THREAD_FLAG, this.numThreads
			};

			ProjectTest.checkProjectOutput("Index Simple " +
					this.numThreads + " Threads", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testThreadSearchSimple() {
			String expect = "search-simple-simple.txt";
			String actual = "search-simple-" + this.numThreads + ".txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR, "simple");
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "simple.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, expect);
			Path result = Paths.get(ProjectTest.RESULT_DIR, actual);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString(),
					ProjectTest.THREAD_FLAG, this.numThreads
			};

			ProjectTest.checkProjectOutput("Search Simple" +
					this.numThreads + " Threads", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testThreadIndexComplex() {
			String expect = "index-all.txt";
			String actual = "index-all-" + this.numThreads + ".txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR);
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, expect);
			Path result = Paths.get(ProjectTest.RESULT_DIR, actual);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.INDEX_FLAG, result.toString(),
					ProjectTest.THREAD_FLAG, this.numThreads
			};

			ProjectTest.checkProjectOutput("Index Complex " +
					this.numThreads + " Threads", args, result, output);
		}

		@Test(timeout = TIMEOUT)
		public void testThreadSearchComplex() {
			String expect = "search-index-complex.txt";
			String actual = "search-complex-" + this.numThreads + ".txt";

			Path input  = Paths.get(ProjectTest.INPUT_DIR);
			Path query  = Paths.get(ProjectTest.QUERY_DIR, "complex.txt");
			Path output = Paths.get(ProjectTest.OUTPUT_DIR, expect);
			Path result = Paths.get(ProjectTest.RESULT_DIR, actual);

			String[] args = new String[] {
					ProjectTest.DIR_FLAG, input.toString(),
					ProjectTest.QUERY_FLAG, query.toString(),
					ProjectTest.SEARCH_FLAG, result.toString(),
					ProjectTest.THREAD_FLAG, this.numThreads
			};

			ProjectTest.checkProjectOutput("Search Complex" +
					this.numThreads + " Threads", args, result, output);
		}
	}
}
