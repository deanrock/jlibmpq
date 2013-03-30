/*
 *  mpq_jni.cpp -- some hooks to work with Java
 *
 *  Copyright (C) 2003 Ryan Henszey <henszey@gmail.com>
 * http://zthread.com/pages/jlibmpq.php
 *
 * So I uhh guess this is subject to the gnu since it is an extention or
 *  something like that, I really have no idea.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  $Id: mpq.h,v 1.1 2005/04/09 22:09:18 ufoz Exp $
 */

#include <jni.h>
#include <iostream>
using namespace std;

#include "com_zthread_libmpq_MPQ.h"
#include "mpq.h"

bool initialized = false;

//Only 16 open at a time, why? why not!
bool *mpq_a_used = new bool[16];
mpq_archive * mpq_a = new mpq_archive[16];



JNIEXPORT jstring JNICALL Java_com_zthread_libmpq_MPQ_getLibraryVersion(JNIEnv * env, jclass c) {
	
	char* version = libmpq_version();

	return env->NewStringUTF(version);

}


JNIEXPORT jint JNICALL Java_com_zthread_libmpq_MPQ_openArchive(JNIEnv * env, jobject obj, jstring jarchive){
	
	if(!initialized){
		for(int i = 0; i < 16; i++){
			mpq_a_used[i] = false;
		}
		initialized = true;
	}

    jboolean iscopy;
    const char *archive_file = env->GetStringUTFChars(jarchive, &iscopy);

	int archiveHandle = -1;

	for(int i = 0; i < 16; i++){
		if(mpq_a_used[i] == false){
			mpq_a_used[i] = true;
			archiveHandle = i;
			break;
		}
	}

	int code = libmpq_archive_open(&mpq_a[archiveHandle],(unsigned char *)archive_file);

	env->ReleaseStringUTFChars(jarchive, archive_file);

	if(code < 0){
		mpq_a_used[archiveHandle] = false;
		return code;
	}
	else{
		return archiveHandle;
	}
	

}


JNIEXPORT jint JNICALL Java_com_zthread_libmpq_MPQ_getArchiveInfo(JNIEnv * env, jobject obj,jint archiveHandle, jint info){

	return libmpq_archive_info(&mpq_a[archiveHandle],info);

}

JNIEXPORT jint JNICALL Java_com_zthread_libmpq_MPQ_closeArchive(JNIEnv * env, jobject obj, jint archiveHandle){

	mpq_a_used[archiveHandle] = false;

	return libmpq_archive_close(&mpq_a[archiveHandle]);

}


JNIEXPORT jint JNICALL Java_com_zthread_libmpq_MPQ_getFileInfoByNumber(JNIEnv *env , jobject obj,jint archiveHandle, jint file, jint info){

	return libmpq_file_info(&mpq_a[archiveHandle],info,file);

}

JNIEXPORT jbyteArray JNICALL Java_com_zthread_libmpq_MPQ_getFileData(JNIEnv * env, jobject obj, jint archiveHandle, jint file){


	int size = libmpq_file_info(&mpq_a[archiveHandle], LIBMPQ_FILE_UNCOMPRESSED_SIZE, file);

	jbyteArray jb;
	if(size > 1){
		char * buffer = new char[size];

		int code = libmpq_file_getdata(&mpq_a[archiveHandle],file,(unsigned char*)buffer);

		jb = env->NewByteArray(size);

		env->SetByteArrayRegion(jb, 0, size, (jbyte *)buffer);

		//delete[] buffer;
	}
	else{
		jb = env->NewByteArray(0);
	}
	
	return jb;

}


JNIEXPORT jint JNICALL Java_com_zthread_libmpq_MPQ_getFileNumberByName(JNIEnv * env, jobject obj, jint archiveHandle, jstring jfile){

    jboolean iscopy;
    const char *file = env->GetStringUTFChars(jfile, &iscopy);
	

	int file_number = libmpq_file_number(&mpq_a[archiveHandle], (const char *)file);

	env->ReleaseStringUTFChars(jfile, file);
	return file_number;



}