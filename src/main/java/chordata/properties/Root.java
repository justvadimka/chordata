package chordata.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.serializer.ObjectSerializer;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public record Root(@JsonProperty("propertiesHolder") PropertiesHolder propertiesHolder,
                   @JsonProperty("unknown") ValuesHolder unknownValues) {

  public static final Serializer<Root> SERIALIZER = new RootSerializer();

  private static final class RootSerializer extends ObjectSerializer<Root> {

    @Override
    protected void serializeObject(SerializationContext context, SerializerOutput output,
        Root root) throws IOException {
      output.writeObject(context, root.propertiesHolder, PropertiesHolder.SERIALIZER);
      output.writeObject(context, root.unknownValues, ValuesHolder.SERIALIZER);
    }

    @Override
    protected Root deserializeObject(SerializationContext context, SerializerInput input,
        int versionNumber) throws IOException, ClassNotFoundException {
      PropertiesHolder propertiesHolder = input.readObject(context, PropertiesHolder.SERIALIZER);
      ValuesHolder unknownValues = input.readObject(context, ValuesHolder.SERIALIZER);
      return new Root(propertiesHolder, unknownValues);
    }
  }
}
