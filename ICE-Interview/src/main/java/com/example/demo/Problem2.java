package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Problem2 {
	/**
	 * default comparator between strings.
	 */
	public static Comparator<String> defaultcomparator = new Comparator<String>() {
		@Override
		public int compare(String r1, String r2) {
			return r1.compareTo(r2);
		}
	};
		
	public static void main(String args[]) {
		try {
			ArrayList<BinaryFileBuffer> bfbs = new ArrayList<>();
			BufferedReader br1 =  new BufferedReader(new InputStreamReader(new FileInputStream(new File("c:\\file1.txt"))));
			BinaryFileBuffer bfb = new BinaryFileBuffer(br1);
			bfbs.add(bfb);
			BufferedReader br2 =  new BufferedReader(new InputStreamReader(new FileInputStream(new File("c:\\file1.txt"))));
			BinaryFileBuffer bfb2 = new BinaryFileBuffer(br2);
			bfbs.add(bfb2);
			PriorityQueue<BinaryFileBuffer> pq = new PriorityQueue<>(11, new Comparator<BinaryFileBuffer>() {
				@Override
				public int compare(BinaryFileBuffer i, BinaryFileBuffer j) {
					return defaultcomparator.compare(i.peek(), j.peek());
				}
			});
			
			for (BinaryFileBuffer bfbr : bfbs) {
				if (!bfbr.empty()) {
					pq.add(bfbr);
				}
			}
			BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("c:\\sorted.txt")));
			while (pq.size() > 0) {
				BinaryFileBuffer bfbout = pq.poll();
				String r = bfbout.pop();
				fbw.write(r);
				fbw.newLine();
				if (bfbout.empty()) {
					bfbout.fbr.close();
				} else {
					pq.add(bfbout); // add it back
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	
	
}


/**
 * This is essentially a thin wrapper on top of a BufferedReader... which keeps
 * the last line in memory.
 *
 */
final class BinaryFileBuffer {
	public BinaryFileBuffer(BufferedReader r) throws IOException {
		this.fbr = r;
		reload();
	}

	public void close() throws IOException {
		this.fbr.close();
	}

	public boolean empty() {
		return this.cache == null;
	}

	public String peek() {
		return this.cache;
	}

	public String pop() throws IOException {
		String answer = peek().toString();// make a copy
		reload();
		return answer;
	}

	private void reload() throws IOException {
		this.cache = this.fbr.readLine();
	}

	public BufferedReader fbr;

	private String cache;

}