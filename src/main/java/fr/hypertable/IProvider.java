package fr.hypertable;

import java.util.LinkedList;

import fr.hypertable.AppTransactionAdmin.Dumper;

public interface IProvider {
	void init(boolean hasCache) throws AppException;
	
	String tmOperation(String op, String taskid, String url, String periode) throws AppException;
	boolean isToStop(String taskid);
	
	void close();
	void setOperation(String operation);
	
	boolean getOnOff();
	byte[] getDocument(String k) throws AppException;
	void putDocument(long version, String k, byte[] bytes) throws AppException;
	int purgeDocument(long version) throws AppException;
	
	void beginTransaction(boolean multi) throws AppException ;
	void rollBack();
	void commit(boolean hasQueuedTasks) throws AppException;
	
	void purgeCellFromStorage(String line, String column, String cellType) throws AppException;
//	public CachedCell getFromMemCache(String cacheKey);
//	public void putInMemCache(String cacheKey, long version, byte[] bytes);
	
	String onOff(boolean on) throws AppException;
	void listLinesS(String ver, LinkedList<String> lst, Filter f) throws AppException;
	void listDocs(long minVersion, LinkedList<String> lst, Filter f) throws AppException;
	void dumpLineS(Dumper d) throws AppException;
	void delLine(String line) throws AppException;
	
	Archive getStorageArchive(String line, String column, String cellType, boolean nullIfNot)
			throws AppException;
	void putArchiveInStorage(Archive a, long version, boolean creation) throws AppException;
	
	Versions getVersionsFromStorage(String line) throws AppException;
	
	CachedCell getCellFromStorage(String line, String column, String cellType)
			throws AppException;
	void putInStorage(String line, String column, String cellType,
                      long version, byte[] bytes, boolean creation) throws AppException;
	
	boolean removeTaskFromDB(String taskid) throws AppException;

	String enQueue(String url, byte[] body) throws AppException;
	
	String enQueueCron(String url, String nextStart) throws AppException;
	
}
