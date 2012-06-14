import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.petebevin.markdown.MarkdownProcessor;

public class DocumentCompiler {
	public static void main(String[] args) throws Exception{
		String source = "../README.md";
		String target = "../README.pdf";
		String sourceFile = new Scanner( new File(source), "UTF-8").useDelimiter("\\A").next();
		
		// Convert from markdown to html
		MarkdownProcessor mp = new MarkdownProcessor();
		String html = "<html><body>" + mp.markdown(sourceFile) + "</body></html>";

		// Use Document to create pdf
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(new BufferedOutputStream(new FileOutputStream(new File(target))));
	}
}