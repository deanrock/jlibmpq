package com.zthread.libmpq;

public class MPQ {
	
	private static final String LIST_FILE = "(listfile)";
	
	private static final int LIBMPQ_MPQ_ARCHIVE_SIZE				= 1;		/* MPQ archive size */
	private static final int LIBMPQ_MPQ_HASHTABLE_SIZE			= 2;		/* MPQ archive hashtable size */
	private static final int LIBMPQ_MPQ_BLOCKTABLE_SIZE		= 3;		/* MPQ archive blocktable size */
	private static final int LIBMPQ_MPQ_BLOCKSIZE					= 4;		/* MPQ archive blocksize */
	private static final int LIBMPQ_MPQ_NUMFILES						= 5;		/* Number of files in the MPQ archive */
	private static final int LIBMPQ_MPQ_COMPRESSED_SIZE		= 6;		/* Compressed archive size */
	private static final int LIBMPQ_MPQ_UNCOMPRESSED_SIZE 	= 7;		/* Uncompressed archive size */
	

	
	static {
		System.loadLibrary("libmpq");
	}
	
	public static native String getLibraryVersion();
	private native int openArchive(						String file);
	private native int getArchiveInfo(				int archiveHandle, int info);
	protected native int getFileInfoByNumber(		int archiveHandle, int file, int info);
	protected native byte[] getFileData(				int archiveHandle, int file);
	private native int getFileNumberByName(		int archiveHandle, String file);
	private native int closeArchive(					int archiveHandle);
	
	
	protected int archiveHandle = -1;
	
	
	public MPQ(String archive) throws Exception {
		int code = openArchive(archive);
		
		if(code < 0)
			throw new Exception("Opening MPQ Error Code: " + code);
		else
			archiveHandle = code;
	}
	
	public int getArchiveSize(){
		return getArchiveInfo(archiveHandle, LIBMPQ_MPQ_ARCHIVE_SIZE);
	}
	
	public int getHashTableSize(){
		return getArchiveInfo(archiveHandle, LIBMPQ_MPQ_HASHTABLE_SIZE);
	}
	
	public int getBlockTableSize(){
		return getArchiveInfo(archiveHandle, LIBMPQ_MPQ_BLOCKTABLE_SIZE);
	}
	
	public int getBlockSize(){
		return getArchiveInfo(archiveHandle, LIBMPQ_MPQ_BLOCKSIZE);
	}
	
	public int getNumberOfFiles(){
		return getArchiveInfo(archiveHandle, LIBMPQ_MPQ_NUMFILES);
	}
	
	public int getCompressedSize(){
		return getArchiveInfo(archiveHandle, LIBMPQ_MPQ_COMPRESSED_SIZE);
	}
	
	public int getUncompressedSize(){
		return getArchiveInfo(archiveHandle, LIBMPQ_MPQ_UNCOMPRESSED_SIZE);
	}
	
	public int closeArchive(){
		return closeArchive(archiveHandle);
	}
	
	public String[] getFileList() throws Exception {
		int file = getFileNumberByName(archiveHandle,"(listfile)");
		
		if(file < 1)
			throw new Exception("No file list (or other error): " + file);
		
		String fileCrap = new String(getFileData(archiveHandle,file));
		
		return fileCrap.split("\r\n");
	}
	
	public MPQFile getFile(String fileName){
		int file = getFileNumberByName(archiveHandle,fileName);
		
		return new MPQFile(this,file,fileName);
	}
	
	public MPQFile getFile(int file){

		return new MPQFile(this,file,"");

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
