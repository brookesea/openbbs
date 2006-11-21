package org.openbbs.blackboard.persistence.prevalence;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang.Validate;

/**
 * A LogFile implementation which writes all commands to a file
 * on the local filesystem.
 */
public class SimpleLogFile implements LogFile
{
   private ObjectOutputStream logStream = null;
   private File outputFile = null;
   
   /**
    * Create a new LogFile and set the ouput file to the specified
    * file.
    */
   public SimpleLogFile(File outputFile) {
      this.setOutputFile(outputFile);
   }
   
   /**
    * Create new LogFile. You have to set the outputFile before
    * the LogFile can be used.
    */
   public SimpleLogFile() {
      return;
   }

   /**
    * Set the output file where the commands that are written to this
    * LogFile will be stored.
    */
   public void setOutputFile(File outputFile) {
      Validate.notNull(outputFile, "outputFile must not be null");

      if (this.isOpen()) {
         this.closeLog();
      }

      this.outputFile = outputFile;
   }

   /**
    * @see LogFile#playback(PlaybackDelegate)
    */
   public void playback(PlaybackDelegate playbackDelegate) {
      Validate.notNull(playbackDelegate, "playbackDelegate is null");

      if (this.isOpen()) {
         this.closeLog();
      }

      try {
         ObjectInputStream oin = new ObjectInputStream(new FileInputStream(this.outputFile));
         boolean eof = false;
         while (!eof) {
            try {
               Object object = oin.readObject();
               ((PrevalenceCommand)object).playback(playbackDelegate);
            }
            catch (EOFException _) {
               eof = true;
            }
         }
         oin.close();
      }
      catch (Exception exc) {
         throw new PrevalencePersistenceException("failed to restore entries from output file "
               + this.outputFile, exc);
      }
   }

   /**
    * @see LogFile#writeCommand(PrevalenceCommand)
    */
   public void writeCommand(PrevalenceCommand command) {
      Validate.notNull(command);

      if (!this.isOpen()) {
         this.log();
      }

      try {
         this.logStream.writeObject(command);
      }
      catch (IOException exc) {
         throw new LogFileException("failed to write command " + command + " to " + this.outputFile, exc);
      }
   }

   /**
    * @see LogFile#closeLog()
    */
   public void closeLog() {
      if (!this.isOpen()) return; // not open

      try {
         this.logStream.flush();
         this.logStream.close();
      }
      catch (IOException exc) {
         throw new LogFileException("failed to close output file " + this.outputFile, exc);
      }
   }

   /**
    * Test if the logfile is currently open.
    */
   public boolean isOpen() {
      return this.logStream != null;
   }

   /**
    * Open the file for writing.
    */
   private void log() {
      Validate.isTrue(!this.isOpen(), "command stream is already open");
      Validate.notNull(this.outputFile, "outputFile is not set");

      try {
         this.logStream = new ObjectOutputStream(new FileOutputStream(this.outputFile, true));
      }
      catch (IOException exc) {
         throw new LogFileException("unable to open logFile " + this.outputFile, exc);
      }
   }
}
