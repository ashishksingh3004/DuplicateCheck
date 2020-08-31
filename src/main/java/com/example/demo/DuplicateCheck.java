package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazon.ion.IonReader;
import com.amazon.ion.IonStruct;
import com.amazon.ion.IonSystem;
import com.amazon.ion.IonValue;
import com.amazon.ion.IonWriter;
import com.amazon.ion.system.IonReaderBuilder;
import com.amazon.ion.system.IonSystemBuilder;
import com.amazon.ion.system.IonTextWriterBuilder;

public class DuplicateCheck {

	public static void main(String[] args) {
		
		
		IonTextWriterBuilder textWriterBuilder = IonTextWriterBuilder.standard();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		IonWriter writer = textWriterBuilder.build(out);
		
		Set<String> orignal = new HashSet<String>();
		Set<String> duplicate = new HashSet<String>();
		try {
			InputStream newFile = new DataInputStream(new FileInputStream("C:\\Users\\snghqrw\\Desktop\\testIon.ion"));
			InputStream oldFile = new DataInputStream(new FileInputStream("C:\\Users\\snghqrw\\Desktop\\oldIonfile.ion"));
			IonSystem system = IonSystemBuilder.standard().build();
			Iterator<IonValue> iterold = system.iterate(oldFile);
			while(iterold.hasNext()) {
				IonValue ionValue = (IonValue) iterold.next();
				IonStruct fptIon = (IonStruct) ionValue;
				String feed_attribute = fptIon.get("feed_attribute").toString();
				orignal.add(feed_attribute);
			}
			Iterator<IonValue> iter = system.iterate(newFile);
			while(iter.hasNext()) {
				IonValue ionValue = (IonValue) iter.next();
				IonStruct fptIon = (IonStruct) ionValue;
				String feed_attribute = fptIon.get("feed_attribute").toString();
				if(orignal.contains(feed_attribute)) {
					duplicate.add(feed_attribute);
				}else {
					writer.writeValue(ionValue);
				}
			}
			
			System.out.println(orignal.size());
			System.out.println(duplicate.size());
			for (String string : duplicate) {
				System.out.println(string);
			}
			
			
			StringBuilder stringBuilder = new StringBuilder();
			    try (IonWriter prettyWriter = IonTextWriterBuilder.pretty().build(stringBuilder)) {
			       new DuplicateCheck().rewrite(out.toString(), prettyWriter);
			    }
			   /* System.out.println(stringBuilder.toString());*/
			
			    FileOutputStream fo = new FileOutputStream(new File("C:\\Users\\snghqrw\\Desktop\\cleanIon.ion"));
			    fo.write(stringBuilder.toString().getBytes());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		   		    
		}
	
	void rewrite(String textIon, IonWriter writer) throws IOException {
	    IonReader reader = IonReaderBuilder.standard().build(textIon);
	    writer.writeValues(reader);
	}



}
