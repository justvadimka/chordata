package chordata.properties.primitive;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

@JsonTypeInfo(use = Id.CLASS, property = "@class")
public interface Primitive {

  void writeValue(SerializerOutput output) throws IOException;
}
