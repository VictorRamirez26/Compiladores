package compiler.reader;

public class TextLine {

    private String data;
    private int lineIndex;

    public TextLine() {
        this.data = null;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    @Override
    public String toString() {
        return "TextLine{" +
                "data='" + data + '\'' +
                ", lineIndex=" + lineIndex +
                '}';
    }
}
