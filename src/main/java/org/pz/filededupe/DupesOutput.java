/*
 * Looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2020 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.util.ArrayList;
import java.util.Set;

public class DupesOutput {

	public DupesOutput() {}

	/**
	 * The class that does the work
	 * @param dupes the table of file checksums, which shows the duplicate files
	 * @return the number of duplicates found
	 */
	public int showDupes(  LongStringListTable dupes ) {

		int dupesCount = 0;

		// get a set of all the keys (which are checksums)
		final Set<Long> keys = dupes.getKeySet();

		// go down the list looking for checksums with more than one associated file
		for( final Long key : keys ) {
			final ArrayList<String> paths = dupes.getEntry( key );
			if( paths.size() > 1) {
				dupesCount += paths.size();
				System.out.println( "These files are the same:");
				for( final String filepath : paths) {
					System.out.println( "\t" + filepath );
				}
				System.out.println();
			}
		}
		return( dupesCount );
	}
}
