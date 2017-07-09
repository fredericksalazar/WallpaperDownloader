#!/bin/sh
if /usr/bin/java -version 2>&1 | grep 'version "1.7' >/dev/null
then
	echo "WallpaperDownloader requires Java 8 or higher" >&2
	exit 1
fi

# Memory usage is limited to 256 MBytes of RAM
# -Dsun.java2d.xrender=f to avoid RenderBadPicture exception and JVM crashes when previewing wallpapers
exec /usr/bin/java -Dsun.java2d.xrender=f -Xmx256m -Xms128m -jar '/opt/wallpaperdownloader/jar/wallpaperdownloader.jar' "$@"
