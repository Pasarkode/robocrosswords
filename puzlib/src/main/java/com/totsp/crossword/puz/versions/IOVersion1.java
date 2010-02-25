package com.totsp.crossword.puz.versions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.totsp.crossword.puz.Box;
import com.totsp.crossword.puz.IO;
import com.totsp.crossword.puz.Puzzle;
import com.totsp.crossword.puz.PuzzleMeta;

public class IOVersion1 implements IOVersion {

	public void read(Puzzle puz, InputStream is) throws IOException {
		PuzzleMeta meta = this.readMeta(is);
		puz.setSource(meta.source);
		puz.setDate(meta.date);
		DataInputStream dis = new DataInputStream(is);
		Box[][] boxes = puz.getBoxes();
		for(Box[] row : boxes ){
			for(Box b : row){
				if(b == null){
					continue;
				}
				b.cheated = dis.readBoolean();
				b.responder = IO.readNullTerminatedString(dis);
			}
		}
		try{
			puz.setTime(dis.readLong());
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		
	}

	public PuzzleMeta readMeta(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		PuzzleMeta meta = new PuzzleMeta();
		meta.author = IO.readNullTerminatedString(dis);
		meta.source = IO.readNullTerminatedString(dis);
		meta.title = IO.readNullTerminatedString(dis);
		meta.date = new Date( dis.readLong() );
		meta.percentComplete = dis.readInt();
		return meta;
		
	}

	public void write(Puzzle puz, OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		IO.writeNullTerminatedString(dos, puz.getAuthor());
		IO.writeNullTerminatedString(dos, puz.getSource());
		IO.writeNullTerminatedString(dos, puz.getTitle());
		dos.writeLong(puz.getDate() == null ? 0 : puz.getDate().getTime());
		dos.writeInt(puz.getPercentComplete());
		Box[][] boxes = puz.getBoxes();
		for(Box[] row : boxes ){
			for(Box b : row){
				if(b == null){
					continue;
				}
				dos.writeBoolean(b.cheated);
				IO.writeNullTerminatedString(dos, b.responder);
			}
		}
		dos.writeLong(puz.getTime());
	}

}
