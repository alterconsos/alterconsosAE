# L'application alterconsosAE

Cette implémentation de l'application fonctionne sous Google App Engine, java 17, _legacy bundle_.

Ce bundle inclut TaskQueue qui est utilisé pour les tasks.

Elle est dérivée de la version fonctionnant sous Java 8 avec quelques adaptations:
- AppImage, qui n'est PAS dans le _legacy bundle_, n'est plus utilisé (voir `fr.hypertable.StorePhoto`), remplacée par du pur Java de base.
- la gestion des mots de passe et keys a été reportée dans `WEB-ING.keys.properties` qui **ne doit pas être stocké dans git**.

## Installation dans IntelliJ

Dans `Menu >> New Project from existing sources` : ça créé le projet avec `src/main/java` comme _source directory_. 

Si ce n'est pas le cas, un clic droit sur ce folder ouvre une option `Mark Directory as source`

## Installation de jars externes

Normalement les jars externes sont déclarés comme <dependency> dans pom.xml.

Toutefois les jars poi poi-ooxml poi-ooxml-schemas fonctionnent avec leurs versions 3.9-20121203 et pas avec les versions postérieures disponibles dans Maven Central.

Il faut donc les installer dans un repository maven local.

Voir: https://softwarecave.org/2014/06/14/adding-external-jars-into-maven-project/

Créer un folder `./lib`

Pour **chaque** jar, l'enregistrer pour maven: (voir le script `docs/installJars.sh`)

	mvn install:install-file -Dfile=libext/poi-3.9-20121203.jar \
	-DartifactId=poi -Dversion=3.9-20121203 \
	-DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib

Puis dans pom.xml:

	<repositories>
		<repository> <!-- Un seul repository local -->
		<id>project.local</id>
		<name>project</name>
		<url>file:${project.basedir}/lib</url>
		</repository>
	</repositories>
	
	<dependencies>
		<!-- Une dependency par jar -->
		<dependency>
			<groupId>com.example</groupId>
			<artifactId>poi</artifactId>
			<version>3.9-20121203</version>
		</dependency>

En cas de problème de cache sur le local repository exécuter dans un terminal:

	mvn dependency:purge-local-repository

## Build

Créer une configuration de run: `Menu >> Run >> Edit configurations`

- Maven:
	+ Name: "Compile"
	+ Run: "compile war:war"

Ou, dans un terminal: `mvn compile war:war`

## Test local

Créer deux configurations de run: `Menu >> Run >> Edit configurations`

- Maven
	+ Name: "Run alterconsos"
	+ Run: "appengine:run"
- Remote JVM debug
	+ Name: "Debug alterconsos"
	+ normalement les paramètres sont OK

Commandes:

	Dans IntelliJ: Menu >> Run >> Run alterconsos
	
	OU dans un terminal: 
	mvn appengine:run
	
	OU dans un terminal:
	~/Téléchargements/google-cloud-sdk/bin/java_dev_appserver.sh target/alterconsos-1.0

La base de données est installée dans: 

	~/IdeaProjects/alterconsosAE/target/alterconsos-1.0/WEB-INF/appengine-generated/local_db.bin

Penser à la backuper pour reprendre des tests dans le directory `backups` qui **n'est PAS dans git**.

Le chargement des données opérationnelles dans une base de test vierge (non existante) se fait:

- d'abord en ouvrant la page `localhost:8080/admin.html`
	+ Donner le mot de passe d'administration
  	+ Mettre ON
- par l'application **acDL**
  	+ dumper le site de production
  	+ déclarer le site de test `localhost:8080/` et son mot de passe d'administration (le même qu'en production sans quand on l'a changé pour une nouvelle version).

Quand la base est chargée, ouvrir l'application par `localhost:8080/`.

## Debug

Dans `pom.xml` il faut compléter la `<configuration>` du plugin `appengine-maven-plugin` :

