package chordata.properties;

import chordata.ChordataException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.serializer.ObjectSerializer;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record PropertiesHolder(@JsonProperty("properties") List<Property> properties,
                               @JsonProperty("unknown") ValuesHolder unknownValues) {

  public static final Serializer<PropertiesHolder> SERIALIZER = new PropertiesHolderSerializer();

  private static final class PropertiesHolderSerializer extends ObjectSerializer<PropertiesHolder> {

    private static final int VERSION = 0;

    public PropertiesHolderSerializer() {
      super(VERSION);
    }

    @Override
    protected void serializeObject(SerializationContext context, SerializerOutput output,
        PropertiesHolder propertiesHolder) throws IOException {
      List<Property> properties = propertiesHolder.properties;
      output.writeInt(properties.size());
      for (Property p : properties) {
        output.writeObject(context, p, Property.SERIALIZER);
      }
      ValuesHolder unknownValues = propertiesHolder.unknownValues;
      output.writeObject(context, unknownValues, ValuesHolder.SERIALIZER);
    }

    @Override
    protected PropertiesHolder deserializeObject(SerializationContext context,
        SerializerInput input, int versionNumber) throws IOException, ClassNotFoundException {
      if (versionNumber != VERSION) {
        throw new ChordataException(
            String.format("Cannot deserialize PropertiesHolder of version %d. Expected version %d",
                versionNumber, VERSION));
      }

      int count = input.readInt();
      List<Property> properties = new ArrayList<>(count);
      for (int i = 0; i < count; i++) {
        Property p = input.readObject(context, Property.SERIALIZER);
        properties.add(p);
      }
      ValuesHolder unknownValues = input.readObject(context, ValuesHolder.SERIALIZER);
      return new PropertiesHolder(properties, unknownValues);
    }
  }
}
