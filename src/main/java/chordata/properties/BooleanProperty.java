package chordata.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;
import java.util.Objects;

public class BooleanProperty extends Property {

  public static final Serializer<BooleanProperty> SERIALIZER = new BooleanPropertySerializer();
  public static final int PROPERTY_TYPE = 5;

  @JsonProperty("bool")
  public final boolean value;

  public BooleanProperty(@JsonProperty("bool") boolean value) {
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

  private static class BooleanPropertySerializer extends Serializer<BooleanProperty> {

    @Override
    public void serialize(SerializationContext context, SerializerOutput output,
        BooleanProperty property) throws IOException {
      Objects.requireNonNull(property);
      output.writeBoolean(property.value);
    }

    @Override
    public BooleanProperty deserialize(SerializationContext context, SerializerInput input)
        throws IOException {
      boolean value = input.readBoolean();
      return new BooleanProperty(value);
    }
  }
}
