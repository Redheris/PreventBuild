package dev.redheris.preventbuild.config.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.resources.Identifier;

import java.io.IOException;

public class IdentifierAdapter extends TypeAdapter<Identifier> {
    @Override
    public void write(JsonWriter out, Identifier value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public Identifier read(JsonReader in) throws IOException {
        return Identifier.parse(in.nextString());
    }
}
