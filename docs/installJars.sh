#!/bin/bash
mvn install:install-file -Dfile=libext/poi-3.9-20121203.jar -DartifactId=poi -Dversion=3.9-20121203 -DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib
mvn install:install-file -Dfile=libext/poi-ooxml-3.9-20121203.jar -DartifactId=poi-ooxml -Dversion=3.9-20121203 -DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib
mvn install:install-file -Dfile=libext/poi-ooxml-schemas-3.9-20121203.jar -DartifactId=poi-ooxml-schemas -Dversion=3.9-20121203 -DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib
# SEMBLENT inutiles
# mvn install:install-file -Dfile=libext/commons-fileupload-1.3.jar -DartifactId=commons-fileupload -Dversion=1.3 -DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib
# mvn install:install-file -Dfile=libext/dom4j-1.6.1.jar -DartifactId=dom4j -Dversion=1.6.1 -DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib
# mvn install:install-file -Dfile=libext/jxl.jar -DartifactId=jxl -Dversion=1.0 -DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib
# mvn install:install-file -Dfile=libext/stax-api-1.0.1.jar -DartifactId=stax-api -Dversion=1.0.1 -DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib
# mvn install:install-file -Dfile=libext/xmlbeans-2.3.0.jar -DartifactId=xmlbeans -Dversion=2.3.0 -DgroupId=com.example  -Dpackaging=jar -DlocalRepositoryPath=lib
