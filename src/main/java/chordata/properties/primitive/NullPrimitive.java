package chordata.properties.primitive;

import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public record NullPrimitive() implements Primitive {

  @Override
  public void writeValue(SerializerOutput output) throws IOException {
    output.writeNull();
  }
}
