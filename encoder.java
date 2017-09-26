import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map.Entry;
import java.util.Set;

class HuffmanNode 
{
	int data;
	int frequency;
	HuffmanNode left, right;
	
	HuffmanNode()
	{
		this.data = 0;
		this.frequency = 0;
		this.left = null;
		this.right = null;
	}
	
	HuffmanNode(int data, int frequency, HuffmanNode left, HuffmanNode right)
	{
		this.data = data;
		this.frequency = frequency;
		this.left = left;
		this.right = right;
				
	}
}

class PairingHeap
{
	class Node
	{
		HuffmanNode data;
		Node child;
		Node left, right;
		
		public Node(HuffmanNode data, Node child, Node left, Node right)
		{
			this.data = data;
			this.child = child;
			this.left = left;
			this.right = right;
		}
		
		public String toString()
		{
			return " "+ data +" ";
		}
	}
	
	public Node head = null;
    int size = 0;
	
    static HashMap<Integer, String> hm = new HashMap<Integer, String>();
	
	private Node meld(Node p1, Node p2)
	{
		Node small = p1.data.frequency < p2.data.frequency ? p1 : p2;
		Node big = p1.data.frequency >= p2.data.frequency? p1 : p2;
		Node temp = small.child;
		small.child = big;
		big.right = temp;
		big.left = small; 
		if(temp != null)
			temp.left = big;
		return small;
	}
	
	public void insert(HuffmanNode data)
	{
		Node node = new Node(data, null, null, null);
		size++;
		if(head == null)
		{
			head = node;
		}
		else
			head = meld(head, node);
	}
	
	public Node removeMin()
	{
		Node temp = head;
		Node tempChild = head.child;
		head.child = null;
		LinkedList<Node> vector = new LinkedList<Node>();
		if(tempChild == null)
		{
			size--;
			head = null;
			return temp;
			
		}
		while(tempChild != null)
		{
			tempChild.left = null;
			Node next = tempChild.right;
			tempChild.right = null;
			vector.addLast(tempChild);
			tempChild = next;
		}
		
		while(vector.size() > 1)
		{
			Node p1 = vector.removeFirst();
			Node p2 = vector.removeFirst();
			Node p3 = this.meld(p1, p2);
			vector.add(p3);
		}
		size--;
		head = vector.removeFirst();
		return temp;
	}
	
	public void constructHeap(int[] f)
	{
		for(int i = 0 ; i < f.length; i++)
		{
			if(f[i] > 0)
			{
				insert(new HuffmanNode(i, f[i], null, null));
			}
		}
	}
	
	public void generateHuffmanTree() 
	{
		while(size >= 2)
		{
			Node small = removeMin();
			Node big = removeMin();
			HuffmanNode node = new HuffmanNode(-1, small.data.frequency + big.data.frequency, small.data, big.data);
			insert(node);
		}
		head = removeMin();
	}
	
	private void generateCode(HuffmanNode head, String code)
	{
		if(head != null)
		{
			if(head.left != null)
				generateCode(head.left, code + "0");
			if(head.left == null && head.right == null)
			{
				hm.put(head.data, code);
			}
			if(head.right != null)
				generateCode(head.right, code + "1");
		}
	}
	
	public void display()
	{
		System.out.println("\nPairing Heap: ");
		LinkedList<Node> q = new LinkedList<Node>();
		Node temp = head;
		q.add(temp);
		while(q.isEmpty() == false)
		{
			temp = q.remove();
			while(temp!=null)
			{
				if(temp.child != null)
					q.add(temp.child);
				System.out.print(" " + temp.toString() + " ");
				temp = temp.right;
			}
		}
	}
}

class BinaryHeap
{
	int shiftVal, size;
	HuffmanNode[] heap;
	
	public BinaryHeap() 
	{
		shiftVal = 0;
		heap = new HuffmanNode[1000004];
		size = shiftVal;
	}

	public void buildHeap(int[] freqArr) 
	{
		if(freqArr == null)
			return;

		for(int i = 0; i < freqArr.length; i++)
		{
			
			if(freqArr[i] > 0)
			{
				heap[size++]=new HuffmanNode(i, freqArr[i], null, null);
			}
		}

		if(size > shiftVal)
			size--;

		for(int i = parent(size); i >= shiftVal; i--)
		{
			minHeapify(i);
		}
	}

	public HuffmanNode removeMin() 
	{
		if(size < shiftVal)
			return null;
		HuffmanNode temp = heap[shiftVal];
		heap[shiftVal] = heap[size];
		heap[size] = null;
		size--;
		minHeapify(shiftVal);
		return temp;
	}

