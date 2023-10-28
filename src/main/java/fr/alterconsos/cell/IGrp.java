package fr.alterconsos.cell;

import fr.hypertable.AppException;

public interface IGrp {

	void addedDir(int dir)  throws AppException ;
	
	void removedDir(int dir) throws AppException ;
	
	void setup(String initiales, String nom) throws AppException ;
	
	void supprGrp(int date);
	
}
