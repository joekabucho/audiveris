//----------------------------------------------------------------------------//
//                                                                            //
//                       W i n d o w s D e s c r i p t o r                    //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Herve Bitteur and others 2000-2013. All rights reserved.      //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package com.audiveris.installer.windows;

import com.audiveris.installer.Descriptor;
import com.audiveris.installer.DescriptorFactory;
import com.audiveris.installer.Installer;
import com.audiveris.installer.Utilities;
import static com.audiveris.installer.RegexUtil.*;

import hudson.util.jna.Shell32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * Class {@code WindowsDescriptor} implements Installer descriptor
 * for Windows (32 and 64 bits).
 *
 * @author Hervé Bitteur
 */
public class WindowsDescriptor
        implements Descriptor
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = LoggerFactory.getLogger(
            WindowsDescriptor.class);

    /** Specific prefix for application folders. {@value} */
    private static final String TOOL_PREFIX =
            "/" + COMPANY_ID + "/" + TOOL_NAME;

    /**
     * Data for Microsoft Visual C++ 2008 Redistributable.
     */
    private static interface CPP
    {

        /** Registry value name. */
        static final String VALUE = "DisplayName";

        /** Registry key for 32-bit. */
        static final String KEY_32 = "HKLM\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\{9BE518E6-ECC6-35A9-88E4-87755C07200F}";

        /** Download URL for 32-bit. */
        static final String URL_32 = "http://download.microsoft.com/download/5/D/8/5D8C65CB-C849-4025-8E95-C3966CAFD8AE/vcredist_x86.exe";

        /** Registry key for 64-bit. */
        static final String KEY_64 = "HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\{5FCE6D76-F5DC-37AB-B2B8-22AB8CEDB1D4}";

        /** Download URL for 64-bit. */
        static final String URL_64 = "http://download.microsoft.com/download/5/D/8/5D8C65CB-C849-4025-8E95-C3966CAFD8AE/vcredist_x64.exe";

    }

    /**
     * Data for Ghostscript.
     */
    private static interface GS
    {

        /** Registry radix for pure 32/32 or 64/64. */
        static final String RADIX_PURE = "HKLM\\SOFTWARE\\GPL Ghostscript";

        /** Registry radix for Wow (64/32). */
        static final String RADIX_WOW = "HKLM\\SOFTWARE\\Wow6432Node\\GPL Ghostscript";

        /** Download URL for 32-bit. */
        static final String URL_32 = "http://downloads.ghostscript.com/public/gs907w32.exe";

        /** Download URL for 64-bit. */
        static final String URL_64 = "http://downloads.ghostscript.com/public/gs907w64.exe";

    }

    //~ Methods ----------------------------------------------------------------
    //
    //-----------------//
    // getConfigFolder //
    //-----------------//
    @Override
    public File getConfigFolder ()
    {
        return new File(Installer.getBundle().getInstallFolder(), "config");
    }

    //---------------//
    // getDataFolder //
    //---------------//
    @Override
    public File getDataFolder ()
    {
        return new File(Installer.getBundle().getInstallFolder(), "data");
    }

    //--------------------------//
    // getDefaultTessdataPrefix //
    //--------------------------//
    @Override
    public File getDefaultTessdataPrefix ()
    {
        final String pf32 = DescriptorFactory.OS_ARCH.equals("x86")
                ? "ProgramFiles"
                : "ProgramFiles(x86)";
        final String target = System.getenv(pf32);
        final File file = new File(new File(target), Descriptor.TESSERACT_OCR);
        logger.debug("getDefaultTessdataPrefix: {}", file.getAbsolutePath());

        return file;
    }

    //------------------//
    // getInstallFolder //
    //------------------//
    @Override
    public File getInstallFolder ()
    {
        final String appdata = System.getenv("APPDATA");
        final File file = new File(appdata + TOOL_PREFIX);
        logger.debug("getInstallFolder: {}", file.getAbsolutePath());

        return file;
    }

    //---------------//
    // getTempFolder //
    //---------------//
    @Override
    public File getTempFolder ()
    {
        File userDir = new File(System.getProperty("user.home"));
        final File folder = new File(userDir, "audiveris-installation-temp");
        logger.debug("getTempFolder: {}", folder.getAbsolutePath());

        return folder;
    }

    //------------//
    // installCpp //
    //------------//
    @Override
    public void installCpp ()
            throws Exception
    {
        final String url = DescriptorFactory.OS_ARCH.equals("x86") ? CPP.URL_32 : CPP.URL_64;
        Utilities.downloadExecAndInstall(
                "C++ runtime", url, getTempFolder(), "/q");
    }

    //--------------------//
    // installGhostscript //
    //--------------------//
    @Override
    public void installGhostscript ()
            throws Exception
    {
        final String url = DescriptorFactory.OS_ARCH.equals("x86") ? GS.URL_32 : GS.URL_64;
        Utilities.downloadExecAndInstall(
                "Ghostscript", url, getTempFolder(), "/S");
    }

    //---------//
    // isAdmin //
    //---------//
    @Override
    public boolean isAdmin ()
    {
        // The UAC (User Access Control) appeared with Windows Vista
        // Before that, user was granted admin privileges by default
        try {
            // If the IsUserAnAdmin method exists, then we are in Vista or later
            // and just need to check its result
            return Shell32.INSTANCE.IsUserAnAdmin();
        } catch (Throwable ex) {
            // No access to IsUserAnAdmin, so we assume there is no UAC
            return true;
        }
    }

    //----------------//
    // isCppInstalled //
    //----------------//
    @Override
    public boolean isCppInstalled ()
    {
        try {
            // Check Windows registry
            final String key = DescriptorFactory.OS_ARCH.equals("x86") ? CPP.KEY_32 : CPP.KEY_64;
            List<String> output = new ArrayList<>();
            int result = WindowsUtilities.queryRegistry(output, key, "/v", CPP.VALUE);
            logger.debug("C++ query exit:{} output: {}", result, output);
            return result == 0;
        } catch (IOException | InterruptedException ex) {
            //TODO: Dialog w/ error messages
            return false;
        }
    }

    //------------------------//
    // isGhostscriptInstalled //
    //------------------------//
    @Override
    public boolean isGhostscriptInstalled ()
    {
        return getGhostscriptPath() != null;
    }

    //-----------------//
    // relaunchAsAdmin //
    //-----------------//
    @Override
    public void relaunchAsAdmin ()
    {
        try {
            JOptionPane.showMessageDialog(
                    null,
                    "Relaunching as Administrator",
                    "Elevation needed",
                    JOptionPane.INFORMATION_MESSAGE);

            String cmdLine = WindowsUtilities.getCommandLine();
            logger.debug("cmdLine: {}", cmdLine);

            String execName = WindowsUtilities.getModuleFilename();
            logger.debug("execName: {}", execName);

            // Skip fileName with its enclosing quotes
            int start = cmdLine.indexOf(execName);
            String params = cmdLine.substring(start + execName.length() + 1);
            logger.debug("params: {}", params);

            logger.debug("Relaunch as administrator...");
            WindowsUtilities.runElevated(
                    new File(execName),
                    new File("."),
                    params);
            logger.debug("End of relaunch.");
        } catch (HeadlessException | IOException | InterruptedException ex) {
            logger.warn("Could not relaunch process", ex);
        }
    }

    //--------//
    // setenv //
    //--------//
    @Override
    public void setenv (boolean system,
                        String var,
                        String value)
            throws IOException, InterruptedException
    {
        List<String> args = new ArrayList<>();
        args.add("/C");
        args.add("setx");
        args.add(var);
        args.add(value);
        if (system) {
            args.add("/M");
        }
        List<String> output = new ArrayList<>();
        Utilities.runProcess("cmd.exe", output, args.toArray(new String[args.size()]));
        logger.debug("setenv output: {}", output);
    }

    //--------------------//
    // getGhostscriptPath //
    //--------------------//
    /**
     * Retrieve the path to suitable ghostscript executable on Windows
     * environments.
     *
     * This is implemented on registry informations, using CLI "reg" command:
     * reg query "HKLM\SOFTWARE\GPL Ghostscript" /s
     *
     * @return the best suitable path, or null if nothing found
     */
    private String getGhostscriptPath ()
    {
        // Group names
        final String VERSION = "version";
        final String PATH = "path";
        final String ARCH = "arch";

        /** Regex for registry key line. */
        final Pattern keyPattern = Pattern.compile(
                "^HKEY_LOCAL_MACHINE\\\\SOFTWARE\\\\(Wow6432Node\\\\)?GPL Ghostscript\\\\"
                + group(VERSION, "\\d+\\.\\d+") + "$");

        /** Regex for registry value line. */
        final Pattern valPattern = Pattern.compile(
                "^\\s+GS_DLL\\s+REG_SZ\\s+" + group(PATH, ".+") + "$");

        /** Regex for dll name. */
        final Pattern dllPattern = Pattern.compile(
                "gsdll" + group(ARCH, "\\d+") + "\\.dll$");

        Double bestVersion = null; // Best version found so far
        String bestPath = null; // Best path found so far
        boolean relevant = false; // Is current registry info interesting?
        int index = 0; // Line number in registry outputs

        double minVersion = Double.valueOf(Descriptor.GHOSTSCRIPT_MIN_VERSION);

        // Browse registry output lines in sequence
        for (String line : getRegistryGhostscriptOutputs()) {
            logger.debug("Line#{}:{}", ++index, line);

            Matcher keyMatcher = keyPattern.matcher(line);

            if (keyMatcher.matches()) {
                relevant = false;
                // Check version information
                String versionStr = getGroup(keyMatcher, VERSION);
                logger.debug("Version read as: {}", versionStr);
                Double version = Double.valueOf(versionStr);

                if ((version != null) && (version >= minVersion)) {
                    // We have an acceptable version
                    if ((bestVersion == null) || (bestVersion < version)) {
                        bestVersion = version;
                        logger.debug("Best version is: {}", versionStr);
                        relevant = true;
                    } else {
                        logger.debug("Version discarded: {}", versionStr);
                    }
                } else {
                    logger.debug("Version unacceptable: {}", versionStr);
                }
            } else if (relevant) {
                Matcher valMatcher = valPattern.matcher(line);

                if (valMatcher.matches()) {
                    // Read path information
                    bestPath = getGroup(valMatcher, PATH);
                    logger.debug("Best path is: {}", bestPath);
                }
            }
        }

        // Extract prefix and dll from best path found, regardless of arch
        if (bestPath != null) {
            int lastSep = bestPath.lastIndexOf("\\");
            String prefix = bestPath.substring(0, lastSep);
            logger.debug("Prefix is: {}", prefix);

            String dll = bestPath.substring(lastSep + 1);
            logger.debug("Dll is: {}", dll);

            Matcher dllMatcher = dllPattern.matcher(dll);

            if (dllMatcher.matches()) {
                String arch = getGroup(dllMatcher, ARCH);
                String result = prefix + "\\gswin" + arch + "c.exe";
                logger.debug("Final path is: {}", result);

                return result; // Normal exit
            }
        }

        logger.warn("Could not find suitable Ghostscript software");
        return null; // Abnormal exit
    }

    //-------------------------------//
    // getRegistryGhostscriptOutputs //
    //-------------------------------//
    /**
     * Collect the output lines from registry queries about Ghostscript
     *
     * @return the output lines
     */
    private List<String> getRegistryGhostscriptOutputs ()
    {
        /** Radices used in registry search (32, 64 or Wow). */
        final String[] radices = new String[]{
            GS.RADIX_PURE, // Pure 32/32 or 64/64
            GS.RADIX_WOW // Wow (64/32)
        };

        // Access registry twice, one for win32 & win64 and one for Wow
        List<String> outputs = new ArrayList<>();

        for (String radix : radices) {
            logger.debug("Radix: {}", radix);
            try {
                WindowsUtilities.queryRegistry(outputs, radix, "/s");
            } catch (IOException | InterruptedException ex) {
                logger.error("Error in reading registry", ex);
            }
        }

        return outputs;
    }
}