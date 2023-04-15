package chordata.properties.primitive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public record BooleanPrimitive(@JsonProperty("bool") boolean value) implements Primitive {

  @Override
  public void writeValue(SerializerOutput output) throws IOException {
    output.writeBoolean(value);
  }
}
