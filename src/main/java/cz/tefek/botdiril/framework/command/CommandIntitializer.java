package cz.tefek.botdiril.framework.command;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class CommandIntitializer
{
    private static final Logger logger = Logger.getLogger(CommandIntitializer.class);

    public static void load()
    {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info("Classloading begin: ");

        var ignoreJars = Boolean.valueOf(System.getProperty("botdiril.cp.ingoreJars"));

        String classpath = System.getProperty("java.class.path");
        logger.debug("botdiril.cp.ingoreJars = " + ignoreJars);
        String[] classpathEntries = classpath.split(File.pathSeparator);

        for (var cp : classpathEntries)
        {
            try
            {
                var cpFile = new File(cp);

                if (cpFile.isDirectory())
                {
                    var files = FileUtils.listFiles(cpFile, null, true);

                    URL[] urls = { cpFile.toURI().toURL() };
                    var sysLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());

                    for (var classFile : files)
                    {
                        if (!classFile.getName().endsWith(".class"))
                            continue;

                        var className = cpFile.toURI().relativize(classFile.toURI()).getPath().replaceAll("\\.class$", "").replace('/', '.');

                        var modClass = sysLoader.loadClass(className);
                        var annotation = modClass.getDeclaredAnnotation(Command.class);

                        if (annotation != null)
                        {
                            CommandStorage.register(annotation, modClass);
                        }
                    }
                }
                else
                {
                    if (ignoreJars)
                    {
                        continue;
                    }

                    var jarFile = new JarFile(cp);
                    var e = jarFile.entries();

                    URL[] urls = { new URL("jar:file:" + cp + "!/") };

                    var sysLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());

                    while (e.hasMoreElements())
                    {
                        var je = e.nextElement();

                        if (je.isDirectory())
                        {
                            continue;
                        }

                        if (!je.getName().endsWith(".class"))
                        {
                            continue;
                        }

                        var className = je.getName().replaceAll("\\.class$", "").replace('/', '.');

                        var modClass = sysLoader.loadClass(className);
                        var annotation = modClass.getDeclaredAnnotation(Command.class);

                        if (annotation != null)
                        {
                            CommandStorage.register(annotation, modClass);
                        }
                    }

                    jarFile.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(5);
            }
        }

        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
