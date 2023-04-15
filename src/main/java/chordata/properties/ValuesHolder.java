package chordata.properties;

import chordata.ChordataException;
import chordata.properties.primitive.BooleanPrimitive;
import chordata.properties.primitive.ByteArrayPrimitive;
import chordata.properties.primitive.BytePrimitive;
import chordata.properties.primitive.DoublePrimitive;
import chordata.properties.primitive.FloatPrimitive;
import chordata.properties.primitive.IntPrimitive;
import chordata.properties.primitive.LongPrimitive;
import chordata.properties.primitive.NullPrimitive;
import chordata.properties.primitive.Primitive;
import chordata.properties.primitive.StringPrimitive;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.serializer.Serializer;
import com.twitter.serial.stream.SerializerDefs;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ValuesHolder {

  public static final Serializer<ValuesHolder> SERIALIZER = new ValuesHolderSerializer();

  @JsonValue
  public final List<Primitive> values;  // can contain nulls.

  @JsonCreator
  public ValuesHolder(List<Primitive> values) {
    this.values = values;
  }

  private static class ValuesHolderSerializer extends Serializer<ValuesHolder> {

    @Override
    public void serialize(SerializationContext context, SerializerOutput output,
        ValuesHolder valuesHolder) throws IOException {
      Objects.requireNonNull(valuesHolder);
      for (Primitive p : valuesHolder.values) {
        p.writeValue(output);
      }
    }

    @Override
    public ValuesHolder deserialize(SerializationContext context, SerializerInput input)
        throws IOException {
      List<Primitive> values = new ArrayList<>();

      byte nextType = input.peekType();
      while (nextType != SerializerDefs.TYPE_END_OBJECT) {
        Primitive value = switch (nextType) {
          case SerializerDefs.TYPE_UNKNOWN ->
              throw new ChordataException("Unknown type at position " + input.getPosition());
          case SerializerDefs.TYPE_BYTE -> new BytePrimitive(input.readByte());
          case SerializerDefs.TYPE_INT -> new IntPrimitive(input.readInt());
          case SerializerDefs.TYPE_LONG -> new LongPrimitive(input.readLong());
          case SerializerDefs.TYPE_FLOAT -> new FloatPrimitive(input.readFloat());
          case SerializerDefs.TYPE_DOUBLE -> new DoublePrimitive(input.readDouble());
          case SerializerDefs.TYPE_BOOLEAN -> new BooleanPrimitive(input.readBoolean());
          case SerializerDefs.TYPE_NULL -> {
            input.readNull();
            yield new NullPrimitive();
          }
          case SerializerDefs.TYPE_STRING_UTF8, SerializerDefs.TYPE_STRING_ASCII ->
              new StringPrimitive(input.readString());
          case SerializerDefs.TYPE_START_OBJECT, SerializerDefs.TYPE_START_OBJECT_DEBUG ->
              throw new ChordataException("Unsupported value type " + nextType);
          case SerializerDefs.TYPE_EOF -> throw new ChordataException("Unexpected EOF");
          case SerializerDefs.TYPE_BYTE_ARRAY -> new ByteArrayPrimitive(input.readByteArray());
          default -> throw new ChordataException("Unknown value type " + nextType);
        };
        values.add(value);
        nextType = input.peekType();
      }

      return new ValuesHolder(values);
    }
  }
}
