package fr.hypertable;

import fr.alterconsos.cell.Directory;

public interface IAuthChecker {

	/**
	 * L'objectif est FIXER le niveau d'authentification atteient, ET NON de
	 * s'assurer qu'il convient (c'est le rôle des opérations).<br>
	 * Le retour false indique une tentative de prétendre à un niveau
	 * d'authentification alors que la session cliente ne l'a pas. AuthId va
	 * représenter le "username".<b> AuthType indique le niveau / type
	 * d'authentification : 0 : aucune authentification 1 : admin, la plus
	 * élevée autres libres : cadre 1, cadre 2, utilisateur courant ...
	 */
    boolean verifierAuth() throws AppException;

	void setInternalTask(String dirId) throws AppException;

	int getAuthType();

	int getAuthGrp();

	int getAuthUsr();

	int getAuthDir();

	String getAuthDiag();

	int getAuthInfo();

	int getAuthPower();
	
	int[] getDirs();

	String toSHA1(String pwd, int authType, int authGrp);
	
	boolean isMyDir(int dir);
	
	Directory[] myDirs();
	
	/**
	 * Droit d'acces d'une cellule 0 : aucun 1 : restreint
	 * 
	 * 9 : full
	 * 
	 * @param line
	 * @param column
	 * @return
	 */
    int accessLevel(String line, String column, String type);

	int ADMINGEN = -1;
	int ADMINLOC = -2;
	int TASK = -3;

}
