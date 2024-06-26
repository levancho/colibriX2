  package com.despani.x2.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.GZIPOutputStream;

  public class IOUtils extends org.apache.commons.io.IOUtils {

      private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

      public static void fastCopy(File source, File dest) throws IOException {

          FileInputStream fi = new FileInputStream(source);
          FileChannel fic = fi.getChannel();
          MappedByteBuffer mbuf = fic.map(
                  FileChannel.MapMode.READ_ONLY, 0, source.length());
          fic.close();
          fi.close();


          FileOutputStream fo = new FileOutputStream(dest);
          FileChannel foc = fo.getChannel();
          foc.write(mbuf);
          foc.close();
          fo.close();

      }

      public static String readFileContent(String path) {
          try {
              if (!new File(path).exists()) {
                  return "";
              }

              FileInputStream fis = new FileInputStream(path);
              int x = fis.available();
              byte b[] = new byte[x];

              fis.read(b);

              return new String(b);
          } catch (IOException e) {
              // Ignore
          }

          return "";
      }

      /**
       *
       * good for Large Files >2Mb
       * @param filename
       * @return
       */
      private static byte[] largeFileReader(String filename) {
          byte[] bytes = null;
          FileChannel fc = null;
          try {
              fc = new FileInputStream(filename).getChannel();
              MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
              int size = byteBuffer.capacity();
              if (size > 0) {
                  // Retrieve all bytes in the buffer
                  byteBuffer.clear();
                  bytes = new byte[size];
                  byteBuffer.get(bytes, 0, bytes.length);
              }
              fc.close();
          } catch (FileNotFoundException fnf) {
              System.err.println("" + fnf);
          } catch (IOException io) {
              System.err.println("" + io);
          } finally {
              if (fc != null) {
                  try {
                      fc.close();
                  } catch (IOException ioe) {
                      // ignore
                  }
              }
          }
          return bytes;
      }

      public static String readFile(String filename) {
          return readFile(filename, false);
      }

      public static String readFile(String filename, boolean useNIO) {
          String s = "";
          if (useNIO) {
              s = new String(largeFileReader(filename));
          } else {
              s = new String(smallFileReader(filename));
          }
          return s;
      }

      public static byte[] smallFileReader(String filename) {
          byte[] buffer = null;
          FileInputStream in = null;
          try {
              File file = new File(filename);
              int size = (int) file.length();
              if (size > 0) {
                  buffer = new byte[size];
                  in = new FileInputStream(file);
                  in.read(buffer, 0, size);
                  //s = new String(buffer, 0, size);
                  in.close();
              }
          } catch (FileNotFoundException fnfx) {
              System.err.println("File not found: " + fnfx);
          } catch (IOException iox) {
              System.err.println("I/O problems: " + iox);
          } finally {
              if (in != null) {
                  try {
                      in.close();
                  } catch (IOException ioe) {
                  }
              }
          }
          return buffer;
      }

      public static byte[] fileReadNIO(String name) {
          FileInputStream f = null;
          ByteBuffer bb = null;
          try {
              f = new FileInputStream(name);
              FileChannel ch = f.getChannel();
              bb = ByteBuffer.allocateDirect(1024);
              long checkSum = 0L;
              int nRead;
              while ((nRead = ch.read(bb)) != -1) {
                  bb.position(0);
                  bb.limit(nRead);
                  while (bb.hasRemaining()) {
                      checkSum += bb.get();
                  }
                  bb.clear();
              }
          } catch (FileNotFoundException ex) {
              logger.error(ex.getMessage());
          } catch (IOException ex) {
              logger.error(ex.getMessage());
          } finally {
              try {
                  f.close();
              } catch (IOException ex) {
                  logger.error(ex.getMessage());
              }
          }
          return bb.array();

      }

      /**
       *
       * Converts InputStream to String
       * @param is
       * @return
       */
      public static String convertStreamToString(InputStream is) {

          logger.debug("Converting Input Stream to String : ");
          BufferedReader reader = new BufferedReader(new InputStreamReader(is));
          StringBuilder sb = new StringBuilder();

          String line = null;
          try {
              while ((line = reader.readLine()) != null) {
                  sb.append(line);
              }
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              try {
                  is.close();

              } catch (IOException e) {
                  // do nothing
              }
          }

          return sb.toString();
      }


      public static InputStream loadSpringResource(ApplicationContext context, String name) {

          if(context!=null) {
              Resource resource = context.getResource("classpath:" + name);

              try {
                  if (resource != null && resource.exists()) {
                      return resource.getInputStream();
                  }else {
                      String despaniHome= context.getEnvironment().getProperty("DESPANI_HOME");
                     resource = context.getResource("file:"+despaniHome+"/resources/" + name);
                      return resource.getInputStream();
                  }
              } catch (IOException e) {

                  e.printStackTrace();
                  return loadResource(name);
              }

          }
              return loadResource(name, getClazz());

      }

      public static InputStream loadResource(String name) {

          return loadResource(name,getClazz());
      }

      public static InputStream loadResource(String name, Class c) {
          logger.debug("Trying to Load Resource : " + name);
          InputStream in = getClazz().getResourceAsStream(name);
          if (in == null) {
              in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
              if (in == null) {
                  in = getClazz().getClassLoader().getResourceAsStream(name);
              }

              if(in == null){
                  in = getClazz().getClassLoader().getSystemResourceAsStream(name);

              }
          }
          return in;
      }

      private static Class getClazz() {
          return IOUtils.class;
      }

      public static String getFileName(String aPath) {
          if (aPath.lastIndexOf('/') < 0) {
              return aPath;
          }
          return aPath.substring(aPath.lastIndexOf('/') + 1);
      }

      public static int gzipAndCopyContent(OutputStream out, byte[] bytes) throws IOException {
          ByteArrayOutputStream baos = null;
          GZIPOutputStream gzos = null;

          int length = 0;
          try {
              baos = new ByteArrayOutputStream();
              gzos = new GZIPOutputStream(baos);

              gzos.write(bytes);
              gzos.finish();
              gzos.flush();
              gzos.close();

              byte[] gzippedBytes = baos.toByteArray();
              // Set the size of the file.
              length = gzippedBytes.length;
              // Write the binary context out

              copy(new ByteArrayInputStream(gzippedBytes), out);
              out.flush();
          } finally {
              try {
                  if (gzos != null) {
                      gzos.close();
                  }
              } catch (Exception ignored) {

              }
              try {
                  if (baos != null) {
                      baos.close();
                  }
              } catch (Exception ignored) {
              }
          }
          return length;
      }


//      public static File convertMultiPartToFile(MultipartFile file) throws IOException {
//          File convFile = new File(file.getOriginalFilename());
//          FileOutputStream fos = new FileOutputStream(convFile);
//          fos.write(file.getBytes());
//          fos.close();
//          return convFile;
//      }
  }