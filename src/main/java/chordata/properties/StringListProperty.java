package chordata.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StringListProperty extends Property {

  public static final Serializer<StringListProperty> SERIALIZER = new StringListPropertySerializer();
  public static final int PROPERTY_TYPE = 7;

  public record Entry(@JsonProperty("number") int number, @JsonProperty("text") String text) {

  }

  @JsonProperty("entries")
  public final List<Entry> entries;

  public StringListProperty(@JsonProperty("entries") List<Entry> entries) {
    this.entries = entries;
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

  private static class StringListPropertySerializer extends Serializer<StringListProperty> {

    @Override
    public void serialize(SerializationContext context, SerializerOutput output,
        StringListProperty stringListProperty) throws IOException {
      Objects.requireNonNull(stringListProperty, "'stringListProperty' is required");

      output.writeInt(stringListProperty.entries.size());
      for (Entry entry : stringListProperty.entries) {
        output.writeInt(entry.number);
        output.writeString(entry.text);
      }
    }

    @Override
    public StringListProperty deserialize(SerializationContext context, SerializerInput input)
        throws IOException {
      int count = input.readInt();
      List<Entry> entries = new ArrayList<>(count);
      for (int i = 0; i < count; i++) {
        int value = input.readInt();
        String text = input.readString();
        Entry entry = new Entry(value, text);
        entries.add(entry);
      }
      return new StringListProperty(entries);
    }
  }
}
