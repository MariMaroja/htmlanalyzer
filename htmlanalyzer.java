import java.io.*;
import java.net.*;

public class HtmlAnalyzer {
    public static void main(String[] args){
        if (args.length == 0) {
            System.out.println("URL connection error");
            return;
        }

        int depth = 0;
        int maxDepth = -1;
        String deepestText = null;

        try {
            URL url = new URL(args[0]);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(url.openStream())
            );

            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) continue;

                if (line.startsWith("<") && !line.startsWith("</")) {
                    depth++;
                }

                else if (line.startsWith("</")) {
                    depth--;
                }

                else {
                    if (depth > maxDepth) {
                        maxDepth = depth;
                        deepestText = line;
                    }
                }
            }

            br.close();

            if (deepestText != null) {
                System.out.println(deepestText);
            }

            else {
                System.out.println("malformed HTML");
            }
        }

        catch(Exception e) {
            System.out.println("URL connection error");
        }
    }
}
