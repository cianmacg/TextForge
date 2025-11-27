package ie.atu.forge.Tokenisers;

import java.util.Arrays;

/**
 * Simply an Object to store an array of Bytes. Used by BPE.java.
 *
 * @param bytes Array of bytes.
 */
public record ByteSequence(byte[] bytes) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ByteSequence)) return false;
        ByteSequence other = (ByteSequence) o;
        return Arrays.equals(this.bytes, other.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
