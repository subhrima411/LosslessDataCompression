import java.io.IOException;
import java.util.BitSet;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

class DecoderNode 
{
	int data;
	boolean isEnd;
	DecoderNode left,right;
	
	public DecoderNode() 
	{
		data = 0;
		isEnd = false;
		left = null;
		right = null;
	}
	
	public DecoderNode(int data, boolean isLeaf, DecoderNode left, DecoderNode right) 
	{
		this.data = data;
		this.isEnd = isLeaf;
		this.left = null;
		this.right = null;
	}
	
}

class DecoderTree 
{	
	DecoderNode root;
	
	public DecoderTree() 
	{
		root = new DecoderNode();
	}
	
	public DecoderNode getRoot()
	{
		return root;
	}
	
	public void insert(String code, DecoderNode node, int data)
	{	
		for(int i = 0;i < code.length() ; i++)
		{	
			if(code.charAt(i) == '0')
			{
				if(node.left == null)
				{
					node.left = new DecoderNode();
				}
				node = node.left;
			}
			else
			{
				if(node.right == null)
				{
					node.right = new DecoderNode();
				}
				node = node.right;
			}
		}
		node.data = data;
		node.isEnd = true;
	}
}


public class decoder 
{	
	public static void main(String args[]) throws IOException
	{
		decoder dec = new decoder();
		File codeFile = new File(args[1]);
		File encodedFile = new File(args[0]);
		File decodedFile = new File("decoded.txt");

		FileInputStream fin = null;

		BufferedWriter fileWriter=null;
		BufferedReader codeReader=null;
		
		try
		{	
			long s = System.currentTimeMillis();
			codeReader = new BufferedReader(new FileReader(codeFile));
			
			DecoderTree tree = new DecoderTree();
			dec.decoderTree(codeReader,tree);
			
			fileWriter = new BufferedWriter(new FileWriter(decodedFile));
			fin = new FileInputStream(encodedFile);
			
			long size = encodedFile.length();
			dec.writeDecodedData(fileWriter,fin,tree,(int)size);
			
			System.out.println("Decoding Time: "+(System.currentTimeMillis()-s));
			//compareFiles();
		}
		finally
		{
			if (fin != null) 
			{
				fin.close();
			}
			if(fileWriter != null)
			{
				fileWriter.close();
			}
			if(codeReader != null)
			{
				codeReader.close();
			}
		}
	}
	
	public void writeDecodedData(BufferedWriter fileWriter, FileInputStream encoded, DecoderTree tree, int size) throws IOException
	{
		DecoderNode node = tree.getRoot();		
		byte[] b = new byte[size];
		
		encoded.read(b);
		BitSet bs = BitSet.valueOf(b);
		bs.set((b.length) * 8);
		
		for(int i = 0;i < bs.length()-1; i++)
		{
			if(bs.get(i) == false)
			{
				node = node.left;
			}
			else
			{
				node = node.right;
			}
			if(node.isEnd)
			{	
				fileWriter.write(""+node.data);
				fileWriter.write("\n");
				node = tree.getRoot();
			}
		}
	}
	
	public void decoderTree(BufferedReader reader, DecoderTree tree) throws IOException
	{
		String str = null;
		String[] dataVal = null;
		while ((str = reader.readLine()) != null) 
		{
			dataVal = str.split(" ");
			tree.insert(dataVal[1], tree.getRoot(), Integer.parseInt(dataVal[0]));
		}
	}
}

