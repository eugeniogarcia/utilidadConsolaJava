package org.pz.filededupe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LongStringListTable {

	/**
	 *  the main data structure, with a key consisting of a long
	 *  and a value consisting of an ArrayList of filenames.
	 */
	private final HashMap<Long, ArrayList<String>> table;

	public LongStringListTable() {
		table = new HashMap<>();
	}

	/**
	 * Inserts a new filename and a numeric value associated
	 * with the file (size, cheksum, etc.) This numeric is
	 * the key to the entry; an ArrayList of associated filenames
	 * is the value.
	 *   If a matching numeric key already exists in the table,
	 * then the filename is added to the list of files associated
	 * with the numeric key.
	 *
	 * @param filename  the filename to add
	 * @param numeric  the numeric associated with the filename
	 */
	public void insertEntry( String filename, Long numeric ) {
		final ArrayList<String> tableEntry =
				table.computeIfAbsent( numeric, c -> new ArrayList<>() );
		tableEntry.add( filename );
	}

	public Set<Long> getKeySet() {
		return table.keySet();
	}

	public ArrayList<String> getEntry( long key ) {
		return table.get( key );
	}
}
