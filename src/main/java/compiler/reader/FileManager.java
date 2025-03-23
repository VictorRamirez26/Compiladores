package compiler.reader;


import java.io.BufferedReader; //https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManager {

    private BufferedReader reader;
    private String filePath;
    private int lineIndex;


    public FileManager(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        reader = new BufferedReader(new java.io.FileReader(filePath));
        this.lineIndex = 0;
    }

    public TextLine readNextLine() throws IOException {
        TextLine textLine = new TextLine();
        textLine.setLineIndex(++lineIndex);
        textLine.setData(reader.readLine());
        return textLine;
    }

    public boolean hasMoreLines() throws IOException {
        if (!reader.ready()) {
            reader.close();
        }
        return true;
    }





}
