package org.openbbs.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.Validate;

/**
 * Helps reading objects from an ObjectInputStream.
 */
public class ObjectReader
{
   private ObjectInputStream inputStream = null;
   private boolean streamIsEmpty = false;

   public ObjectReader(File file) throws IOException {
      Validate.notNull(file);
      try {
         this.inputStream = new ObjectInputStream(new FileInputStream(file));
      }
      catch (EOFException _) {
         this.streamIsEmpty = true;
      }
      catch (FileNotFoundException _) {
         this.streamIsEmpty = true;
      }
   }

   public ObjectReader(ObjectInputStream inputStream) {
      Validate.notNull(inputStream);
      this.inputStream = inputStream;
   }

   public void readObjects(Delegate delegate) throws Exception {
      Validate.notNull(delegate);

      if (this.streamIsEmpty) {
         return;
      }

      boolean eof = false;
      try {
         while (!eof) {
            try {
               Object object = this.inputStream.readObject();
               delegate.didReadObject(object);
            }
            catch (EOFException _) {
               eof = true;
            }
         }
      }
      finally {
         this.inputStream.close();
      }
   }

   public static interface Delegate
   {
      public void didReadObject(Object object) throws Exception;
   }
}
