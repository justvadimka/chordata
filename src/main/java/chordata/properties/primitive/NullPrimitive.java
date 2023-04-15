package chordata.properties.primitive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public final class NullPrimitive implements Primitive {

  @JsonProperty("null")
  Object object;

  @Override
  public void writeValue(SerializerOutput output) throws IOException {
    output.writeNull();
  }
}
