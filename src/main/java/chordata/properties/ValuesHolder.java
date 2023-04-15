package chordata.properties;

import chordata.ChordataException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerDefs;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @param values can contain nulls.
 */
public record ValuesHolder(@JsonProperty("values") List<Object> values) {

  public static final Serializer<ValuesHolder> SERIALIZER = new ValuesHolderSerializer();

  private static class ValuesHolderSerializer extends Serializer<ValuesHolder> {

    @Override
    public void serialize(SerializationContext context, SerializerOutput output,
        ValuesHolder valuesHolder) throws IOException {
      Objects.requireNonNull(valuesHolder);

      for (Object value : valuesHolder.values) {
        if (value instanceof Byte b) {
          output.writeByte(b);
        } else if (value instanceof Integer i) {
          output.writeInt(i);
        } else if (value instanceof Long l) {
          output.writeLong(l);
        } else if (value instanceof Float f) {
          output.writeFloat(f);
        } else if (value instanceof Double d) {
          output.writeDouble(d);
        } else if (value instanceof Boolean b) {
          output.writeBoolean(b);
        } else if (value == null) {
          output.writeNull();
        } else if (value instanceof String s) {
          output.writeString(s);
        } else if (value instanceof byte[] barr) {
          output.writeByteArray(barr);
        } else {
          throw new ChordataException("Unsupported value type " + value.getClass());
        }
      }
    }

    @Override
    public ValuesHolder deserialize(SerializationContext context, SerializerInput input)
        throws IOException {
      List<Object> values = new ArrayList<>();

      byte nextType = input.peekType();
      while (nextType != SerializerDefs.TYPE_END_OBJECT) {
        Object value = switch (nextType) {
          case SerializerDefs.TYPE_UNKNOWN ->
              throw new ChordataException("Unknown type at position " + input.getPosition());
          case SerializerDefs.TYPE_BYTE -> input.readByte();
          case SerializerDefs.TYPE_INT -> input.readInt();
          case SerializerDefs.TYPE_LONG -> input.readLong();
          case SerializerDefs.TYPE_FLOAT -> input.readFloat();
          case SerializerDefs.TYPE_DOUBLE -> input.readDouble();
          case SerializerDefs.TYPE_BOOLEAN -> input.readBoolean();
          case SerializerDefs.TYPE_NULL -> {
            input.readNull();
            yield null;
          }
          case SerializerDefs.TYPE_STRING_UTF8, SerializerDefs.TYPE_STRING_ASCII ->
              input.readString();
          case SerializerDefs.TYPE_START_OBJECT, SerializerDefs.TYPE_START_OBJECT_DEBUG ->
              throw new ChordataException("Unsupported value type " + nextType);
          case SerializerDefs.TYPE_EOF -> throw new ChordataException("Unexpected EOF");
          case SerializerDefs.TYPE_BYTE_ARRAY -> input.readByteArray();
          default -> throw new ChordataException("Unknown value type " + nextType);
        };
        values.add(value);
        nextType = input.peekType();
      }

      return new ValuesHolder(values);
    }
  }
}
