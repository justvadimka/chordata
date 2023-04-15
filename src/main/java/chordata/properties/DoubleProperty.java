package chordata.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;
import java.util.Objects;

public final class DoubleProperty extends Property {

  public static final Serializer<DoubleProperty> SERIALIZER = new DoublePropertySerializer();
  public static final int PROPERTY_TYPE = 3;

  @JsonProperty("double")
  public final double value;

  public DoubleProperty(@JsonProperty("double") double value) {
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

  private static class DoublePropertySerializer extends Serializer<DoubleProperty> {

    @Override
    public void serialize(SerializationContext context, SerializerOutput output,
        DoubleProperty property) throws IOException {
      Objects.requireNonNull(property);
      output.writeDouble(property.value);
    }

    @Override
    public DoubleProperty deserialize(SerializationContext context, SerializerInput input)
        throws IOException {
      double value = input.readDouble();
      return new DoubleProperty(value);
    }
  }
}
