package parsing;

import java.util.Observer;

/**
 * Created by ideadapt on 30.04.15.
 */
public interface FileParserInterface {
    void parseFile(String filename);

    void addObserver(Observer anObserver);

    void parseString(String code);
}
