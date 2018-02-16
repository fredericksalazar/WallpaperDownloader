#!/bin/sh
# Only for packaging!
# Script for snap packaging wallpaperdownloader application. It is not related to the code itself
# Not good, needed for fontconfig
export XDG_DATA_HOME=$SNAP/usr/share
# Font Config
export FONTCONFIG_PATH=$SNAP/etc/fonts/config.d
export FONTCONFIG_FILE=$SNAP/etc/fonts/fonts.conf
export HOME=$SNAP_USER_DATA
# Memory usage is limited to 256 MBytes of RAM
# -Dsun.java2d.xrender=f to avoid RenderBadPicture exception and JVM crashes when previewing wallpapers
desktop-launch java -Dsun.java2d.xrender=f -Xmx256m -Xms128m -jar -Duser.home=$SNAP_USER_DATA $SNAP/jar/wallpaperdownloader.jar
