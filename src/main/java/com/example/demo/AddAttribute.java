package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import com.amazon.ion.IonReader;
import com.amazon.ion.IonStruct;
import com.amazon.ion.IonSystem;
import com.amazon.ion.IonType;
import com.amazon.ion.IonValue;
import com.amazon.ion.IonWriter;
import com.amazon.ion.system.IonReaderBuilder;
import com.amazon.ion.system.IonSystemBuilder;
import com.amazon.ion.system.IonTextWriterBuilder;

public class AddAttribute {

	public static void main(String[] args) {
	
		IonTextWriterBuilder textWriterBuilder = IonTextWriterBuilder.standard();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IonWriter writer = textWriterBuilder.build(out);
        
        try {
        	
        	InputStream newFile = new DataInputStream(new FileInputStream("C:\\Users\\snghqrw\\Desktop\\testIon.ion"));
        	IonSystem system = IonSystemBuilder.standard().build();
			Iterator<IonValue> iter = system.iterate(newFile);
			while(iter.hasNext()) {
				IonValue ionValue = (IonValue) iter.next();
				IonStruct struct = (IonStruct) ionValue;
				String table_name = struct.get("table_name").toString();
				String domain_id = struct.get("domain_id").toString();
				String stage_version = struct.get("stage_version").toString();
				String feed_attribute = struct.get("feed_attribute").toString();
				String attribute_class = struct.get("attribute_class").toString();
				String imds_attribute = struct.get("imds_attribute").toString();
				
				writer.writeValue(ionValue);
				
				StringBuilder stringBuilder = new StringBuilder();
			    try (IonWriter jsonWriter = IonTextWriterBuilder.json().withPrettyPrinting().build(stringBuilder)) {
			        new AddAttribute().rewrite(out.toString(), jsonWriter);
			    }
			    System.out.println(stringBuilder.toString());
				
				FileOutputStream fo = new FileOutputStream(new File("C:\\Users\\snghqrw\\Desktop\\cleanIon.ion"));
				fo.write(out.toString().getBytes());
			}
        	
        }catch(Exception e) {
        	System.out.println(e);
        }
		
	}
	
	
	void rewrite(String textIon, IonWriter writer) throws IOException {
	    IonReader reader = IonReaderBuilder.standard().build(textIon);
	    writer.writeValues(reader);
	}
}
