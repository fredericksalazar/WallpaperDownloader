#!/bin/sh
# Only for packaging!
# Script for snap packaging wallpaperdownloader application. It is not related to the code itself
# Old exports are not needed anymore because of desktop/gtk3 part use within snapcraft.xml configuration file
export HOME=$SNAP_USER_DATA
# Memory usage is limited to 256 MBytes of RAM
# -Dsun.java2d.xrender=f to avoid RenderBadPicture exception and JVM crashes when previewing wallpapers
desktop-launch java -Dsun.java2d.xrender=f -Xmx256m -Xms128m -jar -Duser.home=$SNAP_USER_DATA $SNAP/jar/wallpaperdownloader.jar
