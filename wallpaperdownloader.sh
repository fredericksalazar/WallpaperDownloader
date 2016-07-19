#!/bin/sh
# Only for packaging!
# Script for snap packaging wallpaperdownloader application. It is not related to the code itself
# Not good, needed for fontconfig
export XDG_DATA_HOME=$SNAP/usr/share
# Font Config
export FONTCONFIG_PATH=$SNAP/etc/fonts/config.d
export FONTCONFIG_FILE=$SNAP/etc/fonts/fonts.conf
export HOME=$SNAP_USER_DATA
java -jar -Duser.home=$SNAP_USER_DATA $SNAP/jar/wallpaperdownloader.jar
