package chordata.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public class StringProperty extends Property {

  public static final Serializer<StringProperty> SERIALIZER = new StringPropertySerializer();
  public static final int PROPERTY_TYPE = 6;

  @JsonProperty("string")
  public final String value;

  public StringProperty(@JsonProperty("string") String value) {
    this.value = value;
  }

  @Override
  void serializeProperties(SerializationContext context, SerializerOutput output)
      throws IOException {
    SERIALIZER.serialize(context, output, this);
  }

  @Override
  int getPropertyType() {
    return PROPERTY_TYPE;
  }

  private static class StringPropertySerializer extends Serializer<StringProperty> {

    @Override
    public void serialize(SerializationContext context, SerializerOutput output,
        StringProperty property) throws IOException {
      output.writeString(property.value);
    }

    @Override
    public StringProperty deserialize(SerializationContext context, SerializerInput input)
        throws IOException {
      String value = input.readString();
      return new StringProperty(value);
    }
  }
}
