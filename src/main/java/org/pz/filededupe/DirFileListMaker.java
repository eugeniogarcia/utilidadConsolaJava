/*
 * Looks for duplicate files based on CRC-32 checksumming.
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.stream.Collectors;


class DirFileListMaker
{
	ArrayList<Path> go( Path dir, boolean skipSubDirs ) {
		if( dir == null || dir.toString().isEmpty() ) {
			throw( new InvalidParameterException(
					"Error: Directory to process is null or empty in " +
							this.getClass().getSimpleName()));
		}

		ArrayList<Path> fileSet;
		try {
			//Files.walk's second param gives depth of subdirs to search
			// Integer.MAX_VALUE means, search all subdirectories
			fileSet =
					Files.walk( dir, skipSubDirs? 1 : Integer.MAX_VALUE )
					.filter( p -> p.toFile().isFile() )
					.peek( System.out::println )
					.collect( Collectors.toCollection( ArrayList::new ));
		} catch( final Throwable t ) {
			System.err.println("Error creating fileset in " + dir +
					"Directory will be skipped." );
			//return an empty ArrayList in case of error
			return( new ArrayList<>(0) );
		}
		return( fileSet );
	}
}
