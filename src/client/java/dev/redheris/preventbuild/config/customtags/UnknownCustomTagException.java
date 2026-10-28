package dev.redheris.preventbuild.config.customtags;

public class UnknownCustomTagException extends RuntimeException {
    public UnknownCustomTagException(String tagName) {
        super("Unknown custom tag: \"" + tagName + "\"");
    }
}
