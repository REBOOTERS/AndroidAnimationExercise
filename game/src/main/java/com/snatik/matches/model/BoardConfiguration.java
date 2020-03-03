package com.snatik.matches.model;

public class BoardConfiguration {

	private static final int _6 = 6;
	private static final int _12 = 12;
	private static final int _18 = 18;
	private static final int _28 = 28;
	private static final int _32 = 32;
	private static final int _50 = 50;

	public final int difficulty;
	public final int numTiles;
	public final int numTilesInRow;
	public final int numRows;
	public final int time;

	public BoardConfiguration(int difficulty) {
		this.difficulty = difficulty;
		switch (difficulty) {
		case 1:
			numTiles = _6;
			numTilesInRow = 3;
			numRows = 2;
			time = 60;
			break;
		case 2:
			numTiles = _12;
			numTilesInRow = 4;
			numRows = 3;
			time = 90;
			break;
		case 3:
			numTiles = _18;
			numTilesInRow = 6;
			numRows = 3;
			time = 120;
			break;
		case 4:
			numTiles = _28;
			numTilesInRow = 7;
			numRows = 4;
			time = 150;
			break;
		case 5:
			numTiles = _32;
			numTilesInRow = 8;
			numRows = 4;
			time = 180;
			break;
		case 6:
			numTiles = _50;
			numTilesInRow = 10;
			numRows = 5;
			time = 210;
			break;	
		default:
			throw new IllegalArgumentException("Select one of predefined sizes");
		}
	}

}
