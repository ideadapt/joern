package tools.index;

/**
 * Created by ideadapt on 17.04.15.
 */
public enum SourceLanguage {
    C(new String[]{"c", "cpp", "h", "cc", "hpp", "java"}),
    ECMAScript5(new String[]{"js"});

    private final String[] extensions;

    SourceLanguage(String[] extensions){
        this.extensions = extensions;
    }

    public String[] getExtensions(){
        return extensions;
    }
}
