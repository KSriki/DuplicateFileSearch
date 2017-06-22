import java.beans.DefaultPersistenceDelegate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {

	static HashMap<String, ArrayList<String>> dupes = new HashMap<String,ArrayList<String>>();
	public void walk ( String path ){
		File root = new File(path);
		File[] list = root.listFiles();

		if (list==null) return;

		for (File f : list){
			if (f.isDirectory()){
				walk(f.getAbsolutePath());
				//				System.out.println("Dir: " + f.getAbsoluteFile() );
			}else{
				if(f.exists()){
					//					Pattern pattern = Pattern.compile(regex);
					//	Matcher matcher = pattern.matcher(f.getName());

					//	while (matcher.find()) {
					//Use MD5 algorithm
					MessageDigest md5Digest;
					String checksum;

					//MD5 hash
					/*
						try {
							md5Digest = MessageDigest.getInstance("MD5");
							checksum = getFileChecksum(md5Digest, f);
							//see checksum
						//	System.out.println(checksum);
							if(dupes.get(checksum) != null){
								dupes.get(checksum).add(f.getName());
							}
							else{
								ArrayList<String> temp = new ArrayList<String>();
								temp.add(f.getName());
								dupes.put(checksum, temp);
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						}

					 */
					//Use SHA-1 algorithm
					MessageDigest shaDigest;
					try {
						shaDigest = MessageDigest.getInstance("SHA-1");

						String shaChecksum = getFileChecksum(shaDigest, f);
						if(dupes.get(shaChecksum) != null){
							dupes.get(shaChecksum).add(f.getName());
						}
						else{
							ArrayList<String> temp = new ArrayList<String>();
							temp.add(f.getName());
							dupes.put(shaChecksum, temp);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

	}
	public static void main(String[] args){
		//Create checksum for this file

		Application filewalker = new Application();
		Scanner console = new Scanner(System.in);
		System.out.println("Enter directory to search: ");
		String dir = console.nextLine();
		filewalker.walk(dir);


		for(Map.Entry<String, ArrayList<String>> entry: dupes.entrySet()){
			if(entry.getValue().size() > 1){

				System.out.printf("For %s, the dupe files are: ",entry.getKey());
				ArrayList<String> dupeFiles = entry.getValue();
				for(int i = 0; i < dupeFiles.size();i++){
					System.out.printf("%s ",dupeFiles.get(i));
				}
				System.out.println("");
			}
		}


	}

	private static String getFileChecksum(MessageDigest digest, File file) throws IOException
	{
		//Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);

		//Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0; 

		//Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		};

		//close the stream; We don't need it now.
		fis.close();

		//Get the hash's bytes
		byte[] bytes = digest.digest();

		//This bytes[] has bytes in decimal format;
		//Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< bytes.length ;i++)
		{
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		//return complete hash
		return sb.toString();
	}
}