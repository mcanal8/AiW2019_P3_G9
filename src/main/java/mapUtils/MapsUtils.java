package maps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MapsUtils {

	//this method create a new map file, writing the "text" string inside the new file
	@SuppressWarnings("resource")
	public static String createNewMap(String inputFile, String text) {

		String encoding = "UTF-8";

		String outFile = inputFile.replace(".html", "-out-1.html");

		String line = "";
		BufferedReader csv;
		Writer out;

		try {
			csv = new BufferedReader(new InputStreamReader(
					new FileInputStream(inputFile), encoding));

			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFile), "UTF-8"));

			while((line = csv.readLine()) != null) {
				if(line.contains("WRITE HERE")){
					out.write("\n" + text + "\n");
				}
				else{
					out.write(line+"\n");
				}
			}

			out.close();
			
		} catch ( IOException e) {
			e.printStackTrace();
		}
		return line;
	}
}


