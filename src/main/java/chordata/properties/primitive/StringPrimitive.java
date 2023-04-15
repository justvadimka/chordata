package chordata.properties.primitive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public record StringPrimitive(@JsonProperty("string") String value) implements Primitive {

  @Override
  public void writeValue(SerializerOutput output) throws IOException {
    output.writeString(value);
  }
}