	public void minHeapify(int index) 
	{
		int min = getChild(index);
		if(min == index)
			return;
		else 
		{
			HuffmanNode temp = heap[index];
			heap[index] = heap[min];
			heap[min] = temp;
			minHeapify(min);
		}
		
	}

    public HuffmanNode peep() 
    {
		if(size>=shiftVal)
		{
			return heap[shiftVal];
		}
		return null;
	}

	public int parent(int index) 
	{
		return ((index+1)/2)-1;
	}

	public int child(int i, int j) 
	{
		// ith child of j
		return 2*(j+1)+i-1;
	}

	public int getChild(int index) 
	{
		int small = index;
		for(int i=0; i < 2; i++)
		{
			if(child(i,index) <= size)
			{
					if(heap[child(i,index)].frequency < heap[small].frequency)
					{
						small = child(i, index);
					}
			}
		}
		return small;
	}

	public void setRoot(HuffmanNode smallNode) 
	{
		heap[shiftVal]=smallNode;
		minHeapify(shiftVal);
	}
	
	public void generateHuffmanTree()
	{
		while(size > shiftVal)
		{
			HuffmanNode smallestNode = removeMin();
			HuffmanNode head = peep(); 
			HuffmanNode newNode = new HuffmanNode(-1, smallestNode.frequency + head.frequency, smallestNode, head);
			setRoot(newNode);
		}
	}
}

class FourWayHeap 
{

	int shiftVal, size;
	HuffmanNode[] heap;
	
	public FourWayHeap() 
	{
		shiftVal = 3;
		size = 3;
		heap = new HuffmanNode[1000004];
	}
	
	public void buildFourWayHeap(int[] frequency) 
	{
		if(frequency == null)
			return;
		
		for(int i = 0; i < frequency.length; i++)
		{
			if(frequency[i] > 0)
			{
				heap[size++] = new HuffmanNode(i, frequency[i], null, null);
			}
		}
		
		if(size > shiftVal)
			size--;
		
		for(int i = parent(size); i >= shiftVal; i--)
		{
			minHeapify(i);
		}
	}

	public void minHeapify(int index) 
	{
		int smallChild = getChild(index);
		if(smallChild == index)
			return;
		else {
			HuffmanNode temp = heap[index];
			heap[index] = heap[smallChild];
			heap[smallChild] = temp;
			minHeapify(smallChild);
		}
	}
	
	public HuffmanNode removeMin() 
	{
		if(size < shiftVal)
			return null;
		HuffmanNode temp = heap[shiftVal];
		heap[shiftVal] = heap[size];
		heap[size] = null;
		size--;
		minHeapify(shiftVal);
		return temp;
	}

	public HuffmanNode peek() 
	{
		if(size >= shiftVal)
		{
			return heap[shiftVal];
		}
		return null;
	}

	public int parent(int index) 
	{
		if(index > shiftVal)
			return ((index - shiftVal- 1)/4) + shiftVal;
		return -1;
	}

	public int child(int i, int j) 
	{
	 	return ((j - shiftVal) * 4) + i + shiftVal;
	}
	
	public int getChild(int index) 
	{
		int small = index;
		for(int i = 1; i <= 4; i++)
		{
			if(child(i,index) <= size)
			{
					if(heap[child(i,index)].frequency < heap[small].frequency)
					{
						small = child(i, index);
					}
			}
		}
		return small;
	}

	public void setRoot(HuffmanNode minimumNode) 
	{
		heap[shiftVal] = minimumNode;
		minHeapify(shiftVal);
	}
	
	public void generateHuffmanTree()
	{
		while(size > shiftVal)
		{
			HuffmanNode smallestNode = removeMin();
			HuffmanNode head = peek(); 
			HuffmanNode node = new HuffmanNode(-1, smallestNode.frequency + head.frequency, smallestNode, head);
			setRoot(node);
		}
	}
}


public class encoder 
{
	HashMap<Integer, String> codes = new HashMap<Integer,String>();
	
	public void generateCode(HuffmanNode root, StringBuilder sb)
	{
		if(root != null && root.left == null && root.right == null)
		{
			codes.put(root.data, sb.toString());
			return;
		}
		
		if(root.left != null)
		{
			sb.append('0');
			generateCode(root.left, sb);
			sb.deleteCharAt(sb.length() - 1);
		}
		
		if(root.right != null)
		{
			sb.append('1');
			generateCode(root.right, sb);
			sb.deleteCharAt(sb.length() - 1);
		}
	}
	
