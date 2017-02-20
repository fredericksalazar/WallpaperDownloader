#!/bin/sh
# Memory usage is limited to 256 MBytes of RAM
exec /usr/bin/java -Xmx256m -Xms128m -jar '/opt/wallpaperdownloader/jar/wallpaperdownloader.jar' "$@"
