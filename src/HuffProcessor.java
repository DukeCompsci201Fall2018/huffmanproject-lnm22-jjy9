import java.util.PriorityQueue;

/**
 * Although this class has a history of several years,
 * it is starting from a blank-slate, new and clean implementation
 * as of Fall 2018.
 * <P>
 * Changes include relying solely on a tree for header information
 * and including debug and bits read/written information
 * 
 * @author Owen Astrachan
 */

public class HuffProcessor {

	public static final int BITS_PER_WORD = 8;
	public static final int BITS_PER_INT = 32;
	public static final int ALPH_SIZE = (1 << BITS_PER_WORD); 
	public static final int PSEUDO_EOF = ALPH_SIZE;
	public static final int HUFF_NUMBER = 0xface8200;
	public static final int HUFF_TREE  = HUFF_NUMBER | 1;

	private final int myDebugLevel;
	
	public static final int DEBUG_HIGH = 4;
	public static final int DEBUG_LOW = 1;
	
	public HuffProcessor() {
		this(0);
	}
	
	public HuffProcessor(int debug) {
		myDebugLevel = debug;
	}

	/**
	 * Compresses a file. Process must be reversible and loss-less.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be compressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void compress(BitInputStream in, BitOutputStream out){
		int[] counts = readForCounts(in);
		HuffNode root = makeTreeFromCounts(counts);
		String[] codings = makeCodingsFromTree(root);
		
		out.writeBits(BITS_PER_INT, HUFF_TREE);
		writeHeader(root,out);
		
		in.reset();
		writeCompressedBits(codings,in,out);
		out.close();


	}
	private void writeCompressedBits(String[] codings, BitInputStream in, BitOutputStream out) {
		// TODO Auto-generated method stub
		
	}

	private void writeHeader(HuffNode root, BitOutputStream out) {
		// TODO Auto-generated method stub
		
	}

	private String[] makeCodingsFromTree(HuffNode root) {
		String[] codings = new String[ALPH_SIZE + 1];
		return null;
	}

	private int[] readForCounts(BitInputStream in) {
		int[] counts = new int[ALPH_SIZE + 1];
		while (true){
			int val = in.readBits(BITS_PER_WORD);
			if (val == -1) break;
			counts[in.readBits(BITS_PER_WORD)] += 1;
			System.out.println("index is "+in.readBits(BITS_PER_WORD));
			System.out.println("Counts[index] ...");
			System.out.println(counts[in.readBits(BITS_PER_WORD)]);
		}
		counts[PSEUDO_EOF] = 1;
		return counts;
	}
	
	private HuffNode makeTreeFromCounts(int[] counts) {
/*		PriorityQueue<HuffNode> pq = new PriorityQueue<>();

		for(every index such that freq[index] > 0) {
		    pq.add(new HuffNode(index,freq[index],null,null);
		}

		while (pq.size() > 1) {
		    HuffNode left = pq.remove();
		    HuffNode right = pq.remove();
		    // create new HuffNode t with weight from
		    // left.weight+right.weight and left, right subtrees
		    pq.add(t);
		}
		HuffNode root = pq.remove();*/
		return null;
	}

	/**
	 * Decompresses a file. Output file must be identical bit-by-bit to the
	 * original.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be decompressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void decompress(BitInputStream in, BitOutputStream out){

		int bits = in.readBits(BITS_PER_INT);
		if (bits != HUFF_TREE) {
			throw new HuffException("illegal header starts with "+bits);
		}
		
		HuffNode root = readTreeHeader(in);
		readCompressedBits(root, in, out);
		out.close();
	}

	private void readCompressedBits(HuffNode root, BitInputStream in, BitOutputStream out) {
		// TODO Auto-generated method stub
		
		HuffNode current = root;
		System.out.println("ummm hello");
		while (current != null) {
			int bit = in.readBits(1);
			if (bit == -1) {
				throw new HuffException("bad input, no PSEUDO_EOF");
			}

			else {
				if (bit ==0) current = current.myLeft;
				else current = current.myRight;
				
				if (current.myLeft == null && current.myRight == null) {
					if(current.myValue == PSEUDO_EOF) {
						break;
					}
					else {
						out.writeBits(BITS_PER_WORD, current.myValue);
						current = root;
					}
				}
			}
		}
		
	}

	private HuffNode readTreeHeader(BitInputStream in) {
		// TODO Auto-generated method stub
		
		int bit = in.readBits(1);
		if (bit == -1) {
			throw new HuffException("bad input, no PSEUDO_EOF");
		}
		if (bit == 0) {
			HuffNode left = readTreeHeader(in);
			HuffNode right = readTreeHeader(in);
			return new HuffNode(0, 0, left, right);
		}
		else {
			int nineBits = in.readBits(BITS_PER_WORD + 1);
			return new HuffNode(nineBits, 0, null, null);
		}
	}
}