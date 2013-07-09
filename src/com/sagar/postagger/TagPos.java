package com.sagar.postagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TagPos {
	/**
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH"), false));
		BufferedReader br = new BufferedReader(new FileReader(System.getenv("REVIEWS_PATH")));
		BufferedReader brTags = new BufferedReader(new FileReader(System.getenv("TAGSET_PATH")));
		MaxentTagger tagger = new MaxentTagger(System.getenv("TAGGER_PATH"));

		ArrayList<String[]> reviews = new ArrayList<String[]>();
		String nextLine;
		while((nextLine = br.readLine()) != null){
			reviews.add(nextLine.split(";"));
		}
		br.close();

		System.out.println("Total reviews: " + reviews.size() + " (should be 800)");

		ArrayList<String[]> tagset = new ArrayList<String[]>();
		while((nextLine = brTags.readLine()) != null){
			tagset.add(nextLine.split(";"));
		}

		System.out.println("Total tags: " + tagset.size() + " (should be 36)");

		ArrayList<String[]> matrix = new ArrayList<String[]>();
		String[] header = new String[tagset.size() + 1];
		for(int i = 0; i < header.length-1; i++){
			header[i] = tagset.get(i)[0].trim();
		}
		header[header.length-1] = "class";
		matrix.add(header);


		for(String[] s:reviews){
			String[] row = new String[tagset.size() + 1];
			String review = s[0].
								replace('_', ' ').
								replace('$', ' ').
								replace(';',' ');

			String taggedString = tagger.tagString(review);
			//row[0]=taggedString;
			for(int i = 0; i < row.length-1; i++){
				row[i] = "" + (taggedString.length() - taggedString.replaceAll("_"+tagset.get(i)[0].trim(), "").length());
			}
			row[row.length-1] = s[1];
			matrix.add(row);
		}

		for(String[] r:matrix){
			for(String c:r){
				writer.write(c);
				writer.write(';');
			}
			writer.write(System.getProperty("line.separator"));
		}
		writer.close();
	}

}
