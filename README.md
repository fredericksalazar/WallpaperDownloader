# Wallpaper Downloader Application #

## Home ##

This is the Wiki related to Wallpaper Downloader Project. It gathers all the important information attached to this software project.

## Quick summary ##
This is a JAVA software project developed for downloading wallpapers from different sources. It is a crossed-platform standalone application.

## Current Version ##
2.7

## Installation ##
You can install wallpaperdownloader using different ways.

### Arch Linux ###
It is in [AUR](https://aur.archlinux.org/packages/wallpaperdownloader) repository. Just install it from there:

```
#!bash

yaourt -S wallpaperdownloader
```

#### Linux, OSX, Windows with Java ####
- Download and install [java](https://java.com/en/download/).
- Download and store wallpaperdownloaded.jar from [wallpaperdownloader repository](https://bitbucket.org/eloy_garcia_pca/wallpaperdownloader/src)
- Go to the directory where you stored the file and double click on it. (If you are using Linux, you can exceute it via terminal. Open a terminal, cd to the directoy where you 
stored it and execute it using *java -jar wallpaperdownloader.jar*)

#### Linux with Snap package tool installed ####
This application is packaged using [snapcraft tool](http://snapcraft.io/) and it is published in Canonical's snap [store](https://uappexplorer.com/app/wallpaperdownloader.egarcia).

Assuming you have snapd install on your distribution (snapd is available for many distros right now such as Ubuntu, Archlinux, Fedora, OpenSUSE...), open a terminal and type:

    sudo snap install wallpaperdownloader

**Only for Ubuntu users (16.04 and above):** If you install the snap package in Ubuntu, it is recommended to install **snapd-xdg-open** package to allow WallpaperDownloader snap 
application to open some links in your browser.

    sudo apt install snapd-xdg-open

## How to build the snap package and install it ##
If you wish, you can build the snap package and install it form the source code. It is necessary to have snapd and snapcraft installed on your system. If you are running Ubuntu, you 
will have snapd installed by default. If you want to install snapcraft on Ubuntu:

    sudo apt install snapcraft

Clone the repository:

    git clone https://eloy_garcia_pca@bitbucket.org/eloy_garcia_pca/wallpaperdownloader.git

Build the package:

    cd wallpaperdownloader
    cd snap
    snapcraft

Install the snap package (please, check the name of the snap package built):

    sudo snap install <wallpaperdownloader*.snap>

## Features (V 2.7) ##

New Features:

- KDE support added (not available in snap package version).

- Now, user can define several different directories for the automated changer.

- Pause/resume functionality to download wallpapers.

- New option to start the application minimized.

- Changelog added

Bugs fixed:

- Social Wallpapering provider now stores the image files with the correct suffix

## Features (V 2.6) ##

New Features:

- Windows 10 support added.

- Now, user can define the level of notifications.

## Features (V 2.5) ##

New Features:

- New providers implemented: [Devianart](http://www.deviantart.com/), [Bing](http://www.bing.com/), [Social Wallpapering](http://www.socwall.com/) and 
[WallpaperFusion](https://www.wallpaperfusion.com/)

- **Ubuntu users** who have installed the application via snap package can now click on the links in About tab and the browser will be opened. For this integration, it is necessary 
to install **snapd-xdg-open** package in the host system.

Bugs fixed:

- Now, downloads directory size defined by the user is accurately calculated and the application removes no favorite wallpapers randomly until it fits this size.

## Features (V 2.4) ##

New Features:

- User can define a custom directory for the changer process and it can be different from the download directory. This feature is useful for users who want to download wallpapers in 
one directory but move them to another one for keeping their collection in a separate location.

- New button to move all the favorite wallpapers to a custom directory set by the user. It is useful for users who keep another location to save all their wallpapers.

Bugs fixed:

- Changer now sets favorite and non favorite wallpapers.

## Features (V 2.3) ##

- Programmable changer: Now, you can program a changer background process to automatically change your wallpaper every 1, 5, 10, 20, 30 or 60 minutes.
- Changer implemented for XFCE desktop environment (only for native version). not implemented in snap package yet.
- A new option to change the wallpaper has been implemented from system tray icon.
- Bugs Fixed: 

1. Now, if a wallpaper is set as favorite, it isn't downloaded again. 

2. Progress bar which shows the space occupied by the wallpapers downloaded is more accurate and it is properly refreshed when new wallpapers are downloaded or removed. 

3. Main window is minimized instead of hiding in system tray in desktop environments that don't support traditional system tray icons (such as GNOME 3 and KDE Plasma 5)

## Features (V 2.2) ##

- Image preview: New button to preview the wallpaperdownloaded. Delete, set the current wallpaper or set as favorite or unset it directly from the preview window.
- Changer: A new button has been implemented to set the current wallpaper from the application. It works for GNOME 3, Unity and MATE desktop enviroments and Windws 7 too.

## Features (V 2.1) ##

- About tab to display contact info implemented.
- The application now remembers the wallpapers removed by the user. This way, it won't download them again.
- Incomplete downlads for some wallpapers and PNG downloads fixed.
- Some parameters used to search within Wallhaven have been either removed or changed in order to work properly.

## Features (V 2.0) ##
- Wallpaper management revamped. A new interface for managing (delete, set favorite and set no favorite) wallpapers has been implemented. All these tasks are now available in a new 
window.

## Features (V 1.7) ##
- A new tab (Wallpapers) has been implemented
- Last 5 wallpapers feature: Now, the user can see the last 5 wallpapers downloaded and interact with them directly (setting the wallpapers as favorite or removing them)
- A button (and tool) for managing favorite wallpapers has been implemented
- A button (and tool) for managing no favorite wallpapers has been implemented
- A button (and tool) for deleting wallpapers has been implemented

## Features (V 1.5) ##
- A new feature for changing the downloads directory has been implemented
- A progress bar is displayed to let the user watch the amount of space occupied within the downloads directory
- A warning system has been implemented. If the occupied space is higher than 90% (within the downloads directory), the user will be warned

## Features (V 1.1) ##
- A new button for copying downloads path into the clipboard has been added
- Some icons have been added
- DialogManager has been implemented in order to popup information dialogs on the screen

## Features (V 1.0) ##
- Only one provider implemented ([Wallhaven.cc](http://alpha.wallhaven.cc/))
- You can type several keywords to search for wallpapers (separated by ***;***)
- You can search specific resolution wallpapers or all resolutions available
- Search type implemented: Relevance, Date added, Views, Favorites, Random
- You can set the period of time to download the next wallpaper (1, 5, 10, 20, 30 min)
- You can set tha maximun size for download directory (in MBytes)
- You can open directly from the application the directory where all the wallpapers are downloaded

## How do I get set up locally? ##

* [Prerequisites](https://bitbucket.org/eloy_garcia_pca/wallpaperdownloader/wiki/Prerequisites)
* [Cloning project](https://bitbucket.org/eloy_garcia_pca/wallpaperdownloader/wiki/Clonig%20project)
* [Setting up Wallpaper Downloader project in Eclipse](https://bitbucket.org/eloy_garcia_pca/wallpaperdownloader/wiki/SettingUp)
* [Deployment instructions](https://bitbucket.org/eloy_garcia_pca/wallpaperdownloader/wiki/Deployment)

## Who do I talk to? ##

* Repo owner and developer: <eloy.garcia.pca@gmail.com>
* For more information, please check my [portfolio web page at https://egara.github.io](https://egara.github.io)

## Issues ##

Please, report issues using [Wallpaper Downloader issue-tracking tool](https://bitbucket.org/eloy_garcia_pca/wallpaperdownloader/issues)
