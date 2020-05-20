#!/bin/bash
# Be nice and add for each OS
#if [[ "$OSTYPE" == "linux-gnu"* ]]; then
#    echo "Linux"
#elif [[ "$OSTYPE" == "darwin"* ]]; then
#    echo "Mac"
#elif [[ "$OSTYPE" == "cygwin" ]]; then
#	echo "Windows Cygwin"
#elif [[ "$OSTYPE" == "msys" ]]; then
#	echo "Windows 2"
#else
#	echo "The operating system $OSTYPE is not supported..."
#    exit 1
#fi

# Check if it has maven
mvn -v
if [ "${?}" -gt 0 ]; then
	echo "Error! Maven not found, install it to proceed..."
    exit 1
fi

# Check if it has git
git --version
if [ "${?}" -gt 0 ]; then
	echo "Error! git not found, install it to proceed..."
    exit 1
fi

# Detect if java exists
java -version
if [ "${?}" -gt 0 ]; then
	echo "Error! java not found, install java 1.8 to proceed..."
    exit 1
fi

# -----------------------------------------------------------------------------
# Download the jpf from the git repository
cd ..
dir="jpf-backup/"
if [ -d "$DIR" ]; then
	git clone https://github.com/pcanelas/jpf-backup.git
fi

# Set the jpf site configuration
echo "Need permissions to generate jpf site properties..."
sudo mkdir /home/.jpf
sudo echo "
# JPF site configuration

jpf-core = $PWD/jpf-backup/jpf-core

jpf-nhandler = $PWD/jpf-backup/jpf-nhandler

jpf-symbc = $PWD/jpf-backup/jpf-symbc

extensions=\${jpf-core},\${jpf-nhandler},\${jpf-symbc}

# Automatic generated jpf site properties

" > /home/.jpf/site.properties
cd -

# Cleans installs the maven dependencies
cd regen
mvn clean install
cd ..

