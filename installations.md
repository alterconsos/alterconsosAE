# Installations
### Java 17
Installer java 17 (depuis Oracle):
- https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- Linux x64 Debian Package: https://download.oracle.com/java/17/archive/jdk-17.0.9_linux-x64_bin.deb
- Déclarer JAVA_HOME (requis pour Maven) dans `~/.bashrc`:

  	export JAVA_HOME=/usr/lib/jvm/jdk-17-oracle-x64
  	export CATALINA_HOME="$HOME/apache-tomcat-9.0.82"

### Maven
Installer Maven:
- https://maven.apache.org/download.cgi
- Binary zip archive: https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz

  	tar xzvf apache-maven-3.9.5-bin.tar.gz
  	Alternatively use your preferred archive extraction tool.

- Ajouter `/usr/share/maven/bin` dans le `PATH` dans `~/.bashrc`:

  	export PATH=$PATH:/usr/share/maven/bin

- Vérifier par: `mvn -v`(dans un nouveau terminal).

### IntelliJ Community Edition (gratuit)
Ce studio permet le debug dans IntelliJ et la génération d'un `war` pour déploiement sous Tomcat.
- version: IntelliJ IDEA 2023.2.3 (Community Edition)

  	sudo snap install intellij-idea-community --classic

### Mise sous github.com
`Menu >> VCS >> Share Project on github`

La première fois dans IntelliJ il va demander l'autorisation d'accès au dépôt.

Ultérieurement on accède à git par la seconde icône en haut à gauche dans la barre verticale (`Commit Alt 0`).

### Installer Gcloud CLI
Nécessaire pour les tests locaux et déployer.

https://cloud.google.com/sdk/docs/install

