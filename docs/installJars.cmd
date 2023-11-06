@echo off
call mvn install:install-file -Dfile=libext/poi-3.9-20121203.jar -DartifactId=poi -Dversion=3.9-20121203 -DgroupId=poi  -Dpackaging=jar -DlocalRepositoryPath=lib
call mvn install:install-file -Dfile=libext/poi-ooxml-3.9-20121203.jar -DartifactId=poi-ooxml -Dversion=3.9-20121203 -DgroupId=poi  -Dpackaging=jar -DlocalRepositoryPath=lib
call mvn install:install-file -Dfile=libext/poi-ooxml-schemas-3.9-20121203.jar -DartifactId=poi-ooxml-schemas -Dversion=3.9-20121203 -DgroupId=poi  -Dpackaging=jar -DlocalRepositoryPath=lib
