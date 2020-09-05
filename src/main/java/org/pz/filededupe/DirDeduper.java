/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

class DirDeduper {

	private LongStringListTable chksumTable;
	public DirDeduper() {}

	/**
	 * Main method: validates directory, gets paths of all files (incl. subidrectories), 
	 *   creates and loads the table of checksums for the files, and then prints out duplicates.
	 * @param pathToDir directory to scan
	 * @param noSubdirFlag skip scanning the subdirectories?
	 * @param table the table to hold the checksums
	 * @return total number of files examined
	 */
	public int go( String pathToDir, boolean noSubdirFlag, LongStringListTable table) {
		final String origPath = Objects.requireNonNull( pathToDir );
		chksumTable = table;

		final File dir = new File( pathToDir );
		if( !dir.isDirectory() ) {
			throw( new InvalidPathException(
					pathToDir, "Error: " + pathToDir + " is not a directory" ));
		}

		// create a list of all the files in the directory and its subdirectories
		final Path path = FileSystems.getDefault().getPath( origPath );
		final ArrayList<Path> fileSet = new DirFileListMaker().go( path, noSubdirFlag );
		if( fileSet.isEmpty() ) {
			System.out.println("Directory " + origPath + " contains no files");
			return( 0 );
		}

		System.out.println("Number of files found to check: " + fileSet.size());

		// calculate checksum for every file in fileSet and insert it into a hash table
		fileSet.forEach( this::updateChecksums );

		// return the total number of files examined
		return( fileSet.size() );
	}

	/**
	 * Calculates the checksum for a file and puts in the chksumTable.
	 * @param p the filepath for the file to checksum
	 */
	void updateChecksums( Path p ) {
		try {
			final long chksum = new FileChecksum(p).calculate();
			chksumTable.insertEntry( p.toString(), chksum );
			//  System.out.println( "checksum: " + chksum + " file: " + p );
		} catch( final IOException e ) {
			// error message has already been displayed, so skip this file
		}
	}
}