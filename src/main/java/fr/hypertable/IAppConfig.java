package fr.hypertable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.Properties;

import org.json.simple.AcJSONObject;

import fr.alterconsos.AppConfig.MailServerException;

public interface IAppConfig {
	Logger log();

	IProvider newProvider();

	String build();

	String myUrl();
	
	boolean isAdmin(String key);
	
	String dhReelle();
	
	SimpleDateFormat sdf1();

	SimpleDateFormat sdfj();

	SimpleDateFormat sdfjhs();

	SimpleDateFormat sdfd();

	SimpleDateFormat sdfjhsm();
		
	String[] requestArgs();

	TimeZone timezone();

	int maxRetries();

	int maildelai();

	Operation operation(String name) throws AppException;

	Task task(String name) throws AppException;

	IAuthChecker authChecker();

	Date aammjj2Date(int aammjj);

	int getMondayOf(int aammjj, int nbWeeks);
	
	Date newDate();
	
	int aujourdhui();

	int getDayOfWeek();
	
	int getDayOfWeek(int d);

	int maintenant();

	int maintenantSimul();

	String mail_password();

	String mapsgoogle_apis();

	String mailserver();

	String url4mail();

	String[] contacts();

	void reloadConfig(AcJSONObject arg, Properties keys);
		
	String getEMails(String lst);
	
	String parseEmails(String email);
	
	String pingMail(String errMsg) throws MailServerException;
	
	String postMail(String mailer, String url, String to, String subject,
                    String text, String errMsg) throws MailServerException;
	
	int maxConnections();
	
	String dbURL();
	
	String username();
	
	String password();

	int providerWorkers();
	
}
