package com.zthread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import com.zthread.libmpq.MPQ;
import com.zthread.libmpq.MPQFile;

public class Main {
	public static void main(String[] args) throws Exception{
		
		String path = "C:\\Warcraft III\\";
		/*
		String[] mpqs = {"patch.MPQ", "texture.MPQ", "model.MPQ", "wmo.MPQ", 
				"terrain.MPQ", "interface.MPQ", "misc.MPQ", "dbc.MPQ", "base.MPQ", 
				"sound.MPQ", "fonts.MPQ", "speech.MPQ"};*/
		String[] mpqs = {"war3.MPQ"};
		
		for(int i = 0; i < mpqs.length; i++){
			MPQ mpq = new MPQ(path+mpqs[i]);
			
			System.out.println(mpqs[i]);
			System.out.println();
			System.out.println("lib version--------'" + MPQ.getLibraryVersion() + "'");
			System.out.println();
			//System.out.println("open code----------'" + mpq.openArchive(archive) + "'");
			System.out.println();
			System.out.println("archive size-------'" + mpq.getArchiveSize() + "'");
			System.out.println("hash table size----'" + mpq.getHashTableSize() + "'");
			System.out.println("block table size---'" + mpq.getBlockTableSize() + "'");
			System.out.println("block size---------'" + mpq.getBlockSize() + "'");
			System.out.println("number of files----'" + mpq.getNumberOfFiles() + "'");
			System.out.println("compressed size----'" + mpq.getCompressedSize() + "'");
			System.out.println("uncompressed size--'" + mpq.getUncompressedSize() + "'");
			
			String[] fileList = mpq.getFileList();
			

			for(int j = 0; j < fileList.length; j++){
			//for(int j = 0; j < 10; j++){
			
				
				
				MPQFile mpqFile = mpq.getFile(fileList[j]);
				
				System.out.println(mpqFile.getFileNumber() + "\t" + mpqFile.getUncompressedSize() + "\t" + mpqFile.getFileName());

				new File(mpqFile.getFileName().substring(0,mpqFile.getFileName().lastIndexOf("\\"))).mkdirs();
				File file = new File(mpqFile.getFileName());
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(mpqFile.getData());
				fos.close();
				

				if(mpqFile.getFileName().toLowerCase().indexOf(".blp") != -1){
		      String line;
		      String output = "";
		      Process p = Runtime.getRuntime().exec("BLP2toTGA.exe " + mpqFile.getFileName());
		      BufferedReader input = 
		        new BufferedReader
		          (new InputStreamReader(p.getInputStream()));
		      while ((line = input.readLine()) != null) {
		     	 output+=line;
	        }
		      input.close();
				}
		     
		    // if(output.indexOf("Error") == -1)
		     //file.delete();
				
			}

			mpq.closeArchive();
			System.out.println();
		}
		
		
/*
		MPQ mpq = new MPQ(path+mpqs[9]);
		
		byte[] wav = mpq.getFile("Sound\\Music\\CityMusic\\Gnomeragon\\gnomeragon01-zone.mp3");
		
		System.out.println(wav.length);
		
		FileOutputStream fos = new FileOutputStream("gnomeragon01-zone.mp3");
		
		fos.write(wav);
		
		fos.close();

		mpq.closeArchive();
		*/
		
		
		/*
		for(int i = 1; i < 2; i++){
			System.out.println();
			System.out.println("compressed size----'" + mpq.getFileInfoByNumber(0, i,LIBMPQ_FILE_COMPRESSED_SIZE) + "'");
			System.out.println("uncompressed size--'" + mpq.getFileInfoByNumber(0, i,LIBMPQ_FILE_UNCOMPRESSED_SIZE) + "'");
			System.out.println("compression type---'" + mpq.getFileInfoByNumber(0, i,LIBMPQ_FILE_COMPRESSION_TYPE) + "'");
			System.out.println("type int-----------'" + mpq.getFileInfoByNumber(0, i,LIBMPQ_FILE_TYPE_INT) + "'");
			System.out.println("type char----------'" + mpq.getFileInfoByNumber(0, i,LIBMPQ_FILE_TYPE_CHAR) + "'");
			System.out.println("hash 1-------------'" + mpq.getFileInfoByNumber(0, i,LIBMPQ_FILE_HASH1) + "'");
			System.out.println("hash 2-------------'" + mpq.getFileInfoByNumber(0, i,LIBMPQ_FILE_HASH1) + "'");
			System.out.println();
		
		}
		for(int i = 1; i < 2; i++){
			System.out.println("file data----------'" + mpq.getFileData(0, 4902) + "'");
		}
		
		System.out.println("ListFilenum--------'" + mpq.getFileNumberByName(0,"(listfile)") + "'");
		
		System.out.println("close code---------'" + mpq.closeArchive(0) + "'");
		*/
		

	}
}
