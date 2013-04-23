package no.ntnu.webintelligence.nhlAnalyze;

import java.io.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

public class StripHTML extends HTMLEditorKit.ParserCallback{
	StringBuffer s;
	
	public StripHTML() {
	
	}

    public void parse(Reader in) throws IOException {
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        // the third parameter is TRUE to ignore charset directive
        delegator.parse(in, this, Boolean.TRUE);
    }

    public void handleText(char[] text, int pos) {
        s.append(text);
    }

    public String getText() {
        return s.toString();
    }

    public static void main(String[] args) {
        try {
            // the HTML to convert
            FileReader in = new FileReader("data/nlh/L/L1.1.htm");
            StripHTML parser = new StripHTML();
            parser.parse(in);
            in.close();
            System.out.println(parser.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
