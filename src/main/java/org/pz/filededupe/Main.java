/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Main {

	public static void main( final String[] args )
	{
		// default is to visit subdirectories
		boolean nosubdirs = false;

		printCopyright();

		if( args.length == 0 ) {
			showUsage();
			return;
		}

		//Mira si nos estan preguntando por la ayuda
		final List<String> argList = Arrays.asList(args);
		if( argList.contains( "-h" )  || argList.contains( "-help" ) ||
				argList.contains( "--h" ) || argList.contains( "--help" )) {
			showUsage();
			return;
		}


		final LinkedList<String> dirs = new LinkedList<>();
		//Añade los directorios a la lista
		for( final String arg : args ) {
			if( !arg.startsWith("-") ) {
				dirs.add( arg );
			}
			else {
				if( arg.equalsIgnoreCase( "-nosubdirs" ) ||
						arg.equalsIgnoreCase( "--nosubdirs")) {
					nosubdirs = true;
				}
				else {
					System.err.println( "Invalid command: " + arg );
				}
			}
		}

		//Creamos nuestra tabla
		// Create the dupe table, where file checksums are stored
		final LongStringListTable dupesTable = new LongStringListTable();

		// Create the directory deduping engine
		final DirDeduper dirDeduper = new DirDeduper();

		// Call the deduping engine on each specified directory
		if( dirs.size() > 0 ) {
			for( final String dir : dirs ) {
				dirDeduper.go( dir, nosubdirs, dupesTable );
			}
		}
		else {  //happens only if a single dash option other than -h is specified
			System.err.println( "Error: no directory specified. Exiting");
		}

		// Scan the dupesTable and print out all duplicates to stdout
		final DupesOutput dupesList = new DupesOutput();
		final int dupesCount = dupesList.showDupes( dupesTable );

		System.out.println( "Number of duplicates found: " + dupesCount );
	}

	/**
	 * Simply prints the copyright
	 */
	private static void printCopyright() {
		System.out.println(
				"FileDedupe v.1.2 (c) Copyright 2017-20 Andrew Binstock. All rights reserved.\n" );
	}

	/**
	 * Explains program usage
	 */
	private static void showUsage() {

		System.out.println(
				"FileDedupe finds duplicate files in a diretory or disk drive.\n" +
						"arguments: one or more directories to process;\n" +
						"           -nosubdirs do not check subdirectories (default: checks all subdirs)\n" +
						"           -h or -help prints this message"
				);
	}
}
