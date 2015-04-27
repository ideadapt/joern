package fileWalker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderedWalker extends SourceFileWalker
{
	FileNameMatcher matcher = new FileNameMatcher();
	List<SourceFileListener> listeners = new LinkedList<>();
	private final String BASE_PATH = Paths.get("").toAbsolutePath().toString()+"/";
	
	public OrderedWalker(String[] extensions)
	{
		String extensionList = Arrays.asList(extensions).stream().collect(Collectors.joining(","));
		String pattern = "*.{" + extensionList + "}";
		setFilenameFilter(pattern);
	}	
	
	@Override
	public void setFilenameFilter(String filter)
	{
		matcher.setFilenameFilter(filter);
	}

	@Override
	public void addListener(SourceFileListener listener)
	{
		listeners.add(listener);
	}

	@Override
	protected void walkExistingFileOrDirectory(String dirOrFileName) throws IOException
	{
		walk(dirOrFileName);
	}

	private void walk(String dirOrFileName)
	{
		File file = new File(dirOrFileName);
		File[] dirContent = file.listFiles();
		Path path = file.toPath();
		
		// if this is not a directory, or invalid path
		if(dirContent == null) return;
		Arrays.sort(dirContent);
		
		reportDirectoryEnter(path);
		
		for(File f : dirContent){
			String absolutePath = f.getAbsolutePath();

			if(f.isDirectory()){	
				walk(absolutePath);
				continue;
			}

			Path filePath = f.toPath();
			Path relativePath = Paths.get(filePath.toString().replace(BASE_PATH, ""));
			if(matcher.fileMatches(relativePath))
				reportFile(relativePath);
		}
	
		reportDirectoryLeave(path);
	}

	private void reportDirectoryEnter(Path path)
	{
		for(SourceFileListener listener: listeners )
			listener.preVisitDirectory(path);
	}
	
	private void reportDirectoryLeave(Path path)
	{
		for(SourceFileListener listener: listeners )
			listener.postVisitDirectory(path);
	}

	private void reportFile(Path path)
	{
		for(SourceFileListener listener: listeners )
			listener.visitFile(path);

	}
}
