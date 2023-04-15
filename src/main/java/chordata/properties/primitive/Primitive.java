package chordata.properties.primitive;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

@JsonTypeInfo(use = Id.DEDUCTION)
@JsonSubTypes({
    @Type(BooleanPrimitive.class),
    @Type(ByteArrayPrimitive.class),
    @Type(BytePrimitive.class),
    @Type(DoublePrimitive.class),
    @Type(FloatPrimitive.class),
    @Type(IntPrimitive.class),
    @Type(LongPrimitive.class),
    @Type(NullPrimitive.class),
    @Type(StringPrimitive.class)
})
public sealed interface Primitive permits BooleanPrimitive, ByteArrayPrimitive, BytePrimitive,
    DoublePrimitive, FloatPrimitive, IntPrimitive, LongPrimitive,
    NullPrimitive, StringPrimitive {

  void writeValue(SerializerOutput output) throws IOException;
}
