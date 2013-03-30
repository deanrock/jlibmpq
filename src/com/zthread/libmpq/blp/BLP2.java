package com.zthread.libmpq.blp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BLP2 {
	
	public final int GL_RGBA8 = 0x8058;
	public final int GL_COMPRESSED_RGB_S3TC_DXT1_EXT = 0x83F0;
	public final int GL_COMPRESSED_RGBA_S3TC_DXT1_EXT = 0x83F1;
	public final int GL_COMPRESSED_RGBA_S3TC_DXT3_EXT = 0x83F2;
	public final int GL_COMPRESSED_RGBA_S3TC_DXT5_EXT = 0x83F3;
	
	public final byte[] blp2File;
	
	public long width;
	public long height;
	public long offsets[] = new long[16];
	public long sizes[] = new long[16];

	private byte[] attr = new byte[4];
	
	public int format;
	
	private boolean compressed;
	
	private boolean mipmapped;
	
	public int blocksize;
	
	
	public BLP2(String file) throws IOException{
		this(new File(file));
	}
	
	public BLP2(File file) throws IOException{
		FileInputStream fis = new FileInputStream(file);
		byte[] blpBytes = new byte[fis.available()];
		fis.read(blpBytes);
		fis.close();
		this.blp2File = blpBytes;
		init();
	}
	
	public BLP2(byte[] blp2File){
		this.blp2File = blp2File;
		init();
	}
	
	private void init(){

		int pos = 0;
		
		
//	0-4
		byte[] btemp = new byte[4];
		System.arraycopy(blp2File,pos,btemp,0,4);
		pos+=4;
		
		//System.out.println((char)btemp[0] + "\t" + (char)btemp[1] + "\t" + (char)btemp[2] + "\t" + (char)btemp[3]);
		
		if(btemp[3] != '2'){
			//System.out.println("not BLP2");
			return;
		}
//	4-8
		long ltemp = arr2long(blp2File,pos);
		pos+=4;
		
		//if(ltemp == 1)
		//	System.out.println("This is probably a BLP");
		
//	8-12
		System.arraycopy(blp2File,pos,attr,0,4);
		pos+=4;
		
		//System.out.println((int)attr[0] + "\t" + (int)attr[1] + "\t" + (int)attr[2] + "\t" + (int)attr[3]);
		
		if((int)attr[0] == 2){
			compressed = true;
			format = GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
			
			blocksize = 8;
			
			if(attr[1] == 8){
				format = GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
				blocksize = 16;
			}
			else{
				if(attr[3] == 0){
					format = GL_COMPRESSED_RGB_S3TC_DXT1_EXT;
				}
			}
			
		}
		else if((int)attr[0] == 1){
			compressed = false;
			format = GL_RGBA8;
		}
		else{
			System.out.println("throwing exception....");
		}
		
		if(attr[3] != 0){
			mipmapped = true;
		}
		else{
			mipmapped = false;
		}
		
		
//	12-16
		width = arr2long(blp2File,pos);
		pos+=4;
		//System.out.println(width);
		
//	12-20
		height = arr2long(blp2File,pos);
		pos+=4;
		//System.out.println(height);
		
//	20-84
		for(int i = 0; i < offsets.length; i++){
			offsets[i] = arr2long(blp2File,pos);
			//System.out.println(offsets[i]);
			pos+=4;
		}
		
//	84-148
		for(int i = 0; i < sizes.length; i++){
			sizes[i] = arr2long(blp2File,pos);
			//System.out.println(sizes[i]);
			pos+=4;
		}
	}
	
	private static long arr2long (byte[] arr, int start) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}
	
}
