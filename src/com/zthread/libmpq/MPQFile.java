package com.zthread.libmpq;

public class MPQFile {
	
	private static final int LIBMPQ_FILE_COMPRESSED_SIZE		= 1;		/* MPQ compressed filesize of given file */
	private static final int LIBMPQ_FILE_UNCOMPRESSED_SIZE	= 2;		/* MPQ uncompressed filesize of given file */
	private static final int LIBMPQ_FILE_COMPRESSION_TYPE	= 3;		/* MPQ compression type of given file */
	private static final int LIBMPQ_FILE_TYPE_INT					= 4;		/* file is given by number */
	private static final int LIBMPQ_FILE_TYPE_CHAR					= 5;		/* file is given by name */
	private static final int LIBMPQ_FILE_HASH1							= 6;		/* hash value 1 */
	private static final int LIBMPQ_FILE_HASH2							= 7;		/* hash value 2 */
	
	//private final int archiveHandle;
	private final MPQ mpq;
	private final int file;
	private final String name;
	
	
	public MPQFile(MPQ mpq, int file, String name){
		this.mpq = mpq;
		this.file = file;
		this.name = name == null ? "" : name;

	}
	
	public int getFileNumber(){ return file; }
	public String getFileName(){ return name; }
	
	public int getCompressedSize(){
		return mpq.getFileInfoByNumber(mpq.archiveHandle, file, LIBMPQ_FILE_COMPRESSED_SIZE);
	}
	public int getUncompressedSize(){
		return mpq.getFileInfoByNumber(mpq.archiveHandle, file, LIBMPQ_FILE_UNCOMPRESSED_SIZE);
	}
	public int getCompressionType(){
		return mpq.getFileInfoByNumber(mpq.archiveHandle, file, LIBMPQ_FILE_COMPRESSION_TYPE);
	}
	public int getFileType(){
		return mpq.getFileInfoByNumber(mpq.archiveHandle, file, LIBMPQ_FILE_TYPE_INT);
	}
	public char getFileTypeChar(){
		return (char)mpq.getFileInfoByNumber(mpq.archiveHandle, file, LIBMPQ_FILE_TYPE_CHAR);
	}
	public int getHash1(){
		return mpq.getFileInfoByNumber(mpq.archiveHandle, file, LIBMPQ_FILE_HASH1);
	}
	public int getHash2(){
		return mpq.getFileInfoByNumber(mpq.archiveHandle, file, LIBMPQ_FILE_HASH2);
	}
	
	public byte[] getData(){
		return mpq.getFileData(mpq.archiveHandle,file);
	}
}
