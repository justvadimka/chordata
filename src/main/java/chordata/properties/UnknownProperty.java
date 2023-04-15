package chordata.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;
import java.util.Objects;

public final class UnknownProperty extends Property {

  public static final Serializer<UnknownProperty> SERIALIZER = new UnknownPropertySerializer();

  @JsonProperty("unknown")
  public final ValuesHolder values;

  @JsonProperty("propertyType")
  public int propertyType;

  UnknownProperty(ValuesHolder values) {
    this.values = values;
  }

  public UnknownProperty(@JsonProperty("unknown") ValuesHolder values,
      @JsonProperty("propertyType") int propertyType) {
    this.values = values;
    this.propertyType = propertyType;
  }

  public void setPropertyType(int propertyType) {
    this.propertyType = propertyType;
  }

  @Override
  void serializeProperties(SerializationContext context, SerializerOutput output)
      throws IOException {
    SERIALIZER.serialize(context, output, this);
  }

  @Override
  public int getPropertyType() {
    return propertyType;
  }

  private static class UnknownPropertySerializer extends Serializer<UnknownProperty> {

    @Override
    public void serialize(SerializationContext context, SerializerOutput output,
        UnknownProperty unknownProperty) throws IOException {
      Objects.requireNonNull(unknownProperty);
      ValuesHolder.SERIALIZER.serialize(context, output, unknownProperty.values);
    }

    @Override
    public UnknownProperty deserialize(SerializationContext context, SerializerInput input)
        throws IOException, ClassNotFoundException {
      ValuesHolder values = input.readObject(context, ValuesHolder.SERIALIZER);
      return new UnknownProperty(values);
    }
  }
}
