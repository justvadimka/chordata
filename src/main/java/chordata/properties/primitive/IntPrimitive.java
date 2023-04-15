package chordata.properties.primitive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public record IntPrimitive(@JsonProperty("int") int value) implements Primitive {

  @Override
  public void writeValue(SerializerOutput output) throws IOException {
    output.writeInt(value);
  }
}
