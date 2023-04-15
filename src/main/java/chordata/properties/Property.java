package chordata.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.twitter.serial.serializer.ObjectSerializer;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;
import java.util.Objects;

@JsonPropertyOrder({"name"})
@JsonTypeInfo(use = Id.CLASS, property = "@class")
public abstract class Property {

  public static final Serializer<Property> SERIALIZER = new PropertySerializer();

  @JsonProperty("name")
  private String name;

  void setName(@JsonProperty("name") String name) {
    this.name = name;
  }

  abstract void serializeProperties(SerializationContext context, SerializerOutput output)
      throws IOException;

  abstract int getPropertyType();

  static class PropertySerializer extends ObjectSerializer<Property> {

    private static int VERSION_NUMBER = 1;

    PropertySerializer() {
      super(VERSION_NUMBER);
    }

    @Override
    protected void serializeObject(SerializationContext context, SerializerOutput output,
        Property property) throws IOException {
      output.writeString(property.name);
      output.writeInt(property.getPropertyType());
      property.serializeProperties(context, output);
    }

    @Override
    protected Property deserializeObject(SerializationContext context, SerializerInput input,
        int versionNumber) throws IOException, ClassNotFoundException {
      String name = input.readString();
      int propertyType = input.readInt();

      Property property = switch (propertyType) {
        case BooleanProperty.PROPERTY_TYPE -> input.readObject(context, BooleanProperty.SERIALIZER);
        case DoubleProperty.PROPERTY_TYPE -> input.readObject(context, DoubleProperty.SERIALIZER);
        case StringProperty.PROPERTY_TYPE -> input.readObject(context, StringProperty.SERIALIZER);
        case StringListProperty.PROPERTY_TYPE ->
            input.readObject(context, StringListProperty.SERIALIZER);
        default -> {
          UnknownProperty unknownProperty = input.readObject(context, UnknownProperty.SERIALIZER);
          Objects.requireNonNull(unknownProperty);
          unknownProperty.setPropertyType(propertyType);
          yield unknownProperty;
        }
      };

      property.setName(name);
      return property;
    }
  }
}