Si on veut un point d'arrêt dès le debug, par exemple dans le chargement de la config dans AppConfig, il mettre `suspend=y` dans `appengine-maven-plugin / jvmFlags`.

	<plugin>
		...
		<artifactId>appengine-maven-plugin</artifactId>
		<configuration>
			...
			<jvmFlags>
			<jvmFlag>-Xdebug</jvmFlag>
			<jvmFlag>-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n</jvmFlag>
			</jvmFlags>
		</configuration>
	</plugin>

Commandes:
- il faut :
- recompiler (ce n'est pas sûr, `appengine:run` a l'air de recompiler): dans un terminal: `mvn compile`
- puis lancer l'exécution. Par exemple dans un terminal: `mvn appengine:run`
- puis dans IntelliJ: `Menu >> Run >> Debug alterconsos`

## Chargement des données par acDL

Le path d'installation de `nwjs` étant dans la variable `NW` :
- se mettre dans le directory courant de `acDL`
- lancer (sous Windows): `%NW%\nw .`
- **Attention**: le host local de test n'est pas toujours http://localhost:8080 mais peut être http://127.0.0.1:8080

## Déploiement

Déployer l'application
```bash
gcloud app deploy target/alterconsosae-1.0/WEB-INF/appengine-web.xml
# OU
mvn appengine:deploy
```

Ajouter "alterconsos.fr@gmail.com" dans App Engine > Settings > Emailsender

Activer le service cloudscheduler dans le compte google via <https://console.cloud.google.com/apis/library/cloudscheduler.googleapis.com>

Déployer le cron.yaml en charge de l'envoi des mail
```bash
gcloud app deploy src\main\webapp\WEB-INF\cron.yaml
```

## URL du site

- <https://alterconsos2.ew.r.appspot.com/> (à confirmer)
- <https://alterconsos2.ew.r.appspot.com/admin.html> Pour admin

## Fichiers de configuration

Ces fichiers peuvent avoir à être changés entre les phases de test et le déploiement.
- `src/main/webapp/WEB-INF/ac.json` : fichier principal en notation JSON. Il figure dans git.
- `src/main/webapp/WEB-INF/keys.properties` : fichier contenant les données de sécurité qui ne doivent pas figurer dans git.
- **ne pas oublier de recompiler après les avoir modifier** afin qu'ils se retrouvent bien dans `target`.

### ac.json

```json
{
	"maintenantX":17050101,
	"maintenant":0,
	"build":"2311",

	"configClass":"fr.alterconsos.AppConfig",
	"providerClass":"fr.hypertable.AppDS",

	"maxConnections":100,
	"dbURL":"",
	"username":"",
	"providerHasCache":true,
	"providerWorkers":2,

	"url":"http://localhost:8080/alterconsosAE",
	"urlX":"http://alterconsosae.ew.r.appspot.com",
	"url4mail":"http://alterconsos.fr",

	"contacts": [
		"Uniquement pour les questions purement techniques, </i> : <a href='mailto:alterconsos.fr@gmail.com'>Équipe des développeurs</a>"
	],

	"maildelai": 5,
	"mailserverS":"simu",
	"mailserverJ":"javamail",
	"mailserver":"http://alterconsos.fr/tech/server3.php",
	"mdps":{"A":"999", "B":"999"},
	"smtp":true,

	"adminSender" : "alterconsos.fr@gmail.com",
	"adminMails" : "alterconsos.fr@gmail.com",

	"emailfilterX" : "toto.fr alfred@gmail.com",
	"emailfilter" : ""
}
```

Quand une lettre suit le nom de l'entrée (par exemple `mailserverS` pour l'entrée `mailserver`), c'est une valeur ignorée qui est laissée à titre d'exemple ou d'option.

"maintenant":0
- sert en test à fixer une date et une heure _simulée_. Format AAMMJJHH
- en pratique on laisse "0" pour avoir la _vraie_ heure système.

"build":"2311"
- texte purement informatif identifiant la build visible dans l'application en appuyant sur le bouton Préférences.

