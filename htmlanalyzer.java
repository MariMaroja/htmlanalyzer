import java.io.*;
import java.net.*;
import java.util.*;

public class HtmlAnalyzer {
    public static void main(String[] args) {

        //check if the URL argument was provided
        if (args.length == 0) {
            System.out.println("URL connection error");
            return;
        }

        //stack to store opened HTML tags
        Stack<String> tagStack = new Stack<>();
        //maximum depth found in the document
        int maxDepth = -1;
        //deepest text found in the HTML structure
        String deepestText = null;

        try {
            //create URL object from command line argument
            URL url = new URL(args[0]);
            //open a stream to read the content of the URL
            BufferedReader br = new BufferedReader(
                new InputStreamReader(url.openStream())
            );

            String line;

            //read the HTML content 
            while ((line = br.readLine()) != null) {
                //remove leading and indentation
                line = line.trim();
                
                //ignore empty lines
                if (line.isEmpty()) continue;

                //if the line is a closing tag
                if (line.startsWith("</")) {
                    //extract the tag name without </ and >
                    String closeTag = line.substring(2, line.length() - 1);

                    //check for malformed HTML 
                    if (tagStack.isEmpty() || !tagStack.peek().equals(closeTag)) {
                        System.out.println("malformed HTML");
                        return;
                    }

                    //remove the matching opening tag from the stack
                    tagStack.pop();

                    //if the line is a opening tag
                } else if (line.startsWith("<")) {
                    //extract the tag name without <and >
                    String openTag = line.substring(1, line.length() - 1);
                    //push the tag into the stack
                    tagStack.push(openTag);

                    //otherwise the line is text content
                } else {
                    //current depth is the number of opened tags
                    int currentDepth = tagStack.size();

                    //update the deepest text if this is the deepest level
                    if (currentDepth > maxDepth) {
                        maxDepth = currentDepth;
                        deepestText = line;
                    }
                }
            }

            //close the reader
            br.close();

            //if stack is not empty, HTML is malformed
            if (!tagStack.isEmpty()) {
                System.out.println("malformed HTML");
                return;
            }

            //print the deepest text 
            if (deepestText != null) {
                System.out.println(deepestText);
                
                //no text founded or malformed structure
            } else {
                System.out.println("malformed HTML");
            }

            //catch any exception
        } catch (Exception e) {
            System.out.println("URL connection error");
        }
    }
}