	public static void main(String[] args) throws IOException 
	{
		long s = System.currentTimeMillis();
		int[] frequencies = new int[1000000];
		File codeFile = new File("code_table.txt");
		File input = new File(args[0]);
		
		BufferedWriter fileWriter = null;
		BufferedReader reader = null;
		BufferedOutputStream encoded = null;
		
		encoder enc = new encoder();
		FourWayHeap fourWay = null;
		
		try 
		{	
			reader = new BufferedReader(new FileReader(input));
		    readData(reader, frequencies);
		    System.out.println("Reading Time: " + (System.currentTimeMillis()-s));
		    

	    	//testBinaryHeap(frequencies);
			//testFourWayHeap(frequencies);
			//testPairingHeap(frequencies);
			s = System.currentTimeMillis();
		    fourWay = new FourWayHeap(); 
		    fourWay.buildFourWayHeap(frequencies);
		    System.out.println("Time to Build the heap: " + (System.currentTimeMillis()-s));
		    
		    fourWay.generateHuffmanTree();
			
		    enc.generateCode(fourWay.peek(), new StringBuilder());
		    fileWriter = new BufferedWriter(new FileWriter(codeFile));
		    enc.writeCodeTable(fileWriter);
			fileWriter.close();
			
	    	//encode input in a binary file
	    	s = System.currentTimeMillis();
	    	encoded = new BufferedOutputStream(new FileOutputStream("encoded.bin"));
	    	reader = new BufferedReader(new FileReader(input));
	    	enc.encodeIn(reader, encoded);
	    	
	    	System.out.println("Encoding Time: " +(System.currentTimeMillis()-s));
		}
		catch(IOException ioExc)
		{
			System.out.println("Exception occured  " + ioExc.getMessage());
			ioExc.printStackTrace();
		}
		finally
		{
			if(encoded != null)
			{
				encoded.flush();
				encoded.close();
		    }
		    if (fileWriter != null)
		    {
		        fileWriter.close();
		    }		    
		}
	}
	
	private static void testPairingHeap(int[] frequencies) 
	{
		// TODO Auto-generated method stub
		long s = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++)
		{
			PairingHeap heap = new PairingHeap();
			heap.constructHeap(frequencies);
			heap.generateHuffmanTree();
		}
		long f = System.currentTimeMillis();
		System.out.println("Pairing Heap Average Performance: " + ((f - s)/10.0f));
	}

	private static void testFourWayHeap(int[] frequencies) 
	{
		// TODO Auto-generated method stub
		long s = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++)
		{
			FourWayHeap fourWay = new FourWayHeap();
			fourWay.buildFourWayHeap(frequencies);
			fourWay.generateHuffmanTree();
		}
		
		long f = System.currentTimeMillis();
		System.out.println("Four Way Heap Average Performance: " + ((f - s)/10.0f));
	}

	private static void testBinaryHeap(int[] frequencies) 
	{
		// TODO Auto-generated method stub
		long s = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++)
		{
			BinaryHeap binaryHeap = new BinaryHeap();
			binaryHeap.buildHeap(frequencies);
			binaryHeap.generateHuffmanTree();
		}
		long f = System.currentTimeMillis();
		System.out.println("Binary Heap Average Performance: " + ((f - s)/10.0f));
	}

	public void writeCodeTable(BufferedWriter fileWriter) throws IOException
	{
		Iterator<Integer> itr = codes.keySet().iterator();
		while(itr.hasNext()){
			Integer temp = itr.next();
			fileWriter.write(temp + " " + codes.get(temp));
			fileWriter.newLine();
		}
	}
	
	public static void readData(BufferedReader inputReader, int[] frequencyTable) throws IOException
	{
		String str; 
		while((str = inputReader.readLine()) != null){
			if(str.length() > 0){
		        frequencyTable[(Integer.parseInt(str))]++;
			}
		}
	}
	
	public void encodeIn(BufferedReader reader, BufferedOutputStream fileWriter) throws IOException
	{
			StringBuilder sb = new StringBuilder(); 
			String str;
			BitSet bs = new BitSet();
			int l = 0;
			while((str = reader.readLine()) != null)
			{
				if(str.length() > 0)
				{
				    sb.append(codes.get(Integer.parseInt(str)));
				
				    for(int i = 0 ; i < sb.length(); i++)
				    {
					   if(sb.charAt(i) == '1')
					   {
						   bs.set(l + i);
					   }
				    }
				
			     	l += sb.length();
				    sb.setLength(0);
			   }
	       }	
			fileWriter.write(bs.toByteArray());	
		}
}
