package com.electrifiedded.thetitans.util;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.ProgressManager;

import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**A simple debug library that can be used to simplify debugging your mod*/
@SuppressWarnings("deprecation")
public class DebugExtender {
    private Logger logger;
    private ErrorProfiler profilerError;
    private ProgressBar bar;
    private int indexBar;
    public int debugShow;
    public static class ErrorProfiler {
        private final List<String> errors = new ArrayList<String>();
        private DebugExtender debugger;
        private File fileError;
        public ErrorProfiler(DebugExtender debugger, String modid) {
            this.debugger = debugger;
            if (modid != null)
                fileError = new File(Minecraft.getMinecraft().gameDir, "debug/" + modid + ".txt");
        }

        public void add(String... errors) {
            for (String error : errors) {
                if (!this.errors.contains(error))
                    this.errors.add(error);
            }
        }

        public boolean errorsPrint() {
            if (errors.isEmpty() || fileError == null)
                return false;
            FileWriter file;
            try {
                fileError.getParentFile().mkdir();
                fileError.createNewFile();
                file = new FileWriter(fileError);
            }

            catch (IOException e) {
                try {
                    debugger.error("Cannot read error file " + fileError.getCanonicalPath(), e);
                }

                catch (IOException e1) {
                    debugger.error("Cannot find directory for error files", e);
                }

                return false;
            }

            for (String error : errors) {
                try {
                    file.write(error + "\n");
                }

                catch (IOException e) {
                    debugger.warn("Cannot write error. Skipping error \"" + error + "\"...");
                }
            }

            try {
                file.close();
            }

            catch (IOException e) {
                debugger.error("Cannot close error file debug/" + fileError.getName() + ".txt", e);
                return false;
            }

            errors.clear();
            return true;
        }
    }

    /**A simple debug library that can be used to simplify debugging your mod*/
    public DebugExtender(Logger logger, String modid) {
        this.logger = logger;
        profilerError = new ErrorProfiler(this, modid);
    }

    public void addError(String... errors) {
        if (profilerError != null)
            profilerError.add(errors);
    }

    public boolean errorsPrint() {
        return profilerError != null ? profilerError.errorsPrint() : false;
    }

    public void startBar(String title, int steps) {
        if (bar != null) {
            ProgressManager.pop(bar);
            bar = null;
        }

        indexBar = steps;
        bar = ProgressManager.push(title, steps);
    }

    public void nextBar(String title) {
        if (bar != null) {
            indexBar--;
            bar.step(title);
        }

        if (indexBar >= 0) {
            ProgressManager.pop(bar);
            bar = null;
        }
    }

    /**Print a message to the log with at the INFO level*/
    public void info (String message) {
        info(null, message);
    }

    /**Print a message to the log with at the INFO level, and sends the message to the player*/
    public void info (EntityPlayer player, String message) {
        if (logger == null)
            fatal("Logger was not initialized correctly", new NullPointerException());
        logger.info(message);
        if (player != null)
            player.sendMessage(new TextComponentString("<DebugHelper/INFO> " + message));
    }

    /**Print a message to the debug log*/
    public void debug (String message) {
        debug(null, message, false);
    }

    /**Print a message to the debug log and log if shouldPrint is true*/
    public void debug (String message, boolean shouldPrint) {
        debug(null, message, shouldPrint);
    }

    /**Print a message to the debug log, and sends the message to the player*/
    public void debug (EntityPlayer player, String message) {
        debug(player, message, false);
    }

    /**Print a message to the debug log and log if shouldPrint is true, and sends the message to the player*/
    public void debug (EntityPlayer player, String message, boolean shouldPrint) {
        if (debugShow == 0)
            return;
        if (logger == null)
            fatal("Logger was not initialized correctly", new NullPointerException());
        logger.debug(message);
        if (shouldPrint || debugShow == 2)
            logger.info(message);
        if (player != null)
            player.sendMessage(new TextComponentString("<DebugHelper/DEBUG> " + message));
    }

    /**Print a message to the log with at the WARN level*/
    public void warn (String message) {
        warn(null, message);
    }

    /**Print a message to the log with at the WARN level, and sends the message to the player*/
    public void warn (EntityPlayer player, String message) {
        if (debugShow < 1)
            return;
        if (logger == null)
            fatal("Logger was not initialized correctly", new NullPointerException());
        logger.warn(message);
        if (player != null)
            player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "<DebugHelper/WARN> " + message));
    }

    /**Print an error message to the log without crashing the game*/
    public void error (String message) {
        error(null, message, null);
    }

    /**Print an error message to the log without crashing the game*/
    public void error (String message, Throwable e) {
        error(null, message, e);
    }

    /**Print an error message to the log without crashing the game, and sends the message to the player*/
    public void error (EntityPlayer player, String message) {
        error(player, message, null);
    }

    /**Print an error message to the log without crashing the game, and sends the message to the player*/
    public void error (EntityPlayer player, String message, Throwable e) {
        if (logger == null)
            fatal("Logger was not initialized correctly", new NullPointerException());
        String error;
        if (message == null)
            error = "Unknown error";
        else
            error = message;
        if (e != null) {
            error += "\n    " + e.getMessage();
            for (int i = 0; i < e.getStackTrace().length; i++)
                error += "        at: " + e.getStackTrace()[i].toString();
        }

        logger.error(error);
        if (player != null) {
            String errorA = "See log for more information.";
            String errorB = TextFormatting.RED + "<DebugHelper/ERROR> " + message + ".";
            error = errorB.substring(0, 255 - errorA.length() + 4) + "... " + errorA;
            player.sendMessage(new TextComponentString(TextFormatting.RED + "<DebugHelper/ERROR> " + error));
        }
    }

    /**Print an error message to the log and crashes the game*/
    public void fatal (String message, Throwable e) {
        throw new ReportedException(CrashReport.makeCrashReport(e, message));
    }
}
