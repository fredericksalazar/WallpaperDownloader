In order to create the deb package:

Copy .../wallpaperdownloader/deb directory to another temporal location
cd $TEMP_DIR/deb/
Modify whatever you want
chmod 644 $TEMP_DIR/deb/debian/usr/share/doc/wallpaperdownloader/wallpaperdownloader.png
chmod 755 $TEMP_DIR/deb/debian/usr/bin/wallpaperdownloader.sh
chown -R root:root $TEMP_DIR/deb/debian/
cd $TEMP_DIR/deb/
dpkg --build debian
Rename deb package

Sources:

http://blog.pryds.eu/2013/02/package-java-apps-for-ubuntu-and-debian.html
