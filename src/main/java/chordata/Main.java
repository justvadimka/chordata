package chordata;

import chordata.properties.Root;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.serial.stream.Serial;
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Main {

  public static final int WRONG_USAGE_EXIT_CODE = 100;
  public static final int FILE_NOT_FOUND_EXIT_CODE = 101;
  public static final int UNEXPECTED_EXCEPTION_EXIT_CODE = 102;

  private enum InputFileType {
    BINARY("-b"),
    JSON("-j");

    private final String key;

    InputFileType(String key) {
      this.key = key;
    }

    static InputFileType fromKey(String key) {
      return Arrays.stream(InputFileType.values())
          .filter(ft -> ft.key.equals(key))
          .findFirst()
          .orElse(null);
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 3) {
      printUsage();
      System.exit(WRONG_USAGE_EXIT_CODE);
    }

    InputFileType inputFileType = InputFileType.fromKey(args[0]);
    if (inputFileType == null) {
      printUsage();
      System.exit(WRONG_USAGE_EXIT_CODE);
    }

    try {
      Path inputFilePath = Path.of(args[1]);
      if (!Files.isRegularFile(inputFilePath)) {
        System.err.printf("File %s not found%n", inputFilePath);
        System.exit(FILE_NOT_FOUND_EXIT_CODE);
      }

      Path outputFilePath = Path.of(args[2]);
      try (FileInputStream is = new FileInputStream(inputFilePath.toFile());
          BufferedInputStream bis = new BufferedInputStream(is);
          FileOutputStream os = new FileOutputStream(outputFilePath.toFile());
          BufferedOutputStream bos = new BufferedOutputStream(os)) {
        process(inputFileType, bis, bos);
      }
    } catch (Throwable t) {
      System.err.println("Unexpected exception occurred:");
      System.err.println();
      t.printStackTrace(System.err);
      System.exit(UNEXPECTED_EXCEPTION_EXIT_CODE);
    }
  }

  private static void printUsage() {
    System.err.println("Usage: java -jar chordata.jar <-b|-j> <input file> <output file>");
    System.err.println();
    System.err.println("-b    <input file> is binary, JSON to <output file>");
    System.err.println("-j    <input file> is JSON, binary output to <output file>");
  }

  private static void process(InputFileType inputFileType, InputStream inputStream,
      OutputStream outputStream) throws Exception {
    Serial serial = new ByteBufferSerial();
    ObjectMapper objectMapper = new ObjectMapper();

    switch (inputFileType) {
      case BINARY -> processBinaryFile(inputStream, outputStream, serial, objectMapper);
      case JSON -> processJsonFile(inputStream, outputStream, serial, objectMapper);
    }
  }

  private static void processBinaryFile(InputStream inputStream, OutputStream out, Serial serial,
      ObjectMapper objectMapper) throws Exception {
    byte[] arr = inputStream.readAllBytes();
    Root root = serial.fromByteArray(arr, Root.SERIALIZER);

    objectMapper.writerWithDefaultPrettyPrinter()
        .writeValue(out, root);
  }

  private static void processJsonFile(InputStream inputStream, OutputStream out, Serial serial,
      ObjectMapper objectMapper) throws Exception {
    Root root = objectMapper.readValue(inputStream, Root.class);

    byte[] arr = serial.toByteArray(root, Root.SERIALIZER);
    out.write(arr);
  }
}