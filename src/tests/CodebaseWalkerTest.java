package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fileWalker.OrderedWalker;
import org.junit.Test;

import fileWalker.SourceFileWalker;

public class CodebaseWalkerTest
{
	@Test
	public void testRecursiveDirSearch()
	{
		String[] args = { "src/tests/samples/" };

		SourceFileWalker provider = new OrderedWalker(new String[]{"cpp", "c"});

		try {
			Set<String> expected = new HashSet<String>();
			expected.add("src/tests/samples/tiff.cpp");
			expected.add("src/tests/samples/subdir/test.c");
			expected.add("src/tests/samples/test.c");

			FilenameAggregator listener = new FilenameAggregator();
			provider.addListener(listener);
			provider.walk(args);

			assertEquals(expected, new HashSet<>(listener.filenames));
		}
		catch (IOException e) {
			fail("IO Error");
		}
	}
}
