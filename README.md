# Jude
Jude is a desktop Java application useful for seeing the UIDefaults keys and values for a Java look and feel; this can be helpful with Swing programming.  The name of the software is an acronym for Java User-Interface Defaults Explorer.   Jude will also list the system properties.

![Jude](http://argonium.github.io/jude1.png)

On the main screen, clicking on the "L&F" button will display a popup menu listing the installed look and feels.  Choosing a new L&F will update the table data on that page.  Users can also add additional L&Fs to Jude by following these steps:

1. In the directory containing jude.jar, create a subdirectory called .themes
1. In .themes, put any L&F JAR files and a text file called themes.txt
1. Each line in themes.txt should be the name of the class in an L&F JAR (from step 2) that extends the javax.swing.LookAndFeel class
1. Start Jude

For example, to include the Napkin L&F in the list of installed L&Fs, the themes.txt file would contain the line "net.sourceforge.napkinlaf.NapkinLookAndFeel" (no quotes).  The .themes directory would include the file napkinlaf.jar from the Napkin Look and Feel web site.  Each line of themes.txt can either contain a class name or start with the "#" symbol, meaning it's a comment and should be ignored by the parser.

![Jude](http://argonium.github.io/jude2.png)

The application requires Java 5 or later to build and execute.

There is currently no help file, but there is tooltip text for most of the controls, so the interface should be easy to understand.

To run the appication, build it via Ant ('ant clean dist'), and then open via 'java -jar jude.jar' (or double-click jude.jar).

The source code is released under the MIT license.