"configClass":"fr.alterconsos.AppConfig", "providerClass":"fr.hypertable.AppDS"
- nom des classe d'amorce. Ne pas changer ces noms.
- pour l'environnement Tomcat/Postgresql par exemple il y a une autre classe `AppPG` qui remplace `AppDS`.

"maxConnections":100 ... "providerWorkers":2
- paramètres de configuration de l'accès à la base Postgresql (environnement Tomcat/Postgresql seulement).

"url":"http://localhost:8080/alterconsosAE"
- c'est l'URL sous laquelle l'application peut être invoquée.
- elle change en test local et en production.

"url4mail":"http://alterconsos.fr",
- c'est l'URL qui figure dans les mails hebdomadaires envoyés aux alterconsos.
- celle citée ici est en réalité une adresse de redirection vers l'adresse réelle de déploiement qui est généralement désagréable à lire et surtout à retenir.

"contacts": [],
- liste des lignes HTML qui seront insérées dans chaque synthèse hebdomadaires (typiquement les adresses de contacts).
- laisser une liste vide évite d'être dérangé (assez fréquemment) par des alterconsos qui ne vont pas chercher l'adresse de leur animateur.

"maildelai": 5,
- o2switch limite le nombre d'envois de mail par heure.
- avec une valeur 5 on attend 5s entre chaque envoi pour ralentir cette cadence.

mailserverS": ...
- "simu", : les mails ne sont pas envoyés mais il y a une trace de log à la place. Utile en test pour éviter d'arroser tout le monde par des fake news.
- "javamail" : les mails sont envoyés par le protocole javamail qui un jour a été payant chez Google. Les paramètres du compte envoyeur sont dans `mail.properties`
- "http://alterconsos.fr/tech/server3.php" : les mails sont envoyés en s'adressant à ce service (`mailServer`) écrit en PHP et hébergé sur `alterconsos.fr/tech`. 
  - De cette manière Google ne voit pas d'envoi de mails mais une sollicitation d'une URL externe.
  - Dans ce script il y a deux environnements décrits "A" et "B". En fait on ne se sert que de "A", "B" a été utilisé pour tester.

"mdps":{"A":"999", "B":"999"}, "smtp":true,
- liste des environnements connus dans `server3.php`
- "999" ne sert à rien: le mot de passe effectivement employé par l'application et transmis à `server3.php` est déclaré dans `keys.properties`.

"adminSender" : "alterconsos.fr@gmail.com",
- adresse mail de l'envoyeur des mails hebdomadaires. Cette adresse n'est jamais lue et les alterconsos qui y posent des questions ou y répondent sont toujours déçus.

"adminMails" : "alterconsos.fr@gmail.com",
- quelques exceptions / reports sont envoyés pour information sur cette adresse.

"emailfilter" : "toto.fr alfred@gmail.com",
- à un certain moment en test il faut bien envoyer de _vrais_ mails par le _vrai_ protocole.
- pour éviter d'arroser tout le monde, seuls les mails dont l'adresse **se termine par** l'un des mots listés seront effectivement envoyés.
- Y mettre, en test seulement, les adresses des alterconsos **cobaye** de son choix, séparées par UN ESPACE. 

### keys.properties

mail.password
- mot de passe de connexion au compte mail d'envoi de mail.
- ce mot de passe est celui employé pour javamail comme pour les environnements A et B de l'application PHP mailServer

mapsgoogle.apis
- clé d'accès personnelle à l'API de Google Maps: ce texte remplace ${maps.gooleapis} dans src/webapp/WEB-INF/app.html

pg.password
- mot de passe de l'utilisateur alterconsos d'accès à la base Postgresql (dans l'environnement TomCat/Postgresql)

admin.key
- quand on ouvre la page admin.html, il est demandé un mot de passe pour identifier qu'on est bien l'administrateur. C'est celui-ci qu'il faut donner.
